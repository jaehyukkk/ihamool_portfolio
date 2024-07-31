package com.ilogistic.delivery_admin_backend.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcelColumn(
    val name: String,
    val dataType: ExcelDataType = ExcelDataType.STRING
)

enum class ExcelDataType {
    STRING, NUMBER, BOOLEAN, DATE
}
