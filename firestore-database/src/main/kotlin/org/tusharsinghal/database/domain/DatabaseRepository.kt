package org.tusharsinghal.database.domain

import org.tusharsinghal.database.domain.models.ComparisonOperator

interface DatabaseRepository<T> {
    // Create a new entity
    fun create(entity: T): T

    // Read an entity by its ID
    fun findById(id: String): T?
    // Get all entities
    fun getAll(): List<T>
    fun findByFieldName(fieldName: String, value: Any): List<T>
    fun findByCondition(fieldName: String, operator: ComparisonOperator, value: Any): List<T>
    fun findByConditions(conditions: List<Triple<String, ComparisonOperator, Any>>): List<T>

    // Update an existing entity
    fun update(entity: T): T
    fun updateFieldsById(id: String, fields: Map<String, Any>): Boolean
    fun updateFieldsByConditions(
        conditions: List<Triple<String, ComparisonOperator, Any>>,
        fields: Map<String, Any>
    ): Boolean
    fun patchUpdateById(id: String, entity: T): Boolean
    fun patchUpdateByConditions(conditions: List<Triple<String, ComparisonOperator, Any>>, entity: T): Boolean

    // Delete an entity by its ID
    fun deleteById(id: String): Boolean
    fun deleteByCondition(fieldName: String, operator: ComparisonOperator, value: Any): Boolean
    fun deleteByConditions(conditions: List<Triple<String, ComparisonOperator, Any>>): Boolean

}
