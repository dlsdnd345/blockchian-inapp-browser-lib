package co.iw.inappbrowserlib

import android.webkit.JavascriptInterface
import org.json.JSONArray


/**
 * 웹 통신 인터페이스
 */
interface IBridgeKlaytn {

    @JavascriptInterface
    fun getNetwork(): Number

    @JavascriptInterface
    fun enable(): String

    @JavascriptInterface
    fun sendAsync(payload: String): String
}