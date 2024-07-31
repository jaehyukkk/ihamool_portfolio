package com.ilogistic.delivery_admin_backend.config

import org.hibernate.boot.MetadataBuilder
import org.hibernate.boot.spi.MetadataBuilderContributor
import org.hibernate.dialect.function.StandardSQLFunction
import org.hibernate.type.StandardBasicTypes

class CustomMysqlDialect : MetadataBuilderContributor {
    override fun contribute(metadataBuilder: MetadataBuilder) {
        metadataBuilder.applySqlFunction(
            "group_concat",
            StandardSQLFunction("group_concat", StandardBasicTypes.STRING)
        )
    }
}
