package at.htlleonding.taskosaurus.view.components

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import at.htlleonding.taskosaurus.data.model.ConfettiParticle
import kotlin.collections.forEach

@Composable
fun ConfettiAnimation(particles: List<ConfettiParticle>, transition: InfiniteTransition) {
    val time by transition.animateFloat(0f, 1000f, infiniteRepeatable(tween(8000, easing = LinearEasing)), "confetti")
    Canvas(Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val progress = (time * particle.speed / 100f) % 1.5f
            if (progress <= 1.0f) {
                val x = size.width * particle.initialX
                val y = size.height * progress
                rotate(particle.rotation + time * particle.rotationSpeed, Offset(x, y)) {
                    drawRect(
                        particle.color.copy(alpha = if (progress > 0.8f) (1f - progress) * 5f else 1f),
                        Offset(x - particle.size / 2, y - particle.size / 2),
                        androidx.compose.ui.geometry.Size(particle.size, particle.size)
                    )
                }
            }
        }
    }
}