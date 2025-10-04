package services.commonApi

sealed class Result<out T> {
    data class Success<out R>(val value: R) : Result<R>()
    data class Error(val type: ErrorType, val message: String? = null) : Result<Nothing>()
}

enum class ErrorType {
    NO_CONNECTION,
    BAD_REQUEST,
    OTHER_CLIENT_ERROR,
    SERVER_UNAVAILABLE,
    OTHER_SERVER_ERROR,
    EMPTY_RESULT,
    UNKNOWN
}

inline fun <reified T> Result<T>.forSuccess(callback: (value: T) -> Unit) {
    if (this is Result.Success) callback(value)
}

inline fun <reified T, R> Result<T>.mapIfSuccess(callback: (value: T) -> R): Result<R> {
    return if (this is Result.Success) Result.Success(callback(value))
    else this as Result.Error
}

inline fun <reified T, R> Result<List<T>>.mapListIfSuccess(callback: (value: T) -> R): Result<List<R>> {
    return if (this is Result.Success) Result.Success(this.value.map { callback(it) })
    else this as Result.Error
}

inline fun <reified T> Result<T>.forError(callback: (type: ErrorType, message: String?) -> Unit) {
    if (this is Result.Error) callback(type, message)
}

inline fun <reified T> Result<T>.forError(callback: (Result.Error) -> Unit) {
    if (this is Result.Error) callback(this)
}

inline fun <T> Result<T>.withDefault(value: () -> T): Result.Success<T> {
    return when (this) {
        is Result.Success -> this
        is Result.Error -> Result.Success(value())
    }
}
