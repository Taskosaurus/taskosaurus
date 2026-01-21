package at.htlleonding.taskosaurus.data.utitlity;

import android.os.Build

object EmulatorHelper {

    /**
     * Detects if the app is running on an emulator.
     * Returns true for AVD, Genymotion, and other common emulators.
     */
    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.lowercase().contains("emulator")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.lowercase().contains("emulator")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.PRODUCT == "google_sdk")
    }
}
