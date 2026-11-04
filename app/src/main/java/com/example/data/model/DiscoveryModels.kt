package com.example.data.model

enum class VerificationStatus(val label: String, val colorHex: Long) {
    VERIFIED("Verified by Record", 0xFF10B981),
    UNVERIFIED("User-Entered / Unverified", 0xFFF59E0B),
    NEEDS_VERIFICATION("Needs Field Verification", 0xFF3B82F6),
    UNKNOWN("Unknown / To Be Investigated", 0xFF94A3B8),
    ENGINEER_APPROVED("Engineer Sign-off", 0xFF8B5CF6)
}

data class DiscoveryQuestion(
    val id: String,
    val category: String,
    val question: String,
    val helpText: String,
    var currentAnswer: String,
    var status: VerificationStatus,
    var sourceAttribution: String,
    var lastUpdated: String
)

data class FamilyTimelineEvent(
    val year: String,
    val era: String,
    val title: String,
    val description: String,
    val verifiedSource: String,
    val status: VerificationStatus
)

data class BusinessMasterProfile(
    val entityName: String,
    val establishedYear: String,
    val currentFacilityLocation: String,
    val primaryProcessModel: String,
    val secondaryProcesses: List<String>,
    val verifiedProducts: List<String>,
    val keyEquipmentCount: Int,
    val totalTankCapacityKl: Double,
    val complianceStatusSummary: String,
    val digitizationPriorityScore: Int, // 0-100
    val verifiedFactsRatio: String
)
