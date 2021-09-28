package com.pureblacksoft.petidence.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.activity.GuideActivity
import com.pureblacksoft.petidence.activity.MainActivity
import com.pureblacksoft.petidence.data.PetType
import com.pureblacksoft.petidence.databinding.FragmentHomeBinding
import com.pureblacksoft.petidence.veriable.ConstVeriable
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(R.layout.fragment_home)
{
    companion object
    {
        lateinit var accessedPetType: PetType
    }

    private lateinit var mContext: Context
    private lateinit var mActivity: MainActivity
    private lateinit var rootHF: View

    override fun onAttach(context: Context)
    {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val binding = FragmentHomeBinding.bind(view)
        rootHF = binding.root

        //region Get Pet Counts
        rootHF.tvCatCountHF.text = MainActivity.petidenceDB.readPetListWithType(ConstVeriable.CAT_PET_TYPE_ID).size.toString()
        rootHF.tvDogCountHF.text = MainActivity.petidenceDB.readPetListWithType(ConstVeriable.DOG_PET_TYPE_ID).size.toString()
        rootHF.tvBirdCountHF.text = MainActivity.petidenceDB.readPetListWithType(ConstVeriable.BIRD_PET_TYPE_ID).size.toString()
        rootHF.tvFishCountHF.text = MainActivity.petidenceDB.readPetListWithType(ConstVeriable.FISH_PET_TYPE_ID).size.toString()
        //endregion

        //region Button Connections
        rootHF.rlCatCardHF.setOnClickListener {
            accessedPetType =
                PetType(id = ConstVeriable.CAT_PET_TYPE_ID,
                    sciName = getString(R.string.Pet_Sci_Name_Cat),
                    breed1 = getString(R.string.Pet_Cat_Breed_1),
                    breed2 = getString(R.string.Pet_Cat_Breed_2),
                    breed3 = getString(R.string.Pet_Cat_Breed_3),
                    breed4 = getString(R.string.Pet_Cat_Breed_4),
                    breed5 = getString(R.string.Pet_Cat_Breed_5))

            startGuideActivity()
        }

        rootHF.rlDogCardHF.setOnClickListener {
            accessedPetType =
                PetType(id = ConstVeriable.DOG_PET_TYPE_ID,
                    sciName = getString(R.string.Pet_Sci_Name_Dog),
                    breed1 = getString(R.string.Pet_Dog_Breed_1),
                    breed2 = getString(R.string.Pet_Dog_Breed_2),
                    breed3 = getString(R.string.Pet_Dog_Breed_3),
                    breed4 = getString(R.string.Pet_Dog_Breed_4),
                    breed5 = getString(R.string.Pet_Dog_Breed_5))

            startGuideActivity()
        }

        rootHF.rlBirdCardHF.setOnClickListener {
            accessedPetType =
                PetType(id = ConstVeriable.BIRD_PET_TYPE_ID,
                    sciName = getString(R.string.Pet_Sci_Name_Bird),
                    breed1 = getString(R.string.Pet_Bird_Breed_1),
                    breed2 = getString(R.string.Pet_Bird_Breed_2),
                    breed3 = getString(R.string.Pet_Bird_Breed_3),
                    breed4 = getString(R.string.Pet_Bird_Breed_4),
                    breed5 = getString(R.string.Pet_Bird_Breed_5))

            startGuideActivity()
        }

        rootHF.rlFishCardHF.setOnClickListener {
            accessedPetType =
                PetType(id = ConstVeriable.FISH_PET_TYPE_ID,
                    sciName = getString(R.string.Pet_Sci_Name_Fish),
                    breed1 = getString(R.string.Pet_Fish_Breed_1),
                    breed2 = getString(R.string.Pet_Fish_Breed_2),
                    breed3 = getString(R.string.Pet_Fish_Breed_3),
                    breed4 = getString(R.string.Pet_Fish_Breed_4),
                    breed5 = getString(R.string.Pet_Fish_Breed_5))

            startGuideActivity()
        }
        //endregion

        return view
    }

    private fun startGuideActivity()
    {
        val intent = Intent(mContext, GuideActivity::class.java)
        mContext.startActivity(intent)
    }
}

//PureBlack Software / Murat BIYIK