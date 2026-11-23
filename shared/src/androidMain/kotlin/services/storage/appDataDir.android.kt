package services.storage

import ru.alexmaryin.simschecklist.AppAndroid
import java.io.File

actual fun getAppDataDir(): File = AppAndroid.instance().filesDir
