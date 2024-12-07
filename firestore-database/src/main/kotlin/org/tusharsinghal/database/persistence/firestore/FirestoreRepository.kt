package org.tusharsinghal.database.persistence.firestore

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.cloud.firestore.*
import org.tusharsinghal.database.domain.models.BaseModel
import org.tusharsinghal.database.domain.DatabaseRepository
import org.tusharsinghal.database.domain.models.ComparisonOperator
import kotlin.reflect.KClass


@Suppress("UNCHECKED_CAST")
class FirestoreRepository<T : BaseModel>(
    private val firestore: Firestore,
    collectionName: String,
    private val entityClass: KClass<T>
) : DatabaseRepository<T> {
    private val collection: CollectionReference = firestore.collection(collectionName)

    private val objectMapper = jacksonObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)  // Ignore unknown properties
    }
    override fun create(entity: T): T {
        val id = entity.id ?: collection.document().id // If ID is null, generate a random ID
        entity.id = id
        val documentRef = collection.document(id)
        val entityMap = convertToMap(entity)
        documentRef.set(entityMap).get()
        return entity
    }

    override fun findById(id: String): T? {
        val document = collection.document(id).get().get()
        return document?.toObject(entityClass)
    }

    override fun getAll(): List<T> {
        val documents = collection.get().get()
        return documents.mapNotNull { it.toObject(entityClass) }
    }

    override fun findByFieldName(fieldName: String, value: Any): List<T> {
        return findByConditions(listOf(Triple(fieldName, ComparisonOperator.EQUALS, value)))
    }

    override fun findByCondition(fieldName: String, operator: ComparisonOperator, value: Any): List<T> {
        return findByConditions(listOf(Triple(fieldName, operator, value)))
    }

    override fun findByConditions(conditions: List<Triple<String, ComparisonOperator, Any>>): List<T> {
        val query: Query = buildQuery(conditions)

        // Execute the query and map the results
        val documents = query.get().get()
        return documents.mapNotNull { it.toObject(entityClass) }
    }

    override fun update(entity: T): T {
        if(entity.id == null) throw IllegalArgumentException("Entity ID cannot be null for update")
        collection.document(entity.id!!).set(convertToMap(entity)).get()
        return entity
    }

    override fun updateFieldsById(id: String, fields: Map<String, Any>): Boolean {
        val documentRef = collection.document(id)
        val updateResult = documentRef.update(convertToMap(fields)).get()
        return updateResult != null
    }

    override fun patchUpdateById(id: String, entity: T): Boolean {
        val documentRef = collection.document(id)
        val fields = getNonNullFields(entity)

        val updateResult = documentRef.update(fields).get()
        return updateResult != null
    }

    override fun patchUpdateByConditions(conditions: List<Triple<String, ComparisonOperator, Any>>, entity: T): Boolean {
        val query: Query = buildQuery(conditions)

        val documents = query.get().get()
        val batch = firestore.batch()
        val fields = getNonNullFields(entity)
        documents.forEach { doc ->
            batch.update(doc.reference, fields)
        }
        val batchResult = batch.commit().get()
        return batchResult != null
    }

    override fun updateFieldsByConditions(conditions: List<Triple<String, ComparisonOperator, Any>>, fields: Map<String, Any>): Boolean {
        val query: Query = buildQuery(conditions)

        // Execute the query and update the documents
        val documents = query.get().get()
        val batch = firestore.batch()
        documents.forEach { doc ->
            batch.update(doc.reference, convertToMap(fields))
        }
        val batchResult = batch.commit().get()
        return batchResult != null
    }

    override fun deleteById(id: String): Boolean {
        collection.document(id).delete().get()
        return true
    }

    override fun deleteByCondition(fieldName: String, operator: ComparisonOperator, value: Any): Boolean {
        return deleteByConditions(listOf(Triple(fieldName, operator, value)))
    }

    override fun deleteByConditions(conditions: List<Triple<String, ComparisonOperator, Any>>): Boolean {
        val query: Query = buildQuery(conditions)
        val documents = query.get().get()
        val batch = firestore.batch()
        documents.forEach { doc ->
            batch.delete(doc.reference)
        }
        val batchResult = batch.commit().get()
        return batchResult != null
    }


    private fun DocumentSnapshot.toObject(clazz: KClass<T>): T? {
        return objectMapper.convertValue(this.data, clazz.java)
    }

    private fun QueryDocumentSnapshot.toObject(clazz: KClass<T>): T? {
        return objectMapper.convertValue(this.data, clazz.java)
    }

    private fun buildQuery(conditions: List<Triple<String, ComparisonOperator, Any>>): Query {
        var query: Query = collection
        for (condition in conditions) {
            val (fieldName, operator, value) = condition
            query = when (operator) {
                ComparisonOperator.EQUALS -> query.whereEqualTo(fieldName, convertToFirestoreCompatible(value))
                ComparisonOperator.GREATER_THAN -> query.whereGreaterThan(fieldName, convertToFirestoreCompatible(value))
                ComparisonOperator.GREATER_THAN_OR_EQUAL -> query.whereGreaterThanOrEqualTo(fieldName, convertToFirestoreCompatible(value))
                ComparisonOperator.LESS_THAN -> query.whereLessThan(fieldName, convertToFirestoreCompatible(value))
                ComparisonOperator.LESS_THAN_OR_EQUAL -> query.whereLessThanOrEqualTo(fieldName, convertToFirestoreCompatible(value))
                ComparisonOperator.ARRAY_CONTAINS -> query.whereArrayContains(fieldName, convertToFirestoreCompatible(value))
            }
        }
        return query
    }

    private fun getNonNullFields(entity: T): Map<String, Any> {
        return convertToMap(entity).filter { it.value != null } as Map<String, Any>
    }

    private fun convertToMap(entity: Any): Map<String, Any?> {
        val jsonString = objectMapper.writeValueAsString(entity)
        return objectMapper.readValue(jsonString, Map::class.java) as Map<String, Any?>
    }

    private fun convertToFirestoreCompatible(entity: Any): Any {
        return when (entity) {
            is Number -> entity.toDouble()
            is Boolean -> entity
            is CharSequence -> entity.toString()
            is Enum<*> -> entity.name
            else -> {
                val jsonString = objectMapper.writeValueAsString(entity)
                objectMapper.readValue(jsonString, Map::class.java) as Map<String, Any?>
            }
        }
    }

}


