package com.example.springthymeleafintegration.config

import com.example.springthymeleafintegration.filter.LoginFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.servlet.Filter

@Configuration
class WebConfig {

    @Bean
    fun logFilter(): FilterRegistrationBean<Filter> {
        val filterRegistrationBean: FilterRegistrationBean<Filter> = FilterRegistrationBean<Filter>()
        filterRegistrationBean.filter = LoginFilter()
        filterRegistrationBean.order = 1
        filterRegistrationBean.addUrlPatterns("/*")
        return filterRegistrationBean
    }
}