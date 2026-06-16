package com.coffeebliss.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffeebliss.utils.Reward
import com.coffeebliss.utils.RewardCatalog
import com.coffeebliss.viewmodel.CoffeeBlissViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemScreen(
    viewModel: CoffeeBlissViewModel,
    onBack: () -> Unit,
    onRedeemHistoryClick: () -> Unit
) {
    val member by viewModel.currentMember.collectAsState()
    var message by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var selectedReward by remember { mutableStateOf<Reward?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog && selectedReward != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Konfirmasi Penukaran") },
            text = {
                Column {
                    Text("Apakah kamu yakin ingin menukar:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "${selectedReward!!.icon} ${selectedReward!!.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text("dengan ${selectedReward!!.pointsRequired} Poin?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Poin saat ini: ${member?.totalPoints ?: 0}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                    Text(
                        "Sisa poin: ${(member?.totalPoints ?: 0) - selectedReward!!.pointsRequired}",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showConfirmDialog = false
                    selectedReward?.let { reward ->
                        viewModel.redeemReward(reward.name, reward.pointsRequired) { success, msg ->
                            message = msg
                            isSuccess = success
                        }
                    }
                }) { Text("Tukar Sekarang") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tukar Poin") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = onRedeemHistoryClick) {
                        Icon(Icons.Default.History, "Riwayat Redeem", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Current points card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Poin Kamu Saat Ini", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                "${member?.totalPoints ?: 0} Poin",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text("🏆", fontSize = 40.sp)
                    }
                }
            }

            if (message.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSuccess)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = message,
                            modifier = Modifier.padding(16.dp),
                            color = if (isSuccess) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            item {
                Text(
                    "Pilih Reward",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(RewardCatalog.rewards) { reward ->
                RewardCard(
                    reward = reward,
                    currentPoints = member?.totalPoints ?: 0,
                    onRedeem = {
                        selectedReward = reward
                        showConfirmDialog = true
                        message = ""
                    }
                )
            }
        }
    }
}

@Composable
private fun RewardCard(
    reward: Reward,
    currentPoints: Int,
    onRedeem: () -> Unit
) {
    val canRedeem = currentPoints >= reward.pointsRequired

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (canRedeem) MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(reward.icon, fontSize = 40.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    reward.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (canRedeem) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    reward.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${reward.pointsRequired} Poin",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onRedeem,
                enabled = canRedeem,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (canRedeem) "Tukar" else "Kurang\nPoin", fontSize = 12.sp)
            }
        }
    }
}
