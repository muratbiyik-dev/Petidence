package com.pureblacksoft.petidence.modified

import android.content.Context
import android.util.AttributeSet
import android.widget.ExpandableListView

class NonScrollExpandableListView: ExpandableListView
{
    constructor (context: Context) : super(context)

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor (context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        val customHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, customHeightMeasureSpec)

        val params = layoutParams
        params.height = measuredHeight
    }
}

//PureBlack Software / Murat BIYIK