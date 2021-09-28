package com.pureblacksoft.petidence.function

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.activity.MainActivity
import com.pureblacksoft.petidence.adapter.PetAdapter
import com.pureblacksoft.petidence.service.FoodDownloadService
import com.pureblacksoft.petidence.service.PlayDownloadService
import com.pureblacksoft.petidence.service.SleepDownloadService
import com.pureblacksoft.petidence.veriable.ConstVeriable
import org.json.JSONException

class AppFun
{
    companion object
    {
        fun setVisibility(view: View, visibile: Boolean)
        {
            if (visibile)
            {
                if (view.visibility == View.GONE)
                {
                    view.visibility = View.VISIBLE
                }
            }
            else
            {
                if (view.visibility == View.VISIBLE)
                {
                    view.visibility = View.GONE
                }
            }
        }

        fun savedState (adapter: PetAdapter, linearManager: LinearLayoutManager): Parcelable?
        {
            var state: Parcelable? = null

            if (adapter.itemCount != 0)
            {
                state = linearManager.onSaveInstanceState()
            }

            return state
        }

        fun restoreState(state: Parcelable?, linearManager: LinearLayoutManager)
        {
            if (state != null)
            {
                linearManager.onRestoreInstanceState(state)
            }
        }

        fun setPetType(context: Context, petType: Int, tvPetLabel: TextView, ivPetIcon: ImageView)
        {
            when (petType)
            {
                ConstVeriable.CAT_PET_TYPE_ID ->
                {
                    tvPetLabel.text = context.getText(R.string.Pet_Type_Cat)
                    ivPetIcon.setImageResource(R.drawable.ic_pet_cat_128)
                }
                ConstVeriable.DOG_PET_TYPE_ID ->
                {
                    tvPetLabel.text = context.getText(R.string.Pet_Type_Dog)
                    ivPetIcon.setImageResource(R.drawable.ic_pet_dog_128)
                }
                ConstVeriable.BIRD_PET_TYPE_ID ->
                {
                    tvPetLabel.text = context.getText(R.string.Pet_Type_Bird)
                    ivPetIcon.setImageResource(R.drawable.ic_pet_bird_128)
                }
                ConstVeriable.FISH_PET_TYPE_ID ->
                {
                    tvPetLabel.text = context.getText(R.string.Pet_Type_Fish)
                    ivPetIcon.setImageResource(R.drawable.ic_pet_fish_128)
                }
            }
        }

        fun downloadDataJSON(context: Context, downloadId: Int)
        {
            val downloadURL: String?
            val listSize: Int?
            val downloadIntent: Intent?

            when (downloadId)
            {
                ConstVeriable.FOOD_DOWNLOAD_ID ->
                {
                    downloadURL = ConstVeriable.FOOD_DATA_URL
                    listSize = MainActivity.petidenceDB.readFoodList().size
                    downloadIntent = Intent(context, FoodDownloadService::class.java)
                }
                ConstVeriable.SLEEP_DOWNLOAD_ID ->
                {
                    downloadURL = ConstVeriable.SLEEP_DATA_URL
                    listSize = MainActivity.petidenceDB.readSleepList().size
                    downloadIntent = Intent(context, SleepDownloadService::class.java)
                }
                ConstVeriable.PLAY_DOWNLOAD_ID ->
                {
                    downloadURL = ConstVeriable.PLAY_DATA_URL
                    listSize = MainActivity.petidenceDB.readPlayList().size
                    downloadIntent = Intent(context, PlayDownloadService::class.java)
                }
                else ->
                {
                    downloadURL = null
                    listSize = null
                    downloadIntent = null
                }
            }

            SystemFun.logI("Download", "downloadDataJSON() -> Data $downloadId")

            val requestQueue = Volley.newRequestQueue(context)

            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, ConstVeriable.PETIDENCE_URL + downloadURL, null,
                { response ->
                    try
                    {
                        val jsonArray = response.getJSONArray("results")
                        val arrayLength = jsonArray.length()

                        fun startDownloadService(pListSize: Int, resetData: Boolean)
                        {
                            downloadIntent?.putExtra("listSizeStr", pListSize.toString())
                            downloadIntent?.putExtra("arrayLengthStr", arrayLength.toString())
                            downloadIntent?.putExtra("jsonArrayStr", jsonArray.toString())
                            downloadIntent?.putExtra("resetDataStr", resetData.toString())
                            context.startService(downloadIntent)
                        }

                        when
                        {
                            arrayLength > listSize!! ->
                            {
                                SystemFun.logI("Download", "(arrayLength > listSize) -> Update Data $downloadId")

                                startDownloadService(listSize, false)
                            }
                            arrayLength == listSize ->
                            {
                                SystemFun.logI("Download", "(arrayLength == listSize) -> Keep Data $downloadId")
                            }
                            arrayLength < listSize ->
                            {
                                SystemFun.logI("Download", "(arrayLength < listSize) -> Reset Data $downloadId")

                                startDownloadService(0, true)
                            }
                        }
                    }
                    catch (e: JSONException)
                    {
                        e.printStackTrace()
                    }
                },
                { error ->
                    Log.e("Download", "(Connection Failed) -> $error.toString()")
                })

            requestQueue.add(jsonObjectRequest)
        }
    }
}

//PureBlack Software / Murat BIYIK