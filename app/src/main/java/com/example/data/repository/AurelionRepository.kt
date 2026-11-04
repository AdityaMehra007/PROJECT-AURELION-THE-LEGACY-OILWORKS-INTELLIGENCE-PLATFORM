package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AurelionRepository {

    // Current Active Role for testing governance
    private val _currentRole = MutableStateFlow(UserRole.OWNER_ADMIN)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Offline / Online Status
    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

    // --- 1. BUSINESS DISCOVERY & PROFILE ---
    private val _masterProfile = MutableStateFlow(
        BusinessMasterProfile(
            entityName = "Aurelion Legacy Oilworks & Refining Ltd.",
            establishedYear = "1968",
            currentFacilityLocation = "Plot 42-B, Industrial Petro-Corridor, Gujarat / Mumbai Basin",
            primaryProcessModel = "Atmospheric Distillation, Solvent Dewaxing & Wax Finishing",
            secondaryProcesses = listOf(
                "Kerosene Fractionation & Acid-Clay Bleaching",
                "Slack Wax Sweating & Pressing",
                "Industrial Base Oil Blending & Lube Finishing",
                "Petroleum Bulk Storage & Decanting"
            ),
            verifiedProducts = listOf(
                "Superior Kerosene Oil (IS 1459 Standard)",
                "Paraffin Wax Semi-Refined 58/60°C",
                "Microcrystalline Industrial Wax Grade M",
                "Light Industrial Distillate (Process Oil 60)"
            ),
            keyEquipmentCount = 28,
            totalTankCapacityKl = 4850.0,
            complianceStatusSummary = "PESO Class B/C Storage Active; OISD-117 Fire Audit Valid; CPCB Consent up for renewal Q3",
            digitizationPriorityScore = 88,
            verifiedFactsRatio = "21 of 26 Documented (80.7%)"
        )
    )
    val masterProfile: StateFlow<BusinessMasterProfile> = _masterProfile.asStateFlow()

    private val _discoveryQuestions = MutableStateFlow(
        listOf(
            DiscoveryQuestion(
                id = "Q1_NAME",
                category = "Family & Heritage",
                question = "What was the founding business entity name and origin year?",
                helpText = "Confirm original registration records or grandfather's ledger",
                currentAnswer = "Aurelion Oil Trading Co., registered in 1968 by the patriarch, starting with retail kerosene distribution and wax block trade.",
                status = VerificationStatus.VERIFIED,
                sourceAttribution = "Municipal Deed & 1968 Registration Certificate #7842",
                lastUpdated = "2026-10-15"
            ),
            DiscoveryQuestion(
                id = "Q2_TECH",
                category = "Process Technology",
                question = "What refining and processing units are installed on site?",
                helpText = "Distinguish active plant from decommissioned pilot units",
                currentAnswer = "250 BPD Atmospheric Fractionation column, Solvent Dewaxing unit with chilling scrape-surface exchangers, Fuller's Earth clay contact tank, and hydraulic wax slab molding presses.",
                status = VerificationStatus.VERIFIED,
                sourceAttribution = "1994 Plant Expansion Blueprint (Father's engineering files)",
                lastUpdated = "2026-10-20"
            ),
            DiscoveryQuestion(
                id = "Q3_FEED",
                category = "Feedstock & Sourcing",
                question = "What primary hydrocarbon feedstocks are procured?",
                helpText = "Identify supplier names, ASTM specs, and delivery modes",
                currentAnswer = "Reduced Crude Oil (RCO) / Heavy Atmospheric Gas Oil cuts and Slack Wax feedstock sourced via rail/road tankers.",
                status = VerificationStatus.VERIFIED,
                sourceAttribution = "Refinery Supply Agreement 2024 & Delivery Gate Invoices",
                lastUpdated = "2026-10-22"
            ),
            DiscoveryQuestion(
                id = "Q4_WAX_CAP",
                category = "Wax Specialization",
                question = "What are the exact melt point and oil content capabilities for wax?",
                helpText = "Laboratory ASTM D721 verification required",
                currentAnswer = "Semi-refined paraffin wax achieves 58-60°C congealing point with oil content ≤ 1.5%. Microcrystalline wax achieves 72-76°C.",
                status = VerificationStatus.VERIFIED,
                sourceAttribution = "Certified External Test Report (SGS Inspection 2025)",
                lastUpdated = "2026-10-25"
            ),
            DiscoveryQuestion(
                id = "Q5_KEROSENE_REG",
                category = "Kerosene Compliance",
                question = "What PESO petroleum class licenses cover kerosene handling?",
                helpText = "Check storage license under Petroleum Rules 2002",
                currentAnswer = "Form XV license under PESO for Class B petroleum storage in bulk (Tanks TK-101 and TK-102) with 1,200 KL authorized capacity.",
                status = VerificationStatus.VERIFIED,
                sourceAttribution = "PESO Petroleum License #P/HQ/GJ/15/8920",
                lastUpdated = "2026-10-28"
            ),
            DiscoveryQuestion(
                id = "Q6_UNKNOWN_ASSET",
                category = "Factory Identity",
                question = "What is the certified burst pressure rating of Vacuum Vessel V-302?",
                helpText = "Nameplate is corroded, hydraulic test certificate missing",
                currentAnswer = "Original hydrostatic test record not found in legacy cabinet. De-rated conservatively to 3.5 bar pending third-party inspection.",
                status = VerificationStatus.NEEDS_VERIFICATION,
                sourceAttribution = "Father's handwritten maintenance log (1998)",
                lastUpdated = "2026-10-29"
            )
        )
    )
    val discoveryQuestions: StateFlow<List<DiscoveryQuestion>> = _discoveryQuestions.asStateFlow()

    private val _familyTimeline = MutableStateFlow(
        listOf(
            FamilyTimelineEvent(
                year = "1968",
                era = "Founding Era (1st Generation)",
                title = "The Founder Establishes Aurelion Oilworks",
                description = "Grandfather acquires first 2-acre plot, installs small direct-fired pot still and wooden wax settling pans.",
                verifiedSource = "Land Registry Document & First Sales Ledger",
                status = VerificationStatus.VERIFIED
            ),
            FamilyTimelineEvent(
                year = "1987",
                era = "Industrial Expansion (2nd Generation)",
                title = "Father Commissions Fractionation Column & Dewaxing",
                description = "The father graduates with chemical diploma, transitions company from trading to continuous distillation and filter pressing.",
                verifiedSource = "P&ID Drawings signed by Chief Engineer 1987",
                status = VerificationStatus.VERIFIED
            ),
            FamilyTimelineEvent(
                year = "2006",
                era = "Quality Modernization",
                title = "LIMS Laboratory & Solvent Chilling Installed",
                description = "Automated refrigerated scraping exchangers added for microcrystalline wax crystallization and ASTM testing bench.",
                verifiedSource = "Equipment Invoices & Calibration Logs",
                status = VerificationStatus.VERIFIED
            ),
            FamilyTimelineEvent(
                year = "2026",
                era = "Digital Twin & AI Era (3rd Generation)",
                title = "Aurelion Industrial OS Deployed",
                description = "Platform preserves heritage, enforces zero-trust safety, grounds chemical engineering calculations, and provides digital MES.",
                verifiedSource = "System Implementation Charter",
                status = VerificationStatus.ENGINEER_APPROVED
            )
        )
    )
    val familyTimeline: StateFlow<List<FamilyTimelineEvent>> = _familyTimeline.asStateFlow()

    // --- 2. FACILITY DIGITAL TWIN ---
    private val _equipmentAssets = MutableStateFlow(
        listOf(
            EquipmentAsset(
                tag = "T-101",
                name = "Atmospheric Fractionation Column",
                category = "Distillation Unit",
                area = "Process Area 1",
                manufacturer = "Bharat Heavy Vessels Ltd.",
                modelOrSerial = "COL-32-TRAY-87",
                ratedCapacity = "250 BPD / 16.5 MT/day",
                status = AssetStatus.OPERATIONAL,
                lastInspectionDate = "2026-08-14",
                nextInspectionDate = "2027-02-14",
                verification = VerificationStatus.ENGINEER_APPROVED,
                documentationRef = "Asset Manual T-101-REV3"
            ),
            EquipmentAsset(
                tag = "HE-102",
                name = "Kerosene Cut Shell & Tube Condenser",
                category = "Heat Exchanger",
                area = "Process Area 1",
                manufacturer = "Alpha Laval India",
                modelOrSerial = "STHE-45-KW",
                ratedCapacity = "185 kW thermal duty",
                status = AssetStatus.OPERATIONAL,
                lastInspectionDate = "2026-09-01",
                nextInspectionDate = "2027-03-01",
                verification = VerificationStatus.VERIFIED,
                documentationRef = "TEMA Data Sheet HE-102"
            ),
            EquipmentAsset(
                tag = "PU-204",
                name = "Dewaxed Oil Charge Centrifugal Pump",
                category = "Fluid Transfer",
                area = "Dewaxing Unit",
                manufacturer = "Kirloskar Brothers",
                modelOrSerial = "KPD-50/20",
                ratedCapacity = "45 m³/h @ 4.2 bar",
                status = AssetStatus.OPERATIONAL,
                lastInspectionDate = "2026-10-10",
                nextInspectionDate = "2026-12-10",
                verification = VerificationStatus.VERIFIED,
                documentationRef = "Pump Curve PK-204"
            ),
            EquipmentAsset(
                tag = "PR-301",
                name = "Hydraulic Wax Slab Molding Press",
                category = "Wax Finishing",
                area = "Wax Plant",
                manufacturer = "Hindustan Hydraulics",
                modelOrSerial = "HP-120-SLAB",
                ratedCapacity = "120 slabs/hr (5 kg slabs)",
                status = AssetStatus.STANDBY,
                lastInspectionDate = "2026-09-20",
                nextInspectionDate = "2027-01-20",
                verification = VerificationStatus.VERIFIED,
                documentationRef = "Operating SOP WX-301"
            ),
            EquipmentAsset(
                tag = "BO-401",
                name = "Packaged Steam Boiler (Furnace Oil)",
                category = "Utilities",
                area = "Utility Yard",
                manufacturer = "Thermax India",
                modelOrSerial = "REV-600",
                ratedCapacity = "600 kg/h steam @ 10 bar",
                status = AssetStatus.OPERATIONAL,
                lastInspectionDate = "2026-07-11",
                nextInspectionDate = "2026-11-15",
                verification = VerificationStatus.ENGINEER_APPROVED,
                documentationRef = "IBR Boiler Fitness Certificate #8821"
            )
        )
    )
    val equipmentAssets: StateFlow<List<EquipmentAsset>> = _equipmentAssets.asStateFlow()

    private val _tankAssets = MutableStateFlow(
        listOf(
            TankAsset(
                tag = "TK-101",
                name = "Raw Reduced Crude Storage",
                serviceProduct = "Reduced Crude Oil (RCO)",
                maxCapacityKl = 1200.0,
                currentLevelMeters = 7.42,
                maxLevelMeters = 9.50,
                currentVolumeKl = 938.0,
                temperatureCelsius = 38.5,
                observedDensity = 892.4,
                calibrationDate = "2025-11-10",
                status = AssetStatus.OPERATIONAL
            ),
            TankAsset(
                tag = "TK-102",
                name = "Superior Kerosene Cut Receiver",
                serviceProduct = "Superior Kerosene (IS 1459)",
                maxCapacityKl = 800.0,
                currentLevelMeters = 5.18,
                maxLevelMeters = 8.20,
                currentVolumeKl = 505.0,
                temperatureCelsius = 31.0,
                observedDensity = 804.2,
                calibrationDate = "2025-11-12",
                status = AssetStatus.OPERATIONAL
            ),
            TankAsset(
                tag = "TK-103",
                name = "Hot Molten Paraffin Wax Tank",
                serviceProduct = "Semi-Refined Paraffin 58/60",
                maxCapacityKl = 450.0,
                currentLevelMeters = 4.85,
                maxLevelMeters = 6.00,
                currentVolumeKl = 363.0,
                temperatureCelsius = 72.0,
                observedDensity = 788.0,
                calibrationDate = "2026-01-15",
                status = AssetStatus.OPERATIONAL
            ),
            TankAsset(
                tag = "TK-104",
                name = "Light Process Oil Distillate",
                serviceProduct = "Process Distillate Cut-2",
                maxCapacityKl = 600.0,
                currentLevelMeters = 2.10,
                maxLevelMeters = 7.00,
                currentVolumeKl = 180.0,
                temperatureCelsius = 33.2,
                observedDensity = 852.1,
                calibrationDate = "2025-09-20",
                status = AssetStatus.STANDBY
            )
        )
    )
    val tankAssets: StateFlow<List<TankAsset>> = _tankAssets.asStateFlow()

    // --- 3. PRODUCTION MES & BATCHES ---
    private val _productionBatches = MutableStateFlow(
        listOf(
            ProductionBatch(
                batchNumber = "BATCH-2026-104",
                workOrderId = "WO-KER-8891",
                productName = "Superior Kerosene Oil",
                grade = "IS 1459 Grade A",
                targetQuantityKg = 15000.0,
                actualProducedKg = 14680.0,
                feedQuantityChargedKg = 16200.0,
                currentStage = BatchStage.QC_TESTING,
                startTime = "2026-10-30 06:00",
                estimatedCompletion = "2026-10-30 18:30",
                yieldPercentage = 90.6,
                genealogyTrail = listOf(
                    GenealogyStep("RCO Feed Pumping", "TK-101", "LOT-RCO-941", "INT-FEED-01", "Operator Sharma", "06:15", "Feed rate 2.2 KL/h stabilized"),
                    GenealogyStep("Atmospheric Cut @ 175-265°C", "T-101", "INT-FEED-01", "INT-KER-01", "ChemEng Patel", "08:45", "Top reflux ratio 2.4:1"),
                    GenealogyStep("Fuller's Earth Clay Bleach", "CT-201", "INT-KER-01", "LOT-KER-104-FIN", "Operator Sharma", "12:30", "Color Saybolt +28 reached"),
                    GenealogyStep("QC Lab Quarantine", "LAB-01", "LOT-KER-104-FIN", "SMP-KER-992", "QC Analyst Mehta", "14:15", "ASTM D93 Flash point testing in progress")
                ),
                verifiedSpecRef = "IS 1459:2018 Controlled Copy Rev 4",
                approvalStatus = "Quarantined for QC Release"
            ),
            ProductionBatch(
                batchNumber = "BATCH-2026-105",
                workOrderId = "WO-WAX-4420",
                productName = "Semi-Refined Paraffin Wax",
                grade = "58/60 Slabs (Oil ≤ 1.5%)",
                targetQuantityKg = 8000.0,
                actualProducedKg = 7740.0,
                feedQuantityChargedKg = 8600.0,
                currentStage = BatchStage.DEWAXING_SWEATING,
                startTime = "2026-10-29 20:00",
                estimatedCompletion = "2026-10-31 08:00",
                yieldPercentage = 90.0,
                genealogyTrail = listOf(
                    GenealogyStep("Slack Wax Melting", "TK-103", "LOT-SLK-312", "INT-MOLTEN-02", "Operator Verma", "20:30", "Heated to 78°C via low-pressure steam"),
                    GenealogyStep("Sweating Chamber Chilling", "SW-101", "INT-MOLTEN-02", "INT-CAKE-01", "ChemEng Patel", "23:00", "Controlled gradient cooling 1.2°C/hr")
                ),
                verifiedSpecRef = "ASTM D721 / D1321 Standard",
                approvalStatus = "In Process"
            )
        )
    )
    val productionBatches: StateFlow<List<ProductionBatch>> = _productionBatches.asStateFlow()

    // --- 4. SPECIALIZED PRODUCT WORKSPACES ---
    val keroseneSpecs = listOf(
        KeroseneProductSpec(
            gradeName = "Superior Kerosene Oil (IS 1459:2018)",
            flashPointMinC = 35.0,
            smokePointMinMm = 18.0,
            distillation90RecoveryMaxC = 260.0,
            totalSulfurMaxPercent = 0.15,
            densityRangeKgM3 = "780 - 820 @ 15°C",
            authorizedMarkets = "Domestic Lighting, Industrial Burners & Authorized Distribution",
            storageClassification = "PESO Petroleum Class B (Flash point 23°C to 65°C)"
        ),
        KeroseneProductSpec(
            gradeName = "Industrial Solvent Kerosene (Special Cut)",
            flashPointMinC = 42.0,
            smokePointMinMm = 21.0,
            distillation90RecoveryMaxC = 240.0,
            totalSulfurMaxPercent = 0.05,
            densityRangeKgM3 = "795 - 815 @ 15°C",
            authorizedMarkets = "Pesticide Formulation, Printing Inks, Degreasing Solvents",
            storageClassification = "PESO Petroleum Class B"
        ),
        KeroseneProductSpec(
            gradeName = "Aviation Turbine Fuel Component (Jet A-1 baseline)",
            flashPointMinC = 38.0,
            smokePointMinMm = 25.0,
            distillation90RecoveryMaxC = 250.0,
            totalSulfurMaxPercent = 0.03,
            densityRangeKgM3 = "775 - 840 @ 15°C",
            authorizedMarkets = "Specialized Technical Review Only",
            storageClassification = "PESO Class B (High consequence specification)"
        )
    )

    val waxSpecs = listOf(
        WaxProductSpec(
            gradeName = "Paraffin Wax Semi-Refined 58/60",
            congealingPointRangeC = "58.0°C - 60.5°C (ASTM D938)",
            oilContentMaxPercent = 1.5,
            needlePenetrationAt25C = "16 - 22 dmm (ASTM D1321)",
            sayboltColorMin = "+25",
            appearance = "Solid white to translucent block, odorless",
            packageTypes = "5 kg slabs in 25 kg corrugated master cartons / pastilles"
        ),
        WaxProductSpec(
            gradeName = "Paraffin Wax Fully Refined 62/64",
            congealingPointRangeC = "62.0°C - 64.5°C",
            oilContentMaxPercent = 0.5,
            needlePenetrationAt25C = "12 - 16 dmm",
            sayboltColorMin = "+30",
            appearance = "Brilliant white crystalline slab",
            packageTypes = "Food packaging grade, cosmetic grade 5 kg slabs"
        ),
        WaxProductSpec(
            gradeName = "Microcrystalline Industrial Wax (Grade M-75)",
            congealingPointRangeC = "72.0°C - 78.0°C",
            oilContentMaxPercent = 2.5,
            needlePenetrationAt25C = "25 - 35 dmm (ductile / flexible)",
            sayboltColorMin = "+15 (Amber to light yellow)",
            appearance = "Flexible, high adhesion, fine micro-crystalline matrix",
            packageTypes = "Laminating wax for electrical insulation & rubber compounding"
        )
    )

    // --- 5. INVENTORY & RECONCILIATION ---
    private val _inventoryItems = MutableStateFlow(
        listOf(
            ItemMaster("SKU-FEED-01", "Reduced Crude Feedstock (RCO)", "Raw Feed", "KL", 938.0, 120.0, 300.0, "Tank TK-101", "LOT-RCO-941", 42500.0),
            ItemMaster("SKU-KER-IS1459", "Superior Kerosene (Bulk)", "Finished Kerosene", "KL", 505.0, 80.0, 150.0, "Tank TK-102", "LOT-KER-104-FIN", 58200.0),
            ItemMaster("SKU-WAX-5860", "Paraffin Wax 58/60 Slabs", "Finished Wax", "MT", 36.3, 10.0, 12.0, "Warehouse Bay 3", "LOT-WAX-26-88", 84500.0),
            ItemMaster("SKU-SLK-WAX", "Heavy Slack Wax Feed", "Intermediate", "MT", 48.0, 15.0, 20.0, "Tank TK-103", "LOT-SLK-312", 52000.0),
            ItemMaster("SKU-CHEM-CLAY", "Fuller's Earth Bleaching Clay", "Reagent", "MT", 14.5, 2.0, 5.0, "Chemical Store B", "LOT-CLAY-772", 18500.0),
            ItemMaster("SKU-PKG-CARTON", "Heavy Corrugated Cartons (25kg)", "Packaging", "Units", 1450.0, 200.0, 500.0, "Packaging Store", "LOT-BOX-101", 65.0)
        )
    )
    val inventoryItems: StateFlow<List<ItemMaster>> = _inventoryItems.asStateFlow()

    private val _reconciliationAudits = MutableStateFlow(
        listOf(
            ReconciliationAudit(
                period = "October 2026 (Month-to-Date)",
                materialName = "Superior Kerosene Cut (TK-102)",
                openingStock = 480.0,
                receipts = 0.0,
                consumedInProduction = 0.0,
                dispatched = 295.0,
                closingPhysicalDip = 505.0,
                bookBalance = 512.0, // 480 + 327 (production yield) - 295 = 512
                discrepancy = -7.0, // -7 KL variance (-1.36%)
                tolerancePercent = 0.5,
                status = "DISCREPANCY_FLAGGED",
                reviewedBy = "Finance Controller & Plant Manager"
            ),
            ReconciliationAudit(
                period = "October 2026 (Month-to-Date)",
                materialName = "Paraffin Wax Slabs (58/60)",
                openingStock = 28.5,
                receipts = 0.0,
                consumedInProduction = 0.0,
                dispatched = 42.0,
                closingPhysicalDip = 36.3,
                bookBalance = 36.5,
                discrepancy = -0.2, // -0.2 MT (-0.5%)
                tolerancePercent = 0.8,
                status = "RECONCILED",
                reviewedBy = "Storekeeper & Plant Auditor"
            )
        )
    )
    val reconciliationAudits: StateFlow<List<ReconciliationAudit>> = _reconciliationAudits.asStateFlow()

    // --- 6. LABORATORY & LIMS ---
    private val _limsSamples = MutableStateFlow(
        listOf(
            LimsSample(
                sampleId = "SMP-KER-992",
                batchOrLotNumber = "BATCH-2026-104",
                productName = "Superior Kerosene (IS 1459)",
                samplingPoint = "Run-Down Receiver TK-102",
                sampledBy = "Operator Sharma",
                timestamp = "2026-10-30 14:15",
                tests = listOf(
                    LimsTestRecord("Flash Point (Abel)", "ASTM D93 / IS 1448 P:20", "Min 35.0 °C", "38.5 °C", true, "QC Analyst Mehta", "ABEL-FLASH-01", true),
                    LimsTestRecord("Smoke Point", "ASTM D1322 / IS 1448 P:31", "Min 18.0 mm", "20.5 mm", true, "QC Analyst Mehta", "SMK-LAMP-02", true),
                    LimsTestRecord("Final Boiling Point", "ASTM D86", "Max 300.0 °C", "288.0 °C", true, "QC Analyst Mehta", "DISTILL-D86-01", true),
                    LimsTestRecord("Total Sulfur", "ASTM D4294", "Max 0.15 %", "0.08 %", true, "QC Analyst Mehta", "XRF-SULF-03", true),
                    LimsTestRecord("Copper Strip Corrosion", "ASTM D130 (3 hrs @ 50°C)", "Max Class 1", "Class 1a", true, "QC Analyst Mehta", "WATER-BATH-04", true)
                ),
                disposition = QualityDisposition.RELEASED,
                technicalReviewer = "Quality Head Dr. Rao",
                coaNumber = "COA-2026-KER-094"
            ),
            LimsSample(
                sampleId = "SMP-WAX-841",
                batchOrLotNumber = "BATCH-2026-103",
                productName = "Paraffin Wax 58/60 Slabs",
                samplingPoint = "Molding Line #1 Slabs",
                sampledBy = "Operator Verma",
                timestamp = "2026-10-29 11:30",
                tests = listOf(
                    LimsTestRecord("Congealing Point", "ASTM D938", "58.0 - 60.0 °C", "59.2 °C", true, "QC Analyst Mehta", "THERMO-M938", true),
                    LimsTestRecord("Oil Content", "ASTM D721 (MEK solvent)", "Max 1.50 %", "1.18 %", true, "QC Analyst Mehta", "EVAP-EXT-02", true),
                    LimsTestRecord("Needle Penetration @ 25°C", "ASTM D1321 (100g, 5s)", "16 - 22 dmm", "18.5 dmm", true, "QC Analyst Mehta", "PENETRO-01", true),
                    LimsTestRecord("Saybolt Color", "ASTM D156", "Min +25", "+28", true, "QC Analyst Mehta", "COLOR-SAY-01", true)
                ),
                disposition = QualityDisposition.RELEASED,
                technicalReviewer = "Quality Head Dr. Rao",
                coaNumber = "COA-2026-WAX-312"
            )
        )
    )
    val limsSamples: StateFlow<List<LimsSample>> = _limsSamples.asStateFlow()

    // --- 7. MAINTENANCE (CMMS) & SAFETY (PTW) ---
    private val _safetyPermits = MutableStateFlow(
        listOf(
            PermitToWork(
                permitId = "PTW-2026-044",
                type = PermitType.HOT_WORK,
                plantLocation = "Distillation Unit Area 1",
                equipmentTag = "HE-102",
                description = "Replacement of flange gasket on shell side steam bypass line with grinding and bolt torqueing.",
                gasTestPassed = true,
                lotoApplied = true,
                fireWatchAssigned = true,
                authorizingOfficer = "Safety Officer Nair",
                contractorOrTeam = "Mechanical Maintenance Team A",
                validUntil = "2026-10-30 18:00",
                status = PermitStatus.AUTHORIZED
            ),
            PermitToWork(
                permitId = "PTW-2026-045",
                type = PermitType.CONFINED_SPACE,
                plantLocation = "Tank Farm Area C",
                equipmentTag = "TK-104",
                description = "Manhole opening and internal wall inspection for sludge accumulation.",
                gasTestPassed = false,
                lotoApplied = true,
                fireWatchAssigned = false,
                authorizingOfficer = "Awaiting Safety Officer Nair",
                contractorOrTeam = "Certified Tank Cleaning Specialists",
                validUntil = "2026-10-31 16:00",
                status = PermitStatus.REQUESTED
            )
        )
    )
    val safetyPermits: StateFlow<List<PermitToWork>> = _safetyPermits.asStateFlow()

    private val _maintenanceOrders = MutableStateFlow(
        listOf(
            MaintenanceWorkOrder(
                id = "WO-MNT-892",
                equipmentTag = "PU-204",
                equipmentName = "Dewaxed Oil Charge Pump",
                priority = "HIGH",
                taskDescription = "Replace mechanical seal and inspect impeller clearance due to 0.4 mm/s vibration warning.",
                isPreventive = true,
                assignedTech = "Technician Dave",
                partsRequired = "Burgmann M377 Mechanical Seal, Viton O-rings",
                status = "IN_PROGRESS",
                scheduledDate = "2026-10-30"
            ),
            MaintenanceWorkOrder(
                id = "WO-MNT-893",
                equipmentTag = "BO-401",
                equipmentName = "Packaged Steam Boiler",
                priority = "CRITICAL",
                taskDescription = "Annual IBR safety relief valve bench testing and burner nozzle decoking.",
                isPreventive = true,
                assignedTech = "Senior Boilermaker Joshi",
                partsRequired = "Nozzle 4.5 GPH, Gasket kit 10 bar",
                status = "PENDING",
                scheduledDate = "2026-11-05"
            ),
            MaintenanceWorkOrder(
                id = "WO-MNT-894",
                equipmentTag = "PR-301",
                equipmentName = "Hydraulic Wax Slab Press",
                priority = "MEDIUM",
                taskDescription = "Clean cooling water jackets and replenish ISO VG 68 hydraulic oil.",
                isPreventive = false,
                assignedTech = "Technician Dave",
                partsRequired = "ISO VG 68 Oil (40 Liters)",
                status = "COMPLETED",
                scheduledDate = "2026-10-28"
            )
        )
    )
    val maintenanceOrders: StateFlow<List<MaintenanceWorkOrder>> = _maintenanceOrders.asStateFlow()

    // --- 8. COMPLIANCE & FINANCIAL COSTING ---
    private val _complianceObligations = MutableStateFlow(
        listOf(
            ComplianceObligation(
                topic = "Petroleum Storage Licensing",
                authority = "Petroleum and Explosives Safety Organisation (PESO)",
                referenceCode = "Form XV / Petroleum Rules 2002",
                summary = "Bulk storage authorization for Class B petroleum (Kerosene cuts) and Class C in designated above-ground dyked tanks.",
                licenseOrCertNumber = "PESO/P/HQ/GJ/15/8920",
                expiryOrRenewalDate = "2027-12-31",
                verifiedOfficialSourceUrl = "https://www.peso.gov.in/",
                isCompliant = true,
                responsiblePerson = "EHS Manager Nair",
                lastAuditDate = "2025-11-20"
            ),
            ComplianceObligation(
                topic = "Refinery Fire Protection Systems",
                authority = "Oil Industry Safety Directorate (OISD)",
                referenceCode = "OISD-STD-117",
                summary = "Fire water storage minimum 4 hours pumping capacity, fixed foam pourers on storage tanks TK-101 and TK-102.",
                licenseOrCertNumber = "OISD-AUDIT-2025-91",
                expiryOrRenewalDate = "2026-11-30",
                verifiedOfficialSourceUrl = "https://oisd.gov.in/oisd-standards",
                isCompliant = true,
                responsiblePerson = "Chief Safety Officer Nair",
                lastAuditDate = "2025-12-05"
            ),
            ComplianceObligation(
                topic = "Consent to Operate (Air & Water Effluent)",
                authority = "Central Pollution Control Board (CPCB) / SPCB",
                referenceCode = "Water Act 1974 & Air Act 1981",
                summary = "Effluent treatment plant (ETP) oil-water separator discharge limits and boiler flue gas stack monitoring.",
                licenseOrCertNumber = "PCB/CTO/2023/1182",
                expiryOrRenewalDate = "2026-12-15",
                verifiedOfficialSourceUrl = "https://cpcb.nic.in/",
                isCompliant = true,
                responsiblePerson = "Plant Manager Sharma",
                lastAuditDate = "2026-04-10"
            )
        )
    )
    val complianceObligations: StateFlow<List<ComplianceObligation>> = _complianceObligations.asStateFlow()

    private val _batchCosting = MutableStateFlow(
        listOf(
            BatchCostBreakdown(
                batchNumber = "BATCH-2026-104",
                productName = "Superior Kerosene Oil (14,680 kg)",
                quantityKg = 14680.0,
                feedstockCostInr = 688500.0,
                thermalEnergyFuelInr = 42300.0,
                electricalPowerInr = 18600.0,
                chemicalAdditivesInr = 8500.0,
                directLaborInr = 12500.0,
                packagingInr = 0.0, // Bulk pipeline/tanker
                overheadsInr = 24000.0,
                sellingPricePerKgInr = 68.50
            ),
            BatchCostBreakdown(
                batchNumber = "BATCH-2026-103",
                productName = "Paraffin Wax 58/60 Slabs (7,740 kg)",
                quantityKg = 7740.0,
                feedstockCostInr = 402480.0,
                thermalEnergyFuelInr = 31200.0,
                electricalPowerInr = 24500.0,
                chemicalAdditivesInr = 6800.0,
                directLaborInr = 18400.0,
                packagingInr = 20120.0, // Corrugated cartons & liners
                overheadsInr = 19500.0,
                sellingPricePerKgInr = 88.00
            )
        )
    )
    val batchCosting: StateFlow<List<BatchCostBreakdown>> = _batchCosting.asStateFlow()

    // --- 9. CHEMICAL ENGINEERING KNOWLEDGE CENTER ---
    val knowledgeArticles = listOf(
        KnowledgeArticle(
            id = "ART-FUND-01",
            title = "Material & Energy Balances in Petroleum Fractionation",
            category = KnowledgeCategory.FUNDAMENTALS,
            plainLanguageSummary = "Everything that enters the distillation column must exit somewhere—either as top vapor (kerosene/naphtha), side draws, bottom residue, or waste losses. Energy balance accounts for the heat put into the reboiler versus heat pulled by condensers.",
            technicalExplanation = "For continuous or batch distillation at steady state: Accumulation = Input - Output + Generation - Consumption. With non-reactive hydrocarbon cuts, Generation = 0. The overall mass balance is: F = D + B + L, where F is feed mass rate, D is overhead distillate, B is bottoms residue, and L is vapor/liquid loss.",
            governingEquations = "Mass: F = D + B + L\nComponent: F·z_i = D·x_{D,i} + B·x_{B,i}\nEnergy: Q_reboiler + F·h_f = Q_condenser + D·h_d + B·h_b + Q_loss",
            variablesAndUnits = "F, D, B in kg/h or MT/day; h (enthalpy) in kJ/kg; Q (thermal duty) in kW or kcal/h; z_i, x_i mole/mass fractions.",
            assumptionsAndLimits = "Assumes negligible chemical cracking below 350°C. Heavy crudes with high asphalt content require vacuum operation to avoid coking.",
            safetyAndEnvironmentalRules = "Never operate distillation columns beyond rated relief pressure. Maintain pressure relief valve (PRV) setpoints and nitrogen blanket on distillate receivers.",
            literatureSources = listOf("Perry's Chemical Engineers' Handbook (9th Ed.), Sec. 13", "Coulson & Richardson Vol. 6 - Chemical Engineering Design"),
            verifiedBy = "Senior Chemical Engineer Review (PE #4892)"
        ),
        KnowledgeArticle(
            id = "ART-PETRO-02",
            title = "Kerosene & Middle Distillate Boiling Ranges & Specifications",
            category = KnowledgeCategory.KEROSENE_DISTILLATES,
            plainLanguageSummary = "Kerosene is a middle distillate petroleum fraction boiling roughly between 150°C and 280°C. Key quality metrics are flash point (fire safety during handling) and smoke point (clean, soot-free burning).",
            technicalExplanation = "True Boiling Point (TBP) cuts determine the yield of kerosene. The initial boiling point (IBP) controls the flash point (governed by lighter C9-C10 alkanes), while the 90% or end boiling point (FBP) determines viscosity, freeze point, and smoke formation tendencies (higher aromatics degrade smoke point).",
            governingEquations = "Flash Point Correlation (Pensky-Martens / Abel):\nT_flash ≈ a · T_{10%} + b (ASTM D93)\nSmoke Point Index = f(Aromatic Vol% / Naphthenes)",
            variablesAndUnits = "T_flash (°C, min 35°C for IS 1459); Smoke Point (mm, min 18 mm); Density @ 15°C (775-820 kg/m³).",
            assumptionsAndLimits = "Only applies to straight-run or hydrotreated middle distillates. Cracker stocks require saturation to meet color and odor stability.",
            safetyAndEnvironmentalRules = "Class B Petroleum storage rules apply. Static electricity hazards require bonding and grounding during truck/drum filling at pumping speeds < 1 m/s initially.",
            literatureSources = listOf("Wauquier, J.P. - Petroleum Refining Vol. 1", "Bureau of Indian Standards IS 1459:2018 Kerosene Spec"),
            verifiedBy = "Senior Refinery Technologist"
        ),
        KnowledgeArticle(
            id = "ART-WAX-03",
            title = "Petroleum Wax Technology: Paraffin vs Microcrystalline Waxes",
            category = KnowledgeCategory.WAX_TECHNOLOGY,
            plainLanguageSummary = "Paraffin waxes are primarily straight-chain normal alkanes (C20-C35) that form large, brittle crystals and have sharp melting points (48-65°C). Microcrystalline waxes contain branched and cyclic hydrocarbons (C30-C60), forming tiny micro-crystals with high flexibility, higher melting points (65-85°C), and high oil-holding capacity.",
            technicalExplanation = "Separation of paraffin wax from lube distillates utilizes chilling and solvent dewaxing (MEK/Toluene or MIBK) or thermal sweating. De-oiling reduces the residual oil content below 1.5% for semi-refined or 0.5% for fully refined grades. Needle penetration (ASTM D1321) measures hardness; congealing point (ASTM D938) measures solid-liquid transition.",
            governingEquations = "Oil Content % (ASTM D721) = [(W_residue - W_blank) / W_sample] × 100\nWax Crystallization Heat: Q = m · ΔH_fusion (ΔH_fus ≈ 200 - 220 kJ/kg)",
            variablesAndUnits = "Oil content in wt%; Needle penetration in 0.1 mm (dmm); Congealing point in °C.",
            assumptionsAndLimits = "Slack wax feedstock must have controlled boiling range. Sweating is only effective on straight-chain macrocrystalline paraffin waxes.",
            safetyAndEnvironmentalRules = "Maintain molten wax lines with steam tracing (60-80°C). Molten wax burns can cause deep thermal injury—wear heat-resistant gloves and face shields.",
            literatureSources = listOf("Freund, M. et al. - Paraffin Products: Properties, Technologies, and Applications", "ASTM D721 & ASTM D1321"),
            verifiedBy = "Chief Wax Formulator"
        ),
        KnowledgeArticle(
            id = "ART-PLANT-04",
            title = "Centrifugal Pump Curves, NPSH & Cavitation in Hydrocarbon Transfer",
            category = KnowledgeCategory.PLANT_ENGINEERING,
            plainLanguageSummary = "If the pressure at the suction of a pump drops below the vapor pressure of the hot oil, vapor bubbles form and violently collapse on the impeller blades (cavitation), destroying the metal and cutting flow.",
            technicalExplanation = "NPSH_available = P_suction + (v²/2g) - P_vapor. For reliable operation without cavitation, NPSH_available must exceed NPSH_required by the pump manufacturer by at least 0.6 to 1.0 meter safety margin.",
            governingEquations = "NPSH_a = (P_surface - P_vapor)/(ρ·g) + Z_liquid - h_friction_losses\nP_shaft = (Q · H · ρ · g) / (3600 · η)",
            variablesAndUnits = "P in Pa; ρ in kg/m³; g in 9.81 m/s²; H (head) in meters; Q in m³/h; η in pump efficiency (0.6-0.85); P_shaft in kW.",
            assumptionsAndLimits = "Calculations must account for hydrocarbon viscosity correction factors (Hydraulic Institute charts) when pumping viscous residues.",
            safetyAndEnvironmentalRules = "Hydrocarbon pump seal failure is the #1 cause of refinery fires. Dual mechanical seals with pressurized API Plan 53 barrier fluid required for light distillate services.",
            literatureSources = listOf("API Standard 610 - Centrifugal Pumps for Petroleum, Petrochemical and Natural Gas Industries"),
            verifiedBy = "Reliability Engineer"
        )
    )

    // --- 10. FAMILY KNOWLEDGE ARCHIVE ---
    val familyArchiveItems = listOf(
        FamilyArchiveItem(
            id = "DOC-HIST-01",
            title = "Founder's 1968 Distillation Notebook (Scan & Transcript)",
            dateOrEra = "1968 - 1974",
            authorOrSource = "Founder Patriarch (Handwritten Gujarati & English Notes)",
            category = "Founder Notebook",
            description = "Detailed empirical observations on pot still firing rates using wood and furnace oil, color changes during kerosene condensation, and seasonal wax solidification in brass trays.",
            preservedWisdom = "Golden rule: 'When the vapor turns from pale yellow to light straw, cut the receiver immediately to avoid contaminating the kerosene cut with heavier lube fractions.'",
            modernSafetyReview = "Modern analysis confirms: this corresponds precisely to 260°C cut-point where ASTM D86 distillation tail begins and flash point drops if contaminated with naphtha.",
            verification = VerificationStatus.VERIFIED
        ),
        FamilyArchiveItem(
            id = "DOC-HIST-02",
            title = "1987 Fractionation Column Blueprint & Tray Hydraulic Calcs",
            dateOrEra = "1987",
            authorOrSource = "Father (Diploma Chemical Project & Plant Drawings)",
            category = "Equipment Drawing",
            description = "Original mechanical drafting of T-101 32-tray sieve column with downcomer dimensions, weir heights (50 mm), and bubble caps on bottom stripping trays.",
            preservedWisdom = "Weir liquid height maintained at 50 mm ensures sufficient liquid hold-up on each tray for vapor-liquid mass transfer equilibrium.",
            modernSafetyReview = "Column shell thickness verified by ultrasonic NDT testing in 2025; maximum allowable working pressure (MAWP) certified at 2.5 bar gauge.",
            verification = VerificationStatus.ENGINEER_APPROVED
        ),
        FamilyArchiveItem(
            id = "DOC-HIST-03",
            title = "Fuller's Earth Clay Bleaching Formula & Contact Time Log",
            dateOrEra = "1994",
            authorOrSource = "Senior Plant Chemist & Father",
            category = "Handwritten Log",
            description = "Temperature-versus-clay dose chart for achieving Saybolt +28 color on high-sulfur kerosene cuts without oxidizing the oil.",
            preservedWisdom = "Optimum clay dosage is 1.2% by weight at 85°C for 25 minutes of high-shear agitation, followed by plate-and-frame filtration through pre-coated diatomaceous earth.",
            modernSafetyReview = "Standardized in current SOP-CLAY-04; caution: spent clay can undergo pyrophoric auto-oxidation if left unwashed in contact with air.",
            verification = VerificationStatus.VERIFIED
        )
    )

    // --- 11. INDUSTRIAL AI CHAT (GROUNDED) ---
    private val _aiChatMessages = MutableStateFlow(
        listOf(
            AiChatMessage(
                id = "MSG-SYS-INIT",
                isUser = false,
                text = "Welcome to AURELION Industrial Intelligence Brain. I am your specialized chemical engineering and oilworks operations assistant.\n\nSelect a specialized role mode above (Executive Owner, Plant Manager, Chemical Engineer, QC/LIMS, or EHS Safety). Every recommendation is grounded strictly in verified factory records, ASTM/OISD standards, and plant data.",
                timestamp = "11:30",
                persona = AssistantPersona.OWNER_STRATEGIST,
                groundedCitations = listOf("Aurelion Master Profile", "IS 1459:2018", "OISD-STD-117")
            )
        )
    )
    val aiChatMessages: StateFlow<List<AiChatMessage>> = _aiChatMessages.asStateFlow()

    // --- USER ACTIONS & SIMULATION ENGINES ---
    fun switchRole(newRole: UserRole) {
        _currentRole.value = newRole
    }

    fun toggleOfflineMode() {
        _isOfflineMode.value = !_isOfflineMode.value
    }

    fun updateDiscoveryAnswer(questionId: String, newAnswer: String, newStatus: VerificationStatus, source: String) {
        _discoveryQuestions.value = _discoveryQuestions.value.map { q ->
            if (q.id == questionId) {
                q.copy(
                    currentAnswer = newAnswer,
                    status = newStatus,
                    sourceAttribution = source,
                    lastUpdated = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                )
            } else q
        }
    }

    fun addPermitToWork(permit: PermitToWork) {
        _safetyPermits.value = listOf(permit) + _safetyPermits.value
    }

    fun updatePermitStatus(permitId: String, newStatus: PermitStatus) {
        _safetyPermits.value = _safetyPermits.value.map { p ->
            if (p.permitId == permitId) p.copy(status = newStatus) else p
        }
    }

    fun recordTankDip(tankTag: String, newLevelMeters: Double, newTempC: Double, newDensity: Double) {
        _tankAssets.value = _tankAssets.value.map { tank ->
            if (tank.tag == tankTag) {
                // Cylindrical approximation: Volume proportional to level
                val estimatedVolume = (newLevelMeters / tank.maxLevelMeters) * tank.maxCapacityKl
                tank.copy(
                    currentLevelMeters = newLevelMeters,
                    currentVolumeKl = (estimatedVolume * 10.0).toInt() / 10.0,
                    temperatureCelsius = newTempC,
                    observedDensity = newDensity
                )
            } else tank
        }
    }

    fun advanceBatchStage(batchNumber: String) {
        _productionBatches.value = _productionBatches.value.map { b ->
            if (b.batchNumber == batchNumber) {
                val stages = BatchStage.values()
                val nextIndex = (b.currentStage.ordinal + 1).coerceAtMost(stages.size - 1)
                b.copy(currentStage = stages[nextIndex])
            } else b
        }
    }

    fun sendAiMessage(prompt: String, persona: AssistantPersona) {
        val userMsg = AiChatMessage(
            id = UUID.randomUUID().toString(),
            isUser = true,
            text = prompt,
            timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            persona = persona
        )

        // Generate rigorous grounded response based on persona and Aurelion database
        val replyText: String
        val citations: List<String>
        val disclaimer: String

        when (persona) {
            AssistantPersona.OWNER_STRATEGIST -> {
                replyText = "Executive Summary for Aurelion Oilworks:\n\n" +
                        "1. Gross Margin Analysis: Kerosene Batch #104 achieved 31.8% gross margin, while Paraffin Wax #103 yielded 34.6%. Wax continues to provide superior contribution margin per MT.\n" +
                        "2. Capital Utilization: Tank farm capacity is at 74.2% (3,601 KL in use). TK-102 holds 505 KL of approved Superior Kerosene ready for bulk dispatch.\n" +
                        "3. Critical Risk Flag: Storage Tank TK-102 shows a -7 KL (-1.36%) reconciliation variance this period exceeding the 0.5% tolerance. Audit investigation is mandated prior to financial closing."
                citations = listOf("Batch Costing Model (BATCH-104)", "Reconciliation Audit MTD Oct 2026", "Tank Farm Register")
                disclaimer = "Financial figures reflect operational standard costing. Statutory ledger reconciliation pending monthly sign-off."
            }
            AssistantPersona.CHEMICAL_ENGINEER -> {
                replyText = "Engineering Analysis & Process Review:\n\n" +
                        "1. Fractionation Reflux Ratio: Operating T-101 at 2.4:1 reflux maintains the 90% recovery point at 258°C, comfortably below the 260°C maximum specified in IS 1459.\n" +
                        "2. Condenser Heat Duty: At a kerosene overhead rate of 620 kg/h with latent heat of vaporization λ ≈ 310 kJ/kg, condenser HE-102 is operating at ~115 kW duty (rated for 185 kW, 62% load).\n" +
                        "3. Dewaxing Yield: Slack wax sweating cooling gradient must not exceed 1.2°C/hr to prevent oil entrapment in the paraffin crystal lattice."
                citations = listOf("T-101 Tray Hydraulic Design (DOC-HIST-02)", "IS 1459:2018 Technical Specs", "Perry's Chem Eng Handbook Sec 13")
                disclaimer = "Engineering estimation only. High-consequence setpoint modifications require formal Management of Change (MOC) review."
            }
            AssistantPersona.QUALITY_LIMS -> {
                replyText = "Quality & LIMS Laboratory Report:\n\n" +
                        "1. Sample SMP-KER-992 from Batch #104 has passed all 5 mandatory ASTM parameters: Flash Point is 38.5°C (Min 35.0°C), Smoke Point is 20.5 mm (Min 18 mm), Copper Corrosion is Class 1a.\n" +
                        "2. CoA #COA-2026-KER-094 is signed by Technical Reviewer Dr. Rao and released for customer delivery.\n" +
                        "3. Note on Paraffin Wax #103: Oil content achieved 1.18% (ASTM D721), compliant with Semi-Refined 58/60 grade (limit ≤ 1.50%)."
                citations = listOf("LIMS Sample Record SMP-KER-992", "ASTM D93 / D1322 Standard Methods", "CoA #COA-2026-KER-094")
                disclaimer = "All release dispositions are bound to verified instrument calibration logs."
            }
            AssistantPersona.SAFETY_EHS -> {
                replyText = "Safety Management & EHS Status:\n\n" +
                        "1. Hot Work Permit PTW-2026-044 on HE-102 is active until 18:00 with continuous LOTO and explosive gas test verified at 0.0% LEL.\n" +
                        "2. Confined Space Permit PTW-2026-045 for TK-104 is currently HELD because oxygen/toxic gas testing has not yet been executed.\n" +
                        "3. PESO Class B storage license #P/HQ/GJ/15/8920 is active through 2027. OISD-STD-117 fire water storage is at 100% capacity."
                citations = listOf("PESO Petroleum Rules 2002 Form XV", "OISD-STD-117 Fire Protection Standard", "PTW Register PTW-2026-044")
                disclaimer = "AI cannot authorize hot work or override safety interlocks. Physical gas monitoring by a competent officer is mandatory."
            }
            AssistantPersona.PLANT_MANAGER -> {
                replyText = "Plant Operations & Shift Briefing:\n\n" +
                        "1. Shift Throughput: Batch-104 (Kerosene) is in QC quarantine, yielding 14,680 kg (90.6% yield on feed). Batch-105 (Wax) is midway through cooling.\n" +
                        "2. Bottleneck: Pump PU-204 scheduled for seal replacement this evening. Transfer line diverted to Standby Pump PU-205.\n" +
                        "3. Warehouse: Finished wax inventory at 36.3 MT with 10 MT reserved for Mumbai distributor dispatch tomorrow."
                citations = listOf("MES Batch Execution Log", "CMMS Work Order WO-MNT-892", "Item Master SKU-WAX-5860")
                disclaimer = "Production schedules are subject to raw material quality release."
            }
        }

        val aiMsg = AiChatMessage(
            id = UUID.randomUUID().toString(),
            isUser = false,
            text = replyText,
            timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            persona = persona,
            groundedCitations = citations,
            engineeringDisclaimer = disclaimer
        )

        _aiChatMessages.value = _aiChatMessages.value + listOf(userMsg, aiMsg)
    }
}
