package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EngineeringAssuranceLevel
import com.example.ui.AurelionViewModel
import com.example.ui.theme.*

@Composable
fun CalculatorsScreen(viewModel: AurelionViewModel) {
    var selectedCalculatorTab by remember { mutableStateOf(0) }
    val tabs = listOf("API & Density", "Tank Strapping", "Heat Duty Q", "Mass Balance")

    val apiInput by viewModel.calcApiInput.collectAsState()
    val unitResult by viewModel.calcUnitResult.collectAsState()

    val tankDia by viewModel.calcTankDiameter.collectAsState()
    val tankDip by viewModel.calcTankDip.collectAsState()
    val tankTemp by viewModel.calcTankTemp.collectAsState()
    val tankDensity by viewModel.calcTankDensity.collectAsState()
    val tankResult by viewModel.calcTankResult.collectAsState()

    val heatFlow by viewModel.calcHeatFlow.collectAsState()
    val heatCp by viewModel.calcHeatCp.collectAsState()
    val heatTin by viewModel.calcHeatTin.collectAsState()
    val heatTout by viewModel.calcHeatTout.collectAsState()
    val heatResult by viewModel.calcHeatResult.collectAsState()

    val massFeed by viewModel.calcMassFeed.collectAsState()
    val massMain by viewModel.calcMassMain.collectAsState()
    val massBy by viewModel.calcMassByProduct.collectAsState()
    val massResult by viewModel.calcMassResult.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(IndustrialNavyDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- HEADER ---
        item {
            Column {
                Text(
                    text = "ENGINEERING CALCULATIONS WORKSPACE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberCopperBright,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Industrial Scientific Tools",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Deterministic, unit-consistent arithmetic engine for refinery and wax plant operations.",
                    fontSize = 12.sp,
                    color = TextSecondaryDark
                )
            }
        }

        // --- TAB SELECTOR ---
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedCalculatorTab,
                containerColor = IndustrialNavyCard,
                contentColor = AmberCopperBright,
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedCalculatorTab == index,
                        onClick = { selectedCalculatorTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedCalculatorTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag("calc_tab_$index")
                    )
                }
            }
        }

        // --- TAB CONTENT ---
        when (selectedCalculatorTab) {
            // TAB 0: API GRAVITY & DENSITY
            0 -> {
                item {
                    AssuranceBanner(
                        level = EngineeringAssuranceLevel.OPERATIONAL_DATA,
                        formula = "SG @ 60°F = 141.5 / (API + 131.5)  •  Density = SG × 999.012 kg/m³"
                    )
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Petroleum Density & Gravity Converter",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = apiInput,
                                onValueChange = { viewModel.updateApiInput(it) },
                                label = { Text("Observed API Gravity (°API)") },
                                trailingIcon = { Text("°API", color = AmberCopperBright, modifier = Modifier.padding(end = 12.dp)) },
                                modifier = Modifier.fillMaxWidth().testTag("api_input_field"),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = IndustrialBorder)
                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "CALCULATED PETROPHYSICAL PROPERTIES:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberCopperBright
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            CalcResultRow(label = "Specific Gravity (SG @ 60°F)", value = "${unitResult.specificGravity}")
                            CalcResultRow(label = "Standard Density @ 15°C", value = "${unitResult.densityKgM3} kg/m³")
                            CalcResultRow(label = "Weight (lb/US Gallon)", value = "${unitResult.poundsPerGallon} lb/gal")

                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                color = IndustrialNavySurface,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Classification: ${if (unitResult.apiGravity > 31.1) "Light Crude / Middle Distillate (Kerosene cut)" else if (unitResult.apiGravity > 22.3) "Medium Petroleum Fraction" else "Heavy Fuel / Residue"}",
                                    fontSize = 11.sp,
                                    color = AmberCopperLight,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }
            }

            // TAB 1: TANK STRAPPING & DIPPING
            1 -> {
                item {
                    AssuranceBanner(
                        level = EngineeringAssuranceLevel.OPERATIONAL_DATA,
                        formula = "Raw Vol = π·(D/2)²·H  •  VCF = 1 - 0.00085·(T - 15°C)  •  Mass = Vol·ρ / 1000"
                    )
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Vertical Storage Tank Dipping Estimator",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = tankDia,
                                    onValueChange = { viewModel.updateTankInputs(it, tankDip, tankTemp, tankDensity) },
                                    label = { Text("Tank Dia (m)") },
                                    modifier = Modifier.weight(1f).testTag("tank_dia_field"),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = tankDip,
                                    onValueChange = { viewModel.updateTankInputs(tankDia, it, tankTemp, tankDensity) },
                                    label = { Text("Liquid Dip (m)") },
                                    modifier = Modifier.weight(1f).testTag("tank_dip_field"),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = tankTemp,
                                    onValueChange = { viewModel.updateTankInputs(tankDia, tankDip, it, tankDensity) },
                                    label = { Text("Temp (°C)") },
                                    modifier = Modifier.weight(1f).testTag("tank_temp_field"),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = tankDensity,
                                    onValueChange = { viewModel.updateTankInputs(tankDia, tankDip, tankTemp, it) },
                                    label = { Text("Density (kg/m³)") },
                                    modifier = Modifier.weight(1f).testTag("tank_density_field"),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = IndustrialBorder)
                            Spacer(modifier = Modifier.height(14.dp))

                            CalcResultRow(label = "Physical Liquid Volume (Raw)", value = "${tankResult.rawVolumeKl} KL (m³)")
                            CalcResultRow(label = "Temperature Corrected Vol @ 15°C", value = "${tankResult.temperatureCorrectedVolumeKl} KL")
                            CalcResultRow(label = "Calculated Hydrocarbon Mass", value = "${tankResult.massMetricTons} Metric Tons")
                            CalcResultRow(label = "Tank Capacity Utilization", value = "${tankResult.capacityPercent}%")
                        }
                    }
                }
            }

            // TAB 2: HEAT EXCHANGER DUTY
            2 -> {
                item {
                    AssuranceBanner(
                        level = EngineeringAssuranceLevel.ENGINEER_REVIEWED,
                        formula = "Q = ṁ · Cp · (T_out - T_in)  •  Thermal Duty in kW & kcal/h"
                    )
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Sensible Heat Exchanger Duty Calculator",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = heatFlow,
                                    onValueChange = { viewModel.updateHeatInputs(it, heatCp, heatTin, heatTout) },
                                    label = { Text("Flow (kg/h)") },
                                    modifier = Modifier.weight(1f).testTag("heat_flow_field"),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = heatCp,
                                    onValueChange = { viewModel.updateHeatInputs(heatFlow, it, heatTin, heatTout) },
                                    label = { Text("Cp (kJ/kg·°C)") },
                                    modifier = Modifier.weight(1f).testTag("heat_cp_field"),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = heatTin,
                                    onValueChange = { viewModel.updateHeatInputs(heatFlow, heatCp, it, heatTout) },
                                    label = { Text("Inlet Temp (°C)") },
                                    modifier = Modifier.weight(1f).testTag("heat_tin_field"),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = heatTout,
                                    onValueChange = { viewModel.updateHeatInputs(heatFlow, heatCp, heatTin, it) },
                                    label = { Text("Outlet Temp (°C)") },
                                    modifier = Modifier.weight(1f).testTag("heat_tout_field"),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = IndustrialBorder)
                            Spacer(modifier = Modifier.height(14.dp))

                            CalcResultRow(label = "Temperature Difference (ΔT)", value = "${heatResult.deltaTC} °C")
                            CalcResultRow(label = "Thermal Heat Duty (kW)", value = "${heatResult.heatDutyKw} kW")
                            CalcResultRow(label = "Thermal Heat Duty (kcal/h)", value = "${heatResult.heatDutyKcalH} kcal/h")
                        }
                    }
                }
            }

            // TAB 3: MASS BALANCE
            3 -> {
                item {
                    AssuranceBanner(
                        level = EngineeringAssuranceLevel.PRELIMINARY_ESTIMATE,
                        formula = "Feed = Main Product + By-Products + Loss  •  Reconciliation"
                    )
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Production Mass Balance & Loss Analyzer",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = massFeed,
                                onValueChange = { viewModel.updateMassInputs(it, massMain, massBy) },
                                label = { Text("Feed Charged (kg)") },
                                modifier = Modifier.fillMaxWidth().testTag("mass_feed_field"),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = massMain,
                                    onValueChange = { viewModel.updateMassInputs(massFeed, it, massBy) },
                                    label = { Text("Main Product (kg)") },
                                    modifier = Modifier.weight(1f).testTag("mass_main_field"),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = massBy,
                                    onValueChange = { viewModel.updateMassInputs(massFeed, massMain, it) },
                                    label = { Text("By-Product (kg)") },
                                    modifier = Modifier.weight(1f).testTag("mass_by_field"),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = IndustrialBorder)
                            Spacer(modifier = Modifier.height(14.dp))

                            CalcResultRow(label = "Calculated Unaccounted Loss", value = "${massResult.calculatedLossKg} kg")
                            CalcResultRow(label = "Main Product Yield %", value = "${massResult.mainYieldPercent}%")
                            CalcResultRow(label = "Manufacturing Loss %", value = "${massResult.lossPercent}%")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AssuranceBanner(level: EngineeringAssuranceLevel, formula: String) {
    Surface(
        color = IndustrialNavyCard,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(level.colorHex).copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ASSURANCE LEVEL",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(level.colorHex),
                    letterSpacing = 0.8.sp
                )
                Surface(
                    color = Color(level.colorHex).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = level.label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(level.colorHex),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = formula,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                color = AmberCopperBright
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Disclaimer: High-consequence equipment sizing or relief valve setpoints must be independently verified by a licensed professional engineer.",
                fontSize = 9.sp,
                color = TextSecondaryDark
            )
        }
    }
}

@Composable
fun CalcResultRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondaryDark
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = TextPrimaryDark
        )
    }
}
