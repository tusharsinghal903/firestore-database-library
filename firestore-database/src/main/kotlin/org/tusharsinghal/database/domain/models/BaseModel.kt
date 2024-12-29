package org.tusharsinghal.database.domain.models

abstract class BaseModel {
    abstract var id: String?
    val createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()
}