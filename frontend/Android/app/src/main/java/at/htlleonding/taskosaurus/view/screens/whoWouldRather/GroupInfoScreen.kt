package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.R
import at.htlleonding.taskosaurus.data.model.Player
import at.htlleonding.taskosaurus.ui.theme.AppDimensions
import at.htlleonding.taskosaurus.ui.theme.LocalAppDimensions
import at.htlleonding.taskosaurus.ui.theme.bodyText
import at.htlleonding.taskosaurus.ui.theme.heading1
import at.htlleonding.taskosaurus.ui.theme.heading2
import at.htlleonding.taskosaurus.ui.theme.labelText
import at.htlleonding.taskosaurus.view.components.MemberListHeader
import at.htlleonding.taskosaurus.view.components.PlayerCard
import at.htlleonding.taskosaurus.view.components.QrSection
import at.htlleonding.taskosaurus.view.utility.generateQrCode
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupInfoScreen(
    groupId: Int,
    viewModel: ViewModel = viewModel(),
    isTabletMode: Boolean = false,
    onBackToGame: () -> Unit = {}
) {
    val dims = LocalAppDimensions.current
    val groups by viewModel.groups.collectAsState()
    val group = groups.find { it.id == groupId }
    val players = group?.players ?: emptyList()
    val qrData = "https://taskosaurus.at/group/$groupId"
    val qrBitmap = remember(qrData) { generateQrCode(qrData) }

    Scaffold(
        topBar = {
            if (!isTabletMode) {
                TopAppBar(title = { Text(stringResource(R.string.info_title)) })
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(top = padding.calculateTopPadding()).fillMaxSize()) {
            if (dims.isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(dims.screenPaddingH),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        QrSection(qrBitmap, group?.name, dims)
                    }
                    Column(modifier = Modifier.weight(1.2f).fillMaxHeight()) {
                        MemberListHeader(players.size, dims)
                        Spacer(Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        ) {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(players) { player ->
                                    PlayerCard(player, false, dims, onClick = {})
                                    if (player != players.last()) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = dims.playerItemPaddingH),
                                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = dims.screenPaddingH, vertical = dims.screenPaddingV)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        QrSection(qrBitmap, group?.name, dims)
                        Spacer(Modifier.height(if (dims.isTablet) 36.dp else 24.dp))
                        MemberListHeader(players.size, dims)
                        Spacer(Modifier.height(if (dims.isTablet) 12.dp else 8.dp))
                    }

                    Card(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        shape = RoundedCornerShape(if (dims.isTablet) 20.dp else 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            items(players) { player ->
                                PlayerCard(player, false, dims, onClick = {})
                                if (player != players.last()) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = dims.playerItemPaddingH),
                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}