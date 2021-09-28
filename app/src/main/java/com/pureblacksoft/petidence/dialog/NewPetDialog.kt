package com.pureblacksoft.petidence.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.activity.MainActivity
import com.pureblacksoft.petidence.adapter.PetAdapter
import com.pureblacksoft.petidence.adapter.PetTypeSpinnerAdapter
import com.pureblacksoft.petidence.data.Pet
import com.pureblacksoft.petidence.databinding.LinearNewPetBinding
import com.pureblacksoft.petidence.fragment.PetFragment
import com.pureblacksoft.petidence.function.AppFun
import com.pureblacksoft.petidence.veriable.ConstVeriable
import kotlinx.android.synthetic.main.linear_new_pet.view.*

class NewPetDialog(context: Context, activity: Activity) : Dialog(activity), AdapterView.OnItemSelectedListener
{
    var onConfirmPet: (() -> Unit)? = null
    var onCancelPet: (() -> Unit)? = null

    private val mContext = context
    private lateinit var rootNPL: View
    private lateinit var etPetNameNPL: EditText
    private lateinit var etPetAgeNPL: EditText
    private lateinit var etPetHeightNPL: EditText
    private lateinit var etPetWeightNPL: EditText
    private lateinit var etPetBreedNPL: EditText
    private lateinit var llPetTypeNPL: LinearLayout
    private lateinit var sPetTypeNPL: Spinner
    private lateinit var ivPetIconNPL: ImageView
    private lateinit var tvPetLabelNPL: TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = LinearNewPetBinding.inflate(layoutInflater)
        rootNPL = binding.root
        setContentView(rootNPL)

        etPetNameNPL = rootNPL.etPetNameNPL
        etPetAgeNPL = rootNPL.etPetAgeNPL
        etPetHeightNPL = rootNPL.etPetHeightNPL
        etPetWeightNPL = rootNPL.etPetWeightNPL
        etPetBreedNPL = rootNPL.etPetBreedNPL
        llPetTypeNPL = rootNPL.llPetTypeNPL

        //region Spinner
        sPetTypeNPL = rootNPL.sPetTypeNPL
        ivPetIconNPL = rootNPL.ivPetIconNPL
        tvPetLabelNPL = rootNPL.tvPetLabelNPL

        sPetTypeNPL.onItemSelectedListener = this

        //region Spinner Elements
        val petIconList = mutableListOf<Int>()
        petIconList.add(R.drawable.ic_pet_cat_128)
        petIconList.add(R.drawable.ic_pet_dog_128)
        petIconList.add(R.drawable.ic_pet_bird_128)
        petIconList.add(R.drawable.ic_pet_fish_128)

        val petLabelList = mutableListOf<String>()
        petLabelList.add(mContext.getString(R.string.Pet_Type_Cat))
        petLabelList.add(mContext.getString(R.string.Pet_Type_Dog))
        petLabelList.add(mContext.getString(R.string.Pet_Type_Bird))
        petLabelList.add(mContext.getString(R.string.Pet_Type_Fish))
        //endregion

        //region Spinner Adapter
        val petTypeSpinnerAdapter = PetTypeSpinnerAdapter(mContext, petIconList, petLabelList)
        sPetTypeNPL.adapter = petTypeSpinnerAdapter
        //endregion
        //endregion

        //region Button Connections
        llPetTypeNPL.setOnClickListener {
            sPetTypeNPL.performClick()
        }

        rootNPL.llConfirmPetNPL.setOnClickListener {
            //region Empty Number Control
            val age = if (etPetAgeNPL.text.toString() == "") -1
            else etPetAgeNPL.text.toString().toInt()

            val height = if (etPetHeightNPL.text.toString() == "") -1
            else etPetHeightNPL.text.toString().toInt()

            val weight = if (etPetWeightNPL.text.toString() == "") -1
            else etPetWeightNPL.text.toString().toInt()
            //endregion

            when (PetFragment.currentProcessId)
            {
                ConstVeriable.ADD_PET_PROCESS_ID ->
                {
                    MainActivity.petidenceDB.insertPet(
                        Pet(name = etPetNameNPL.text.toString(),
                            age = age,
                            height = height,
                            weight = weight,
                            breed = etPetBreedNPL.text.toString(),
                            typeId = PetFragment.petTypeId))
                }
                ConstVeriable.EDIT_PET_PROCESS_ID ->
                {
                    MainActivity.petidenceDB.updatePet(PetAdapter.selectedPet,
                        Pet(name = etPetNameNPL.text.toString(),
                            age = age,
                            height = height,
                            weight = weight,
                            breed = etPetBreedNPL.text.toString(),
                            typeId = PetFragment.petTypeId))
                }
            }

            onConfirmPet?.invoke()
        }

        rootNPL.llCancelPetNPL.setOnClickListener {
            onCancelPet?.invoke()
        }
        //endregion
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
    {
        when (position)
        {
            0 ->
            {
                PetFragment.petTypeId = ConstVeriable.CAT_PET_TYPE_ID
            }
            1 ->
            {
                PetFragment.petTypeId = ConstVeriable.DOG_PET_TYPE_ID
            }
            2 ->
            {
                PetFragment.petTypeId = ConstVeriable.BIRD_PET_TYPE_ID
            }
            3 ->
            {
                PetFragment.petTypeId = ConstVeriable.FISH_PET_TYPE_ID
            }
        }

        AppFun.setPetType(mContext, PetFragment.petTypeId, tvPetLabelNPL, ivPetIconNPL)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun show()
    {
        super.show()

        //region Process Control
        when (PetFragment.currentProcessId)
        {
            ConstVeriable.ADD_PET_PROCESS_ID ->
            {
                etPetNameNPL.text.clear()
                etPetAgeNPL.text.clear()
                etPetHeightNPL.text.clear()
                etPetWeightNPL.text.clear()
                etPetBreedNPL.text.clear()

                llPetTypeNPL.performClick()
            }
            ConstVeriable.EDIT_PET_PROCESS_ID ->
            {
                //region Empty Number Control
                val ageStr = if (PetAdapter.selectedPet.age == -1) ""
                else PetAdapter.selectedPet.age.toString()

                val heightStr = if (PetAdapter.selectedPet.height == -1) ""
                else PetAdapter.selectedPet.height.toString()

                val weightStr = if (PetAdapter.selectedPet.weight == -1) ""
                else PetAdapter.selectedPet.weight.toString()
                //endregion

                etPetNameNPL.setText(PetAdapter.selectedPet.name)
                etPetAgeNPL.setText(ageStr)
                etPetHeightNPL.setText(heightStr)
                etPetWeightNPL.setText(weightStr)
                etPetBreedNPL.setText(PetAdapter.selectedPet.breed)

                when (PetAdapter.selectedPet.typeId)
                {
                    ConstVeriable.CAT_PET_TYPE_ID -> sPetTypeNPL.setSelection(0)
                    ConstVeriable.DOG_PET_TYPE_ID -> sPetTypeNPL.setSelection(1)
                    ConstVeriable.BIRD_PET_TYPE_ID -> sPetTypeNPL.setSelection(2)
                    ConstVeriable.FISH_PET_TYPE_ID -> sPetTypeNPL.setSelection(3)
                }
            }
        }
        //endregion
    }
}

//PureBlack Software / Murat BIYIK