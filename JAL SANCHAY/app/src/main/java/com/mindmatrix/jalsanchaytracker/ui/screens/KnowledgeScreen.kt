package com.mindmatrix.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mindmatrix.jalsanchaytracker.domain.model.KnowledgeArticle
import com.mindmatrix.jalsanchaytracker.ui.components.IconBadge
import com.mindmatrix.jalsanchaytracker.ui.components.JalBackground
import com.mindmatrix.jalsanchaytracker.ui.components.JalLogoHeader
import com.mindmatrix.jalsanchaytracker.ui.components.KnowledgeIllustration
import com.mindmatrix.jalsanchaytracker.ui.components.PremiumCard

private data class TextResource(
    val title: String,
    val source: String,
    val link: String,
    val note: String
)

@Composable
fun KnowledgeScreen() {
    val articles = listOf(
        KnowledgeArticle("Urban rooftop harvesting", "Urban", "Use first-flush filters, covered tanks, and gutter guards. In apartments, connect multiple downpipes to one shared storage tank only after checking overflow and maintenance access."),
        KnowledgeArticle("Rural recharge pits", "Rural", "Recharge pits near farms help groundwater recover and reduce runoff. Use layers of stones, gravel, and sand so muddy water does not directly enter the ground."),
        KnowledgeArticle("DIY barrel system", "DIY", "A food-grade barrel, mesh filter, overflow pipe, and tap can support gardening water needs. Keep the barrel covered to prevent mosquitoes and algae."),
        KnowledgeArticle("Maintenance checklist", "DIY", "Clean catchments before monsoon, inspect cracks, remove leaves from gutters, wash filters, and drain stagnant sections regularly."),
        KnowledgeArticle("Understanding runoff", "Basics", "Runoff coefficient is the percentage of rainwater that reaches storage. Smooth concrete or metal roofs collect more water than rough tile, soil, or broken surfaces."),
        KnowledgeArticle("Tank overflow planning", "Basics", "If collected water is more than tank capacity, the extra water should go to a recharge pit, garden bed, or safe drain. Never allow overflow near building foundations.")
    )
    val resources = listOf(
        TextResource(
            "Rainwater harvesting books",
            "Google Books",
            "https://books.google.com/books?q=rainwater+harvesting",
            "Use this search to find previewable books and manuals about rooftop harvesting, tank sizing, and water conservation."
        ),
        TextResource(
            "Rainwater harvesting research papers",
            "Google Scholar",
            "https://scholar.google.com/scholar?q=rainwater+harvesting+rooftop+runoff+coefficient",
            "Useful for project reports, references, formulas, runoff coefficients, and case studies."
        ),
        TextResource(
            "Water conservation guides",
            "Google Search",
            "https://www.google.com/search?q=rainwater+harvesting+guide+pdf",
            "Search for text/PDF guides from universities, governments, and water conservation organizations."
        )
    )
    val categories = listOf("All", "Basics", "Urban", "Rural", "DIY")
    var selected by remember { mutableStateOf("All") }
    JalBackground {
        LazyColumn(
            Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                JalLogoHeader("Knowledge hub", "Text-only learning notes, guides, and eBook links")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { category ->
                        FilterChip(
                            selected = selected == category,
                            onClick = { selected = category },
                            label = { Text(category) }
                        )
                    }
                }
            }
            item { KnowledgeIllustration() }
            item {
                PremiumCard {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        IconBadge(Icons.Default.School)
                        Text("Quick learning path", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Text("1. Measure your catchment area in square meters.")
                    Text("2. Learn local rainfall in millimeters from weather reports.")
                    Text("3. Pick runoff coefficient based on roof material.")
                    Text("4. Compare collected liters with tank capacity.")
                    Text("5. Plan overflow into recharge pits or gardens.")
                }
            }
            items(articles.filter { selected == "All" || it.category == selected }) { article ->
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = .94f))
                ) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        IconBadge(Icons.Default.MenuBook, MaterialTheme.colorScheme.primary)
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(article.title, fontWeight = FontWeight.Bold)
                            Text(article.category, color = MaterialTheme.colorScheme.primary)
                            Text(article.body)
                        }
                    }
                }
            }
            item {
                Text("eBooks and text resources", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            items(resources) { resource ->
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = .94f))
                ) {
                    SelectionContainer {
                        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            IconBadge(Icons.Default.Public, MaterialTheme.colorScheme.secondary)
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(resource.title, fontWeight = FontWeight.Bold)
                                Text(resource.source, color = MaterialTheme.colorScheme.primary)
                                Text(resource.note)
                                Text(resource.link, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
