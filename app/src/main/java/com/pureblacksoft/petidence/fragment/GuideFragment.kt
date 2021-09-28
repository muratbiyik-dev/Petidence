package com.pureblacksoft.petidence.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.activity.GuideActivity
import com.pureblacksoft.petidence.databinding.FragmentGuideBinding
import com.pureblacksoft.petidence.function.AppFun
import com.pureblacksoft.petidence.veriable.ConstVeriable
import kotlinx.android.synthetic.main.appbar_guide.view.*
import kotlinx.android.synthetic.main.fragment_guide.view.*
import kotlinx.android.synthetic.main.linear_pet.view.*

class GuideFragment: Fragment(R.layout.fragment_guide)
{
    private lateinit var mContext: Context
    private lateinit var mActivity: GuideActivity
    private lateinit var rootGF: View

    override fun onAttach(context: Context)
    {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as GuideActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_guide, container, false)
        val binding = FragmentGuideBinding.bind(view)
        rootGF = binding.root

        //region Toolbar
        rootGF.tvGuideGAB.text = getString(R.string.Guide_Title)
        rootGF.ivGuideGAB.setImageResource(R.drawable.ic_guide_bwhite_32)
        //endregion

        //region Get Info
        rootGF.tvPetNamePL.text = mActivity.guidePetStr1
        rootGF.tvPetAgePL.text = mActivity.guidePetStr2
        rootGF.tvPetHeightPL.text = mActivity.guidePetStr3
        rootGF.tvPetWeightPL.text = mActivity.guidePetStr4
        rootGF.tvPetBreedPL.text = mActivity.guidePetStr5
        AppFun.setPetType(mContext, mActivity.guidePetTypeId, rootGF.tvPetLabelPL, rootGF.ivPetIconPL)
        //endregion

        //region Button Connections
        rootGF.llFoodGF.setOnClickListener {
            mActivity.currentFragmentId = ConstVeriable.FOOD_FRAGMENT_ID
            mActivity.currentFragment = FoodFragment()
            mActivity.fragmentTransaction = mActivity.supportFragmentManager.beginTransaction()
            mActivity.fragmentTransaction.replace(R.id.containerGA, mActivity.currentFragment).commit()
        }

        rootGF.llSleepGF.setOnClickListener {
            mActivity.currentFragmentId = ConstVeriable.SLEEP_FRAGMENT_ID
            mActivity.currentFragment = SleepFragment()
            mActivity.fragmentTransaction = mActivity.supportFragmentManager.beginTransaction()
            mActivity.fragmentTransaction.replace(R.id.containerGA, mActivity.currentFragment).commit()
        }

        rootGF.llPlayGF.setOnClickListener {
            mActivity.currentFragmentId = ConstVeriable.PLAY_FRAGMENT_ID
            mActivity.currentFragment = PlayFragment()
            mActivity.fragmentTransaction = mActivity.supportFragmentManager.beginTransaction()
            mActivity.fragmentTransaction.replace(R.id.containerGA, mActivity.currentFragment).commit()
        }
        //endregion

        return view
    }
}

//PureBlack Software / Murat BIYIK