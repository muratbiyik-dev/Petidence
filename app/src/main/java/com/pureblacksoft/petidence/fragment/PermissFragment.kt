package com.pureblacksoft.petidence.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.activity.MainActivity
import com.pureblacksoft.petidence.databinding.FragmentPermissBinding
import com.pureblacksoft.petidence.dialog.ApprovalDialog
import com.pureblacksoft.petidence.veriable.ConstVeriable
import kotlinx.android.synthetic.main.fragment_permiss.view.*

class PermissFragment: Fragment(R.layout.fragment_permiss)
{
    private lateinit var mContext: Context
    private lateinit var mActivity: MainActivity
    private lateinit var rootPF2: View

    override fun onAttach(context: Context)
    {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_permiss, container, false)
        val binding = FragmentPermissBinding.bind(view)
        rootPF2 = binding.root

        //region Permission Control
        val reqPermissList = mutableListOf<String>()
        reqPermissList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        reqPermissList.add(Manifest.permission.ACCESS_FINE_LOCATION)

        val grantPermissList = mutableListOf<String>()

        for (reqPermiss in reqPermissList)
        {
            if (ContextCompat.checkSelfPermission(mContext, reqPermiss) == PackageManager.PERMISSION_GRANTED)
            {
                grantPermissList.add(reqPermiss)
            }
        }

        if (reqPermissList == grantPermissList)
        {
            startMapFragment()
        }
        //endregion

        //region Button Connections
        rootPF2.tvPermissGrantPF2.setOnClickListener {
            for (reqPermiss in reqPermissList)
            {
                Dexter.withContext(mContext).withPermission(reqPermiss).withListener(object: PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?)
                    {
                        startMapFragment()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse)
                    {
                        if (response.isPermanentlyDenied)
                        {
                            val builder = ApprovalDialog.alertBuilder(mContext)
                            builder.setTitle(R.string.Permiss_Dialog_Title)
                            builder.setMessage(R.string.Permiss_Dialog_Message)
                            builder.setPositiveButton(R.string.Dialog_Ok) { _, _ ->
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                intent.data = Uri.fromParts("package", mContext.packageName, null)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                            builder.setNegativeButton(R.string.Dialog_Cancel) { _, _ -> }
                            builder.show()
                        }
                        else
                        {
                            //region Permiss Toast
                            val permissToast = Toast.makeText(mContext, R.string.Permiss_Toast, Toast.LENGTH_SHORT)
                            permissToast.setGravity(Gravity.BOTTOM, 0, 160)
                            permissToast.show()
                            //endregion
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(req: PermissionRequest?, token: PermissionToken?)
                    {
                        token?.continuePermissionRequest()
                    }
                }).check()
            }
        }
        //endregion

        return view
    }

    private fun startMapFragment()
    {
        MainActivity.currentFragmentId = ConstVeriable.MAP_FRAGMENT_ID
        mActivity.currentFragment = MapFragment()
        mActivity.fragmentTransaction = mActivity.supportFragmentManager.beginTransaction()
        mActivity.fragmentTransaction.replace(R.id.containerMA, mActivity.currentFragment).commit()
    }
}

//PureBlack Software / Murat BIYIK