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
                                itemsIndexed(players) { index, player ->
                                    PlayerItem(player, index == players.lastIndex, dims)
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
                                PlayerItem(player, player == players.last(), dims)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QrSection(qrBitmap: Bitmap?, groupName: String?, dims: AppDimensions) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.size(dims.qrCardSize),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(if (dims.isTablet) 20.dp else 16.dp),
                contentAlignment = Alignment.Center) {
                qrBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = stringResource(R.string.info_qr_description),
                        modifier = Modifier.fillMaxSize(),
                        filterQuality = FilterQuality.None
                    )
                } ?: CircularProgressIndicator()
            }
        }
        Spacer(Modifier.height(if (dims.isTablet) 20.dp else 16.dp))
        Text(
            text = groupName ?: stringResource(R.string.info_loading_group),
            style = dims.heading1(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.info_scan_to_join),
            style = dims.bodyText(),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MemberListHeader(count: Int, dims: AppDimensions) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(R.string.info_members_header), style = dims.heading2(), fontWeight = FontWeight.Bold)
        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape) {
            Text("$count",
                modifier = Modifier.padding(
                    horizontal = if (dims.isTablet) 14.dp else 10.dp,
                    vertical = if (dims.isTablet) 4.dp else 2.dp),
                style = dims.labelText(), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PlayerItem(player: Player, isLast: Boolean, dims: AppDimensions) {
    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(dims.memberItemPadding),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(dims.memberAvatarSize).clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center) {
                Text(player.name.take(1).uppercase(), style = dims.heading2(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
            Spacer(Modifier.width(if (dims.isTablet) 16.dp else 12.dp))
            Text(player.name, style = dims.heading2())
        }
        if (!isLast) HorizontalDivider(Modifier.padding(horizontal = dims.memberItemPadding),
            thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
    }
}

fun generateQrCode(content: String): Bitmap? {
    return try {
        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, 512, 512)
        val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
        for (x in 0 until 512) for (y in 0 until 512)
            bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        bitmap
    } catch (e: Exception) { null }
}