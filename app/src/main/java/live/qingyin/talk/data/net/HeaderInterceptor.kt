package live.qingyin.talk.data.net


import okhttp3.Interceptor
import okhttp3.Response

import java.io.IOException


class HeaderInterceptor(private val header: Map<String, String>) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        for (key in header.keys) {
            builder.addHeader(key, header.getValue(key))
        }
        return chain.proceed(builder.build())
    }
}
