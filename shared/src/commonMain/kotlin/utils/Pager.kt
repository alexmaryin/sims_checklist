package utils

import services.commonApi.ErrorType
import services.commonApi.Result

class Pager<Key, Item>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> Result<Item>,
    private val getNextKey: suspend (currentKey: Key, result: Item) -> Key,
    private val getPreviousKey: suspend (currentKey: Key, result: Item) -> Key,
    private val onError: suspend (ErrorType, String?) -> Unit,
    private val onSuccess: suspend (item: Item, newKey: Key) -> Unit,
    private val endReached: (currentKey: Key, result: Item) -> Boolean
) {
    private var currentKey = initialKey
    private var isMakingRequest = false
    private var isEndReached = false

    private suspend fun load(): Item? {
        if (isMakingRequest || isEndReached) return null
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        when (result) {
            is Result.Success -> return result.value
            is Result.Error -> onError(result.type, result.message)
        }
        onLoadUpdated(false)
        return null
    }

    suspend fun loadNextItem() {
        val item = load() ?: return
        currentKey = getNextKey(currentKey, item)
        onSuccess(item, currentKey)
        onLoadUpdated(false)
        isEndReached = endReached(currentKey, item)
    }

    suspend fun loadPreviousItem() {
        val item = load() ?: return
        currentKey = getPreviousKey(currentKey, item)
        onSuccess(item, currentKey)
        onLoadUpdated(false)
    }

    fun reset() {
        currentKey = initialKey
        isEndReached = false
    }
}