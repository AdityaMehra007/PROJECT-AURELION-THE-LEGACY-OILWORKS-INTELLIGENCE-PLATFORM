package com.example.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DiscoveryQuestion
import com.example.data.model.VerificationStatus
import com.example.ui.AurelionViewModel
import com.example.ui.components.VerificationBadge
import com.example.ui.theme.*

@Composable
fun DiscoveryScreen(viewModel: AurelionViewModel) {
    val masterProfile by viewModel.masterProfile.collectAsState()
    val questions by viewModel.discoveryQuestions.collectAsState()
    val timeline by viewModel.familyTimeline.collectAsState()

    var selectedQuestionForEdit by remember { mutableStateOf<DiscoveryQuestion?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }

    val categories = listOf("ALL", "Family & Heritage", "Process Technology", "Feedstock & Sourcing", "Wax Specialization", "Kerosene Compliance", "Factory Identity")

    val filteredQuestions = if (selectedCategoryFilter == "ALL") questions else questions.filter { it.category == selectedCategoryFilter }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(IndustrialNavyDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- MASTER PROFILE SUMMARY HEADER ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberCopperPrimary),
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
                                text = "BUSINESS DISCOVERY CENTER",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberCopperBright,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = "Living Business Master Profile",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimaryDark
                            )
                        }

                        Surface(
                            color = StatusSafeGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = masterProfile.verifiedFactsRatio,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusSafeGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Facility: ${masterProfile.currentFacilityLocation}",
                        fontSize = 12.sp,
                        color = TextSecondaryDark
                    )
                    Text(
                        text = "Primary Process: ${masterProfile.primaryProcessModel}",
                        fontSize = 12.sp,
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = IndustrialBorder)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Operating Philosophy: Respect the past. Understand the present. Engineer the future.",
                        fontSize = 12.sp,
                        color = AmberCopperLight,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // --- FAMILY TIMELINE EXPEDITION ---
        item {
            Text(
                text = "FAMILY BUSINESS TIMELINE & GENEALOGY",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AmberCopperBright,
                letterSpacing = 1.sp
            )
        }

        items(timeline) { event ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IndustrialNavySurface),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.width(64.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = event.year,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = AmberCopperBright
                        )
                        Text(
                            text = event.era.split(" ").firstOrNull() ?: "",
                            fontSize = 10.sp,
                            color = TextSecondaryDark
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = event.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                            VerificationBadge(status = event.status)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = event.description,
                            fontSize = 12.sp,
                            color = TextSecondaryDark,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Source: ${event.verifiedSource}",
                            fontSize = 10.sp,
                            color = AmberCopperLight,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // --- GUIDED INTERVIEW SECTION ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STRUCTURED INTERVIEWS (${filteredQuestions.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberCopperBright,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Tap item to edit & verify",
                    fontSize = 11.sp,
                    color = TextSecondaryDark
                )
            }
        }

        // Category Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.take(4).forEach { cat ->
                    val isSelected = selectedCategoryFilter == cat
                    Surface(
                        color = if (isSelected) AmberCopperPrimary else IndustrialNavyCard,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .clickable { selectedCategoryFilter = cat }
                            .testTag("filter_$cat")
                    ) {
                        Text(
                            text = cat,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) IndustrialNavyDark else TextPrimaryDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        items(filteredQuestions) { q ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedQuestionForEdit = q }
                    .testTag("question_${q.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = q.category.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberCopperBright,
                            letterSpacing = 0.8.sp
                        )
                        VerificationBadge(status = q.status)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = q.question,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = q.currentAnswer,
                        fontSize = 12.sp,
                        color = TextSecondaryDark,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Ref: ${q.sourceAttribution}",
                            fontSize = 10.sp,
                            color = AmberCopperLight
                        )
                        Text(
                            text = "Updated: ${q.lastUpdated}",
                            fontSize = 10.sp,
                            color = TextSecondaryDark
                        )
                    }
                }
            }
        }
    }

    // --- QUESTION EDIT DIALOG ---
    selectedQuestionForEdit?.let { q ->
        var editAnswer by remember { mutableStateOf(q.currentAnswer) }
        var editSource by remember { mutableStateOf(q.sourceAttribution) }
        var editStatus by remember { mutableStateOf(q.status) }

        AlertDialog(
            onDismissRequest = { selectedQuestionForEdit = null },
            title = {
                Text(
                    text = "Edit & Attribute Fact",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberCopperBright
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = q.question,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryDark
                    )

                    OutlinedTextField(
                        value = editAnswer,
                        onValueChange = { editAnswer = it },
                        label = { Text("Answer / Observation") },
                        modifier = Modifier.fillMaxWidth().testTag("edit_answer_field"),
                        maxLines = 4
                    )

                    OutlinedTextField(
                        value = editSource,
                        onValueChange = { editSource = it },
                        label = { Text("Source Attribution (Deed, Log, SGS)") },
                        modifier = Modifier.fillMaxWidth().testTag("edit_source_field")
                    )

                    Text(
                        text = "Verification Status:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        VerificationStatus.values().take(3).forEach { st ->
                            val isSel = editStatus == st
                            Surface(
                                color = if (isSel) AmberCopperPrimary else IndustrialNavyCard,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .clickable { editStatus = st }
                                    .testTag("status_sel_${st.name}")
                            ) {
                                Text(
                                    text = st.label.split(" ").first(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) IndustrialNavyDark else TextPrimaryDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateDiscoveryAnswer(q.id, editAnswer, editStatus, editSource)
                        selectedQuestionForEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberCopperPrimary),
                    modifier = Modifier.testTag("save_discovery_edit")
                ) {
                    Text("Save Fact", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedQuestionForEdit = null }) {
                    Text("Cancel", color = TextSecondaryDark)
                }
            },
            containerColor = IndustrialNavySurface
        )
    }
}
