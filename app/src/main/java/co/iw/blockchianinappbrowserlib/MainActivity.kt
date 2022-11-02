package co.iw.blockchianinappbrowserlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebMessage
import android.webkit.WebMessagePort
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import co.iw.blockchianinappbrowserlib.databinding.ActivityMainBinding
import co.iw.inappbrowserlib.BlockchainInappBrowserFragment
import co.iw.inappbrowserlib.BridgeFavorlet
import co.iw.inappbrowserlib.IWebViewBridgeListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var webFragment: BlockchainInappBrowserFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initLayout()
    }


    private fun initLayout() {

        binding.btnSearch.setOnClickListener {
            initFragmentWeb(binding.editUrl.text.toString())
        }

        binding.btnPostMessage.setOnClickListener {
            webFragment.postMessage("Pong!")
        }
    }


    private fun initFragmentWeb(url: String) {

        webFragment = BlockchainInappBrowserFragment.newInstance(url = url)
        webFragment.setWebViewBridgeListener(webViewBridgeListener)
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.layoutWeb, webFragment)
        ft.commit()
    }


    private val webViewBridgeListener = object : IWebViewBridgeListener {
        override fun onMessage(port: WebMessagePort, message: WebMessage) {
            Log.i("TAG","[MainActivity] >>> message : ${message.data}")
            Toast.makeText(applicationContext, message.data, Toast.LENGTH_SHORT).show()
        }
    }
}