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
import com.example.data.model.PermitStatus
import com.example.data.model.PermitToWork
import com.example.ui.AurelionViewModel
import com.example.ui.theme.*

@Composable
fun SafetyCmmsScreen(viewModel: AurelionViewModel) {
    val permits by viewModel.safetyPermits.collectAsState()
    val maintenanceOrders by viewModel.maintenanceOrders.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Permit to Work, 1: CMMS Maintenance

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
                    text = "PROCESS SAFETY & CMMS RELIABILITY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberCopperBright,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = if (selectedTab == 0) "Permit-to-Work (PTW) Governance" else "Plant Maintenance & Reliability",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Non-Negotiable Rule: Safety interlocks, physical gas testing, and LOTO lockouts cannot be bypassed or automated by AI.",
                    fontSize = 12.sp,
                    color = StatusDangerRed,
                    fontWeight = FontWeight.SemiBold
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
                        .testTag("tab_safety_ptw")
                ) {
                    Text(
                        text = "Safety PTW (${permits.size})",
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
                        .testTag("tab_cmms_orders")
                ) {
                    Text(
                        text = "CMMS Orders (${maintenanceOrders.size})",
                        fontSize = 12.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) IndustrialNavyDark else TextPrimaryDark,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                    )
                }
            }
        }

        if (selectedTab == 0) {
            items(permits) { permit ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(permit.status.colorHex)),
                    modifier = Modifier.fillMaxWidth().testTag("permit_${permit.permitId}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = permit.permitId,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = AmberCopperBright
                                )
                                Text(
                                    text = permit.type.label,
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark
                                )
                            }

                            Surface(
                                color = Color(permit.status.colorHex).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = permit.status.label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(permit.status.colorHex),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${permit.plantLocation} • Target: ${permit.equipmentTag}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = permit.description,
                            fontSize = 12.sp,
                            color = TextSecondaryDark,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Isolation & Safety Checklist row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ChecklistPill("Gas Test", permit.gasTestPassed)
                            ChecklistPill("LOTO Lock", permit.lotoApplied)
                            ChecklistPill("Fire Watch", permit.fireWatchAssigned)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Auth: ${permit.authorizingOfficer}",
                                    fontSize = 10.sp,
                                    color = AmberCopperLight
                                )
                                Text(
                                    text = "Valid Until: ${permit.validUntil}",
                                    fontSize = 10.sp,
                                    color = TextSecondaryDark
                                )
                            }

                            if (permit.status == PermitStatus.REQUESTED) {
                                Button(
                                    onClick = { viewModel.updatePermitStatus(permit.permitId, PermitStatus.AUTHORIZED) },
                                    colors = ButtonDefaults.buttonColors(containerColor = StatusSafeGreen),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.testTag("auth_permit_${permit.permitId}")
                                ) {
                                    Text("Authorize Permit", fontSize = 11.sp, color = Color.White)
                                }
                            } else if (permit.status == PermitStatus.AUTHORIZED) {
                                Button(
                                    onClick = { viewModel.updatePermitStatus(permit.permitId, PermitStatus.CLOSED_OUT) },
                                    colors = ButtonDefaults.buttonColors(containerColor = IndustrialBorder),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.testTag("close_permit_${permit.permitId}")
                                ) {
                                    Text("Close Out", fontSize = 11.sp, color = TextPrimaryDark)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // CMMS MAINTENANCE ORDERS
            items(maintenanceOrders) { wo ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                    modifier = Modifier.fillMaxWidth().testTag("wo_${wo.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${wo.id} • ${wo.equipmentTag}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = AmberCopperBright
                                )
                                Text(
                                    text = wo.equipmentName,
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark
                                )
                            }

                            Surface(
                                color = if (wo.priority == "CRITICAL") StatusDangerRedBg else AmberCopperBright.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = wo.priority,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (wo.priority == "CRITICAL") StatusDangerRed else AmberCopperBright,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = wo.taskDescription,
                            fontSize = 12.sp,
                            color = TextPrimaryDark,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Parts: ${wo.partsRequired}",
                                fontSize = 11.sp,
                                color = AmberCopperLight
                            )
                            Text(
                                text = "Status: ${wo.status}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusSafeGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Assigned: ${wo.assignedTech}",
                                fontSize = 10.sp,
                                color = TextSecondaryDark
                            )
                            Text(
                                text = "Scheduled: ${wo.scheduledDate}",
                                fontSize = 10.sp,
                                color = TextSecondaryDark
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChecklistPill(label: String, passed: Boolean) {
    Surface(
        color = if (passed) StatusSafeGreenBg else StatusDangerRedBg,
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
            Icon(
                imageVector = if (passed) Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = null,
                tint = if (passed) StatusSafeGreen else StatusDangerRed,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (passed) StatusSafeGreen else StatusDangerRed
            )
        }
    }
}
