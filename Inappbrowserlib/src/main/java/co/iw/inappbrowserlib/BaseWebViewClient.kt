package co.iw.inappbrowserlib

import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.InputStream

class BaseWebViewClient : WebViewClient() {

    private fun injectJS(webView: WebView) {
        try {
            val inputStream: InputStream = webView.context.assets.open("app-to-app-klaytn.js")
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            val encoded: String = Base64.encodeToString(buffer, Base64.NO_WRAP)
            webView.loadUrl(
                "javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var script = document.createElement('script');" +
                        "script.type = 'text/javascript';" +
                        "script.innerHTML = window.atob('" + encoded + "');" +
                        "parent.appendChild(script)" +
                        "})()"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //페이지 이동
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }

    override fun onLoadResource(view: WebView, url: String) {
        super.onLoadResource(view, url)
        injectJS(view)
    }
}