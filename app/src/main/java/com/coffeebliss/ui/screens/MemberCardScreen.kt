package com.coffeebliss.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffeebliss.ui.theme.*
import com.coffeebliss.utils.QRCodeUtils
import com.coffeebliss.viewmodel.CoffeeBlissViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberCardScreen(
    viewModel: CoffeeBlissViewModel,
    onBack: () -> Unit
) {
    val member by viewModel.currentMember.collectAsState()

    val qrBitmap: Bitmap? = remember(member) {
        member?.let {
            QRCodeUtils.generateQRCode("COFFEEBLISS:${it.memberNumber}:${it.email}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kartu Member Digital") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Digital Member Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                CoffeeBrown,
                                CoffeeLight,
                                Color(0xFF8B6355)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text("☕ Coffee Bliss", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Member Card", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when (member?.status) {
                                        "Gold" -> CoffeeGold
                                        "Platinum" -> Color(0xFFE5E4E2)
                                        else -> Color(0xFFC0C0C0)
                                    }
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = member?.status ?: "Silver",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoffeeDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = member?.name ?: "-",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "No. Member: ${member?.memberNumber ?: "-"}",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Text(
                        text = member?.email ?: "-",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Poin", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                            Text(
                                text = "${member?.totalPoints ?: 0}",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoffeeGold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Rp10.000 = 1 Poin", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                            Text("Kumpulkan terus!", fontSize = 11.sp, color = CoffeeAccent)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "QR Code Member",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Tunjukkan QR ini kepada kasir saat transaksi",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // QR Code
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    qrBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "QR Code Member",
                            modifier = Modifier.size(200.dp)
                        )
                    } ?: CircularProgressIndicator(modifier = Modifier.size(200.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = member?.memberNumber ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
