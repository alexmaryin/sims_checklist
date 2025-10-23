package commonUi.utils

import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    companion object {
        fun fromWindowSize(windowsSize: WindowSizeClass): DeviceConfiguration {
            val widthClass = windowsSize.windowWidthSizeClass
            val heightClass = windowsSize.windowHeightSizeClass
            return when (widthClass) {
                WindowWidthSizeClass.COMPACT if heightClass == WindowHeightSizeClass.MEDIUM -> MOBILE_PORTRAIT
                WindowWidthSizeClass.COMPACT if heightClass == WindowHeightSizeClass.EXPANDED -> MOBILE_PORTRAIT
                WindowWidthSizeClass.EXPANDED if heightClass == WindowHeightSizeClass.COMPACT -> MOBILE_LANDSCAPE
                WindowWidthSizeClass.MEDIUM if heightClass == WindowHeightSizeClass.EXPANDED -> TABLET_PORTRAIT
                WindowWidthSizeClass.EXPANDED if heightClass == WindowHeightSizeClass.MEDIUM -> TABLET_LANDSCAPE
                else -> DESKTOP
            }
        }
    }
}