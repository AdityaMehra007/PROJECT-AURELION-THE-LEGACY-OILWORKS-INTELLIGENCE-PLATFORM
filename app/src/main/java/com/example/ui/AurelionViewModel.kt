package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.AurelionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.roundToInt

enum class AppDestination(val label: String, val category: String) {
    DASHBOARD("Command Center", "Overview"),
    DISCOVERY("Business Discovery", "Strategy"),
    KNOWLEDGE("Engineering Center", "Engineering"),
    CALCULATORS("Industrial Calculators", "Engineering"),
    DIGITAL_TWIN("Factory Digital Twin", "Operations"),
    PRODUCTION_MES("Production MES", "Operations"),
    PRODUCT_SPECS("Kerosene & Wax Specs", "Operations"),
    INVENTORY_TANKS("Tanks & Inventory", "Supply Chain"),
    LIMS_QUALITY("Laboratory & LIMS", "Quality"),
    SAFETY_CMMS("Safety & Maintenance", "EHS & CMMS"),
    COMPLIANCE_FINANCE("Compliance & Costing", "Governance"),
    ARCHIVE_AI("Archive & AI Brain", "Knowledge")
}

// Calculators calculation result data class
data class UnitConversionResult(
    val apiGravity: Double,
    val specificGravity: Double,
    val densityKgM3: Double,
    val poundsPerGallon: Double
)

data class TankDipResult(
    val dipHeightMeters: Double,
    val rawVolumeKl: Double,
    val temperatureCorrectedVolumeKl: Double,
    val massMetricTons: Double,
    val capacityPercent: Double
)

data class HeatDutyResult(
    val massFlowKgH: Double,
    val specificHeatKjKgC: Double,
    val deltaTC: Double,
    val heatDutyKw: Double,
    val heatDutyKcalH: Double
)

data class MassBalanceResult(
    val feedChargedKg: Double,
    val mainProductKg: Double,
    val byProductKg: Double,
    val calculatedLossKg: Double,
    val mainYieldPercent: Double,
    val lossPercent: Double
)

class AurelionViewModel(
    private val repository: AurelionRepository = AurelionRepository()
) : ViewModel() {

    // Current navigation destination
    private val _currentDestination = MutableStateFlow(AppDestination.DASHBOARD)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    // Navigation back stack for BackHandler compliance
    private val navigationBackStack = mutableListOf<AppDestination>()

    // Current User Role
    val currentRole = repository.currentRole
    val isOfflineMode = repository.isOfflineMode

    // Data streams from repository
    val masterProfile = repository.masterProfile
    val discoveryQuestions = repository.discoveryQuestions
    val familyTimeline = repository.familyTimeline
    val equipmentAssets = repository.equipmentAssets
    val tankAssets = repository.tankAssets
    val productionBatches = repository.productionBatches
    val keroseneSpecs = repository.keroseneSpecs
    val waxSpecs = repository.waxSpecs
    val inventoryItems = repository.inventoryItems
    val reconciliationAudits = repository.reconciliationAudits
    val limsSamples = repository.limsSamples
    val safetyPermits = repository.safetyPermits
    val maintenanceOrders = repository.maintenanceOrders
    val complianceObligations = repository.complianceObligations
    val batchCosting = repository.batchCosting
    val knowledgeArticles = repository.knowledgeArticles
    val familyArchiveItems = repository.familyArchiveItems
    val aiChatMessages = repository.aiChatMessages

    // AI Chat persona selection
    private val _activeAiPersona = MutableStateFlow(AssistantPersona.OWNER_STRATEGIST)
    val activeAiPersona: StateFlow<AssistantPersona> = _activeAiPersona.asStateFlow()

    // --- CALCULATOR STATES ---
    // 1. API Gravity to Density
    var calcApiInput = MutableStateFlow("38.5")
    var calcUnitResult = MutableStateFlow(calculateApiConversion(38.5))

    // 2. Tank Dipping Volume
    var calcTankDiameter = MutableStateFlow("12.0") // meters
    var calcTankDip = MutableStateFlow("6.5") // meters
    var calcTankTemp = MutableStateFlow("35.0") // °C
    var calcTankDensity = MutableStateFlow("810.0") // kg/m³
    var calcTankResult = MutableStateFlow(calculateTankDip(12.0, 6.5, 35.0, 810.0))

    // 3. Heat Exchanger Duty
    var calcHeatFlow = MutableStateFlow("5000.0") // kg/h
    var calcHeatCp = MutableStateFlow("2.15") // kJ/kg.°C
    var calcHeatTin = MutableStateFlow("40.0") // °C
    var calcHeatTout = MutableStateFlow("165.0") // °C
    var calcHeatResult = MutableStateFlow(calculateHeatDuty(5000.0, 2.15, 40.0, 165.0))

    // 4. Mass Balance
    var calcMassFeed = MutableStateFlow("16200.0")
    var calcMassMain = MutableStateFlow("14680.0")
    var calcMassByProduct = MutableStateFlow("920.0")
    var calcMassResult = MutableStateFlow(calculateMassBalance(16200.0, 14680.0, 920.0))

    fun navigateTo(dest: AppDestination) {
        if (_currentDestination.value != dest) {
            navigationBackStack.add(_currentDestination.value)
            _currentDestination.value = dest
        }
    }

    fun handleBack(): Boolean {
        return if (navigationBackStack.isNotEmpty()) {
            _currentDestination.value = navigationBackStack.removeAt(navigationBackStack.size - 1)
            true
        } else if (_currentDestination.value != AppDestination.DASHBOARD) {
            _currentDestination.value = AppDestination.DASHBOARD
            true
        } else {
            false
        }
    }

    fun switchRole(role: UserRole) {
        repository.switchRole(role)
    }

    fun toggleOffline() {
        repository.toggleOfflineMode()
    }

    fun setAiPersona(persona: AssistantPersona) {
        _activeAiPersona.value = persona
    }

    fun sendAiPrompt(prompt: String) {
        if (prompt.isNotBlank()) {
            repository.sendAiMessage(prompt, _activeAiPersona.value)
        }
    }

    fun updateDiscoveryAnswer(questionId: String, newAnswer: String, newStatus: VerificationStatus, source: String) {
        repository.updateDiscoveryAnswer(questionId, newAnswer, newStatus, source)
    }

    fun advanceBatchStage(batchNumber: String) {
        repository.advanceBatchStage(batchNumber)
    }

    fun updatePermitStatus(permitId: String, newStatus: PermitStatus) {
        repository.updatePermitStatus(permitId, newStatus)
    }

    fun recordTankDip(tankTag: String, newLevelMeters: Double, newTempC: Double, newDensity: Double) {
        repository.recordTankDip(tankTag, newLevelMeters, newTempC, newDensity)
    }

    // --- CALCULATOR IMPLEMENTATION FUNCTIONS ---
    fun updateApiInput(apiStr: String) {
        calcApiInput.value = apiStr
        val api = apiStr.toDoubleOrNull() ?: 30.0
        calcUnitResult.value = calculateApiConversion(api)
    }

    private fun calculateApiConversion(api: Double): UnitConversionResult {
        // SG = 141.5 / (API + 131.5)
        val sg = 141.5 / (api + 131.5)
        val density = sg * 999.012
        val lbPerGal = sg * 8.3454
        return UnitConversionResult(
            apiGravity = (api * 100).roundToInt() / 100.0,
            specificGravity = (sg * 10000).roundToInt() / 10000.0,
            densityKgM3 = (density * 10).roundToInt() / 10.0,
            poundsPerGallon = (lbPerGal * 100).roundToInt() / 100.0
        )
    }

    fun updateTankInputs(diameterStr: String, dipStr: String, tempStr: String, densityStr: String) {
        calcTankDiameter.value = diameterStr
        calcTankDip.value = dipStr
        calcTankTemp.value = tempStr
        calcTankDensity.value = densityStr

        val dia = diameterStr.toDoubleOrNull() ?: 10.0
        val dip = dipStr.toDoubleOrNull() ?: 5.0
        val temp = tempStr.toDoubleOrNull() ?: 30.0
        val density = densityStr.toDoubleOrNull() ?: 800.0
        calcTankResult.value = calculateTankDip(dia, dip, temp, density)
    }

    private fun calculateTankDip(diameterM: Double, dipM: Double, tempC: Double, densityKgM3: Double): TankDipResult {
        val radius = diameterM / 2.0
        // Volume = pi * r^2 * h in cubic meters = KL
        val rawVolumeKl = PI * radius * radius * dipM
        // ASTM D1250 thermal expansion factor approximation for middle distillates (alpha ≈ 0.00085 / °C from 15°C)
        val deltaT = tempC - 15.0
        val vcf = 1.0 - (0.00085 * deltaT)
        val correctedVolume = rawVolumeKl * vcf
        // Mass = Volume (m³) * density (kg/m³) / 1000 = Metric Tons
        val massMt = (correctedVolume * densityKgM3) / 1000.0
        // Nominal max height = 10m
        val capacityPercent = ((dipM / 10.0) * 100.0).coerceIn(0.0, 100.0)

        return TankDipResult(
            dipHeightMeters = dipM,
            rawVolumeKl = (rawVolumeKl * 10).roundToInt() / 10.0,
            temperatureCorrectedVolumeKl = (correctedVolume * 10).roundToInt() / 10.0,
            massMetricTons = (massMt * 10).roundToInt() / 10.0,
            capacityPercent = (capacityPercent * 10).roundToInt() / 10.0
        )
    }

    fun updateHeatInputs(flowStr: String, cpStr: String, tinStr: String, toutStr: String) {
        calcHeatFlow.value = flowStr
        calcHeatCp.value = cpStr
        calcHeatTin.value = tinStr
        calcHeatTout.value = toutStr

        val flow = flowStr.toDoubleOrNull() ?: 1000.0
        val cp = cpStr.toDoubleOrNull() ?: 2.0
        val tin = tinStr.toDoubleOrNull() ?: 25.0
        val tout = toutStr.toDoubleOrNull() ?: 100.0
        calcHeatResult.value = calculateHeatDuty(flow, cp, tin, tout)
    }

    private fun calculateHeatDuty(flowKgH: Double, cpKjKgC: Double, tinC: Double, toutC: Double): HeatDutyResult {
        val deltaT = toutC - tinC
        // Q = m * Cp * deltaT in kJ/h -> kW = (kJ/h) / 3600
        val qKjH = flowKgH * cpKjKgC * deltaT
        val qKw = qKjH / 3600.0
        val qKcalH = qKjH / 4.184
        return HeatDutyResult(
            massFlowKgH = flowKgH,
            specificHeatKjKgC = cpKjKgC,
            deltaTC = deltaT,
            heatDutyKw = (qKw * 100).roundToInt() / 100.0,
            heatDutyKcalH = (qKcalH * 10).roundToInt() / 10.0
        )
    }

    fun updateMassInputs(feedStr: String, mainStr: String, byStr: String) {
        calcMassFeed.value = feedStr
        calcMassMain.value = mainStr
        calcMassByProduct.value = byStr

        val feed = feedStr.toDoubleOrNull() ?: 10000.0
        val main = mainStr.toDoubleOrNull() ?: 8000.0
        val byProduct = byStr.toDoubleOrNull() ?: 1500.0
        calcMassResult.value = calculateMassBalance(feed, main, byProduct)
    }

    private fun calculateMassBalance(feedKg: Double, mainKg: Double, byProductKg: Double): MassBalanceResult {
        val totalProducts = mainKg + byProductKg
        val lossKg = feedKg - totalProducts
        val mainYield = if (feedKg > 0) (mainKg / feedKg) * 100.0 else 0.0
        val lossPercent = if (feedKg > 0) (lossKg / feedKg) * 100.0 else 0.0

        return MassBalanceResult(
            feedChargedKg = feedKg,
            mainProductKg = mainKg,
            byProductKg = byProductKg,
            calculatedLossKg = (lossKg * 10).roundToInt() / 10.0,
            mainYieldPercent = (mainYield * 10).roundToInt() / 10.0,
            lossPercent = (lossPercent * 10).roundToInt() / 10.0
        )
    }
}
