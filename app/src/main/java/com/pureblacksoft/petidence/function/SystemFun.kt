package com.pureblacksoft.petidence.function

import android.app.Activity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.pureblacksoft.petidence.BuildConfig
import com.pureblacksoft.petidence.activity.MainActivity

class SystemFun
{
    companion object
    {
        fun logI(tag: String, msg: String)
        {
            if (BuildConfig.DEBUG)
            {
                Log.i(tag, msg)
            }
        }

        fun hideSoftKeyboard(activity: MainActivity)
        {
            val inputManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        }
    }
}

//PureBlack Software / Murat BIYIK