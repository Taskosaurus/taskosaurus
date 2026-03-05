package at.htlleonding.taskosaurus.data.model

import androidx.compose.ui.graphics.Color

data class ConfettiParticle(
    val initialX: Float, val initialY: Float, val speed: Float,
    val rotation: Float, val rotationSpeed: Float, val color: Color, val size: Float
)
