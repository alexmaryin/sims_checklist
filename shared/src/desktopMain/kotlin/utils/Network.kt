package utils

import java.net.NetworkInterface

actual fun isNetworkConnected() = NetworkInterface.networkInterfaces().anyMatch {
    it.isUp && it.isLoopback.not()
}