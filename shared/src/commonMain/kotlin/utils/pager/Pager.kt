package utils.pager

import services.commonApi.Result

/**
 * A generic class to handle paginated data loading.
 *
 * @param Key The type of the key used for pagination (e.g., Int for page number, String for cursor).
 * @param Item The type of the data item being loaded.
 * @property initialKey The key to start loading from.
 * @property onLoadUpdated A callback invoked when the loading state changes.
 * @property onRequest The suspend function that performs the actual data request.
 * @property getNextKey A function to determine the key for the next page.
 * @property onError A callback for handling request errors.
 * @property onSuccess A callback for handling successful requests.
 * @property endReached A predicate to check if the end of the data has been reached.
 */
class Pager<Key, Item>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> Result<Item>,
    private val getNextKey: suspend (currentKey: Key, result: Item) -> Key,
    private val onError: suspend (Result.Error) -> Unit,
    private val onSuccess: suspend (item: Item, newKey: Key) -> Unit,
    private val endReached: (newKey: Key, result: Item) -> Boolean
) {
    private var currentKey = initialKey
    private var isMakingRequest = false
    private var isEndReached = false

    private suspend fun load(): Item? {
        if (isMakingRequest) return null

        isMakingRequest = true
        onLoadUpdated(true)

        try {
            return when (val result = onRequest(currentKey)) {
                is Result.Success -> result.value
                is Result.Error -> {
                    onError(result)
                    null
                }
            }
        } finally {
            isMakingRequest = false
            onLoadUpdated(false)
        }
    }

    suspend fun loadNextItem() {
        if (isEndReached) {
            return
        }
        val item = load() ?: return
        val newKey = getNextKey(currentKey, item)
        onSuccess(item, newKey)
        currentKey = newKey
        isEndReached = endReached(newKey, item)
    }

    fun reset() {
        currentKey = initialKey
        isEndReached = false
    }
}