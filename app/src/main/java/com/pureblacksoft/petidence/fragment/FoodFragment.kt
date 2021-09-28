package com.pureblacksoft.petidence.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.activity.GuideActivity
import com.pureblacksoft.petidence.activity.MainActivity
import com.pureblacksoft.petidence.adapter.FoodExpListAdapter
import com.pureblacksoft.petidence.data.Food
import com.pureblacksoft.petidence.data.FoodType
import com.pureblacksoft.petidence.databinding.FragmentFoodBinding
import com.pureblacksoft.petidence.function.AppFun
import com.pureblacksoft.petidence.veriable.ConstVeriable
import kotlinx.android.synthetic.main.appbar_guide.view.*
import kotlinx.android.synthetic.main.fragment_food.view.*

class FoodFragment: Fragment(R.layout.fragment_food)
{
    private lateinit var mContext: Context
    private lateinit var mActivity: GuideActivity
    private lateinit var rootFF: View
    private lateinit var foodTypeList: MutableList<FoodType>
    private lateinit var foodHashMap: HashMap<FoodType, MutableList<Food>>
    private lateinit var foodExpListAdapter: FoodExpListAdapter

    override fun onAttach(context: Context)
    {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as GuideActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_food, container, false)
        val binding = FragmentFoodBinding.bind(view)
        rootFF = binding.root

        //region Toolbar
        rootFF.tvGuideGAB.text = getString(R.string.Food_Title)
        rootFF.ivGuideGAB.setImageResource(R.drawable.ic_food_bwhite_32)
        //endregion

        //region Header
        val ivHeaderFF = rootFF.ivHeaderFF

        when(mActivity.guidePetTypeId)
        {
            ConstVeriable.CAT_PET_TYPE_ID -> ivHeaderFF.setImageResource(R.drawable.img_header_food_cat)
            ConstVeriable.DOG_PET_TYPE_ID -> ivHeaderFF.setImageResource(R.drawable.img_header_food_dog)
            ConstVeriable.BIRD_PET_TYPE_ID -> ivHeaderFF.setImageResource(R.drawable.img_header_food_bird)
            ConstVeriable.FISH_PET_TYPE_ID -> ivHeaderFF.setImageResource(R.drawable.img_header_food_fish)
        }
        //endregion

        //region ExpandableListView
        val expListViewFF = rootFF.expListViewFF

        expListViewFF.setOnGroupExpandListener { groupPosition ->
            AppFun.setVisibility(ivHeaderFF, false)
        }

        expListViewFF.setOnGroupCollapseListener { groupPosition ->
            if (!expListViewFF.isGroupExpanded(0) && !expListViewFF.isGroupExpanded(1))
            {
                AppFun.setVisibility(ivHeaderFF, true)
            }
        }

        expListViewFF.setOnChildClickListener { parent, childView, groupPosition, childPosition, id ->

            false
        }
        //endregion

        //region FoodExpListAdapter
        createExpListItems()
        foodExpListAdapter = FoodExpListAdapter(mContext, foodTypeList, foodHashMap)
        expListViewFF.setAdapter(foodExpListAdapter)
        expListViewFF.isClickable = true
        //endregion

        return view
    }

    private fun createExpListItems()
    {
        //region Food Type
        val healthyFoodType = FoodType(ConstVeriable.HEALTHY_FOOD_TYPE_ID, getString(R.string.FoodType_Healthy_Title), R.drawable.ic_food_healthy_bwhite_32)
        val unhealthyFoodType = FoodType(ConstVeriable.UNHEALTHY_FOOD_TYPE_ID, getString(R.string.FoodType_Unhealthy_Title), R.drawable.ic_food_unhealthy_bwhite_32)

        foodTypeList = mutableListOf()
        foodTypeList.add(healthyFoodType)
        foodTypeList.add(unhealthyFoodType)
        //endregion

        //region Food
        foodHashMap = hashMapOf()
        when (mActivity.guidePetTypeId)
        {
            ConstVeriable.CAT_PET_TYPE_ID ->
            {
                foodHashMap[foodTypeList[0]] = MainActivity.petidenceDB.readCatFoodList(1)
                foodHashMap[foodTypeList[1]] = MainActivity.petidenceDB.readCatFoodList(2)
            }
            ConstVeriable.DOG_PET_TYPE_ID ->
            {
                foodHashMap[foodTypeList[0]] = MainActivity.petidenceDB.readDogFoodList(1)
                foodHashMap[foodTypeList[1]] = MainActivity.petidenceDB.readDogFoodList(2)
            }
            ConstVeriable.BIRD_PET_TYPE_ID ->
            {
                foodHashMap[foodTypeList[0]] = MainActivity.petidenceDB.readBirdFoodList(1)
                foodHashMap[foodTypeList[1]] = MainActivity.petidenceDB.readBirdFoodList(2)
            }
            ConstVeriable.FISH_PET_TYPE_ID ->
            {
                foodHashMap[foodTypeList[0]] = MainActivity.petidenceDB.readFishFoodList(1)
                foodHashMap[foodTypeList[1]] = MainActivity.petidenceDB.readFishFoodList(2)
            }
        }
        //endregion
    }
}

//PureBlack Software / Murat BIYIK