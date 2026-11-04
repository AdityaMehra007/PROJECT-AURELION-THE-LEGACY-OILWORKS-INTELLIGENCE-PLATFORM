package com.example.data.model

enum class KnowledgeCategory(val label: String, val iconName: String) {
    FUNDAMENTALS("Engineering Fundamentals", "science"),
    PETROLEUM_HYDROCARBONS("Petroleum & Hydrocarbons", "oil_barrel"),
    WAX_TECHNOLOGY("Wax Technology & Refining", "auto_awesome"),
    KEROSENE_DISTILLATES("Kerosene & Middle Distillates", "local_fire_department"),
    PLANT_ENGINEERING("Plant Equipment & P&ID", "precision_manufacturing"),
    INDUSTRIAL_MANAGEMENT("Costing & SPC Management", "trending_up")
}

data class KnowledgeArticle(
    val id: String,
    val title: String,
    val category: KnowledgeCategory,
    val plainLanguageSummary: String,
    val technicalExplanation: String,
    val governingEquations: String,
    val variablesAndUnits: String,
    val assumptionsAndLimits: String,
    val safetyAndEnvironmentalRules: String,
    val literatureSources: List<String>,
    val verifiedBy: String
)

enum class EngineeringAssuranceLevel(val label: String, val colorHex: Long) {
    EDUCATIONAL_EXAMPLE("Educational Reference", 0xFF94A3B8),
    PRELIMINARY_ESTIMATE("Preliminary Estimation", 0xFFF59E0B),
    OPERATIONAL_DATA("Operational Analysis", 0xFF3B82F6),
    ENGINEER_REVIEWED("PE Reviewed Calculation", 0xFF10B981)
}

data class FamilyArchiveItem(
    val id: String,
    val title: String,
    val dateOrEra: String,
    val authorOrSource: String,
    val category: String, // "Founder Notebook", "Handwritten Log", "Historical Photo", "Equipment Drawing", "P&ID"
    val description: String,
    val preservedWisdom: String,
    val modernSafetyReview: String,
    val verification: VerificationStatus
)

enum class AssistantPersona(val label: String, val roleDesc: String) {
    OWNER_STRATEGIST("Executive Owner Mode", "Business performance, margins, working capital, expansion, and risk management"),
    PLANT_MANAGER("Plant Operations Mode", "Shift scheduling, production bottlenecks, yield optimization, and throughput"),
    CHEMICAL_ENGINEER("Chemical Engineering Mode", "Fractionation theory, heat transfer, ASTM test methods, and process calculations"),
    QUALITY_LIMS("Quality & LIMS Mode", "Specifications, ASTM standards, CoA release, and Out-of-Specification resolution"),
    SAFETY_EHS("Process Safety & EHS Mode", "Permit-to-Work, PESO/OISD standards, LOTO, and emergency protocols")
}

data class AiChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: String,
    val persona: AssistantPersona,
    val groundedCitations: List<String> = emptyList(),
    val engineeringDisclaimer: String? = null
)
