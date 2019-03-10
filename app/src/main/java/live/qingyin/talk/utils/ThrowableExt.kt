package live.qingyin.talk.utils

import android.content.Context
import android.net.ParseException
import com.google.gson.JsonParseException
import live.qingyin.talk.R
import live.qingyin.talk.appContext
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


@JvmOverloads
fun Throwable?.getEasyMessage(context: Context? = appContext): String {
    return getEasyMessage(context = context ?: appContext)
}

fun Throwable?.getEasyMessage(
    context: Context = appContext,
    defaultMessage: String = context.getString(R.string.exception_default)
): String {
    if (this == null) {
        return defaultMessage
    }
    val localMessage = this.message
    return if (this is SocketTimeoutException) {
        context.getString(R.string.exception_socket_time_out)
    } else if (this is ConnectException) {
        context.getString(R.string.exception_connect)
    } else if (this is retrofit2.HttpException) {
        val response = this.response()
        val responseBody = response.errorBody()
        if (responseBody != null) {
            //TODO
        }
        context.getString(R.string.error_server)
    } else if (this is UnknownHostException) {
        context.getString(R.string.exception_unknown_host)
    } else if (this is SecurityException) {
        context.getString(R.string.exception_security)
    } else if (this is JsonParseException
        || this is JSONException
        || this is ParseException
    ) {
        context.getString(R.string.exception_json)
    } else if (this is javax.net.ssl.SSLHandshakeException) {
        context.getString(R.string.exception_ssl)
    } else if (this is java.util.concurrent.TimeoutException) {
        context.getString(R.string.exception_timeout)
    } else {
        if (localMessage == null || localMessage.length >= 40) {
            defaultMessage
        } else {
            localMessage
        }
    }
}