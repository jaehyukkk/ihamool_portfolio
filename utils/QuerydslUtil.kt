package com.ilogistic.delivery_admin_backend.utils

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.core.types.dsl.StringTemplate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class QuerydslUtil {
    companion object{
        public fun betweenDate(
            query: DateTimePath<LocalDateTime>,
            startDate: String?,
            endDate: String?
        ): BooleanExpression? {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            if (!startDate.isNullOrBlank() && !endDate.isNullOrBlank()) {
                val start = LocalDateTime.parse(startDate + "T00:00:00", formatter)
                val end = LocalDateTime.parse(endDate + "T23:59:59", formatter)
                return query.between(start, end)
            }
            return null
        }

        fun betweenStringTemplateDate(datePath: StringPath, startDate: String?, endDate: String?): BooleanExpression? {
            if(!startDate.isNullOrBlank() && !endDate.isNullOrBlank()){
                val inputFormat = SimpleDateFormat("yyyy-MM-dd")
                val outputFormat = SimpleDateFormat("yyyyMMdd")

                val start: Date = inputFormat.parse(startDate)
                val formatStart = outputFormat.format(start)
                val end: Date = inputFormat.parse(endDate)
                val formatEnd = outputFormat.format(end)

                println("---------------------")
                println(formatStart)
                println(formatEnd)
                println("---------------------")
                val dateStringTemplate = datePath.stringValue()

                println("-11--------------------")
                println(datePath)
                println("-11--------------------")

                return datePath.between(formatStart, formatEnd)
            }

            return null
        }

    }
}
