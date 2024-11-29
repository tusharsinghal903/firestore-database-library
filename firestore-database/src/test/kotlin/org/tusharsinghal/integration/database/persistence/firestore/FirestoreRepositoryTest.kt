package org.tusharsinghal.integration.database.persistence.firestore

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.tusharsinghal.base.domain.SampleModel
import org.tusharsinghal.database.domain.DatabaseRepository
import java.math.BigInteger
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FirestoreRepositoryTest {

    private lateinit var sampleRepository: DatabaseRepository<SampleModel>

    @BeforeAll
    fun setUp() {
        sampleRepository = SampleModelRepositoryConfig.sampleRepository()
    }

    @Test
    fun `should create`() {
        val sampleModel = SampleModel(null, 1.toBigInteger())
        val savedSampleModel = sampleRepository.create(sampleModel)
        assertEquals(1.toBigInteger(), savedSampleModel.bigIntegerField)
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should update`() {
        val sampleModel = SampleModel(null, 1.toBigInteger())
        val savedSampleModel = sampleRepository.create(sampleModel)
        val updatedSampleModel = sampleRepository.update(savedSampleModel.copy(bigIntegerField = BigInteger.TWO))
        assertEquals(2.toBigInteger(), updatedSampleModel.bigIntegerField)
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should delete`() {
        val sampleModel = SampleModel(null, 1.toBigInteger())
        val savedSampleModel = sampleRepository.create(sampleModel)
        val isDeleted = sampleRepository.deleteById(savedSampleModel.id!!)
        assert(isDeleted)
    }

    @Test
    fun `should get all`() {
        val sampleModel = SampleModel(null, 1.toBigInteger())
        val savedSampleModel = sampleRepository.create(sampleModel)
        val retrievedSampleModels = sampleRepository.getAll()
        assert(retrievedSampleModels.isNotEmpty())
        sampleRepository.deleteById(savedSampleModel.id!!)
    }
}
