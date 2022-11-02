package co.iw.inappbrowserlib

import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.webkit.WebView.setWebContentsDebuggingEnabled
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import co.iw.inappbrowserlib.databinding.FragmentBlockchainInappBrowserBinding
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.M)
class BlockchainInappBrowserFragment : Fragment() {

    companion object {
        //초기화 함수
        fun newInstance(url: String): BlockchainInappBrowserFragment {
            val fragment = BlockchainInappBrowserFragment()
            val bundle = Bundle()
            bundle.putString("URL", url)
            fragment.arguments = bundle
            return fragment
        }
    }

    interface IWebViewBridgeListener {
        fun onMessage(port: WebMessagePort, message: WebMessage)
    }

    private lateinit var binding: FragmentBlockchainInappBrowserBinding

    private lateinit var webMessagePort: WebMessagePort
    private var webViewBridgeListener: IWebViewBridgeListener? = null

    /**
     * WebViewBridgeManager 리스너 세팅
     */
    fun setWebViewBridgeListener(listener: IWebViewBridgeListener) {
        webViewBridgeListener = listener
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBlockchainInappBrowserBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    override fun onDestroy() {
        super.onDestroy()
        webMessagePort.close()
    }


    /*
     * 데이터 초기화
     */
    private fun init() {

        val url = arguments?.getString("URL")!!
        //웹뷰 셋팅
        initWebViewSetting(url)
    }


    /*
     * 웹뷰 셋팅 초기화
     */
    private fun initWebViewSetting(url: String) {

        with(binding.webView) {

            setBackgroundColor(Color.TRANSPARENT)
            settings.setSupportZoom(false)
            isHorizontalScrollBarEnabled = false
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.loadsImagesAutomatically = true
            webViewClient = baseWebViewClient
            setWebContentsDebuggingEnabled(true)
            //addJavascriptInterface(BridgeFavorlet(this), "favorlet")
            //loadUrl(url)
            loadUrl("file:///android_asset/index.html")
        }
    }


    /*
     * 웹 메세지 포트 초기화
     */
    fun initWebMessagePort() {
        val channel = binding.webView.createWebMessageChannel()
        webMessagePort = channel[0]
        webMessagePort.setWebMessageCallback(webMessageCallback)

        binding.webView.postWebMessage(WebMessage("favorlet", arrayOf(channel[1])), Uri.EMPTY)
    }


    /*
     * Provider 자바스크립트 파일 주입
     */
    private fun injectJS(webView: WebView) {
        try {
            val inputStream: InputStream = webView.context.assets.open("klaytn-provider.js")
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


    /*
     * [Android] -> [Web] 메세지 전달
     */
    public fun postMessage(string: String) {
        webMessagePort.postMessage(WebMessage(string))
    }


    /*
     * WebViewClient
     */
    private val baseWebViewClient = object : WebViewClient() {

        override fun onLoadResource(view: WebView, url: String) {
            super.onLoadResource(view, url)
            injectJS(view)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            initWebMessagePort()
        }
    }


    /**
     * [Web] -> [Android] 메세지 전달
     */
    private val webMessageCallback = object : WebMessagePort.WebMessageCallback() {
        override fun onMessage(port: WebMessagePort, message: WebMessage) {
            Log.i("TAG",">>> onMessage : ${message.data}")
            webViewBridgeListener?.onMessage(port, message)
        }
    }
}