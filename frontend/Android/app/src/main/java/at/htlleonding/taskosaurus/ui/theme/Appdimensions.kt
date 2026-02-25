package at.htlleonding.taskosaurus.ui.theme

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Geräteklasse ─────────────────────────────────────────────────────────────
// PHONE = Handy (Portrait ODER Landscape — Handy-Landscape bleibt Handy!)
// TABLET_PORTRAIT / TABLET_LANDSCAPE = nur echte Tablets (screenWidthDp >= 600)

enum class DeviceClass { PHONE, TABLET_PORTRAIT, TABLET_LANDSCAPE }

// ─── Alle Größen zentral ──────────────────────────────────────────────────────

data class AppDimensions(
    val device: DeviceClass,

    // NAV RAIL
    val railWidth: Dp,
    val railWidthExpanded: Dp,
    val railIconSize: Dp,
    val railItemHeight: Dp,

    // NAV BOTTOM BAR
    val navIconSize: Dp,

    // FLOATING BACKGROUND CARDS (TitleScreen)
    val floatingCardWidth: Dp,
    val floatingCardAlpha: Float,
    // Anzahl der schwebenden Karten-Positionen (4 Portrait, 6 Landscape)
    val floatingCardPositionCount: Int,

    // TITLE SCREEN
    val titleIconSurface: Dp,
    val titleIconInner: Dp,
    val titleSpacerAfterIcon: Dp,
    val titleSpacerAfterSubtitle: Dp,
    val titleMaxWidth: Dp,
    val fabHeight: Dp,
    val fabIconSize: Dp,
    val fabPadding: Dp,
    val fabSpacing: Dp,

    // GAME SCREEN — Abstimmen
    val voteButtonHeight: Dp,
    val playerAvatarSize: Dp,
    val playerItemPaddingH: Dp,
    val playerItemPaddingV: Dp,
    val progressBarHeight: Dp,
    val progressCardPadding: Dp,
    val questionCardPadding: Dp,
    val questionCardRadius: Dp,

    // PODIUM — Handy-Landscape bekommt kompaktere Werte
    val podiumRowHeight: Dp,
    val podiumBar1: Dp,
    val podiumBar2: Dp,
    val podiumBar3: Dp,
    val podiumColWidth: Dp,
    val podiumAvatarSize: Dp,
    val podiumTinyAvatarSize: Dp,
    val podiumMedalSize: TextUnit,
    val podiumColSpacing: Dp,

    // GROUP LIST ITEM
    val groupCardPadding: Dp,
    val groupIconSize: Dp,
    val groupDotSize: Dp,
    val groupLeaderIconSize: Dp,
    val groupLeaderRadius: Dp,
    val groupLeaderPaddingH: Dp,
    val groupLeaderPaddingV: Dp,

    // SETTINGS
    val settingsPaddingH: Dp,
    val settingsPaddingV: Dp,
    val settingsAvatarSize: Dp,
    val settingsMaxWidth: Dp,

    // GROUP INFO
    val qrCardSize: Dp,
    val memberAvatarSize: Dp,
    val memberItemPadding: Dp,

    // LOGIN
    val loginIconSize: Dp,
    val loginFieldHeight: Dp,
    val loginButtonHeight: Dp,
    val loginPaddingH: Dp,

    // ALLGEMEIN
    val cardRadius: Dp,
    val screenPaddingH: Dp,
    val screenPaddingV: Dp,
    val sectionSpacing: Dp,
    val itemSpacing: Dp,
    // Abstand zwischen Gruppen-Karten in der Liste
    val groupListSpacing: Dp,
) {
    val isTablet: Boolean get() = device != DeviceClass.PHONE
    val isPortrait: Boolean get() = device == DeviceClass.TABLET_PORTRAIT
    val isLandscape: Boolean get() = device == DeviceClass.TABLET_LANDSCAPE
}

// ─── Konfigurationen ──────────────────────────────────────────────────────────

// HANDY — exakt die Originalwerte, nichts verändert
val PhoneDimensions = AppDimensions(
    device = DeviceClass.PHONE,
    railWidth = 80.dp, railWidthExpanded = 220.dp,
    railIconSize = 24.dp, railItemHeight = 52.dp,
    navIconSize = 24.dp,
    floatingCardWidth = 125.dp, floatingCardAlpha = 0.30f, floatingCardPositionCount = 4,
    titleIconSurface = 100.dp, titleIconInner = 50.dp,
    titleSpacerAfterIcon = 40.dp, titleSpacerAfterSubtitle = 16.dp, titleMaxWidth = 300.dp,
    fabHeight = 48.dp, fabIconSize = 24.dp, fabPadding = 24.dp, fabSpacing = 16.dp,
    voteButtonHeight = 48.dp,
    playerAvatarSize = 34.dp, playerItemPaddingH = 12.dp, playerItemPaddingV = 10.dp,
    progressBarHeight = 6.dp, progressCardPadding = 10.dp,
    questionCardPadding = 16.dp, questionCardRadius = 12.dp,
    // Handy-Portrait Podium (original)
    podiumRowHeight = 240.dp,
    podiumBar1 = 140.dp, podiumBar2 = 100.dp, podiumBar3 = 80.dp,
    podiumColWidth = 90.dp, podiumAvatarSize = 40.dp, podiumTinyAvatarSize = 28.dp,
    podiumMedalSize = 24.sp, podiumColSpacing = 8.dp,
    groupCardPadding = 16.dp, groupIconSize = 24.dp, groupDotSize = 14.dp,
    groupLeaderIconSize = 18.dp, groupLeaderRadius = 10.dp,
    groupLeaderPaddingH = 12.dp, groupLeaderPaddingV = 10.dp,
    settingsPaddingH = 24.dp, settingsPaddingV = 20.dp,
    settingsAvatarSize = 100.dp, settingsMaxWidth = 9999.dp,
    qrCardSize = 200.dp, memberAvatarSize = 36.dp, memberItemPadding = 12.dp,
    loginIconSize = 100.dp, loginFieldHeight = 56.dp, loginButtonHeight = 56.dp, loginPaddingH = 24.dp,
    cardRadius = 12.dp, screenPaddingH = 8.dp, screenPaddingV = 4.dp,
    sectionSpacing = 12.dp, itemSpacing = 8.dp,
    groupListSpacing = 8.dp,
)

val TabletPortraitDimensions = AppDimensions(
    device = DeviceClass.TABLET_PORTRAIT,
    railWidth = 96.dp, railWidthExpanded = 260.dp,
    railIconSize = 30.dp, railItemHeight = 68.dp,
    navIconSize = 28.dp,
    // Größere schwebende Karten + mehr Positionen = überall verteilt
    floatingCardWidth = 200.dp, floatingCardAlpha = 0.30f, floatingCardPositionCount = 6,
    titleIconSurface = 160.dp, titleIconInner = 84.dp,
    titleSpacerAfterIcon = 64.dp, titleSpacerAfterSubtitle = 28.dp, titleMaxWidth = 560.dp,
    fabHeight = 68.dp, fabIconSize = 30.dp, fabPadding = 36.dp, fabSpacing = 20.dp,
    voteButtonHeight = 76.dp,
    playerAvatarSize = 56.dp, playerItemPaddingH = 22.dp, playerItemPaddingV = 20.dp,
    progressBarHeight = 12.dp, progressCardPadding = 22.dp,
    questionCardPadding = 28.dp, questionCardRadius = 20.dp,
    podiumRowHeight = 540.dp,
    podiumBar1 = 320.dp, podiumBar2 = 235.dp, podiumBar3 = 185.dp,
    podiumColWidth = 165.dp, podiumAvatarSize = 80.dp, podiumTinyAvatarSize = 52.dp,
    podiumMedalSize = 46.sp, podiumColSpacing = 28.dp,
    groupCardPadding = 22.dp, groupIconSize = 30.dp, groupDotSize = 18.dp,
    groupLeaderIconSize = 22.dp, groupLeaderRadius = 14.dp,
    groupLeaderPaddingH = 16.dp, groupLeaderPaddingV = 14.dp,
    settingsPaddingH = 48.dp, settingsPaddingV = 28.dp,
    settingsAvatarSize = 148.dp, settingsMaxWidth = 520.dp,
    qrCardSize = 280.dp, memberAvatarSize = 52.dp, memberItemPadding = 18.dp,
    loginIconSize = 120.dp, loginFieldHeight = 68.dp, loginButtonHeight = 68.dp, loginPaddingH = 64.dp,
    cardRadius = 20.dp, screenPaddingH = 28.dp, screenPaddingV = 16.dp,
    sectionSpacing = 20.dp, itemSpacing = 12.dp,
    groupListSpacing = 12.dp,
)

val TabletLandscapeDimensions = AppDimensions(
    device = DeviceClass.TABLET_LANDSCAPE,
    railWidth = 96.dp, railWidthExpanded = 260.dp,
    railIconSize = 30.dp, railItemHeight = 68.dp,
    navIconSize = 28.dp,
    floatingCardWidth = 160.dp, floatingCardAlpha = 0.15f, floatingCardPositionCount = 6,
    titleIconSurface = 120.dp, titleIconInner = 62.dp,
    titleSpacerAfterIcon = 36.dp, titleSpacerAfterSubtitle = 20.dp, titleMaxWidth = 440.dp,
    fabHeight = 64.dp, fabIconSize = 28.dp, fabPadding = 32.dp, fabSpacing = 18.dp,
    voteButtonHeight = 64.dp,
    playerAvatarSize = 46.dp, playerItemPaddingH = 18.dp, playerItemPaddingV = 14.dp,
    progressBarHeight = 10.dp, progressCardPadding = 16.dp,
    questionCardPadding = 22.dp, questionCardRadius = 16.dp,
    podiumRowHeight = 320.dp,
    podiumBar1 = 195.dp, podiumBar2 = 145.dp, podiumBar3 = 115.dp,
    podiumColWidth = 130.dp, podiumAvatarSize = 56.dp, podiumTinyAvatarSize = 38.dp,
    podiumMedalSize = 32.sp, podiumColSpacing = 16.dp,
    groupCardPadding = 18.dp, groupIconSize = 28.dp, groupDotSize = 16.dp,
    groupLeaderIconSize = 20.dp, groupLeaderRadius = 12.dp,
    groupLeaderPaddingH = 14.dp, groupLeaderPaddingV = 11.dp,
    settingsPaddingH = 32.dp, settingsPaddingV = 20.dp,
    settingsAvatarSize = 110.dp, settingsMaxWidth = 9999.dp,
    qrCardSize = 220.dp, memberAvatarSize = 44.dp, memberItemPadding = 14.dp,
    loginIconSize = 80.dp, loginFieldHeight = 60.dp, loginButtonHeight = 60.dp, loginPaddingH = 48.dp,
    cardRadius = 16.dp, screenPaddingH = 16.dp, screenPaddingV = 8.dp,
    sectionSpacing = 16.dp, itemSpacing = 10.dp,
    groupListSpacing = 10.dp,
)

// ─── CompositionLocal ─────────────────────────────────────────────────────────

val LocalAppDimensions = compositionLocalOf<AppDimensions> { PhoneDimensions }

@Composable
fun AppDimensionsProvider(content: @Composable () -> Unit) {
    val configuration = LocalConfiguration.current
    val smallestScreenWidth = configuration.smallestScreenWidthDp
    val isTablet = smallestScreenWidth >= 600
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val dims = remember(isTablet, isLandscape) {
        when {
            isTablet && isLandscape  -> TabletLandscapeDimensions
            isTablet && !isLandscape -> TabletPortraitDimensions
            else                     -> PhoneDimensions  // Handy Portrait UND Landscape!
        }
    }
    CompositionLocalProvider(LocalAppDimensions provides dims, content = content)
}

// ─── Typo-Shortcuts ───────────────────────────────────────────────────────────

@Composable fun AppDimensions.displayTitle() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.displayLarge
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.displaySmall
    DeviceClass.PHONE            -> MaterialTheme.typography.headlineLarge
}

@Composable fun AppDimensions.heading1() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.headlineLarge
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.headlineMedium
    DeviceClass.PHONE            -> MaterialTheme.typography.titleLarge
}

@Composable fun AppDimensions.heading2() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.titleLarge
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.titleMedium
    DeviceClass.PHONE            -> MaterialTheme.typography.titleMedium
}

@Composable fun AppDimensions.bodyText() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.bodyLarge
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.bodyMedium
    DeviceClass.PHONE            -> MaterialTheme.typography.bodyMedium
}

@Composable fun AppDimensions.labelText() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.titleSmall
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.bodySmall
    DeviceClass.PHONE            -> MaterialTheme.typography.labelMedium
}

@Composable fun AppDimensions.subtitleText() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.headlineSmall
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.titleLarge
    DeviceClass.PHONE            -> MaterialTheme.typography.bodyLarge
}

@Composable fun AppDimensions.podiumTitle() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.headlineLarge
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.headlineMedium
    DeviceClass.PHONE            -> MaterialTheme.typography.titleMedium
}

@Composable fun AppDimensions.podiumBarNumber() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.displayMedium
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.headlineMedium
    DeviceClass.PHONE            -> MaterialTheme.typography.titleLarge
}

@Composable fun AppDimensions.podiumName() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.titleMedium
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.bodyMedium
    DeviceClass.PHONE            -> MaterialTheme.typography.labelMedium
}

@Composable fun AppDimensions.podiumAvatarLetter() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.headlineMedium
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.titleLarge
    DeviceClass.PHONE            -> MaterialTheme.typography.titleMedium
}

@Composable fun AppDimensions.questionText() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.headlineSmall
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.titleLarge
    DeviceClass.PHONE            -> MaterialTheme.typography.titleMedium
}

@Composable fun AppDimensions.voteButtonText() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.headlineSmall
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.titleLarge
    DeviceClass.PHONE            -> MaterialTheme.typography.titleMedium
}

@Composable fun AppDimensions.progressText() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.titleMedium
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.titleSmall
    DeviceClass.PHONE            -> MaterialTheme.typography.labelMedium
}

@Composable fun AppDimensions.groupName() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.titleLarge
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.titleMedium
    DeviceClass.PHONE            -> MaterialTheme.typography.titleMedium
}

@Composable fun AppDimensions.groupQuestion() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.bodyLarge
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.bodyMedium
    DeviceClass.PHONE            -> MaterialTheme.typography.bodyMedium
}

@Composable fun AppDimensions.settingsTitle() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.displaySmall
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.headlineLarge
    DeviceClass.PHONE            -> MaterialTheme.typography.headlineMedium
}

@Composable fun AppDimensions.settingsName() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.headlineMedium
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.titleLarge
    DeviceClass.PHONE            -> MaterialTheme.typography.titleLarge
}

@Composable fun AppDimensions.loginTitle() = when (device) {
    DeviceClass.TABLET_PORTRAIT  -> MaterialTheme.typography.displayMedium
    DeviceClass.TABLET_LANDSCAPE -> MaterialTheme.typography.headlineMedium
    DeviceClass.PHONE            -> MaterialTheme.typography.displaySmall
}
