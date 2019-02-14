package com.github.zchu.model

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun viewDataCheck() {
        val arrayListOf = arrayListOf(
            ViewData.loading("asasd"),
            ViewData.loading("asasd"),
            ViewData.loaded("asasd"),
            ViewData.loading("asasd"),
            ViewData.loaded("asasd"),
            ViewData.loading("asasd"),
            ViewData.error(null, "asasd"),
            ViewData.loading("asasd")

        )
        for (viewData in arrayListOf) {
            check(viewData)
        }


    }


    fun check(viewData: ViewData<*>) {
        viewData.whenRun {
            onLoading {
                assertEquals(viewData.workState.status, Status.RUNNING)
            }

            onError { throwable, any ->
                assertEquals(viewData.workState.status, Status.FAILED)

            }

            onSuccess {
                assertEquals(viewData.workState.status, Status.SUCCEEDED)

            }
        }

    }
}
