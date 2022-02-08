package com.example.springthymeleafintegration.config

import com.example.springthymeleafintegration.filter.LoginCheckFilter
import com.example.springthymeleafintegration.filter.LoginFilter
import com.example.springthymeleafintegration.interceptor.LogInterceptor
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.Filter

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LogInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/css/**", "/*.ico", "/error")
    }

    @Bean
    fun logFilter(): FilterRegistrationBean<Filter> {
        val filterRegistrationBean: FilterRegistrationBean<Filter> = FilterRegistrationBean<Filter>()
        filterRegistrationBean.filter = LoginFilter()
        filterRegistrationBean.order = 1
        filterRegistrationBean.addUrlPatterns("/*")
        return filterRegistrationBean
    }

    @Bean
    fun loginCheckFilter(): FilterRegistrationBean<Filter> {
        val filterRegistrationBean: FilterRegistrationBean<Filter> = FilterRegistrationBean<Filter>()
        filterRegistrationBean.filter = LoginCheckFilter()
        filterRegistrationBean.order = 2
        filterRegistrationBean.addUrlPatterns("/*")
        return filterRegistrationBean
    }
}