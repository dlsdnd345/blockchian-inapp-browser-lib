package co.iw.blockchianinappbrowserlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import co.iw.blockchianinappbrowserlib.databinding.ActivityMainBinding
import co.iw.inappbrowserlib.BlockchainInappBrowserFragment
import co.iw.inappbrowserlib.BridgeFavorlet

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initLayout()
    }


    private fun initLayout() {

        binding.btnSearch.setOnClickListener {
            initFragmentWeb(binding.editUrl.text.toString())
        }
    }


    private fun initFragmentWeb(url: String) {

        val webFragment = BlockchainInappBrowserFragment.newInstance(url = url)
        webFragment.setWebViewBridgeListener(webViewBridgeListener)
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.layoutWeb, webFragment)
        ft.commit()
    }


    private val webViewBridgeListener = object : BridgeFavorlet.IWebViewBridgeListener {
        override fun webRequest(id: String, action: String, params: String) {
            Log.i("TAG",">>> webRequest id : $id , action: $action, params : $params")
        }
    }
}