package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.data.model.Player
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupInfoScreen(
    groupId: Int,
    viewModel: ViewModel = viewModel()
) {
    val groups by viewModel.groups.collectAsState()
    val group = groups.find { it.id == groupId }
    val players = group?.players ?: emptyList()

    val qrData = "https://taskosaurus.at/group/$groupId"
    val qrBitmap = remember(qrData) { generateQrCode(qrData) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    val isTabletPortrait = configuration.screenWidthDp >= 600 && !isLandscape

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Einladen und Info", style = MaterialTheme.typography.titleMedium) },
                windowInsets = WindowInsets(top = 0.dp)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(0.4f).fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        QrSection(qrBitmap, group?.name)
                    }

                    Column(modifier = Modifier.weight(0.6f).fillMaxHeight()) {
                        MemberListHeader(players.size)
                        Spacer(modifier = Modifier.height(8.dp))
                        ScrollableMemberList(players, modifier = Modifier.weight(1f))
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(
                        horizontal = if (isTabletPortrait) 48.dp else 16.dp,
                        vertical = if (isTabletPortrait) 16.dp else 8.dp
                    )
                ) {
                    item {
                        QrSection(qrBitmap, group?.name, isTabletPortrait)
                        Spacer(modifier = Modifier.height(if (isTabletPortrait) 36.dp else 24.dp))
                        MemberListHeader(players.size, isTabletPortrait)
                        Spacer(modifier = Modifier.height(if (isTabletPortrait) 16.dp else 12.dp))
                    }
                    items(players) { player ->
                        PlayerItem(player, isLast = player == players.last(), isTabletPortrait)
                    }
                }
            }
        }
    }
}

@Composable
private fun QrSection(qrBitmap: Bitmap?, groupName: String?, isTabletPortrait: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.size(if (isTabletPortrait) 280.dp else 200.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(if (isTabletPortrait) 20.dp else 16.dp),
                contentAlignment = Alignment.Center
            ) {
                qrBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier.fillMaxSize(),
                        filterQuality = FilterQuality.None
                    )
                } ?: CircularProgressIndicator(strokeWidth = 2.dp)
            }
        }
        Spacer(modifier = Modifier.height(if (isTabletPortrait) 24.dp else 16.dp))
        Text(
            text = groupName ?: "Lade Gruppe...",
            style = if (isTabletPortrait) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Code scannen zum Beitreten",
            style = if (isTabletPortrait) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MemberListHeader(count: Int, isTabletPortrait: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Mitglieder",
            style = if (isTabletPortrait) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape) {
            Text(
                text = "$count",
                modifier = Modifier.padding(horizontal = if (isTabletPortrait) 14.dp else 10.dp, vertical = if (isTabletPortrait) 4.dp else 2.dp),
                style = if (isTabletPortrait) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ScrollableMemberList(players: List<Player>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(players) { index, player ->
                PlayerItem(player, isLast = index == players.lastIndex)
            }
        }
    }
}

@Composable
private fun PlayerItem(player: Player, isLast: Boolean, isTabletPortrait: Boolean = false) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(if (isTabletPortrait) 16.dp else 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(if (isTabletPortrait) 52.dp else 36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    player.name.take(1).uppercase(),
                    style = if (isTabletPortrait) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(modifier = Modifier.width(if (isTabletPortrait) 16.dp else 12.dp))
            Text(
                player.name,
                style = if (isTabletPortrait) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
            )
        }
        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }
    }
}

fun generateQrCode(content: String): Bitmap? {
    return try {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}