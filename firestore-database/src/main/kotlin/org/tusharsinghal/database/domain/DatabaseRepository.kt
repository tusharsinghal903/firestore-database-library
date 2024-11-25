package org.tusharsinghal.database.domain

import org.tusharsinghal.database.domain.models.ComparisonOperator

interface DatabaseRepository<T> {
    // Create a new entity
    fun create(entity: T): T

    // Read an entity by its ID
    fun findById(id: String): T?

    // Update an existing entity
    fun update(entity: T): T

    // Delete an entity by its ID
    fun deleteById(id: String): Boolean

    // Get all entities
    fun getAll(): List<T>
    fun findByFieldName(fieldName: String, value: Any): List<T>
    fun findByCondition(fieldName: String, operator: ComparisonOperator, value: Any): List<T>
    fun findByConditions(conditions: List<Triple<String, ComparisonOperator, Any>>): List<T>
    fun deleteByCondition(fieldName: String, operator: ComparisonOperator, value: Any): Boolean
    fun deleteByConditions(conditions: List<Triple<String, ComparisonOperator, Any>>): Boolean
}
