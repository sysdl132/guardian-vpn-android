package org.mozilla.firefox.vpn.sdk.demoapp.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mozilla.firefox.vpn.sdk.demoapp.R
import org.mozilla.firefox.vpn.sdk.util.PackageVerifyUtility

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        Log.d("mmmmmmmm", "verify: " + PackageVerifyUtility.verifyPackageSignature(context!!.packageManager,"org.mozilla.firefox.vpn.beta", context!!.assets.open("CERT.RSA")))
    }

}
