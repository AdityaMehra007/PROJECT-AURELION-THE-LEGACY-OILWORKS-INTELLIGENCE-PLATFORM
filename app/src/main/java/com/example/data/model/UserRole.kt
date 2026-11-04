package com.example.data.model

enum class UserRole(
    val title: String,
    val description: String,
    val badge: String
) {
    OWNER_ADMIN(
        title = "Owner / Super Administrator",
        description = "Executive oversight, strategic capital, profitability, and governance",
        badge = "OWNER"
    ),
    FACTORY_MANAGER(
        title = "Factory Operations Manager",
        description = "Production scheduling, plant output, workforce, and supply chain",
        badge = "PLANT MGR"
    ),
    CHEMICAL_ENGINEER(
        title = "Senior Chemical Engineer",
        description = "Process calculations, thermodynamics, equipment rating, and troubleshooting",
        badge = "CHEM ENG"
    ),
    QUALITY_MANAGER(
        title = "Quality Control / LIMS Lead",
        description = "Laboratory testing, ASTM standards, specification approval, and CoA release",
        badge = "QC/LIMS"
    ),
    SAFETY_OFFICER(
        title = "EHS & Process Safety Officer",
        description = "Permit-to-Work, hazard assessments, PESO/OISD compliance, and emergency SOPs",
        badge = "EHS LEAD"
    ),
    MAINTENANCE_SUPERVISOR(
        title = "Reliability & Maintenance Lead",
        description = "CMMS work orders, equipment uptime, preventive routines, and spare parts",
        badge = "MAINTENANCE"
    ),
    PLANT_OPERATOR(
        title = "Plant Field Operator",
        description = "Shift logging, tank dips, batch execution, and immediate inspection checks",
        badge = "OPERATOR"
    )
}
