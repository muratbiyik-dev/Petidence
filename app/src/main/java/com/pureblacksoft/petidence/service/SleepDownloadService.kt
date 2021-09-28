package com.pureblacksoft.petidence.service

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.pureblacksoft.petidence.activity.MainActivity
import com.pureblacksoft.petidence.data.Sleep
import com.pureblacksoft.petidence.function.SystemFun
import org.json.JSONArray
import org.json.JSONException

class SleepDownloadService: IntentService("SleepDownloadService")
{
    companion object
    {
        const val BROADCAST_ACTION = "com.pureblacksoft.petidence.BROADCAST"
        const val EXTENDED_DATA_STATUS = "com.pureblacksoft.petidence.STATUS"
    }

    private val sleepIdList = mutableListOf<Int>()
    private val sleepContentList = mutableListOf<String>()
    private val sleepPetTypeIdList = mutableListOf<Int>()

    override fun onCreate()
    {
        super.onCreate()

        SystemFun.logI("Download", "SleepDownloadService{} -> Created")
    }

    override fun onHandleIntent(intent: Intent?)
    {
        val listSize = intent!!.getStringExtra("listSizeStr")!!.toInt()
        val arrayLength = intent.getStringExtra("arrayLengthStr")!!.toInt()
        val jsonArray = JSONArray(intent.getStringExtra("jsonArrayStr"))
        val resetData = intent.getStringExtra("resetDataStr").toBoolean()

        getSleepData(listSize, arrayLength, jsonArray)
        insertSleepData(resetData)
        serviceFinish()
    }

    private fun getSleepData(listSize: Int, arrayLength: Int, jsonArray: JSONArray)
    {
        SystemFun.logI("Download", "getSleepData() -> Running")

        try
        {
            for (i in listSize until arrayLength)
            {
                val jsonObject = jsonArray.getJSONObject(i)

                val id = jsonObject.getString("id").toInt()
                val content = jsonObject.getString("content_tr")
                val petTypeId = jsonObject.getString("pet_type_id").toInt()

                sleepIdList.add(id)
                sleepContentList.add(content)
                sleepPetTypeIdList.add(petTypeId)
            }
        }
        catch (e: JSONException)
        {
            e.printStackTrace()
        }
    }

    private fun insertSleepData(resetData: Boolean)
    {
        if (resetData)
        {
            SystemFun.logI("Download", "(resetData) -> Delete Sleep Table")

            MainActivity.petidenceDB.deleteSleepTable()
        }

        SystemFun.logI("Download", "insertSleepData() -> Running")

        val silSize = sleepIdList.size
        for (i in 0 until silSize)
        {
            MainActivity.petidenceDB.insertSleep(
                Sleep(id = sleepIdList[i],
                    content = sleepContentList[i],
                    petTypeId = sleepPetTypeIdList[i])
            )
        }
        MainActivity.petidenceDB.close()
    }

    private fun serviceFinish()
    {
        SystemFun.logI("Download", "SleepDownloadService{} -> Finished")

        val status = "Service Finished"
        val localIntent = Intent(BROADCAST_ACTION).putExtra(EXTENDED_DATA_STATUS, status)
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent)
        stopSelf()
    }
}

//PureBlack Software / Murat BIYIK