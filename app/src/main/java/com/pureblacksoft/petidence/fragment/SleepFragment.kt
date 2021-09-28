package com.pureblacksoft.petidence.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.activity.GuideActivity
import com.pureblacksoft.petidence.activity.MainActivity
import com.pureblacksoft.petidence.adapter.SleepAdapter
import com.pureblacksoft.petidence.databinding.FragmentSleepBinding
import com.pureblacksoft.petidence.function.AppFun
import com.pureblacksoft.petidence.veriable.ConstVeriable
import kotlinx.android.synthetic.main.appbar_guide.view.*
import kotlinx.android.synthetic.main.fragment_sleep.view.*

class SleepFragment: Fragment(R.layout.fragment_sleep)
{
    private lateinit var mContext: Context
    private lateinit var mActivity: GuideActivity
    private lateinit var rootSF: View
    private lateinit var sleepAdapter: SleepAdapter

    override fun onAttach(context: Context)
    {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as GuideActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_sleep, container, false)
        val binding = FragmentSleepBinding.bind(view)
        rootSF = binding.root

        //region Toolbar
        rootSF.tvGuideGAB.text = getString(R.string.Sleep_Title)
        rootSF.ivGuideGAB.setImageResource(R.drawable.ic_sleep_bwhite_32)
        //endregion

        //region Header
        val ivHeaderSF = rootSF.ivHeaderSF

        when(mActivity.guidePetTypeId)
        {
            ConstVeriable.CAT_PET_TYPE_ID -> ivHeaderSF.setImageResource(R.drawable.img_header_sleep_cat)
            ConstVeriable.DOG_PET_TYPE_ID -> ivHeaderSF.setImageResource(R.drawable.img_header_sleep_dog)
            ConstVeriable.BIRD_PET_TYPE_ID -> ivHeaderSF.setImageResource(R.drawable.img_header_sleep_bird)
            ConstVeriable.FISH_PET_TYPE_ID -> ivHeaderSF.setImageResource(R.drawable.img_header_sleep_fish)
        }
        //endregion

        //region RecyclerView
        val recyclerViewSF = rootSF.recyclerViewSF
        val linearManager = LinearLayoutManager(mContext)
        linearManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewSF.layoutManager = linearManager

        val divider = DividerItemDecoration(mContext, linearManager.orientation)
        val dividerDrawable = ContextCompat.getDrawable(mContext, R.drawable.shape_divider_app)
        if (dividerDrawable != null)
        {
            divider.setDrawable(dividerDrawable)
            recyclerViewSF.addItemDecoration(divider)
        }
        //endregion

        //region sleepAdapter
        fun setSleepAdapter()
        {
            sleepAdapter = SleepAdapter(MainActivity.petidenceDB.readPetSleepList(mActivity.guidePetTypeId))
            recyclerViewSF.adapter = sleepAdapter

            //region Empty Adapter Control
            if (sleepAdapter.itemCount == 0)
            {
                AppFun.setVisibility(rootSF.tvNoDataSF, true)
            }
            else
            {
                AppFun.setVisibility(rootSF.tvNoDataSF, false)
            }
            //endregion
        }
        //endregion

        //region Get Sleeps
        setSleepAdapter()
        //endregion

        return view
    }
}

//PureBlack Software / Murat BIYIK