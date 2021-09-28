package com.pureblacksoft.petidence.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.ContextThemeWrapper
import com.pureblacksoft.petidence.R

class ApprovalDialog
{
    companion object
    {
        fun alertBuilder(context: Context): AlertDialog.Builder
        {
            return AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogTheme))
        }
    }
}

//PureBlack Software / Murat BIYIK