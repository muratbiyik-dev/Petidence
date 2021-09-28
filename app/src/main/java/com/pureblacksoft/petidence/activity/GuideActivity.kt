package com.pureblacksoft.petidence.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.adapter.PetAdapter
import com.pureblacksoft.petidence.databinding.ActivityGuideBinding
import com.pureblacksoft.petidence.fragment.GuideFragment
import com.pureblacksoft.petidence.fragment.HomeFragment
import com.pureblacksoft.petidence.veriable.ConstVeriable
import kotlinx.android.synthetic.main.activity_guide.view.*

class GuideActivity : AppCompatActivity()
{
    private lateinit var rootGA: View
    var fragmentTransaction = supportFragmentManager.beginTransaction()
    var currentFragment: Fragment = GuideFragment()
    var currentFragmentId = ConstVeriable.GUIDE_FRAGMENT_ID
    var guidePetTypeId = 0
    lateinit var guidePetName: String
    lateinit var guidePetStr1: String
    lateinit var guidePetStr2: String
    lateinit var guidePetStr3: String
    lateinit var guidePetStr4: String
    lateinit var guidePetStr5: String

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        val binding = ActivityGuideBinding.inflate(layoutInflater)
        rootGA = binding.root
        setContentView(rootGA)

        //region Get Info
        if (MainActivity.currentFragmentId == ConstVeriable.PET_FRAGMENT_ID)
        {
            val accessedPet = PetAdapter.accessedPet
            val notSpecifiedStr = getString(R.string.Pet_Not_Specified)

            guidePetTypeId = accessedPet.typeId

            guidePetName = if (accessedPet.name == "") notSpecifiedStr
            else accessedPet.name

            guidePetStr1 = guidePetName

            guidePetStr2 = if (accessedPet.age == -1) notSpecifiedStr
            else getString(R.string.Pet_Age, accessedPet.age)

            guidePetStr3 = if (accessedPet.height == -1) notSpecifiedStr
            else getString(R.string.Pet_Height, accessedPet.height)

            guidePetStr4 = if (accessedPet.weight == -1) notSpecifiedStr
            else getString(R.string.Pet_Weight, accessedPet.weight)

            guidePetStr5 = if (accessedPet.breed == "") notSpecifiedStr
            else accessedPet.breed
        }
        else
        {
            val accessedPetType = HomeFragment.accessedPetType

            guidePetTypeId = accessedPetType.id
            guidePetName = accessedPetType.sciName
            guidePetStr1 = accessedPetType.breed1
            guidePetStr2 = accessedPetType.breed2
            guidePetStr3 = accessedPetType.breed3
            guidePetStr4 = accessedPetType.breed4
            guidePetStr5 = accessedPetType.breed5
        }
        //endregion

        //region BottomToolbar
        rootGA.tvPetNameGA.text = guidePetName
        rootGA.ivBackGA.setOnClickListener {
            onBackPressed()
        }
        //endregion

        //region Load Fragment
        fragmentTransaction.replace(R.id.containerGA, currentFragment).commit();
        //endregion
    }

    override fun onBackPressed()
    {
        if (currentFragmentId != ConstVeriable.GUIDE_FRAGMENT_ID)
        {
            currentFragmentId = ConstVeriable.GUIDE_FRAGMENT_ID
            currentFragment = GuideFragment()
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.containerGA, currentFragment).commit()
        }
        else
        {
            finish()
        }
    }
}

//PureBlack Software / Murat BIYIK