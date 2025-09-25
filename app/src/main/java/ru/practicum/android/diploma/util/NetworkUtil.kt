package ru.practicum.android.diploma.util

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission

/**
 * Utility class for network-related operations.
 *
 * This class provides method to check internet availability.
 */
class NetworkUtil(private val context: Context) {

    /**
     * Retrieves the network capabilities of the currently active network.
     *
     * This function requires the ACCESS_NETWORK_STATE permission.
     *
     * It obtains the ConnectivityManager system service and uses it to get the
     * active network and its capabilities.
     *
     * @param context The context used to access system services.
     * @return The [NetworkCapabilities] of the active network, or `null` if there is no active network.
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun getNetworkCapabilities(): NetworkCapabilities? {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return null
        return connectivityManager.getNetworkCapabilities(network)
    }

    /**
     * Checks if internet connection is available.
     *
     * This function requires the ACCESS_NETWORK_STATE permission.
     *
     * It determines internet availability by checking for an active network and verifying
     * its capabilities, specifically if it has internet access and is validated.
     *
     * @param context The context used to access system services.
     * @return `true` if an internet connection is available and validated, `false` otherwise.
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isInternetAvailable(): Boolean {
        val capabilities = getNetworkCapabilities()

        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
