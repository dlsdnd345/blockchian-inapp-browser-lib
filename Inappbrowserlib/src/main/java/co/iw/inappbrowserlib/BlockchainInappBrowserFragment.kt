package co.iw.inappbrowserlib

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.webkit.WebView.setWebContentsDebuggingEnabled
import androidx.fragment.app.Fragment
import co.iw.inappbrowserlib.databinding.FragmentBlockchainInappBrowserBinding
import java.io.InputStream


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

    private lateinit var activity: Activity
    private lateinit var binding: FragmentBlockchainInappBrowserBinding

    private var webViewBridgeListener: BridgeFavorlet.IWebViewBridgeListener? = null


    /**
     * WebViewBridgeManager 리스너 세팅
     */
    fun setWebViewBridgeListener(listener: BridgeFavorlet.IWebViewBridgeListener) {
        webViewBridgeListener = listener
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBlockchainInappBrowserBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.activity = getActivity() as Activity

        init()
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
            webViewClient = BaseWebViewClient()
            setWebContentsDebuggingEnabled(true)

            val bridgeFavorlet = BridgeFavorlet(this)
            webViewBridgeListener?.let {
                bridgeFavorlet.setWebViewBridgeListener(it)
            }

            addJavascriptInterface(bridgeFavorlet, "favorlet")

            loadUrl(url)
        }
    }




}


private class BaseWebViewClient : WebViewClient() {

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


class BridgeFavorlet(val webView: WebView) : IBridge {

    private var webViewBridgeListener: IWebViewBridgeListener? = null

    /**
     * WebViewBridgeManager -> WebFragment 로 이벤트 전달
     */
    interface IWebViewBridgeListener {
        fun webRequest(id: String, action: String, params: String)
    }

    /**
     * WebViewBridgeManager 리스너 세팅
     */
    fun setWebViewBridgeListener(listener: IWebViewBridgeListener) {
        webViewBridgeListener = listener
    }

    @JavascriptInterface
    override fun getNetwork(): Number {
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

    override fun webRequest(id: String, action: String, params: String) {
        webViewBridgeListener?.webRequest(id, action, params)
    }

    override fun webRequestCallBack(id: String, action: String, callbackParams: String) {
        Handler(Looper.getMainLooper()).post {
            Log.d("TAG",">>> nativeRequest, $id $action $callbackParams")
            webView.loadUrl("javascript:webRequestCallBack('$id', '$action','$callbackParams')")
        }
    }

    override fun nativeRequest(id: String, action: String, params: String) {
        Handler(Looper.getMainLooper()).post {
            Log.d("TAG",">>> nativeRequest, $id $action $params")
            webView.loadUrl("javascript:nativeRequest('$id', '$action','$params')")
        }
    }

    override fun nativeRequestCallBack(id: String, action: String, callbackParams: String) {
        Log.d("TAG",">>> nativeRequestCallBack, $id $action $callbackParams")
    }
}