package com.pureblacksoft.petidence.service

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.pureblacksoft.petidence.activity.MainActivity
import com.pureblacksoft.petidence.data.Food
import com.pureblacksoft.petidence.function.SystemFun
import com.pureblacksoft.petidence.veriable.ConstVeriable
import org.json.JSONArray
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.net.URL

class FoodDownloadService: IntentService("FoodDownloadService")
{
    companion object
    {
        const val BROADCAST_ACTION = "com.pureblacksoft.petidence.BROADCAST"
        const val EXTENDED_DATA_STATUS = "com.pureblacksoft.petidence.STATUS"
    }

    private val foodIdList = mutableListOf<Int>()
    private val foodNameList = mutableListOf<String>()
    private val foodCaloriesList = mutableListOf<Float>()
    private val foodFatList = mutableListOf<Float>()
    private val foodCarbohydrateList = mutableListOf<Float>()
    private val foodProteinList = mutableListOf<Float>()
    private val foodImageList = mutableListOf<ByteArray>()
    private val foodForCatList = mutableListOf<Int>()
    private val foodForDogList = mutableListOf<Int>()
    private val foodForBirdList = mutableListOf<Int>()
    private val foodForFishList = mutableListOf<Int>()

    override fun onCreate()
    {
        super.onCreate()

        SystemFun.logI("Download", "FoodDownloadService{} -> Created")
    }

    override fun onHandleIntent(intent: Intent?)
    {
        val listSize = intent!!.getStringExtra("listSizeStr")!!.toInt()
        val arrayLength = intent.getStringExtra("arrayLengthStr")!!.toInt()
        val jsonArray = JSONArray(intent.getStringExtra("jsonArrayStr"))
        val resetData = intent.getStringExtra("resetDataStr").toBoolean()

        getFoodData(listSize, arrayLength, jsonArray)
        insertFoodData(resetData)
        serviceFinish()
    }

    private fun getFoodData(listSize: Int, arrayLength: Int, jsonArray: JSONArray)
    {
        SystemFun.logI("Download", "getFoodData() -> Running")

        try
        {
            for (i in listSize until arrayLength)
            {
                val jsonObject = jsonArray.getJSONObject(i)

                val id = jsonObject.getString("id").toInt()
                val name = jsonObject.getString("name_tr")
                val calories = jsonObject.getString("calories").toFloat()
                val fat = jsonObject.getString("fat").toFloat()
                val carbohydrate = jsonObject.getString("carbohydrate").toFloat()
                val protein = jsonObject.getString("protein").toFloat()
                val forCat = jsonObject.getString("for_cat").toInt()
                val forDog = jsonObject.getString("for_dog").toInt()
                val forBird = jsonObject.getString("for_bird").toInt()
                val forFish = jsonObject.getString("for_fish").toInt()

                //region Image
                val imageName = jsonObject.getString("image")
                val imageURL = URL(ConstVeriable.PETIDENCE_URL + ConstVeriable.IMAGE_FOLDER_URL + imageName)
                val connection = imageURL.openConnection()
                connection.doInput = true
                connection.connect()
                val inputStream = connection.getInputStream()
                val imageBitmap = BitmapFactory.decodeStream(inputStream)

                //region Convert Bitmap to ByteArray
                val outputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val image = outputStream.toByteArray()
                imageBitmap.recycle()
                //endregion
                //endregion

                foodIdList.add(id)
                foodNameList.add(name)
                foodCaloriesList.add(calories)
                foodFatList.add(fat)
                foodCarbohydrateList.add(carbohydrate)
                foodProteinList.add(protein)
                foodImageList.add(image)
                foodForCatList.add(forCat)
                foodForDogList.add(forDog)
                foodForBirdList.add(forBird)
                foodForFishList.add(forFish)
            }
        }
        catch (e: JSONException)
        {
            e.printStackTrace()
        }
    }

    private fun insertFoodData(resetData: Boolean)
    {
        if (resetData)
        {
            SystemFun.logI("Download", "(resetData) -> Delete Food Table")

            MainActivity.petidenceDB.deleteFoodTable()
        }

        SystemFun.logI("Download", "insertFoodData() -> Running")

        val filSize = foodIdList.size
        for (i in 0 until filSize)
        {
            MainActivity.petidenceDB.insertFood(
                Food(id = foodIdList[i],
                    name = foodNameList[i],
                    calories = foodCaloriesList[i],
                    fat = foodFatList[i],
                    carbohydrate = foodCarbohydrateList[i],
                    protein = foodProteinList[i],
                    image = foodImageList[i],
                    forCat = foodForCatList[i],
                    forDog = foodForDogList[i],
                    forBird = foodForBirdList[i],
                    forFish = foodForFishList[i]))
        }
        MainActivity.petidenceDB.close()
    }

    private fun serviceFinish()
    {
        SystemFun.logI("Download", "FoodDownloadService{} -> Finished")

        val status = "Service Finished"
        val localIntent = Intent(BROADCAST_ACTION).putExtra(EXTENDED_DATA_STATUS, status)
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent)
        stopSelf()
    }
}

//PureBlack Software / Murat BIYIK