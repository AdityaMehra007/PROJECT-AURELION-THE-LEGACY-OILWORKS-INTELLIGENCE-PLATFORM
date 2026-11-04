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
import com.example.data.model.LimsSample
import com.example.data.model.QualityDisposition
import com.example.ui.AurelionViewModel
import com.example.ui.theme.*

@Composable
fun LimsScreen(viewModel: AurelionViewModel) {
    val samples by viewModel.limsSamples.collectAsState()
    var selectedSampleId by remember { mutableStateOf<String?>(samples.firstOrNull()?.sampleId) }

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
                    text = "LABORATORY INFORMATION MANAGEMENT SYSTEM",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberCopperBright,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "LIMS Quality Testing & CoA Release",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Every acceptance threshold is tied to controlled ASTM/BIS specifications. Unverified or altered results are strictly auditable.",
                    fontSize = 12.sp,
                    color = TextSecondaryDark
                )
            }
        }

        items(samples) { sample ->
            val isSelected = selectedSampleId == sample.sampleId

            Card(
                colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) AmberCopperPrimary else IndustrialBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedSampleId = if (isSelected) null else sample.sampleId }
                    .testTag("sample_${sample.sampleId}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = sample.sampleId,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = AmberCopperBright
                            )
                            Text(
                                text = "Batch: ${sample.batchOrLotNumber}",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }

                        Surface(
                            color = Color(sample.disposition.colorHex).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = sample.disposition.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(sample.disposition.colorHex),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = sample.productName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Point: ${sample.samplingPoint}",
                            fontSize = 11.sp,
                            color = TextSecondaryDark
                        )
                        Text(
                            text = sample.timestamp,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = AmberCopperLight
                        )
                    }

                    sample.coaNumber?.let { coa ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "CoA: $coa",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusSafeGreen
                            )
                            Text(
                                text = "Approved by: ${sample.technicalReviewer}",
                                fontSize = 10.sp,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    // --- DETAILED TEST SHEET ---
                    AnimatedVisibility(visible = isSelected) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            HorizontalDivider(color = IndustrialBorder)

                            Text(
                                text = "LABORATORY TEST RESULTS (${sample.tests.size} ASTM PARAMETERS)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberCopperBright,
                                letterSpacing = 0.8.sp
                            )

                            sample.tests.forEach { test ->
                                Surface(
                                    color = IndustrialNavySurface,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = test.testParameter,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimaryDark
                                            )
                                            Surface(
                                                color = if (test.isInSpecification) StatusSafeGreenBg else StatusDangerRedBg,
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = if (test.isInSpecification) "IN SPEC" else "OOS",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (test.isInSpecification) StatusSafeGreen else StatusDangerRed,
                                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Method: ${test.testMethodStandard}",
                                                fontSize = 10.sp,
                                                color = TextSecondaryDark
                                            )
                                            Text(
                                                text = "Spec: ${test.specificationRange}",
                                                fontSize = 10.sp,
                                                color = AmberCopperLight
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(2.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Observed Value: ${test.measuredValue}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Monospace,
                                                color = if (test.isInSpecification) StatusSafeGreen else StatusDangerRed
                                            )
                                            Text(
                                                text = "Inst: ${test.instrumentTag}",
                                                fontSize = 9.sp,
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
}
