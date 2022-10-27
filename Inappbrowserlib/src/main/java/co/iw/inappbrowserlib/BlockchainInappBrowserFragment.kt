package co.iw.inappbrowserlib

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import co.iw.inappbrowserlib.databinding.FragmentBlockchainInappBrowserBinding

class BlockchainInappBrowserFragment : Fragment() {

    companion object {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBlockchainInappBrowserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.activity = getActivity() as Activity

        initLayout()
        init()
    }


    private fun initLayout() {

    }


    private fun init() {

        val url = arguments?.getString("URL")!!
        initWebViewSetting(url)
    }


    private fun initWebViewSetting(url: String) {

        with(binding.webView) {

            setBackgroundColor(Color.TRANSPARENT)
            settings.setSupportZoom(false)
            isHorizontalScrollBarEnabled = false
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.loadsImagesAutomatically = true
            webChromeClient = BaseWebChromeClient()
            webViewClient = BaseWebViewClient()
            loadUrl(url)
        }
    }
}


private class BaseWebViewClient : WebViewClient() {
    //페이지 이동
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        Log.d(">>>>>> check URL", url)
        view.loadUrl(url)
        return true
    }
}


private class BaseWebChromeClient : WebChromeClient() {


}