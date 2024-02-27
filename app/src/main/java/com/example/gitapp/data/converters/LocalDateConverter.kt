package com.example.gitapp.data.converters

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter fun dateToString(localDate: LocalDate): String {
        return localDate.toString()
    }

    @TypeConverter fun stringToDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString)
    }

    @FromJson fun fromJson(dateString: String): LocalDate {
        if (dateString.length > 10) {
            return LocalDate.parse(dateString.substring(0, 10))
        }
        return LocalDate.now()
    }

    @ToJson fun toJson(localDate: LocalDate): String {
        return localDate.toString()
    }
}