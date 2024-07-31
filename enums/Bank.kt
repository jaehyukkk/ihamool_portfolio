package com.ilogistic.delivery_admin_backend.enums

enum class Bank(val value: String, val code: Int) {
    KEB_HANA_BANK("KEB하나은행", 0),
    SC_FIRST_BANK("SC제일은행", 1),
    KOOKMIN_BANK("국민은행", 2),
    SHINHAN_BANK("신한은행", 3),
    FOREIGN_EXCHANGE_BANK("외환은행", 4),
    WOORI_BANK("우리은행", 5),
    KOREA_CITY_BANK("한국시티은행", 6),
    KYONGNAM_BANK("경남은행", 7),
    GWANGJU_BANK("광주은행", 8),
    DAEGU_BANK("대구은행", 9),
    BUSAN_BANK("부산은행", 10),
    JEONBUK_BANK("전북은행", 11),
    JEJU_BANK("제주은행", 12),
    KIUP_BANK("기업은행", 13),
    NH_BANK("농협", 14),
    SH_BANK("수협", 15);

    companion object {
        fun findByValue(value: String): Bank {
            return values().find { it.value == value } ?: throw IllegalArgumentException("해당하는 은행이 없습니다.")
        }

        fun findByCode(code: Int): Bank {
            return values().find { it.code == code } ?: throw IllegalArgumentException("해당하는 은행이 없습니다.")
        }
    }
}
