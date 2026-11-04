package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import com.example.data.model.BatchStage
import com.example.data.model.ProductionBatch
import com.example.ui.AurelionViewModel
import com.example.ui.theme.*

@Composable
fun ProductionMesScreen(viewModel: AurelionViewModel) {
    val batches by viewModel.productionBatches.collectAsState()
    var selectedBatchNumber by remember { mutableStateOf<String?>(batches.firstOrNull()?.batchNumber) }

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
                    text = "MANUFACTURING EXECUTION SYSTEM (MES)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberCopperBright,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Batch Execution & Lot Genealogy",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Full traceability: Feedstock rail/tanker receipt → intermediate distillation cuts → bleaching/sweating → QC quarantine → dispatch.",
                    fontSize = 12.sp,
                    color = TextSecondaryDark
                )
            }
        }

        // --- BATCH LIST ---
        items(batches) { batch ->
            val isSelected = selectedBatchNumber == batch.batchNumber

            Card(
                colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) AmberCopperPrimary else IndustrialBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedBatchNumber = if (isSelected) null else batch.batchNumber }
                    .testTag("batch_${batch.batchNumber}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = batch.batchNumber,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = AmberCopperBright
                            )
                            Text(
                                text = "WO: ${batch.workOrderId}",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }

                        Surface(
                            color = StatusInfoBlueBg,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = batch.currentStage.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusInfoBlue,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${batch.productName} • ${batch.grade}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Feed Charged: ${batch.feedQuantityChargedKg.toInt()} kg",
                            fontSize = 11.sp,
                            color = TextSecondaryDark
                        )
                        Text(
                            text = "Produced: ${batch.actualProducedKg.toInt()} kg",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            text = "Yield: ${batch.yieldPercentage}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusSafeGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Spec: ${batch.verifiedSpecRef}",
                            fontSize = 10.sp,
                            color = AmberCopperLight
                        )

                        Button(
                            onClick = { viewModel.advanceBatchStage(batch.batchNumber) },
                            colors = ButtonDefaults.buttonColors(containerColor = AmberCopperPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("advance_stage_${batch.batchNumber}")
                        ) {
                            Text("Advance Stage →", fontSize = 11.sp, color = Color.White)
                        }
                    }

                    // --- EXPANDED GENEALOGY TRAIL ---
                    AnimatedVisibility(visible = isSelected) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            HorizontalDivider(color = IndustrialBorder)

                            Text(
                                text = "BATCH LOT GENEALOGY TRAIL",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberCopperBright,
                                letterSpacing = 1.sp
                            )

                            batch.genealogyTrail.forEachIndexed { idx, step ->
                                Surface(
                                    color = IndustrialNavySurface,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "${idx + 1}. ${step.stageName}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimaryDark
                                            )
                                            Text(
                                                text = step.timestamp,
                                                fontSize = 11.sp,
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
                                                text = "Unit: ${step.unitTag}  •  ${step.inputLot} → ${step.outputLot}",
                                                fontSize = 10.sp,
                                                fontFamily = FontFamily.Monospace,
                                                color = TextSecondaryDark
                                            )
                                            Text(
                                                text = step.operatorId,
                                                fontSize = 10.sp,
                                                color = AmberCopperLight
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Notes: ${step.notes}",
                                            fontSize = 11.sp,
                                            color = TextSecondaryDark
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
}
