package commonUi

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.statekeeper.ExperimentalStateKeeperApi
import com.arkivanov.essenty.statekeeper.StateKeeperOwner
import com.arkivanov.essenty.statekeeper.saveable
import kotlinx.serialization.KSerializer
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

@OptIn(ExperimentalStateKeeperApi::class)
inline fun <T : Any> StateKeeperOwner.saveableMutableValue(
    serializer: KSerializer<T>,
    key: String? = null,
    crossinline init: () -> T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, MutableValue<T>>> =
    saveable(
        serializer = serializer,
        state = { it.value },
        key = key,
        init = { MutableValue(it ?: init()) },
    )