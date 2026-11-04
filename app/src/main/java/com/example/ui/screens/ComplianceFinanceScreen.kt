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
import com.example.data.model.BatchCostBreakdown
import com.example.data.model.ComplianceObligation
import com.example.ui.AurelionViewModel
import com.example.ui.theme.*

@Composable
fun ComplianceFinanceScreen(viewModel: AurelionViewModel) {
    val complianceList by viewModel.complianceObligations.collectAsState()
    val costingList by viewModel.batchCosting.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Compliance Register, 1: Batch Costing & Margins

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
                    text = "REGULATORY COMPLIANCE & COST ACCOUNTING",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberCopperBright,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = if (selectedTab == 0) "Statutory & Environmental Register" else "Industrial Batch Costing & Margins",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Grounded in verified authorities (PESO, OISD, CPCB) and granular unit economics per production batch.",
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
                        .testTag("tab_compliance")
                ) {
                    Text(
                        text = "Compliance Register (${complianceList.size})",
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
                        .testTag("tab_costing")
                ) {
                    Text(
                        text = "Batch Costing (${costingList.size})",
                        fontSize = 12.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) IndustrialNavyDark else TextPrimaryDark,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                    )
                }
            }
        }

        if (selectedTab == 0) {
            items(complianceList) { comp ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (comp.isCompliant) StatusSafeGreen else StatusDangerRed),
                    modifier = Modifier.fillMaxWidth().testTag("comp_${comp.authority}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = comp.topic,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = "${comp.authority} • ${comp.referenceCode}",
                                    fontSize = 11.sp,
                                    color = AmberCopperBright
                                )
                            }

                            Surface(
                                color = if (comp.isCompliant) StatusSafeGreenBg else StatusDangerRedBg,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = if (comp.isCompliant) "ACTIVE & VALID" else "RENEWAL OVERDUE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (comp.isCompliant) StatusSafeGreen else StatusDangerRed,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = comp.summary,
                            fontSize = 12.sp,
                            color = TextSecondaryDark,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "License: ${comp.licenseOrCertNumber}",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimaryDark
                            )
                            Text(
                                text = "Renewal: ${comp.expiryOrRenewalDate}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberCopperLight
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Lead: ${comp.responsiblePerson}",
                                fontSize = 10.sp,
                                color = TextSecondaryDark
                            )
                            Text(
                                text = "Source: ${comp.verifiedOfficialSourceUrl}",
                                fontSize = 10.sp,
                                color = StatusInfoBlue
                            )
                        }
                    }
                }
            }
        } else {
            // BATCH COSTING & FINANCIAL MARGINS
            items(costingList) { cost ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                    modifier = Modifier.fillMaxWidth().testTag("cost_${cost.batchNumber}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = cost.batchNumber,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = AmberCopperBright
                                )
                                Text(
                                    text = cost.productName,
                                    fontSize = 12.sp,
                                    color = TextPrimaryDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Surface(
                                color = StatusSafeGreenBg,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "+${(cost.grossMarginPercent * 10).toInt() / 10.0}% Margin",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = StatusSafeGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        CostItemRow("Raw Feedstock Sourcing", "₹${cost.feedstockCostInr.toInt()}")
                        CostItemRow("Thermal Energy (Furnace Oil)", "₹${cost.thermalEnergyFuelInr.toInt()}")
                        CostItemRow("Electrical Power", "₹${cost.electricalPowerInr.toInt()}")
                        CostItemRow("Chemicals (Clay/Fuller's Earth)", "₹${cost.chemicalAdditivesInr.toInt()}")
                        CostItemRow("Direct Labor & Operations", "₹${cost.directLaborInr.toInt()}")
                        CostItemRow("Packaging & Liners", "₹${cost.packagingInr.toInt()}")
                        CostItemRow("Plant Overheads", "₹${cost.overheadsInr.toInt()}")

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = IndustrialBorder)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Manufacturing Cost",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                            Text(
                                text = "₹${cost.totalCostInr.toInt()}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
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
                                text = "Unit Cost: ₹${(cost.costPerKgInr * 100).toInt() / 100.0} / kg",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                            Text(
                                text = "Sale Price: ₹${cost.sellingPricePerKgInr} / kg",
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

@Composable
fun CostItemRow(label: String, amountStr: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = TextSecondaryDark)
        Text(text = amountStr, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = TextPrimaryDark)
    }
}
