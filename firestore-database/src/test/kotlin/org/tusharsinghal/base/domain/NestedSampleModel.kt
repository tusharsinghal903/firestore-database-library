package org.tusharsinghal.base.domain

import java.math.BigInteger

data class NestedSampleModel(
    val bigIntegerField: BigInteger,
    val sampleEnum: SampleEnum = SampleEnum.SAMPLE_ENUM_1
)
