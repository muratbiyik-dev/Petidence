package com.pureblacksoft.petidence.service

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.pureblacksoft.petidence.activity.MainActivity
import com.pureblacksoft.petidence.data.Play
import com.pureblacksoft.petidence.function.SystemFun
import org.json.JSONArray
import org.json.JSONException

class PlayDownloadService: IntentService("PlayDownloadService")
{
    companion object
    {
        const val BROADCAST_ACTION = "com.pureblacksoft.petidence.BROADCAST"
        const val EXTENDED_DATA_STATUS = "com.pureblacksoft.petidence.STATUS"
    }

    private val playIdList = mutableListOf<Int>()
    private val playContentList = mutableListOf<String>()
    private val playPetTypeIdList = mutableListOf<Int>()

    override fun onCreate()
    {
        super.onCreate()

        SystemFun.logI("Download", "PlayDownloadService{} -> Created")
    }

    override fun onHandleIntent(intent: Intent?)
    {
        val listSize = intent!!.getStringExtra("listSizeStr")!!.toInt()
        val arrayLength = intent.getStringExtra("arrayLengthStr")!!.toInt()
        val jsonArray = JSONArray(intent.getStringExtra("jsonArrayStr"))
        val resetData = intent.getStringExtra("resetDataStr").toBoolean()

        getPlayData(listSize, arrayLength, jsonArray)
        insertPlayData(resetData)
        serviceFinish()
    }

    private fun getPlayData(listSize: Int, arrayLength: Int, jsonArray: JSONArray)
    {
        SystemFun.logI("Download", "getPlayData() -> Running")

        try
        {
            for (i in listSize until arrayLength)
            {
                val jsonObject = jsonArray.getJSONObject(i)

                val id = jsonObject.getString("id").toInt()
                val content = jsonObject.getString("content_tr")
                val petTypeId = jsonObject.getString("pet_type_id").toInt()

                playIdList.add(id)
                playContentList.add(content)
                playPetTypeIdList.add(petTypeId)
            }
        }
        catch (e: JSONException)
        {
            e.printStackTrace()
        }
    }

    private fun insertPlayData(resetData: Boolean)
    {
        if (resetData)
        {
            SystemFun.logI("Download", "(resetData) -> Delete Play Table")

            MainActivity.petidenceDB.deletePlayTable()
        }

        SystemFun.logI("Download", "insertPlayData() -> Running")

        val pilSize = playIdList.size
        for (i in 0 until pilSize)
        {
            MainActivity.petidenceDB.insertPlay(
                Play(id = playIdList[i],
                    content = playContentList[i],
                    petTypeId = playPetTypeIdList[i])
            )
        }
        MainActivity.petidenceDB.close()
    }

    private fun serviceFinish()
    {
        SystemFun.logI("Download", "PlayDownloadService{} -> Finished")

        val status = "Service Finished"
        val localIntent = Intent(BROADCAST_ACTION).putExtra(EXTENDED_DATA_STATUS, status)
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent)
        stopSelf()
    }
}

//PureBlack Software / Murat BIYIK