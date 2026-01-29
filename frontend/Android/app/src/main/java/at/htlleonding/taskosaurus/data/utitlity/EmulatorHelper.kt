package at.htlleonding.taskosaurus.data.utitlity;

import android.os.Build

object EmulatorHelper {

    /**
     * Detects if the app is running on an emulator (AVD or Genymotion).
     * Modern AVDs are detected via Build.FINGERPRINT, Build.MODEL, and Build.HARDWARE.
     * Real devices like Samsung, Pixel, OnePlus are NOT misdetected.
     */
    fun isEmulator(): Boolean {
        val fingerprint = Build.FINGERPRINT.lowercase()
        val model = Build.MODEL.lowercase()
        val hardware = Build.HARDWARE.lowercase()
        val brand = Build.BRAND.lowercase()
        val device = Build.DEVICE.lowercase()
        val product = Build.PRODUCT.lowercase()

        // The "Smoking Gun" Log
        println("DEVICE_CHECK: Brand=$brand, Model=$model, Hardware=$hardware, Fingerprint=$fingerprint, Product=$product")

        val isAvd = fingerprint.contains("generic") ||
                fingerprint.contains("emulator") ||
                fingerprint.contains("sdk_gphone") ||
                model.contains("google_sdk") ||
                model.contains("emulator") ||
                hardware.contains("goldfish") ||
                hardware.contains("ranchu") ||
                (brand.startsWith("generic") && device.startsWith("generic"))

        return isAvd
    }
}
