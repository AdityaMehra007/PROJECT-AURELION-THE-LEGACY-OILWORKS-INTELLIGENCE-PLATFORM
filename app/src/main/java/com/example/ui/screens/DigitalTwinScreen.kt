package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.AssetStatus
import com.example.data.model.EquipmentAsset
import com.example.data.model.TankAsset
import com.example.ui.AurelionViewModel
import com.example.ui.components.VerificationBadge
import com.example.ui.theme.*

@Composable
fun DigitalTwinScreen(viewModel: AurelionViewModel) {
    val equipment by viewModel.equipmentAssets.collectAsState()
    val tanks by viewModel.tankAssets.collectAsState()

    var selectedTankForDip by remember { mutableStateOf<TankAsset?>(null) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Equipment Register, 1: Tank Farm & Dips

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
                    text = "FACTORY DIGITAL TWIN & ASSET REGISTRY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberCopperBright,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Plant Hardware & Tank Farm Models",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Digital representation tied strictly to physical nameplates, P&ID blueprints, and verified inspection records.",
                    fontSize = 12.sp,
                    color = TextSecondaryDark
                )
            }
        }

        // --- SUB TABS ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = if (selectedTab == 0) AmberCopperPrimary else IndustrialNavyCard,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = 0 }
                        .testTag("tab_equipment")
                ) {
                    Text(
                        text = "Equipment Assets (${equipment.size})",
                        fontSize = 12.sp,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) IndustrialNavyDark else TextPrimaryDark,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                    )
                }

                Surface(
                    color = if (selectedTab == 1) AmberCopperPrimary else IndustrialNavyCard,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = 1 }
                        .testTag("tab_tanks")
                ) {
                    Text(
                        text = "Tank Farm & Dips (${tanks.size})",
                        fontSize = 12.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) IndustrialNavyDark else TextPrimaryDark,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                    )
                }
            }
        }

        if (selectedTab == 0) {
            items(equipment) { asset ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                    modifier = Modifier.fillMaxWidth().testTag("asset_${asset.tag}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = AmberCopperBright.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = asset.tag,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = AmberCopperBright,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = asset.category,
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark
                                )
                            }

                            Surface(
                                color = Color(asset.status.colorHex).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = asset.status.label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(asset.status.colorHex),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = asset.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Location: ${asset.area}",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                            Text(
                                text = "Rating: ${asset.ratedCapacity}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AmberCopperLight
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Mfr: ${asset.manufacturer} (${asset.modelOrSerial})",
                                fontSize = 10.sp,
                                color = TextSecondaryDark
                            )
                            Text(
                                text = "Next Insp: ${asset.nextInspectionDate}",
                                fontSize = 10.sp,
                                color = StatusWarningAmber
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Doc: ${asset.documentationRef}",
                                fontSize = 10.sp,
                                color = AmberCopperLight
                            )
                            VerificationBadge(status = asset.verification)
                        }
                    }
                }
            }
        } else {
            // TANK FARM SECTION
            item {
                Surface(
                    color = IndustrialNavySurface,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Tip: Tap any storage tank to record an updated dipping measurement (liquid height, temperature, density).",
                        fontSize = 11.sp,
                        color = AmberCopperLight,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            items(tanks) { tank ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedTankForDip = tank }
                        .testTag("tank_${tank.tag}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = AmberCopperBright.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = tank.tag,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = AmberCopperBright,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = tank.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                            }

                            Text(
                                text = "${tank.fillPercentage.toInt()}%",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = if (tank.fillPercentage > tank.highLevelAlarmPercent) StatusDangerRed else AmberCopperBright
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Service: ${tank.serviceProduct}",
                            fontSize = 12.sp,
                            color = TextSecondaryDark
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Visual Tank Gauge Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .background(IndustrialNavySurface, RoundedCornerShape(6.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = (tank.fillPercentage / 100.0).toFloat().coerceIn(0f, 1f))
                                    .fillMaxHeight()
                                    .background(
                                        if (tank.fillPercentage > tank.highLevelAlarmPercent) StatusDangerRed else AmberCopperPrimary,
                                        RoundedCornerShape(6.dp)
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Dip Level: ${tank.currentLevelMeters} / ${tank.maxLevelMeters} m",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimaryDark
                            )
                            Text(
                                text = "Volume: ${tank.currentVolumeKl} / ${tank.maxCapacityKl} KL",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = AmberCopperBright
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Temp: ${tank.temperatureCelsius}°C  •  ρ: ${tank.observedDensity} kg/m³",
                                fontSize = 10.sp,
                                color = TextSecondaryDark
                            )
                            Text(
                                text = "Tap to Record Dip ✎",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusInfoBlue
                            )
                        }
                    }
                }
            }
        }
    }

    // --- TANK DIP EDIT MODAL ---
    selectedTankForDip?.let { tank ->
        var editLevel by remember { mutableStateOf(tank.currentLevelMeters.toString()) }
        var editTemp by remember { mutableStateOf(tank.temperatureCelsius.toString()) }
        var editDensity by remember { mutableStateOf(tank.observedDensity.toString()) }

        AlertDialog(
            onDismissRequest = { selectedTankForDip = null },
            title = {
                Text(
                    text = "Record Physical Tank Dip: ${tank.tag}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberCopperBright
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Service: ${tank.serviceProduct}",
                        fontSize = 12.sp,
                        color = TextSecondaryDark
                    )

                    OutlinedTextField(
                        value = editLevel,
                        onValueChange = { editLevel = it },
                        label = { Text("Measured Liquid Dip (Meters)") },
                        modifier = Modifier.fillMaxWidth().testTag("dip_level_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = editTemp,
                        onValueChange = { editTemp = it },
                        label = { Text("Observed Temperature (°C)") },
                        modifier = Modifier.fillMaxWidth().testTag("dip_temp_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = editDensity,
                        onValueChange = { editDensity = it },
                        label = { Text("Observed Density (kg/m³)") },
                        modifier = Modifier.fillMaxWidth().testTag("dip_density_input"),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val lvl = editLevel.toDoubleOrNull() ?: tank.currentLevelMeters
                        val tmp = editTemp.toDoubleOrNull() ?: tank.temperatureCelsius
                        val den = editDensity.toDoubleOrNull() ?: tank.observedDensity
                        viewModel.recordTankDip(tank.tag, lvl, tmp, den)
                        selectedTankForDip = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberCopperPrimary),
                    modifier = Modifier.testTag("save_tank_dip_button")
                ) {
                    Text("Record & Reconcile", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedTankForDip = null }) {
                    Text("Cancel", color = TextSecondaryDark)
                }
            },
            containerColor = IndustrialNavySurface
        )
    }
}
