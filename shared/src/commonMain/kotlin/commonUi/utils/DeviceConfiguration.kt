package commonUi.utils

import androidx.window.core.layout.WindowSizeClass

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    DESKTOP;

    companion object {
        fun fromWindowSize(windowsSize: WindowSizeClass): DeviceConfiguration {
            return when  {
                windowsSize.isWidthAtLeastBreakpoint(1199) -> DESKTOP
                windowsSize.isWidthAtLeastBreakpoint(840) -> MOBILE_LANDSCAPE
                windowsSize.isWidthAtLeastBreakpoint(600) -> TABLET_PORTRAIT
                else -> MOBILE_PORTRAIT
            }
        }
    }
}