package com.example.ui.screens

import androidx.compose.foundation.background
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
import com.example.data.model.KeroseneProductSpec
import com.example.data.model.WaxProductSpec
import com.example.ui.AurelionViewModel
import com.example.ui.theme.*

@Composable
fun ProductWorkspaceScreen(viewModel: AurelionViewModel) {
    var selectedProductTab by remember { mutableStateOf(0) } // 0: Kerosene, 1: Petroleum Wax
    val keroseneSpecs = viewModel.keroseneSpecs
    val waxSpecs = viewModel.waxSpecs

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
                    text = "SPECIALIZED PRODUCT WORKSPACES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberCopperBright,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = if (selectedProductTab == 0) "Kerosene & Middle Distillates" else "Petroleum Wax & Refining",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Controlled product specifications, ASTM test parameter thresholds, and storage compatibility rules.",
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
                    color = if (selectedProductTab == 0) AmberCopperPrimary else IndustrialNavyCard,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedProductTab = 0 }
                        .testTag("tab_product_kerosene")
                ) {
                    Text(
                        text = "Kerosene Workspace",
                        fontSize = 12.sp,
                        fontWeight = if (selectedProductTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedProductTab == 0) IndustrialNavyDark else TextPrimaryDark,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                    )
                }

                Surface(
                    color = if (selectedProductTab == 1) AmberCopperPrimary else IndustrialNavyCard,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedProductTab = 1 }
                        .testTag("tab_product_wax")
                ) {
                    Text(
                        text = "Petroleum Wax Workspace",
                        fontSize = 12.sp,
                        fontWeight = if (selectedProductTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedProductTab == 1) IndustrialNavyDark else TextPrimaryDark,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                    )
                }
            }
        }

        if (selectedProductTab == 0) {
            items(keroseneSpecs) { spec ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                    modifier = Modifier.fillMaxWidth().testTag("spec_${spec.gradeName}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = spec.gradeName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                            Surface(
                                color = StatusWarningAmberBg,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Class B Petroleum",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrassDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Grid of ASTM specs
                        SpecParamRow(label = "Flash Point (ASTM D93 / Abel)", value = "≥ ${spec.flashPointMinC} °C (Fire Safety Gate)")
                        SpecParamRow(label = "Smoke Point (ASTM D1322)", value = "≥ ${spec.smokePointMinMm} mm (Luminosity)")
                        SpecParamRow(label = "90% Recovery Point (ASTM D86)", value = "≤ ${spec.distillation90RecoveryMaxC} °C")
                        SpecParamRow(label = "Total Sulfur (ASTM D4294)", value = "≤ ${spec.totalSulfurMaxPercent} % wt")
                        SpecParamRow(label = "Density Range @ 15°C", value = spec.densityRangeKgM3)

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Target Markets: ${spec.authorizedMarkets}",
                            fontSize = 11.sp,
                            color = AmberCopperLight
                        )
                    }
                }
            }
        } else {
            // WAX WORKSPACE
            items(waxSpecs) { spec ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                    modifier = Modifier.fillMaxWidth().testTag("wax_spec_${spec.gradeName}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = spec.gradeName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                            Surface(
                                color = StatusSafeGreenBg,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Wax Refinery",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusSafeGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        SpecParamRow(label = "Congealing Point (ASTM D938)", value = spec.congealingPointRangeC)
                        SpecParamRow(label = "Oil Content Max (ASTM D721)", value = "≤ ${spec.oilContentMaxPercent} %")
                        SpecParamRow(label = "Needle Penetration (ASTM D1321)", value = spec.needlePenetrationAt25C)
                        SpecParamRow(label = "Saybolt Color (ASTM D156)", value = spec.sayboltColorMin)
                        SpecParamRow(label = "Appearance", value = spec.appearance)

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Packaging Formats: ${spec.packageTypes}",
                            fontSize = 11.sp,
                            color = AmberCopperLight
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpecParamRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextSecondaryDark
        )
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = TextPrimaryDark
        )
    }
}
