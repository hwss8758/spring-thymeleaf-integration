package com.example.springthymeleafintegration.config

import com.example.springthymeleafintegration.argumentresolver.LoginMemberArgumentResolver
import com.example.springthymeleafintegration.filter.LogFilter
import com.example.springthymeleafintegration.filter.LoginCheckFilter
import com.example.springthymeleafintegration.interceptor.LogInterceptor
import com.example.springthymeleafintegration.interceptor.LoginCheckInterceptor
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.Filter

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(LoginMemberArgumentResolver())
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LogInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/css/**", "/*.ico", "/error")

        val excludePathPatternsListOfLoginCheck = listOf<String>(
            "/", "/members/add", "/login", "/logout",
            "/css/**", "/*.ico", "/error"
        )

        registry.addInterceptor(LoginCheckInterceptor())
            .order(2)
            .addPathPatterns("/**")
            .excludePathPatterns(excludePathPatternsListOfLoginCheck)
    }

    @Bean
    fun logFilter(): FilterRegistrationBean<Filter> {
        val filterRegistrationBean: FilterRegistrationBean<Filter> = FilterRegistrationBean<Filter>()
        filterRegistrationBean.filter = LogFilter()
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