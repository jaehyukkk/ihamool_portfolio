package com.ilogistic.delivery_admin_backend.utils

import com.ilogistic.delivery_admin_backend.annotation.ExcelColumn
import com.ilogistic.delivery_admin_backend.annotation.ExcelDataType
import com.ilogistic.delivery_admin_backend.annotation.ExcelDataType.*
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.springframework.stereotype.Component
import org.springframework.util.ObjectUtils
import java.io.IOException
import javax.servlet.http.HttpServletResponse

@Component
class ExcelUtils {

    companion object {
        const val MAX_ROW = 5000
    }

    fun downloadExcel(response: HttpServletResponse, data: List<Any>, clazz: Class<*>, fileName: String) {
        try {
            SXSSFWorkbook().use { workbook ->
                var loop = 1
                val listSize = data.size

                for (start in 0 until listSize step MAX_ROW) {
                    var nextPage = MAX_ROW * loop
                    if (nextPage > listSize) nextPage = listSize
                    val list = ArrayList(data.subList(start, nextPage))
                    getWorkBook(clazz, workbook, start, findHeaderNames(clazz), list, listSize)
                    list.clear()
                    loop++
                }

                response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                response.setHeader("Content-Disposition", "attachment; filename=$fileName.xlsx")

                response.outputStream.use { outputStream ->
                    workbook.write(outputStream)
                    outputStream.flush()
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IllegalAccessException::class, IOException::class)
    fun getWorkBook(
        clazz: Class<*>,
        workbook: SXSSFWorkbook,
        rowIdx: Int,
        headerNames: List<String>,
        data: List<*>,
        maxSize: Int
    ): SXSSFWorkbook {
        val sheetName = "Sheet${rowIdx / MAX_ROW + 1}"
        val sheet = if (ObjectUtils.isEmpty(workbook.getSheet(sheetName))) {
            workbook.createSheet(sheetName)
        } else {
            workbook.getSheet(sheetName)
        } as Sheet
        sheet.defaultColumnWidth = 10
        sheet.defaultRowHeight = 500.toShort()

        val row = sheet.createRow(0)
        createHeaders(workbook, row, headerNames)
        createBody(clazz, data, sheet, rowIdx)

        // 주기적인 flush 진행
        val rowNo = rowIdx % maxSize
        if (rowNo % MAX_ROW == 0) {
            (sheet as SXSSFSheet).flushRows(MAX_ROW)
        }

        return workbook
    }

    fun createHeaders(workbook: SXSSFWorkbook, row: Row, headerNames: List<String>) {
        val font = workbook.createFont().apply {
            color = 255.toShort()
        }

        val headerCellStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            borderLeft = BorderStyle.MEDIUM
            borderRight = BorderStyle.MEDIUM
            borderTop = BorderStyle.MEDIUM
            borderBottom = BorderStyle.MEDIUM
            fillForegroundColor = 102.toShort()
            fillPattern = FillPatternType.SOLID_FOREGROUND
            setFont(font)
        }

        headerNames.forEachIndexed { index, headerName ->
            val cell = row.createCell(index)
            cell.cellStyle = headerCellStyle
            cell.setCellValue(headerName)
        }
    }

    @Throws(IllegalAccessException::class, IOException::class)
    fun createBody(clazz: Class<*>, data: List<*>, sheet: Sheet, rowIdx: Int) {
        data.forEachIndexed { index, item ->
            val row = sheet.createRow(index + 1)
            item?.let { findFieldValue(clazz, it) }?.forEachIndexed { fieldIndex, fieldValue ->
                val cell = row.createCell(fieldIndex)
                setCellValue(cell, fieldValue)
                if ((index + 1) % MAX_ROW == 0) {
                    (sheet as SXSSFSheet).flushRows(MAX_ROW)
                }
            }
        }
    }

    fun findHeaderNames(clazz: Class<*>): List<String> {
        return clazz.declaredFields
            .filter { it.isAnnotationPresent(ExcelColumn::class.java) }
            .map { it.getAnnotation(ExcelColumn::class.java).name }
    }

    @Throws(IllegalAccessException::class)
    fun findFieldValue(clazz: Class<*>, obj: Any): List<Pair<Any, ExcelDataType>> {
        return clazz.declaredFields.filter {
            it.isAnnotationPresent(ExcelColumn::class.java)
        }.map { field ->
            field.isAccessible = true
            val value = field[obj]
            val dataType = field.getAnnotation(ExcelColumn::class.java).dataType
            Pair(value, dataType)
        }
    }

    private fun setCellValue(cell: Cell, fieldValue: Pair<Any, ExcelDataType>) {
        when (fieldValue.second) {
            STRING -> cell.setCellValue(fieldValue.first.toString())
            NUMBER -> cell.setCellValue(fieldValue.first.toString().toDouble())
            BOOLEAN -> cell.setCellValue(fieldValue.first.toString().toBoolean())
            DATE -> cell.setCellValue(fieldValue.first.toString())
        }
    }
}
