package services.fileIO

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.suspendCancellableCoroutine

actual class FileHandler actual constructor() {

    companion object {
        private var currentActivity: ComponentActivity? = null

        fun bindActivity(activity: ComponentActivity) {
            currentActivity = activity
        }
    }

    actual suspend fun openFile(mimeType: String): Pair<String, ByteArray>? {
        val activity = currentActivity ?: return null
        return suspendCancellableCoroutine { cont ->
            lateinit var launcher: ActivityResultLauncher<Array<String>>
            launcher = activity.activityResultRegistry.register(
                "open_file_${System.nanoTime()}",
                ActivityResultContracts.OpenDocument()
            ) { uri: Uri? ->
                val result = uri?.let {
                    val bytes = activity.contentResolver.openInputStream(it)?.use { stream -> stream.readBytes() }
                    bytes?.let { b -> Pair(it.toString(), b) }
                }
                cont.resume(result) { _, _, _ -> }
                launcher.unregister()
            }
            activity.runOnUiThread { launcher.launch(arrayOf(mimeType)) }
            cont.invokeOnCancellation { runCatching { launcher.unregister() } }
        }
    }
}
