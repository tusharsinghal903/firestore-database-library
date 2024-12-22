package org.tusharsinghal.database.domain

import org.tusharsinghal.database.domain.models.ComparisonOperator
import org.tusharsinghal.database.domain.models.DEFAULT_LIMIT
import javax.swing.SortOrder

interface DatabaseRepository<T> {
    // Create a new entity
    fun create(entity: T): T

    // Read an entity by its ID
    fun findById(id: String): T?

    // Get all entities
    fun getAll(
        limit: Int = DEFAULT_LIMIT,
        offset: Int? = null,
        sortBy: String? = null,
        sortOrder: SortOrder? = null
    ): List<T>

    fun findByFieldName(
        fieldName: String, value: Any,
        limit: Int = DEFAULT_LIMIT,
        offset: Int? = null,
        sortBy: String? = null,
        sortOrder: SortOrder? = null
    ): List<T>

    fun findByCondition(
        fieldName: String,
        operator: ComparisonOperator,
        value: Any,
        limit: Int = DEFAULT_LIMIT,
        offset: Int? = null,
        sortBy: String? = null,
        sortOrder: SortOrder? = null
    ): List<T>

    fun findByConditions(
        conditions: List<Triple<String, ComparisonOperator, Any>>,
        limit: Int = DEFAULT_LIMIT,
        offset: Int? = null,
        sortBy: String? = null,
        sortOrder: SortOrder? = null
    ): List<T>

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

    fun incrementFieldByConditions(
        conditions: List<Triple<String, ComparisonOperator, Any>>,
        fieldName: String,
        incrementBy: Long
    ): Boolean

}
