package com.pureblacksoft.petidence.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.data.Food
import com.pureblacksoft.petidence.data.FoodType
import com.pureblacksoft.petidence.databinding.LinearFoodBinding
import com.pureblacksoft.petidence.databinding.LinearFoodTypeBinding
import kotlinx.android.synthetic.main.linear_food.view.*
import kotlinx.android.synthetic.main.linear_food_type.view.*

class FoodExpListAdapter(private val context: Context,
                         private val foodTypeList: MutableList<FoodType>,
                         private val foodHashMap: HashMap<FoodType, MutableList<Food>>): BaseExpandableListAdapter()
{
    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getGroup(groupPosition: Int): Any = foodTypeList[groupPosition]

    override fun getGroupCount(): Int = foodTypeList.size

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, view: View?, parent: ViewGroup?): View
    {
        val inflater = LayoutInflater.from(context)
        val newView = inflater.inflate(R.layout.linear_food_type, parent, false)
        val rootFTL = LinearFoodTypeBinding.bind(newView).root
        val foodType = getGroup(groupPosition) as FoodType
        rootFTL.tvFoodTypeTitleFTL.text = foodType.title
        rootFTL.ivFoodTypeIconFTL.setImageResource(foodType.icon)

        return newView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getChild(groupPosition: Int, childPosition: Int): Any = foodHashMap[foodTypeList[groupPosition]]!![childPosition]

    override fun getChildrenCount(groupPosition: Int): Int = foodHashMap[foodTypeList[groupPosition]]!!.size

    override fun getChildView(groupPosition: Int, childPosition: Int, isLostChild: Boolean, view: View?, parent: ViewGroup?): View
    {
        val inflater = LayoutInflater.from(context)
        val newView = inflater.inflate(R.layout.linear_food, parent, false)
        val rootFL = LinearFoodBinding.bind(newView).root
        val food = getChild(groupPosition, childPosition) as Food
        val foodImage = BitmapFactory.decodeByteArray(food.image, 0, food.image.size)
        rootFL.tvFoodNameFL.text = food.name
        rootFL.tvFoodCaloriesFL.text = context.getString(R.string.Food_Calories, food.calories.toString())
        rootFL.tvFoodFatFL.text = context.getString(R.string.Food_Fat, food.fat.toString())
        rootFL.tvFoodCarbohydrateFL.text = context.getString(R.string.Food_Carbohydrate, food.carbohydrate.toString())
        rootFL.tvFoodProteinFL.text = context.getString(R.string.Food_Protein, food.protein.toString())
        rootFL.ivFoodImageFL.setImageBitmap(Bitmap.createScaledBitmap(foodImage, foodImage.width, foodImage.height, false))

        return newView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    override fun hasStableIds(): Boolean = false
}

//PureBlack Software / Murat BIYIK