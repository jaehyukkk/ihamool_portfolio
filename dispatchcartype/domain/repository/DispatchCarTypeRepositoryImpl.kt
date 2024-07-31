package com.ilogistic.delivery_admin_backend.dispatchcartype.domain.repository

import com.ilogistic.delivery_admin_backend.dispatchcartype.domain.entity.QDispatchCarType.dispatchCarType
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class DispatchCarTypeRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : DispatchCarTypeRepositoryCustom {

    override fun getDispatchCarTypeList(dispatchId: Long): List<Long> {
        return queryFactory
            .select(dispatchCarType.carType.id)
            .from(dispatchCarType)
            .where(dispatchCarType.dispatch.id.eq(dispatchId))
            .fetch()
    }
}
