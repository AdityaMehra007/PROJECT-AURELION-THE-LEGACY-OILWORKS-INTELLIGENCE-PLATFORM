package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AssetStatus
import com.example.data.model.BatchStage
import com.example.data.model.PermitStatus
import com.example.ui.AppDestination
import com.example.ui.AurelionViewModel
import com.example.ui.components.IndustrialMetricCard
import com.example.ui.components.VerificationBadge
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: AurelionViewModel,
    onNavigate: (AppDestination) -> Unit
) {
    val masterProfile by viewModel.masterProfile.collectAsState()
    val batches by viewModel.productionBatches.collectAsState()
    val tanks by viewModel.tankAssets.collectAsState()
    val permits by viewModel.safetyPermits.collectAsState()
    val limsSamples by viewModel.limsSamples.collectAsState()
    val batchCosting by viewModel.batchCosting.collectAsState()
    val reconciliations by viewModel.reconciliationAudits.collectAsState()

    val activeBatchesCount = batches.count { it.currentStage != BatchStage.COMPLETED }
    val activePermitsCount = permits.count { it.status == PermitStatus.AUTHORIZED }
    val pendingPermitsCount = permits.count { it.status == PermitStatus.REQUESTED }
    val flaggedReconciliation = reconciliations.any { it.status == "DISCREPANCY_FLAGGED" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(IndustrialNavyDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- STRATEGIC 5 QUESTIONS EXECUTIVE BANNER ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberCopperPrimary.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "EXECUTIVE COMMAND CENTER",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberCopperBright,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = masterProfile.entityName,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimaryDark
                            )
                        }
                        Surface(
                            color = AmberCopperBright.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "EST. ${masterProfile.establishedYear}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberCopperBright,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = IndustrialBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Today's Five Strategic Answers:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondaryDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    StrategicAnswerRow(
                        question = "1. What is happening in the business?",
                        answer = "$activeBatchesCount active batches running; Tank Farm at 74.2% capacity (3,601 KL in stock); Dispatch active.",
                        accentColor = StatusInfoBlue
                    )
                    StrategicAnswerRow(
                        question = "2. What requires attention today?",
                        answer = "$pendingPermitsCount Permit-to-Work pending gas test check; Pump PU-204 scheduled for mechanical seal overhaul.",
                        accentColor = StatusWarningAmber
                    )
                    StrategicAnswerRow(
                        question = "3. Where are we losing money or material?",
                        answer = "TK-102 Kerosene tank shows -7 KL (-1.36%) reconciliation variance MTD vs 0.5% tolerance.",
                        accentColor = StatusDangerRed
                    )
                    StrategicAnswerRow(
                        question = "4. What risks could affect people or compliance?",
                        answer = "Confined Space Entry on TK-104 isolated; OISD-STD-117 fire foam headers 100% charged.",
                        accentColor = StatusSafeGreen
                    )
                    StrategicAnswerRow(
                        question = "5. What decision should management investigate?",
                        answer = "Paraffin wax margins (+34.6%) outperforming bulk kerosene (+31.8%). Evaluate ramping slab production.",
                        accentColor = AmberCopperBright
                    )
                }
            }
        }

        // --- PRIMARY OPERATIONAL METRICS GRID ---
        item {
            Text(
                text = "PLANT OPERATIONAL KPIS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AmberCopperBright,
                letterSpacing = 1.sp
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IndustrialMetricCard(
                    title = "Production Output",
                    value = "22.4",
                    unit = "MT",
                    subtitle = "2 Batches (Kero & Wax)",
                    statusText = "90.6% Feed Yield",
                    isStatusWarning = false,
                    icon = Icons.Default.PrecisionManufacturing,
                    modifier = Modifier.weight(1f)
                )

                IndustrialMetricCard(
                    title = "Tank Capacity",
                    value = "3,601",
                    unit = "KL",
                    subtitle = "4 Active Bulk Tanks",
                    statusText = "74.2% Filled",
                    isStatusWarning = false,
                    icon = Icons.Default.LocalGasStation,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IndustrialMetricCard(
                    title = "Safety & PTW",
                    value = "$activePermitsCount Active",
                    unit = "",
                    subtitle = "$pendingPermitsCount Permit Pending Review",
                    statusText = "Zero LTI Record",
                    isStatusWarning = false,
                    icon = Icons.Default.Shield,
                    modifier = Modifier.weight(1f)
                )

                IndustrialMetricCard(
                    title = "Reconciliation",
                    value = if (flaggedReconciliation) "-1.36%" else "0.2%",
                    unit = "",
                    subtitle = "TK-102 Distillate Variance",
                    statusText = if (flaggedReconciliation) "Discrepancy Flag" else "In Tolerance",
                    isStatusWarning = flaggedReconciliation,
                    icon = Icons.Default.CompareArrows,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // --- QUICK ACTION LAUNCHERS ---
        item {
            Text(
                text = "OPERATIONAL WORKFLOWS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AmberCopperBright,
                letterSpacing = 1.sp
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionChip(
                    label = "Discovery Center",
                    icon = Icons.Default.Explore,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_discovery")
                ) {
                    onNavigate(AppDestination.DISCOVERY)
                }

                QuickActionChip(
                    label = "Calculators",
                    icon = Icons.Default.Calculate,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_calculators")
                ) {
                    onNavigate(AppDestination.CALCULATORS)
                }

                QuickActionChip(
                    label = "AI Assistant",
                    icon = Icons.Default.AutoAwesome,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_ai")
                ) {
                    onNavigate(AppDestination.ARCHIVE_AI)
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionChip(
                    label = "Production MES",
                    icon = Icons.Default.Factory,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_production")
                ) {
                    onNavigate(AppDestination.PRODUCTION_MES)
                }

                QuickActionChip(
                    label = "Tank Farm & Dips",
                    icon = Icons.Default.Storage,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_tanks")
                ) {
                    onNavigate(AppDestination.INVENTORY_TANKS)
                }

                QuickActionChip(
                    label = "LIMS & Lab",
                    icon = Icons.Default.Biotech,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_lims")
                ) {
                    onNavigate(AppDestination.LIMS_QUALITY)
                }
            }
        }

        // --- ACTIVE BATCH EXECUTION TRACKER ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ACTIVE PRODUCTION BATCHES",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        TextButton(
                            onClick = { onNavigate(AppDestination.PRODUCTION_MES) },
                            modifier = Modifier.testTag("view_all_batches_button")
                        ) {
                            Text("Full MES →", color = AmberCopperBright, fontSize = 12.sp)
                        }
                    }

                    batches.take(2).forEach { batch ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = IndustrialNavySurface,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = batch.batchNumber,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = AmberCopperBright
                                    )
                                    Surface(
                                        color = StatusInfoBlueBg,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = batch.currentStage.label,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StatusInfoBlue,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "${batch.productName} • ${batch.grade}",
                                    fontSize = 12.sp,
                                    color = TextPrimaryDark,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Target: ${batch.targetQuantityKg.toInt()} kg",
                                        fontSize = 11.sp,
                                        color = TextSecondaryDark
                                    )
                                    Text(
                                        text = "Yield: ${batch.yieldPercentage}%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StatusSafeGreen
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StrategicAnswerRow(question: String, answer: String, accentColor: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = question,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )
        Text(
            text = answer,
            fontSize = 12.sp,
            color = TextPrimaryDark,
            lineHeight = 16.sp
        )
    }
}

@Composable
fun QuickActionChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = IndustrialNavyCard,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1f.dp, IndustrialBorder),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = AmberCopperBright,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimaryDark
            )
        }
    }
}
