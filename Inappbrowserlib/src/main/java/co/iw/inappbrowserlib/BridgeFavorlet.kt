package co.iw.inappbrowserlib

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView

class BridgeFavorlet(val webView: WebView) : IBridge {

    @JavascriptInterface
    override fun getNetwork(): Int {
        return 1001
    }

    @JavascriptInterface
    override fun enable(): String {
        //지갑 전달
        return "0x15A3694D7A48c4873C60680D4A7a9C79512689Dc"
    }

    @JavascriptInterface
    override fun sendAsync(payload: String): String {
        Log.i("TAG","!!!!!!!!!!!!!!!!! payload $payload")
        return "0x7aa154d5c667dd46c59e4c76184669b79f5d39f5b5c0bddeeb3e68de0f0687a319a7911b94452649c1deaeda7b919a7eff4bcf1530d1f9930fe5f8754a88c3211b"
    }
}