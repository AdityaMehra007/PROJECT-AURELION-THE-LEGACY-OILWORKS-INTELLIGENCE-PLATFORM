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
import com.example.data.model.ItemMaster
import com.example.data.model.ReconciliationAudit
import com.example.ui.AurelionViewModel
import com.example.ui.theme.*

@Composable
fun InventoryTankScreen(viewModel: AurelionViewModel) {
    val items by viewModel.inventoryItems.collectAsState()
    val reconciliations by viewModel.reconciliationAudits.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Item Master, 1: Reconciliation Engine

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
                    text = "INVENTORY & MATERIAL CONTROL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberCopperBright,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Item Master & Material Reconciliation",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Zero-loss accountability: Tracks bulk receipts, plant issues, production yields, and storage discrepancy audits.",
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
                        .testTag("tab_inv_items")
                ) {
                    Text(
                        text = "Item Master (${items.size})",
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
                        .testTag("tab_inv_reconcile")
                ) {
                    Text(
                        text = "Reconciliation Engine (${reconciliations.size})",
                        fontSize = 12.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) IndustrialNavyDark else TextPrimaryDark,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                    )
                }
            }
        }

        if (selectedTab == 0) {
            items(items) { item ->
                val isBelowReorder = item.quantityOnHand <= item.reorderPoint

                Card(
                    colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isBelowReorder) StatusDangerRed else IndustrialBorder),
                    modifier = Modifier.fillMaxWidth().testTag("item_${item.sku}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = item.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = "${item.sku} • ${item.category}",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextSecondaryDark
                                )
                            }

                            Surface(
                                color = if (isBelowReorder) StatusDangerRedBg else AmberCopperBright.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = if (isBelowReorder) "REORDER ALERT" else "NORMAL",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isBelowReorder) StatusDangerRed else AmberCopperBright,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "On Hand: ${item.quantityOnHand} ${item.unitOfMeasure}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimaryDark
                            )
                            Text(
                                text = "Reserved: ${item.reservedQuantity} ${item.unitOfMeasure}",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                            Text(
                                text = "Min: ${item.reorderPoint} ${item.unitOfMeasure}",
                                fontSize = 11.sp,
                                color = AmberCopperLight
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Location: ${item.storageLocation} (Lot: ${item.lotNumber})",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                            Text(
                                text = "Valuation: ₹${(item.quantityOnHand * item.unitValuationInr).toInt()}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusSafeGreen
                            )
                        }
                    }
                }
            }
        } else {
            // RECONCILIATION ENGINE
            items(reconciliations) { audit ->
                val isDiscrepancy = audit.status == "DISCREPANCY_FLAGGED"

                Card(
                    colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isDiscrepancy) StatusDangerRed else StatusSafeGreen),
                    modifier = Modifier.fillMaxWidth().testTag("audit_${audit.materialName}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = audit.materialName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = audit.period,
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark
                                )
                            }

                            Surface(
                                color = if (isDiscrepancy) StatusDangerRedBg else StatusSafeGreenBg,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = if (isDiscrepancy) "DISCREPANCY FLAGGED" else "RECONCILED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDiscrepancy) StatusDangerRed else StatusSafeGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Arithmetic breakdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Opening: ${audit.openingStock}", fontSize = 11.sp, color = TextSecondaryDark)
                            Text(text = "+ Rec: ${audit.receipts}", fontSize = 11.sp, color = TextSecondaryDark)
                            Text(text = "- Dispatched: ${audit.dispatched}", fontSize = 11.sp, color = TextSecondaryDark)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Book Balance: ${audit.bookBalance}",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimaryDark
                            )
                            Text(
                                text = "Physical Dip: ${audit.closingPhysicalDip}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = AmberCopperBright
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            color = IndustrialNavySurface,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Net Discrepancy: ${audit.discrepancy} (Tolerance: ±${audit.tolerancePercent}%)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDiscrepancy) StatusDangerRed else StatusSafeGreen
                                )
                                Text(
                                    text = "Auditor: ${audit.reviewedBy}",
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
