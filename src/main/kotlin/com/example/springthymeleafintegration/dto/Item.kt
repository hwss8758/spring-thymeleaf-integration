package com.example.springthymeleafintegration.dto

import com.example.springthymeleafintegration.domain.ItemEntity
import com.example.springthymeleafintegration.domain.Region
import com.example.springthymeleafintegration.enumclass.ItemType

data class Item(
    val id: Long? = null,
    val itemName: String? = null,
    val price: Int? = null,
    val quantity: Int? = null,
    val open: Boolean? = null,
    val regions: MutableList<String> = mutableListOf(),
    val itemType: ItemType? = null,
    val deliveryCode: String? = null
) {
    fun toEntity(): ItemEntity {

        val itemEntity = ItemEntity(
            itemName = itemName,
            price = price,
            quantity = quantity
        )

        itemEntity.open = open
        itemEntity.itemType = itemType
        itemEntity.deliveryCode = deliveryCode

        regions.forEach {
            val region = Region(region = it)
            itemEntity.addRegion(region)
        }

        return itemEntity
    }
}
