package com.example.springthymeleafintegration.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotEmpty

@Entity
class Member(

    @field:NotEmpty
    val loginId: String? = null,

    @field:NotEmpty
    val name: String? = null,

    @field:NotEmpty
    val password: String? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun checkPassword(_password: String) = password == _password

    override fun toString(): String {
        return "Member(loginId=$loginId, name=$name, password=$password, id=$id)"
    }
}