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