package at.htlleonding.taskosaurus.view.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import at.htlleonding.taskosaurus.R
import at.htlleonding.taskosaurus.ui.theme.AppDimensions
import at.htlleonding.taskosaurus.ui.theme.bodyText
import at.htlleonding.taskosaurus.ui.theme.heading1

@Composable
fun QrSection(qrBitmap: Bitmap?, groupName: String?, dims: AppDimensions) {
    val isPhoneLandscape = !dims.isTablet && dims.isLandscape
    val adjustedCardSize = if (isPhoneLandscape) 140.dp else dims.qrCardSize
    val adjustedSpacer = if (isPhoneLandscape) 8.dp else (if (dims.isTablet) 20.dp else 16.dp)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.size(adjustedCardSize),
            shape = RoundedCornerShape(if (isPhoneLandscape) 16.dp else 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(if (isPhoneLandscape) 10.dp else 16.dp),
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