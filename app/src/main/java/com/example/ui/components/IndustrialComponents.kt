package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.data.model.VerificationStatus
import com.example.ui.AppDestination
import com.example.ui.theme.*

@Composable
fun AurelionTopAppBar(
    currentDestination: AppDestination,
    currentRole: UserRole,
    isOffline: Boolean,
    onRoleClick: () -> Unit,
    onEmergencyClick: () -> Unit,
    onToggleOffline: () -> Unit,
    onMenuClick: () -> Unit
) {
    Surface(
        color = IndustrialNavySurface,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("app_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Navigation Menu",
                            tint = AmberCopperBright
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AURELION",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                letterSpacing = 1.5.sp,
                                color = AmberCopperBright
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = IndustrialSteelBlue.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "OS v3.2",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondaryDark,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = currentDestination.label,
                            fontSize = 12.sp,
                            color = TextPrimaryDark,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Offline indicator button
                    Surface(
                        color = if (isOffline) StatusWarningAmberBg else IndustrialNavyCard,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clickable { onToggleOffline() }
                            .testTag("offline_toggle_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isOffline) StatusWarningAmber else StatusSafeGreen)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isOffline) "Offline" else "Sync",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isOffline) BrassDark else StatusSafeGreen
                            )
                        }
                    }

                    // Role Chip with switcher
                    Surface(
                        color = AmberCopperPrimary.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AmberCopperPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clickable { onRoleClick() }
                            .testTag("role_switcher_chip")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Role",
                                tint = AmberCopperBright,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentRole.badge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberCopperBright
                            )
                        }
                    }

                    // Emergency SOP Quick button
                    IconButton(
                        onClick = onEmergencyClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("emergency_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Emergency Protocols & SOS",
                            tint = StatusDangerRed
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VerificationBadge(status: VerificationStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor, icon) = when (status) {
        VerificationStatus.VERIFIED -> Triple(StatusSafeGreenBg, StatusSafeGreen, Icons.Default.CheckCircle)
        VerificationStatus.UNVERIFIED -> Triple(StatusWarningAmberBg, StatusWarningAmber, Icons.Default.HelpOutline)
        VerificationStatus.NEEDS_VERIFICATION -> Triple(StatusInfoBlueBg, StatusInfoBlue, Icons.Default.Search)
        VerificationStatus.UNKNOWN -> Triple(IndustrialBorder, TextSecondaryDark, Icons.Default.Info)
        VerificationStatus.ENGINEER_APPROVED -> Triple(Color(0xFFEDE9FE), Color(0xFF7C3AED), Icons.Default.Verified)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = status.label,
                tint = textColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status.label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}

@Composable
fun IndustrialMetricCard(
    title: String,
    value: String,
    unit: String,
    subtitle: String,
    statusText: String,
    isStatusWarning: Boolean = false,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondaryDark,
                    letterSpacing = 0.8.sp
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AmberCopperBright,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimaryDark,
                    fontFamily = FontFamily.Monospace
                )
                if (unit.isNotBlank()) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = unit,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AmberCopperBright,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextSecondaryDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = if (isStatusWarning) StatusDangerRed.copy(alpha = 0.15f) else StatusSafeGreen.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = statusText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isStatusWarning) StatusDangerRed else StatusSafeGreen,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
