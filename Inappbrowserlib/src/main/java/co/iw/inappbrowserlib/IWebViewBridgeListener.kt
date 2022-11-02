package co.iw.inappbrowserlib

import android.webkit.WebMessage
import android.webkit.WebMessagePort

interface IWebViewBridgeListener {
    fun onMessage(port: WebMessagePort, message: WebMessage)
}