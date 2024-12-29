package org.tusharsinghal.integration.database.persistence.firestore

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.tusharsinghal.base.domain.NestedSampleModel
import org.tusharsinghal.base.domain.SampleEnum
import org.tusharsinghal.base.domain.SampleModel
import org.tusharsinghal.database.domain.DatabaseRepository
import org.tusharsinghal.database.domain.models.ComparisonOperator
import java.math.BigInteger
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FirestoreRepositoryTest {

    private lateinit var sampleRepository: DatabaseRepository<SampleModel>
    private val sampleModel = SampleModel(id = null, bigIntegerField =  1.toBigInteger(), NestedSampleModel(bigIntegerField = BigInteger.ONE))
    @BeforeAll
    fun setUp() {
        sampleRepository = SampleModelRepositoryConfig.sampleRepository()

    }

    @Test
    fun `should create`() {
        val savedSampleModel: SampleModel = sampleRepository.create(sampleModel)
        assertEquals(1.toBigInteger(), savedSampleModel.bigIntegerField)
        assert(savedSampleModel.createdAt > 0)
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `create should work with null value`() {
        val savedSampleModel = sampleRepository.create(sampleModel.copy(bigIntegerField = null, nestedSampleModel = null))
        assertEquals(null, savedSampleModel.nestedSampleModel)
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should find by id`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val retrievedSampleModel = sampleRepository.findById(savedSampleModel.id!!)
        assertEquals(savedSampleModel, retrievedSampleModel)
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should get all`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val retrievedSampleModels = sampleRepository.getAll()
        assert(retrievedSampleModels.isNotEmpty())
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should find by field name`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val retrievedSampleModels = sampleRepository.findByFieldName("bigIntegerField", 1.toBigInteger(),)
        assert(retrievedSampleModels.isNotEmpty())
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should find by conditions`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val retrievedSampleModels = sampleRepository.findByConditions(listOf(Triple("bigIntegerField", ComparisonOperator.EQUALS, 1.toBigInteger())))
        assert(retrievedSampleModels.isNotEmpty())
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should find by condition by nested field`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val retrievedSampleModels = sampleRepository.findByConditions(listOf(Triple("nestedSampleModel.bigIntegerField", ComparisonOperator.EQUALS, 1.toBigInteger())))
        assert(retrievedSampleModels.isNotEmpty())
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should find by condition by nested object`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val retrievedSampleModels = sampleRepository.findByConditions(listOf(Triple("nestedSampleModel", ComparisonOperator.EQUALS, NestedSampleModel(bigIntegerField = BigInteger.ONE))))
        assert(retrievedSampleModels.isNotEmpty())
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should find by enum value in nested object`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val retrievedSampleModels = sampleRepository.findByConditions(listOf(Triple("nestedSampleModel.sampleEnum", ComparisonOperator.EQUALS, SampleEnum.SAMPLE_ENUM_1)))
        assert(retrievedSampleModels.isNotEmpty())
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should limit results`() {
        val savedSampleModel1 = sampleRepository.create(sampleModel)
        val savedSampleModel2 = sampleRepository.create(sampleModel)
        val retrievedSampleModels = sampleRepository.findByConditions(listOf(Triple("bigIntegerField", ComparisonOperator.EQUALS, 1.toBigInteger())), 1)
        assertEquals(1, retrievedSampleModels.size)
        sampleRepository.deleteById(savedSampleModel1.id!!)
        sampleRepository.deleteById(savedSampleModel2.id!!)
    }

    @Test
    fun `should update`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val updatedSampleModel = sampleRepository.update(savedSampleModel.copy(bigIntegerField = BigInteger.TWO))
        assert(updatedSampleModel.updatedAt > savedSampleModel.updatedAt)
        assertEquals(2.toBigInteger(), updatedSampleModel.bigIntegerField)
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun shouldUpdateFieldsById() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val isUpdated = sampleRepository.updateFieldsById(savedSampleModel.id!!, mapOf("bigIntegerField" to BigInteger.TWO))
        assert(isUpdated)
        assert(savedSampleModel.updatedAt < sampleRepository.findById(savedSampleModel.id!!)!!.updatedAt)
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should patch update by id`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val patchUpdateObject = SampleModel(id = null, bigIntegerField = BigInteger.TWO, nestedSampleModel = null)
        val isUpdated = sampleRepository.patchUpdateById(savedSampleModel.id!!, patchUpdateObject)
        assert(isUpdated)
        assert(savedSampleModel.updatedAt < sampleRepository.findById(savedSampleModel.id!!)!!.updatedAt)
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should patch update by conditions`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val patchUpdateObject = SampleModel(id = null, bigIntegerField = BigInteger.TWO, nestedSampleModel = null)
        val isUpdated = sampleRepository.patchUpdateByConditions(listOf(Triple("bigIntegerField", ComparisonOperator.EQUALS, 1.toBigInteger())), patchUpdateObject)
        assert(isUpdated)
        assert(savedSampleModel.updatedAt < sampleRepository.findById(savedSampleModel.id!!)!!.updatedAt)
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should update fields by conditions`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val isUpdated = sampleRepository.updateFieldsByConditions(listOf(Triple("bigIntegerField", ComparisonOperator.EQUALS, 1.toBigInteger())), mapOf("bigIntegerField" to BigInteger.TWO))
        assert(isUpdated)
        assert(savedSampleModel.updatedAt < sampleRepository.findById(savedSampleModel.id!!)!!.updatedAt)
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

    @Test
    fun `should delete by ID`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        val isDeleted = sampleRepository.deleteById(savedSampleModel.id!!)
        assert(isDeleted)
    }

    @Test
    fun `should delete by conditions`() {
        sampleRepository.create(sampleModel)
        val isDeleted = sampleRepository.deleteByConditions(listOf(Triple("bigIntegerField", ComparisonOperator.EQUALS, 1.toBigInteger())))
        assert(isDeleted)
    }

    @Test
    fun `should increment field by conditions`() {
        val savedSampleModel = sampleRepository.create(sampleModel)
        sampleRepository.incrementFieldByConditions(listOf(Triple("bigIntegerField", ComparisonOperator.EQUALS, 1.toBigInteger())), "intField", 1)
        val updatedSampleModel = sampleRepository.findById(savedSampleModel.id!!)
        assertEquals(1, updatedSampleModel!!.intField)
        sampleRepository.deleteById(savedSampleModel.id!!)
    }

}
