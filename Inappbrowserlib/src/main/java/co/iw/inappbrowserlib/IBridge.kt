package co.iw.inappbrowserlib

import android.webkit.JavascriptInterface


/**
 * 웹 통신 인터페이스
 */
interface IBridge {


    @JavascriptInterface
    fun getNetwork(): Int

    @JavascriptInterface
    fun enable(): String

    @JavascriptInterface
    fun sendAsync(payload: String): String
}