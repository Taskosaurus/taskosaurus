package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.data.model.Player
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupInfoScreen(
    groupId: Int,
    viewModel: ViewModel = viewModel(),
    isTabletMode: Boolean = false,
    onBackToGame: () -> Unit = {} // NEU: Damit wir am Tablet zurück zum Spiel kommen
) {
    val groups by viewModel.groups.collectAsState()
    val group = groups.find { it.id == groupId }
    val players = group?.players ?: emptyList()
    val qrData = "https://taskosaurus.at/group/$groupId"
    val qrBitmap = remember(qrData) { generateQrCode(qrData) }

    val configuration = LocalConfiguration.current
    // Wir nutzen das Landscape-Layout, wenn genug Platz da ist
    val useLandscapeLayout = configuration.screenWidthDp > 600

    Scaffold(
        topBar = {
            if (!isTabletMode) {
                TopAppBar(title = { Text("Einladen und Info") })
            } else {
                // Im Tablet-Modus eine kleine eigene Bar für den Rückweg
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                }
            }
        }
    ) { padding ->
        val topPadding = if (isTabletMode) padding.calculateTopPadding() else padding.calculateTopPadding()

        Box(modifier = Modifier.padding(top = topPadding).fillMaxSize()) {
            if (useLandscapeLayout) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        QrSection(qrBitmap, group?.name)
                    }

                    Column(modifier = Modifier.weight(1.2f).fillMaxHeight()) {
                        MemberListHeader(players.size)
                        Spacer(modifier = Modifier.height(16.dp))
                        ScrollableMemberList(players)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        QrSection(qrBitmap, group?.name)
                        Spacer(modifier = Modifier.height(32.dp))
                        MemberListHeader(players.size)
                    }
                    itemsIndexed(players) { index, player ->
                        PlayerItem(player, isLast = index == players.lastIndex)
                    }
                }
            }
        }
    }
}

@Composable
private fun QrSection(qrBitmap: Bitmap?, groupName: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(220.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Box(Modifier.padding(20.dp)) {
                qrBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "QR",
                        modifier = Modifier.fillMaxSize(),
                        filterQuality = FilterQuality.None
                    )
                } ?: CircularProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(groupName ?: "", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Code scannen zum Beitreten", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}

@Composable
private fun MemberListHeader(count: Int) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text("Mitglieder", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
            Text("$count", modifier = Modifier.padding(4.dp), color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun ScrollableMemberList(players: List<Player>) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            itemsIndexed(players) { index, player ->
                PlayerItem(player, isLast = index == players.lastIndex)
            }
        }
    }
}

@Composable
private fun PlayerItem(player: Player, isLast: Boolean) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Text(player.name.take(1).uppercase(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.width(16.dp))
            Text(player.name, style = MaterialTheme.typography.bodyLarge)
        }
        if (!isLast) HorizontalDivider(Modifier.padding(top = 12.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
    }
}

fun generateQrCode(content: String): Bitmap? {
    return try {
        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, 512, 512)
        val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
        for (x in 0 until 512) {
            for (y in 0 until 512) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) { null }
}