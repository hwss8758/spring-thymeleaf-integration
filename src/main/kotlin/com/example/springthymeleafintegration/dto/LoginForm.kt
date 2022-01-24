package com.example.springthymeleafintegration.dto

import javax.validation.constraints.NotEmpty

data class LoginForm(

    @field:NotEmpty
    val loginId: String? = null,

    @field:NotEmpty
    val password: String? = null
)
