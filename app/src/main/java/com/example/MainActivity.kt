package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.AppDestination
import com.example.ui.AurelionViewModel
import com.example.ui.components.AurelionTopAppBar
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: AurelionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AurelionTheme {
                val currentDestination by viewModel.currentDestination.collectAsState()
                val currentRole by viewModel.currentRole.collectAsState()
                val isOffline by viewModel.isOfflineMode.collectAsState()

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                var showRoleDialog by remember { mutableStateOf(false) }
                var showEmergencyDialog by remember { mutableStateOf(false) }

                BackHandler {
                    viewModel.handleBack()
                }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = IndustrialNavySurface,
                            modifier = Modifier.width(300.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(horizontal = 16.dp, vertical = 24.dp)
                            ) {
                                // Drawer Header
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.PrecisionManufacturing,
                                        contentDescription = null,
                                        tint = AmberCopperBright,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "AURELION OS",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 18.sp,
                                            letterSpacing = 1.2.sp,
                                            color = AmberCopperBright
                                        )
                                        Text(
                                            text = "Industrial Operations Layer",
                                            fontSize = 11.sp,
                                            color = TextSecondaryDark
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                                HorizontalDivider(color = IndustrialBorder)
                                Spacer(modifier = Modifier.height(14.dp))

                                // Navigation Items
                                AppDestination.values().forEach { destination ->
                                    val isSelected = currentDestination == destination
                                    NavigationDrawerItem(
                                        label = {
                                            Text(
                                                text = destination.label,
                                                fontSize = 13.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) AmberCopperBright else TextPrimaryDark
                                            )
                                        },
                                        selected = isSelected,
                                        onClick = {
                                            viewModel.navigateTo(destination)
                                            scope.launch { drawerState.close() }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = getDestinationIcon(destination),
                                                contentDescription = null,
                                                tint = if (isSelected) AmberCopperBright else TextSecondaryDark
                                            )
                                        },
                                        colors = NavigationDrawerItemDefaults.colors(
                                            selectedContainerColor = IndustrialNavyCard,
                                            unselectedContainerColor = Color.Transparent
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .padding(vertical = 2.dp)
                                            .testTag("nav_${destination.name}")
                                    )
                                }
                            }
                        }
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = IndustrialNavyDark,
                        topBar = {
                            AurelionTopAppBar(
                                currentDestination = currentDestination,
                                currentRole = currentRole,
                                isOffline = isOffline,
                                onRoleClick = { showRoleDialog = true },
                                onEmergencyClick = { showEmergencyDialog = true },
                                onToggleOffline = { viewModel.toggleOffline() },
                                onMenuClick = {
                                    scope.launch {
                                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                containerColor = IndustrialNavySurface,
                                tonalElevation = 6.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .navigationBarsPadding()
                            ) {
                                val quickNavs = listOf(
                                    AppDestination.DASHBOARD to Icons.Default.Dashboard,
                                    AppDestination.PRODUCTION_MES to Icons.Default.Factory,
                                    AppDestination.CALCULATORS to Icons.Default.Calculate,
                                    AppDestination.SAFETY_CMMS to Icons.Default.Shield,
                                    AppDestination.ARCHIVE_AI to Icons.Default.AutoAwesome
                                )

                                quickNavs.forEach { (dest, icon) ->
                                    val isSel = currentDestination == dest
                                    NavigationBarItem(
                                        selected = isSel,
                                        onClick = { viewModel.navigateTo(dest) },
                                        icon = {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = dest.label,
                                                tint = if (isSel) AmberCopperBright else TextSecondaryDark
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = dest.label.split(" ").first(),
                                                fontSize = 10.sp,
                                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSel) AmberCopperBright else TextSecondaryDark
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = AmberCopperPrimary.copy(alpha = 0.2f)
                                        ),
                                        modifier = Modifier.testTag("bottom_nav_${dest.name}")
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentDestination) {
                                AppDestination.DASHBOARD -> DashboardScreen(viewModel) { viewModel.navigateTo(it) }
                                AppDestination.DISCOVERY -> DiscoveryScreen(viewModel)
                                AppDestination.KNOWLEDGE -> KnowledgeScreen(viewModel)
                                AppDestination.CALCULATORS -> CalculatorsScreen(viewModel)
                                AppDestination.DIGITAL_TWIN -> DigitalTwinScreen(viewModel)
                                AppDestination.PRODUCTION_MES -> ProductionMesScreen(viewModel)
                                AppDestination.PRODUCT_SPECS -> ProductWorkspaceScreen(viewModel)
                                AppDestination.INVENTORY_TANKS -> InventoryTankScreen(viewModel)
                                AppDestination.LIMS_QUALITY -> LimsScreen(viewModel)
                                AppDestination.SAFETY_CMMS -> SafetyCmmsScreen(viewModel)
                                AppDestination.COMPLIANCE_FINANCE -> ComplianceFinanceScreen(viewModel)
                                AppDestination.ARCHIVE_AI -> ArchiveAiScreen(viewModel)
                            }
                        }
                    }
                }

                // --- ROLE SWITCHER MODAL ---
                if (showRoleDialog) {
                    AlertDialog(
                        onDismissRequest = { showRoleDialog = false },
                        title = {
                            Text(
                                text = "Switch Industrial Role",
                                fontSize = 16.sp,
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
                                    text = "Demonstrate role-aware governance and operational access:",
                                    fontSize = 12.sp,
                                    color = TextSecondaryDark
                                )

                                UserRole.values().forEach { role ->
                                    val isCurrent = currentRole == role
                                    Surface(
                                        color = if (isCurrent) AmberCopperPrimary.copy(alpha = 0.2f) else IndustrialNavyCard,
                                        shape = RoundedCornerShape(8.dp),
                                        border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.dp, AmberCopperPrimary) else null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.switchRole(role)
                                                showRoleDialog = false
                                            }
                                            .testTag("select_role_${role.name}")
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = role.title,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isCurrent) AmberCopperBright else TextPrimaryDark
                                                )
                                                Text(
                                                    text = role.badge,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = AmberCopperBright
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = role.description,
                                                fontSize = 10.sp,
                                                color = TextSecondaryDark
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showRoleDialog = false }) {
                                Text("Close", color = AmberCopperBright)
                            }
                        },
                        containerColor = IndustrialNavySurface
                    )
                }

                // --- EMERGENCY PROTOCOLS MODAL ---
                if (showEmergencyDialog) {
                    AlertDialog(
                        onDismissRequest = { showEmergencyDialog = false },
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = StatusDangerRed,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "PLANT EMERGENCY DIRECTORY",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = StatusDangerRed
                                )
                            }
                        },
                        text = {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    color = StatusDangerRed.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "In case of fire, hydrocarbon vapor cloud, or emergency shutdown (ESD), activate physical wall alarms and evacuate to Assembly Point A.",
                                        fontSize = 11.sp,
                                        color = StatusDangerRed,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }

                                EmergencyContactRow("Refinery Fire Station (OISD-117)", "Ext. 101 / +91-22-2550-0101")
                                EmergencyContactRow("Chemical Hazmat Incident Line", "+91-22-2550-0102")
                                EmergencyContactRow("Plant Safety Officer Nair (24/7)", "+91-9820-112233")
                                EmergencyContactRow("PESO Regional Inspectorate", "PESO Control Room #889")
                                EmergencyContactRow("Occupational Health Medical Center", "Ext. 104")
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = { showEmergencyDialog = false },
                                colors = ButtonDefaults.buttonColors(containerColor = StatusDangerRed)
                            ) {
                                Text("Acknowledge Protocols", color = Color.White)
                            }
                        },
                        containerColor = IndustrialNavySurface
                    )
                }
            }
        }
    }

    private fun getDestinationIcon(dest: AppDestination): ImageVector {
        return when (dest) {
            AppDestination.DASHBOARD -> Icons.Default.Dashboard
            AppDestination.DISCOVERY -> Icons.Default.Explore
            AppDestination.KNOWLEDGE -> Icons.Default.MenuBook
            AppDestination.CALCULATORS -> Icons.Default.Calculate
            AppDestination.DIGITAL_TWIN -> Icons.Default.PrecisionManufacturing
            AppDestination.PRODUCTION_MES -> Icons.Default.Factory
            AppDestination.PRODUCT_SPECS -> Icons.Default.Science
            AppDestination.INVENTORY_TANKS -> Icons.Default.Storage
            AppDestination.LIMS_QUALITY -> Icons.Default.Biotech
            AppDestination.SAFETY_CMMS -> Icons.Default.Shield
            AppDestination.COMPLIANCE_FINANCE -> Icons.Default.AccountBalance
            AppDestination.ARCHIVE_AI -> Icons.Default.AutoAwesome
        }
    }
}

@Composable
fun EmergencyContactRow(label: String, contact: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = TextPrimaryDark, fontWeight = FontWeight.SemiBold)
        Text(text = contact, fontSize = 11.sp, color = AmberCopperBright, fontWeight = FontWeight.Bold)
    }
}
