package decompose

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class Configuration : Parcelable
    @Parcelize
    object ItemsList : Configuration()

    @Parcelize
    data class Details(val id: Long) : Configuration()
