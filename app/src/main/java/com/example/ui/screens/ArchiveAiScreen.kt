package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import com.example.data.model.AssistantPersona
import com.example.data.model.FamilyArchiveItem
import com.example.ui.AurelionViewModel
import com.example.ui.components.VerificationBadge
import com.example.ui.theme.*

@Composable
fun ArchiveAiScreen(viewModel: AurelionViewModel) {
    val archiveItems = viewModel.familyArchiveItems
    val chatMessages by viewModel.aiChatMessages.collectAsState()
    val activePersona by viewModel.activeAiPersona.collectAsState()

    var selectedTab by remember { mutableStateOf(1) } // 0: Family Archive, 1: AI Industrial Brain
    var currentPromptText by remember { mutableStateOf("") }

    val quickChips = listOf(
        "Analyze Kerosene vs Wax gross margins",
        "Check IS 1459 flash point test limits",
        "Review Hot Work PTW requirements on HE-102",
        "Explain column T-101 reflux ratio optimization"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IndustrialNavyDark)
    ) {
        // --- HEADER & TABS ---
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = "FAMILY ARCHIVE & INDUSTRIAL AI BRAIN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AmberCopperBright,
                letterSpacing = 1.2.sp
            )
            Text(
                text = if (selectedTab == 0) "Preserved Family Heritage & Notebooks" else "Grounded Petrochemical AI Assistant",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimaryDark
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                        .testTag("tab_family_archive")
                ) {
                    Text(
                        text = "Family Archive (${archiveItems.size})",
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
                        .testTag("tab_ai_brain")
                ) {
                    Text(
                        text = "AI Industrial Brain",
                        fontSize = 12.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) IndustrialNavyDark else TextPrimaryDark,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                    )
                }
            }
        }

        if (selectedTab == 0) {
            // FAMILY ARCHIVE LIST
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(archiveItems) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                        modifier = Modifier.fillMaxWidth().testTag("archive_${item.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = AmberCopperBright.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = item.category.uppercase(),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmberCopperBright,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                VerificationBadge(status = item.verification)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = item.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )

                            Text(
                                text = "Era: ${item.dateOrEra} • Author: ${item.authorOrSource}",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = item.description,
                                fontSize = 12.sp,
                                color = TextSecondaryDark,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Preserved Wisdom Quote
                            Surface(
                                color = IndustrialNavySurface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, AmberCopperPrimary.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "PRESERVED FOUNDER WISDOM:",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmberCopperBright
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.preservedWisdom,
                                        fontSize = 11.sp,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        color = AmberCopperLight
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Modern Review: ${item.modernSafetyReview}",
                                fontSize = 10.sp,
                                color = StatusSafeGreen
                            )
                        }
                    }
                }
            }
        } else {
            // AI INDUSTRIAL BRAIN CHAT
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // Persona Switcher Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AssistantPersona.values().take(3).forEach { persona ->
                        val isSel = activePersona == persona
                        Surface(
                            color = if (isSel) AmberCopperPrimary else IndustrialNavyCard,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .clickable { viewModel.setAiPersona(persona) }
                                .testTag("persona_${persona.name}")
                        ) {
                            Text(
                                text = persona.label.replace(" Mode", ""),
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) IndustrialNavyDark else TextPrimaryDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                // Chat Messages Thread
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(chatMessages) { msg ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = if (msg.isUser) Alignment.End else Alignment.Start
                        ) {
                            Surface(
                                color = if (msg.isUser) AmberCopperPrimary else IndustrialNavyCard,
                                shape = RoundedCornerShape(12.dp),
                                border = if (!msg.isUser) androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder) else null,
                                modifier = Modifier.widthIn(max = 320.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    if (!msg.isUser) {
                                        Text(
                                            text = msg.persona.label.uppercase(),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AmberCopperBright,
                                            letterSpacing = 0.8.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }

                                    Text(
                                        text = msg.text,
                                        fontSize = 12.sp,
                                        color = if (msg.isUser) Color.White else TextPrimaryDark,
                                        lineHeight = 16.sp
                                    )

                                    if (msg.groundedCitations.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        HorizontalDivider(color = IndustrialBorder)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Verified Sources: ${msg.groundedCitations.joinToString(" • ")}",
                                            fontSize = 9.sp,
                                            color = AmberCopperLight
                                        )
                                    }

                                    msg.engineeringDisclaimer?.let { disc ->
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = disc,
                                            fontSize = 8.sp,
                                            color = TextSecondaryDark,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        )
                                    }
                                }
                            }

                            Text(
                                text = msg.timestamp,
                                fontSize = 9.sp,
                                color = TextSecondaryDark,
                                modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 2.dp)
                            )
                        }
                    }
                }

                // Quick Prompt Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickChips.take(2).forEach { chip ->
                        Surface(
                            color = IndustrialNavySurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.clickable {
                                currentPromptText = chip
                                viewModel.sendAiPrompt(chip)
                                currentPromptText = ""
                            }
                        ) {
                            Text(
                                text = chip,
                                fontSize = 10.sp,
                                color = AmberCopperLight,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Input Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = currentPromptText,
                        onValueChange = { currentPromptText = it },
                        placeholder = { Text("Ask engineering or business AI...", color = TextSecondaryDark, fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ai_input_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmberCopperBright,
                            unfocusedBorderColor = IndustrialBorder,
                            focusedContainerColor = IndustrialNavyCard,
                            unfocusedContainerColor = IndustrialNavyCard,
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    IconButton(
                        onClick = {
                            if (currentPromptText.isNotBlank()) {
                                viewModel.sendAiPrompt(currentPromptText)
                                currentPromptText = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(AmberCopperPrimary, RoundedCornerShape(10.dp))
                            .testTag("ai_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Prompt",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
