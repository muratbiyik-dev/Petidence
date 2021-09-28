package com.pureblacksoft.petidence.parser

import org.json.JSONArray
import org.json.JSONObject

class JsonParser
{
    private fun parseJsonObject(jsonObject: JSONObject): HashMap<String, String>
    {
        val dataList = HashMap<String, String>()

        val name = jsonObject.getString("name")
        val latitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lat")
        val longitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lng")

        dataList["name"] = name
        dataList["lat"] = latitude
        dataList["lng"] = longitude

        return dataList
    }

    private fun parseJsonArray(jsonArray: JSONArray): List<HashMap<String, String>>
    {
        val dataList = ArrayList<HashMap<String, String>>()

        val arrayLength = jsonArray.length()
        for (i in 0 until arrayLength)
        {
            val data = parseJsonObject(jsonArray[i] as JSONObject)
            dataList.add(data)
        }

        return dataList
    }

    fun parseResult(jsonObject: JSONObject): List<HashMap<String, String>>
    {
        val jsonArray = jsonObject.getJSONArray("results")

        return parseJsonArray(jsonArray)
    }
}

//PureBlack Software / Murat BIYIK