package com.pureblacksoft.petidence.fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.activity.MainActivity
import com.pureblacksoft.petidence.adapter.PetAdapter
import com.pureblacksoft.petidence.databinding.FragmentPetBinding
import com.pureblacksoft.petidence.function.SystemFun
import com.pureblacksoft.petidence.function.AppFun
import com.pureblacksoft.petidence.dialog.ApprovalDialog
import com.pureblacksoft.petidence.dialog.NewPetDialog
import com.pureblacksoft.petidence.veriable.ConstVeriable
import kotlinx.android.synthetic.main.appbar_pet.view.*
import kotlinx.android.synthetic.main.fragment_pet.view.*

class PetFragment: Fragment(R.layout.fragment_pet)
{
    companion object
    {
        var currentProcessId = 0
        var petTypeId: Int = 0
    }

    private lateinit var mContext: Context
    private lateinit var mActivity: MainActivity
    private lateinit var rootPF: View
    private lateinit var petAdapter: PetAdapter
    private var state: Parcelable? = null

    override fun onAttach(context: Context)
    {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_pet, container, false)
        val binding = FragmentPetBinding.bind(view)
        rootPF = binding.root

        //region RecyclerView
        val recyclerViewPF = rootPF.recyclerViewPF
        val linearManager = LinearLayoutManager(mContext)
        linearManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewPF.layoutManager = linearManager

        val divider = DividerItemDecoration(mContext, linearManager.orientation)
        val dividerDrawable = ContextCompat.getDrawable(mContext, R.drawable.shape_divider_app)
        if (dividerDrawable != null)
        {
            divider.setDrawable(dividerDrawable)
            recyclerViewPF.addItemDecoration(divider)
        }
        //endregion

        //region PetAdapter
        val rlSelectionPAB = rootPF.rlSelectionPAB
        val ivAddPetPAB = rootPF.ivAddPetPAB
        val ivEditPetPAB = rootPF.ivEditPetPAB
        val ivDeletePetPAB = rootPF.ivDeletePetPAB

        fun setPetAdapter()
        {
            petAdapter = PetAdapter(MainActivity.petidenceDB.readPetList())
            recyclerViewPF.adapter = petAdapter

            //region Empty Adapter Control
            if (petAdapter.itemCount == 0)
            {
                AppFun.setVisibility(rootPF.llNoPetPF, true)
            }
            else
            {
                AppFun.setVisibility(rootPF.llNoPetPF, false)
            }
            //endregion

            //region Selection Control
            petAdapter.onSingleSelect = {
                AppFun.setVisibility(ivAddPetPAB, false)
                AppFun.setVisibility(rlSelectionPAB, true)
                AppFun.setVisibility(ivEditPetPAB, true)
            }

            petAdapter.onMultiSelect = {
                AppFun.setVisibility(ivEditPetPAB, false)
            }

            petAdapter.onNoSelect = {
                AppFun.setVisibility(rlSelectionPAB, false)
                AppFun.setVisibility(ivAddPetPAB, true)
            }
            //endregion

            AppFun.restoreState(state, linearManager)
        }
        //endregion

        //region Get Pets
        setPetAdapter()
        //endregion

        //region Button Connections
        val newPetDialog = NewPetDialog(mContext, mActivity)

        ivAddPetPAB.setOnClickListener {
            currentProcessId = ConstVeriable.ADD_PET_PROCESS_ID

            newPetDialog.show()
        }

        ivEditPetPAB.setOnClickListener {
            currentProcessId = ConstVeriable.EDIT_PET_PROCESS_ID

            newPetDialog.show()
        }

        ivDeletePetPAB.setOnClickListener {
            val builder = ApprovalDialog.alertBuilder(mContext)
            builder.setMessage(R.string.Pet_Dialog_Message)
            builder.setPositiveButton(R.string.Dialog_Yes) { _, _ ->
                for (pet in petAdapter.selectedPetList) {
                    MainActivity.petidenceDB.deletePet(pet)
                }

                AppFun.setVisibility(rlSelectionPAB, false)
                AppFun.setVisibility(ivAddPetPAB, true)

                state = AppFun.savedState(petAdapter, linearManager)
                setPetAdapter()
            }
            builder.setNegativeButton(R.string.Dialog_No) { _, _ -> }
            builder.show()
        }

        newPetDialog.onConfirmPet = {
            SystemFun.hideSoftKeyboard(mActivity)
            newPetDialog.dismiss()

            when (currentProcessId)
            {
                ConstVeriable.ADD_PET_PROCESS_ID -> {}
                ConstVeriable.EDIT_PET_PROCESS_ID ->
                {
                    AppFun.setVisibility(rlSelectionPAB, false)
                    AppFun.setVisibility(ivAddPetPAB, true)
                }
            }

            state = AppFun.savedState(petAdapter, linearManager)
            setPetAdapter()
        }

        newPetDialog.onCancelPet = {
            SystemFun.hideSoftKeyboard(mActivity)
            newPetDialog.dismiss()
        }
        //endregion

        return view
    }
}

//PureBlack Software / Murat BIYIK