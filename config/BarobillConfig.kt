package com.ilogistic.delivery_admin_backend.config

import com.baroservice.api.BarobillApiProfile
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.baroservice.api.BarobillApiService

@Configuration
class BarobillConfig {

    @Bean
    fun barobillApiService(): BarobillApiService? {
        return BarobillApiService(BarobillApiProfile.TESTBED)
    }
}
