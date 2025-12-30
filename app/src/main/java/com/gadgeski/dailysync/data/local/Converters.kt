// data/local/Converters.kt
package com.gadgeski.dailysync.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ProvidedTypeConverter
@Suppress("unused") // ★ Room からリフレクションで使われるため、IDE上の「未使用」警告を抑制
class Converters {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // LocalDate
    @TypeConverter
    fun fromDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it, dateFormatter) }

    @TypeConverter
    fun dateToString(date: LocalDate?): String? = date?.format(dateFormatter)

    // LocalDateTime
    @TypeConverter
    fun fromDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it, dateTimeFormatter) }

    @TypeConverter
    fun dateTimeToString(dateTime: LocalDateTime?): String? = dateTime?.format(dateTimeFormatter)

    // List<String> <-> String (カンマ区切り)
    @TypeConverter
    fun fromStringList(value: String?): List<String> = // ★ split 後に trim + 空文字除去を入れておくと安全
        value
            ?.takeIf { it.isNotBlank() }
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?: emptyList()

    @TypeConverter
    fun toStringList(list: List<String>?): String = list
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() }
        ?.joinToString(",")
        ?: ""
}
