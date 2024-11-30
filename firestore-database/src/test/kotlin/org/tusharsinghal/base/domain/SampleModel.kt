package org.tusharsinghal.base.domain

import org.tusharsinghal.database.domain.models.BaseModel
import java.math.BigInteger

data class SampleModel(
    override var id: String?,
    val bigIntegerField: BigInteger?,
    val nestedSampleModel: NestedSampleModel?
): BaseModel