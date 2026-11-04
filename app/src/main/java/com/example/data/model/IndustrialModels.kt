package com.example.data.model

// --- FACILITY & DIGITAL TWIN ---
enum class AssetStatus(val label: String, val colorHex: Long) {
    OPERATIONAL("In Operation", 0xFF10B981),
    STANDBY("On Standby", 0xFF3B82F6),
    MAINTENANCE("Scheduled Maint", 0xFFF59E0B),
    OFFLINE("Offline / Isolated", 0xFFEF4444)
}

data class EquipmentAsset(
    val tag: String,
    val name: String,
    val category: String, // Distillation Column, Centrifugal Pump, Shell & Tube Heat Exchanger, Vacuum Filter, Boiler
    val area: String,
    val manufacturer: String,
    val modelOrSerial: String,
    val ratedCapacity: String,
    val status: AssetStatus,
    val lastInspectionDate: String,
    val nextInspectionDate: String,
    val verification: VerificationStatus,
    val documentationRef: String
)

data class TankAsset(
    val tag: String,
    val name: String,
    val serviceProduct: String,
    val maxCapacityKl: Double,
    val currentLevelMeters: Double,
    val maxLevelMeters: Double,
    val currentVolumeKl: Double,
    val temperatureCelsius: Double,
    val observedDensity: Double, // kg/m³
    val calibrationDate: String,
    val status: AssetStatus,
    val highLevelAlarmPercent: Double = 90.0
) {
    val fillPercentage: Double
        get() = (currentVolumeKl / maxCapacityKl * 100.0).coerceIn(0.0, 100.0)
}

// --- PRODUCTION & MES ---
enum class BatchStage(val label: String) {
    QUEUED("Scheduled"),
    CHARGING_FEED("Charging Feedstock"),
    FRACTIONATION("Atmospheric / Vacuum Fractionation"),
    TREATMENT("Chemical / Clay Treatment"),
    DEWAXING_SWEATING("Sweating / Deoiling"),
    HYDRO_FINISHING("Finishing & Stabilization"),
    QC_TESTING("QC Laboratory Quarantine"),
    PACKAGED("Packaged & Ready for Dispatch"),
    COMPLETED("Completed & Reconciled")
}

data class GenealogyStep(
    val stageName: String,
    val unitTag: String,
    val inputLot: String,
    val outputLot: String,
    val operatorId: String,
    val timestamp: String,
    val notes: String
)

data class ProductionBatch(
    val batchNumber: String,
    val workOrderId: String,
    val productName: String,
    val grade: String,
    val targetQuantityKg: Double,
    val actualProducedKg: Double,
    val feedQuantityChargedKg: Double,
    val currentStage: BatchStage,
    val startTime: String,
    val estimatedCompletion: String,
    val yieldPercentage: Double,
    val genealogyTrail: List<GenealogyStep>,
    val verifiedSpecRef: String,
    val approvalStatus: String
)

// --- SPECIALIZED PRODUCTS (KEROSENE & WAX) ---
data class KeroseneProductSpec(
    val gradeName: String, // e.g. "Superior Kerosene Oil (IS 1459)", "Industrial Kerosene Cut", "Aviation Fuel Component"
    val flashPointMinC: Double,
    val smokePointMinMm: Double,
    val distillation90RecoveryMaxC: Double,
    val totalSulfurMaxPercent: Double,
    val densityRangeKgM3: String,
    val authorizedMarkets: String,
    val storageClassification: String // PESO Class B Petroleum
)

data class WaxProductSpec(
    val gradeName: String, // e.g. "Paraffin Wax Semi-Refined 58/60", "Fully Refined Microcrystalline Grade A", "Slack Wax Light"
    val congealingPointRangeC: String,
    val oilContentMaxPercent: Double, // ASTM D721
    val needlePenetrationAt25C: String, // ASTM D1321 (dmm)
    val sayboltColorMin: String,
    val appearance: String,
    val packageTypes: String
)

// --- INVENTORY & RECONCILIATION ---
data class ItemMaster(
    val sku: String,
    val name: String,
    val category: String, // Raw Feed, Intermediate, Finished Kerosene, Finished Wax, Packaging, Reagent
    val unitOfMeasure: String,
    val quantityOnHand: Double,
    val reservedQuantity: Double,
    val reorderPoint: Double,
    val storageLocation: String,
    val lotNumber: String,
    val unitValuationInr: Double
)

data class ReconciliationAudit(
    val period: String,
    val materialName: String,
    val openingStock: Double,
    val receipts: Double,
    val consumedInProduction: Double,
    val dispatched: Double,
    val closingPhysicalDip: Double,
    val bookBalance: Double,
    val discrepancy: Double, // closingPhysical - bookBalance
    val tolerancePercent: Double,
    val status: String, // "RECONCILED", "DISCREPANCY_FLAGGED", "UNDER_INVESTIGATION"
    val reviewedBy: String
)

// --- LABORATORY & QUALITY (LIMS) ---
enum class QualityDisposition(val label: String, val colorHex: Long) {
    IN_TESTING("Testing In Progress", 0xFF3B82F6),
    RELEASED("Approved & Released", 0xFF10B981),
    QUARANTINE_HOLD("Quarantine / Hold", 0xFFF59E0B),
    REJECTED("Out of Spec (Rejected)", 0xFFEF4444),
    RETEST_REQUIRED("Retest Required", 0xFF8B5CF6)
}

data class LimsTestRecord(
    val testParameter: String,
    val testMethodStandard: String, // e.g. ASTM D86, ASTM D93, ASTM D721
    val specificationRange: String,
    val measuredValue: String,
    val isInSpecification: Boolean,
    val analystName: String,
    val instrumentTag: String,
    val calibratedStatus: Boolean
)

data class LimsSample(
    val sampleId: String,
    val batchOrLotNumber: String,
    val productName: String,
    val samplingPoint: String,
    val sampledBy: String,
    val timestamp: String,
    val tests: List<LimsTestRecord>,
    var disposition: QualityDisposition,
    val technicalReviewer: String,
    val coaNumber: String?
)

// --- MAINTENANCE (CMMS) & SAFETY (EHS / PTW) ---
enum class PermitType(val label: String) {
    HOT_WORK("Hot Work (Welding / Grinding)"),
    CONFINED_SPACE("Confined Space Entry (Tank Clean)"),
    COLD_WORK("Cold Mechanical Maintenance"),
    ELECTRICAL_LOTO("Electrical Isolation (LOTO)")
}

enum class PermitStatus(val label: String, val colorHex: Long) {
    REQUESTED("Requested", 0xFFF59E0B),
    AUTHORIZED("Authorized & Active", 0xFF10B981),
    SUSPENDED("Suspended", 0xFFEF4444),
    CLOSED_OUT("Closed & Verified", 0xFF94A3B8)
}

data class PermitToWork(
    val permitId: String,
    val type: PermitType,
    val plantLocation: String,
    val equipmentTag: String,
    val description: String,
    val gasTestPassed: Boolean,
    val lotoApplied: Boolean,
    val fireWatchAssigned: Boolean,
    val authorizingOfficer: String,
    val contractorOrTeam: String,
    val validUntil: String,
    var status: PermitStatus
)

data class MaintenanceWorkOrder(
    val id: String,
    val equipmentTag: String,
    val equipmentName: String,
    val priority: String, // "CRITICAL", "HIGH", "MEDIUM", "ROUTINE"
    val taskDescription: String,
    val isPreventive: Boolean,
    val assignedTech: String,
    val partsRequired: String,
    val status: String, // "PENDING", "IN_PROGRESS", "AWAITING_PARTS", "COMPLETED"
    val scheduledDate: String
)

// --- COMPLIANCE & FINANCIAL COSTING ---
data class ComplianceObligation(
    val topic: String,
    val authority: String, // PESO, OISD, CPCB, State Pollution Control Board, Factory Inspectorate
    val referenceCode: String,
    val summary: String,
    val licenseOrCertNumber: String,
    val expiryOrRenewalDate: String,
    val verifiedOfficialSourceUrl: String,
    val isCompliant: Boolean,
    val responsiblePerson: String,
    val lastAuditDate: String
)

data class BatchCostBreakdown(
    val batchNumber: String,
    val productName: String,
    val quantityKg: Double,
    val feedstockCostInr: Double,
    val thermalEnergyFuelInr: Double,
    val electricalPowerInr: Double,
    val chemicalAdditivesInr: Double,
    val directLaborInr: Double,
    val packagingInr: Double,
    val overheadsInr: Double,
    val sellingPricePerKgInr: Double
) {
    val totalCostInr: Double
        get() = feedstockCostInr + thermalEnergyFuelInr + electricalPowerInr +
                chemicalAdditivesInr + directLaborInr + packagingInr + overheadsInr

    val costPerKgInr: Double
        get() = if (quantityKg > 0) totalCostInr / quantityKg else 0.0

    val grossMarginPercent: Double
        get() {
            val revenue = sellingPricePerKgInr * quantityKg
            return if (revenue > 0) ((revenue - totalCostInr) / revenue) * 100.0 else 0.0
        }
}
