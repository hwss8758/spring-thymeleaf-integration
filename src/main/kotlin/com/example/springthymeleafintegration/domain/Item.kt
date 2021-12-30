package com.example.springthymeleafintegration.domain

import javax.persistence.*

@Entity
@Table
class Item(
    var itemName: String? = null,
    var price: Int? = null,
    var quantity: Int? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
