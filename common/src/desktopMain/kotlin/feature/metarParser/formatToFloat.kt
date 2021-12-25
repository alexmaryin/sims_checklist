package feature.metarParser

actual fun Double.formatToFloat(decimals: Int) = "%.${decimals}f".format(this).toFloat()