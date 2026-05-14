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
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindmatrix.jalsanchaytracker.ui.components.CommunityIllustration
import com.mindmatrix.jalsanchaytracker.ui.components.IconBadge
import com.mindmatrix.jalsanchaytracker.ui.components.JalBackground
import com.mindmatrix.jalsanchaytracker.ui.components.JalLogoHeader
import com.mindmatrix.jalsanchaytracker.ui.components.PremiumCard
import com.mindmatrix.jalsanchaytracker.viewmodel.CommunityViewModel

private data class CommunityMember(
    val name: String,
    val location: String,
    val role: String
)

@Composable
fun CommunityScreen(viewModel: CommunityViewModel = hiltViewModel()) {
    val posts by viewModel.posts.collectAsState()
    var setup by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var memberName by remember { mutableStateOf("") }
    var memberLocation by remember { mutableStateOf("") }
    val members = remember {
        mutableStateListOf(
            CommunityMember("Asha Verma", "Bengaluru, Karnataka", "Rooftop system owner"),
            CommunityMember("Rahul Nair", "Kochi, Kerala", "DIY filter builder"),
            CommunityMember("Meera Patil", "Pune, Maharashtra", "Apartment volunteer")
        )
    }
    JalBackground {
        LazyColumn(
            Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
        item {
            JalLogoHeader("Community", "Share setups and learn from nearby harvesters")
        }
        item { CommunityIllustration() }
        item {
            PremiumCard {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    IconBadge(Icons.Default.PersonAdd)
                    Text("Members", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                Text("Add local members who are interested in rainwater harvesting groups, apartment drives, school projects, or neighborhood tank maintenance.")
                OutlinedTextField(memberName, { memberName = it }, label = { Text("Member name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(memberLocation, { memberLocation = it }, label = { Text("City / State") }, modifier = Modifier.fillMaxWidth())
                Button(
                    onClick = {
                        members.add(0, CommunityMember(memberName.trim(), memberLocation.trim(), "Community member"))
                        memberName = ""
                        memberLocation = ""
                    },
                    enabled = memberName.isNotBlank() && memberLocation.isNotBlank()
                ) { Text("Add member") }
            }
        }
        items(members) { member ->
            MemberCard(member)
        }
        item {
            PremiumCard {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    IconBadge(Icons.Default.LocationCity, MaterialTheme.colorScheme.secondary)
                    Text("Community feed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                OutlinedTextField(setup, { setup = it }, label = { Text("Your harvesting setup") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(city, { city = it }, label = { Text("City / State") }, modifier = Modifier.fillMaxWidth())
                Button(
                    onClick = {
                        viewModel.publish(setup, city, 0.0)
                        setup = ""
                    },
                    enabled = setup.isNotBlank()
                ) { Text("Share") }
            }
        }
        items(posts) { post ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(post.author, fontWeight = FontWeight.Bold)
                    Text(post.city)
                    Text(post.setupDescription)
                    Text("${post.savedLiters.toInt()} L shared impact", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        }
    }
}

@Composable
private fun MemberCard(member: CommunityMember) {
    Card(Modifier.fillMaxWidth(), colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White.copy(alpha = .94f))) {
        Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconBadge(Icons.Default.PersonAdd, MaterialTheme.colorScheme.primary)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(member.name, fontWeight = FontWeight.Bold)
                Text(member.location)
                Text(member.role, color = MaterialTheme.colorScheme.primary)
            }
            Text("Active", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold)
        }
    }
}
