package com.sp.uxpulse.storage

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EventConverter {

    @TypeConverter
    fun fromMap(value: Map<String, Any>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toMap(value: String?): Map<String, Any>? {
        return Gson().fromJson(value, object : TypeToken<Map<String, Any>>() {}.type)
    }
}
