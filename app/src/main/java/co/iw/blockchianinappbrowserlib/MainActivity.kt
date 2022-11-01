package co.iw.blockchianinappbrowserlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import co.iw.blockchianinappbrowserlib.databinding.ActivityMainBinding
import co.iw.inappbrowserlib.BlockchainInappBrowserFragment
import co.iw.inappbrowserlib.BridgeFavorlet

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
    }


    private fun initFragmentWeb(url: String) {

        webFragment = BlockchainInappBrowserFragment.newInstance(url = url)
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.layoutWeb, webFragment)
        ft.commit()
    }
}