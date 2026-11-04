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
import com.example.data.model.KnowledgeArticle
import com.example.data.model.KnowledgeCategory
import com.example.ui.AurelionViewModel
import com.example.ui.theme.*

@Composable
fun KnowledgeScreen(viewModel: AurelionViewModel) {
    val articles = viewModel.knowledgeArticles
    var selectedCategory by remember { mutableStateOf<KnowledgeCategory?>(null) }
    var expandedArticleId by remember { mutableStateOf<String?>(articles.firstOrNull()?.id) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredArticles = articles.filter { article ->
        val matchesCategory = selectedCategory == null || article.category == selectedCategory
        val matchesSearch = searchQuery.isBlank() ||
                article.title.contains(searchQuery, ignoreCase = true) ||
                article.plainLanguageSummary.contains(searchQuery, ignoreCase = true) ||
                article.technicalExplanation.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(IndustrialNavyDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // --- HEADER ---
        item {
            Column {
                Text(
                    text = "CHEMICAL ENGINEERING KNOWLEDGE CENTER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberCopperBright,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Peer-Reviewed Petrochemical Engineering",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Combines plain-language explanations for operations with rigorous thermodynamic equations for professional engineers.",
                    fontSize = 12.sp,
                    color = TextSecondaryDark
                )
            }
        }

        // --- SEARCH BAR ---
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search articles, equations, ASTM methods...", color = TextSecondaryDark, fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AmberCopperBright) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("knowledge_search_field"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmberCopperBright,
                    unfocusedBorderColor = IndustrialBorder,
                    focusedContainerColor = IndustrialNavyCard,
                    unfocusedContainerColor = IndustrialNavyCard,
                    focusedTextColor = TextPrimaryDark,
                    unfocusedTextColor = TextPrimaryDark
                ),
                shape = RoundedCornerShape(10.dp)
            )
        }

        // --- CATEGORY PILLS ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    color = if (selectedCategory == null) AmberCopperPrimary else IndustrialNavyCard,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { selectedCategory = null }
                ) {
                    Text(
                        text = "All Categories",
                        fontSize = 11.sp,
                        fontWeight = if (selectedCategory == null) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedCategory == null) IndustrialNavyDark else TextPrimaryDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                KnowledgeCategory.values().take(3).forEach { cat ->
                    val isSel = selectedCategory == cat
                    Surface(
                        color = if (isSel) AmberCopperPrimary else IndustrialNavyCard,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable { selectedCategory = cat }
                    ) {
                        Text(
                            text = cat.label.split(" ").first(),
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) IndustrialNavyDark else TextPrimaryDark,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // --- ARTICLE LIST ---
        items(filteredArticles) { article ->
            val isExpanded = expandedArticleId == article.id

            Card(
                colors = CardDefaults.cardColors(containerColor = IndustrialNavyCard),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isExpanded) AmberCopperPrimary else IndustrialBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedArticleId = if (isExpanded) null else article.id }
                    .testTag("article_${article.id}")
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
                                text = article.category.label.uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberCopperBright,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = AmberCopperBright
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = article.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = article.plainLanguageSummary,
                        fontSize = 12.sp,
                        color = TextSecondaryDark,
                        lineHeight = 16.sp
                    )

                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            HorizontalDivider(color = IndustrialBorder)

                            // Technical Explanation
                            SectionBox(title = "TECHNICAL ENGINEERING THEORY") {
                                Text(
                                    text = article.technicalExplanation,
                                    fontSize = 12.sp,
                                    color = TextPrimaryDark,
                                    lineHeight = 16.sp
                                )
                            }

                            // Governing Equations
                            SectionBox(title = "GOVERNING EQUATIONS") {
                                Text(
                                    text = article.governingEquations,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberCopperBright
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = article.variablesAndUnits,
                                    fontSize = 10.sp,
                                    color = TextSecondaryDark
                                )
                            }

                            // Safety Considerations
                            SectionBox(title = "PROCESS SAFETY & HAZARDS", isSafety = true) {
                                Text(
                                    text = article.safetyAndEnvironmentalRules,
                                    fontSize = 11.sp,
                                    color = StatusDangerRed,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 15.sp
                                )
                            }

                            // Authoritative Literature
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Sources: ${article.literatureSources.joinToString(" • ")}",
                                    fontSize = 10.sp,
                                    color = AmberCopperLight,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Text(
                                text = "Verification: ${article.verifiedBy}",
                                fontSize = 10.sp,
                                color = StatusSafeGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionBox(
    title: String,
    isSafety: Boolean = false,
    content: @Composable () -> Unit
) {
    Surface(
        color = if (isSafety) StatusDangerRed.copy(alpha = 0.08f) else IndustrialNavySurface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSafety) StatusDangerRed.copy(alpha = 0.3f) else IndustrialBorder
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSafety) StatusDangerRed else AmberCopperBright,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            content()
        }
    }
}
