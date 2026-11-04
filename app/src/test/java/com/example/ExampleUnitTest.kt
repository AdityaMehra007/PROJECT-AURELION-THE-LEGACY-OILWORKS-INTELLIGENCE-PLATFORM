package com.example

import com.example.data.model.*
import com.example.data.repository.AurelionRepository
import com.example.ui.AurelionViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AurelionIndustrialTest {

    private lateinit var repository: AurelionRepository
    private lateinit var viewModel: AurelionViewModel

    @Before
    fun setUp() {
        repository = AurelionRepository()
        viewModel = AurelionViewModel(repository)
    }

    @Test
    fun testApiGravityToDensityCalculation() {
        // Standard kerosene cut API around 38.5
        viewModel.updateApiInput("38.5")
        val result = viewModel.calcUnitResult.value

        // SG = 141.5 / (38.5 + 131.5) = 141.5 / 170.0 ≈ 0.8324
        assertEquals(0.8324, result.specificGravity, 0.001)
        // Density = 0.8324 * 999.012 ≈ 831.5 kg/m³
        assertEquals(831.5, result.densityKgM3, 1.0)
    }

    @Test
    fun testTankStrappingAndDippingVolume() {
        // Diameter = 12m (radius = 6m), Dip = 5m, Temp = 35°C (deltaT = 20°C from 15°C)
        viewModel.updateTankInputs("12.0", "5.0", "35.0", "810.0")
        val result = viewModel.calcTankResult.value

        // Raw Volume = pi * 6^2 * 5 = 180 * pi ≈ 565.5 KL
        assertEquals(565.5, result.rawVolumeKl, 1.0)

        // Temperature corrected volume should be slightly lower due to thermal expansion at 35°C
        assertTrue(result.temperatureCorrectedVolumeKl < result.rawVolumeKl)
        assertTrue(result.massMetricTons > 0.0)
    }

    @Test
    fun testHeatExchangerDutyCalculation() {
        // Flow = 3600 kg/h, Cp = 2.0 kJ/kg·°C, Tin = 30°C, Tout = 130°C (deltaT = 100°C)
        // Q = 3600 * 2.0 * 100 = 720,000 kJ/h -> kW = 720,000 / 3600 = 200 kW
        viewModel.updateHeatInputs("3600.0", "2.0", "30.0", "130.0")
        val result = viewModel.calcHeatResult.value

        assertEquals(100.0, result.deltaTC, 0.1)
        assertEquals(200.0, result.heatDutyKw, 0.5)
    }

    @Test
    fun testMassBalanceAndYieldCalculation() {
        // Feed = 10,000 kg, Main Product = 9,100 kg, By-Product = 600 kg -> Loss = 300 kg (3%)
        viewModel.updateMassInputs("10000.0", "9100.0", "600.0")
        val result = viewModel.calcMassResult.value

        assertEquals(300.0, result.calculatedLossKg, 0.1)
        assertEquals(91.0, result.mainYieldPercent, 0.1)
        assertEquals(3.0, result.lossPercent, 0.1)
    }

    @Test
    fun testDiscoveryQuestionFactUpdate() {
        val initialQuestion = repository.discoveryQuestions.value.first()
        assertEquals(VerificationStatus.VERIFIED, initialQuestion.status)

        repository.updateDiscoveryAnswer(
            questionId = initialQuestion.id,
            newAnswer = "Updated historical deed record verified with municipal office",
            newStatus = VerificationStatus.ENGINEER_APPROVED,
            source = "Municipal Deed Archive Vol 4, Page 12"
        )

        val updatedQuestion = repository.discoveryQuestions.value.first { it.id == initialQuestion.id }
        assertEquals("Updated historical deed record verified with municipal office", updatedQuestion.currentAnswer)
        assertEquals(VerificationStatus.ENGINEER_APPROVED, updatedQuestion.status)
        assertEquals("Municipal Deed Archive Vol 4, Page 12", updatedQuestion.sourceAttribution)
    }

    @Test
    fun testSafetyPermitToWorkLifecycle() {
        val permits = repository.safetyPermits.value
        val pendingPermit = permits.first { it.status == PermitStatus.REQUESTED }

        // Authorize permit
        repository.updatePermitStatus(pendingPermit.permitId, PermitStatus.AUTHORIZED)
        val authorized = repository.safetyPermits.value.first { it.permitId == pendingPermit.permitId }
        assertEquals(PermitStatus.AUTHORIZED, authorized.status)

        // Close out permit
        repository.updatePermitStatus(pendingPermit.permitId, PermitStatus.CLOSED_OUT)
        val closed = repository.safetyPermits.value.first { it.permitId == pendingPermit.permitId }
        assertEquals(PermitStatus.CLOSED_OUT, closed.status)
    }

    @Test
    fun testBatchCostingEconomics() {
        val batchCost = repository.batchCosting.value.first()
        assertTrue(batchCost.totalCostInr > 0)
        assertTrue(batchCost.costPerKgInr > 0)
        assertTrue(batchCost.grossMarginPercent > 0)
    }

    @Test
    fun testLimsQualitySpecificationCheck() {
        val samples = repository.limsSamples.value
        val keroseneSample = samples.first { it.productName.contains("Kerosene") }
        assertEquals(QualityDisposition.RELEASED, keroseneSample.disposition)
        assertTrue(keroseneSample.tests.all { it.isInSpecification })
    }
}
