package org.tusharsinghal.base.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.tusharsinghal.database.domain.models.BaseModel
import java.math.BigInteger

data class SampleModel(
    override var id: String?,
    @JsonSerialize(using = ToStringSerializer::class) val bigIntegerField: BigInteger
): BaseModel