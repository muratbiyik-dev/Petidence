package com.pureblacksoft.petidence.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.activity.GuideActivity
import com.pureblacksoft.petidence.data.Pet
import com.pureblacksoft.petidence.databinding.CardPetBinding
import com.pureblacksoft.petidence.function.AppFun
import kotlinx.android.synthetic.main.card_pet.view.*
import kotlinx.android.synthetic.main.linear_pet.view.*

class PetAdapter(private val petList: MutableList<Pet>): RecyclerView.Adapter<PetAdapter.ViewHolder>()
{
    companion object
    {
        lateinit var accessedPet: Pet
        lateinit var selectedPet: Pet
    }

    val selectedPetList = mutableListOf<Pet>()
    var selectCount: Int = 0
    var onSingleSelect: (() -> Unit)? = null
    var onMultiSelect: (() -> Unit)? = null
    var onNoSelect: (() -> Unit)? = null

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.card_pet, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val pet = petList[position]
        holder.bind(pet)
    }

    override fun getItemCount(): Int = petList.size

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view)
    {
        private val rootPC = CardPetBinding.bind(view).root

        fun bind(pet: Pet)
        {
            val notSpecifiedStr = mContext.getString(R.string.Pet_Not_Specified)

            rootPC.tvPetNamePL.text = if (pet.name == "") notSpecifiedStr
            else pet.name

            rootPC.tvPetAgePL.text = if (pet.age == -1) notSpecifiedStr
            else mContext.getString(R.string.Pet_Age, pet.age)

            rootPC.tvPetHeightPL.text = if (pet.height == -1) notSpecifiedStr
            else mContext.getString(R.string.Pet_Height, pet.height)

            rootPC.tvPetWeightPL.text = if (pet.weight == -1) notSpecifiedStr
            else mContext.getString(R.string.Pet_Weight, pet.weight)

            rootPC.tvPetBreedPL.text = if (pet.breed == "") notSpecifiedStr
            else pet.breed

            AppFun.setPetType(mContext, pet.typeId, rootPC.tvPetLabelPL, rootPC.ivPetIconPL)

            //region Pet Accession
            view.setOnClickListener {
                if (selectedPetList.isEmpty())
                {
                    accessedPet = petList[adapterPosition]

                    val intent = Intent(mContext, GuideActivity::class.java)
                    mContext.startActivity(intent)
                }
            }
            //endregion

            //region Pet Selection
            view.setOnLongClickListener {
                if (selectedPetList.contains(pet))
                {
                    selectedPetList.remove(pet)
                    rootPC.linearPC.setBackgroundColor(mContext.getColor(R.color.Card_BackColor))

                    selectCount--
                }
                else
                {
                    selectedPetList.add(pet)
                    rootPC.linearPC.setBackgroundColor(mContext.getColor(R.color.Ripple_BackColor))

                    selectCount++
                }

                if (selectCount == 1)
                {
                    selectedPet = selectedPetList[0]

                    onSingleSelect?.invoke()
                }
                else if (selectCount > 1)
                {
                    onMultiSelect?.invoke()
                }
                else
                {
                    onNoSelect?.invoke()
                }

                true
            }
            //endregion
        }
    }
}

//PureBlack Software / Murat BIYIK