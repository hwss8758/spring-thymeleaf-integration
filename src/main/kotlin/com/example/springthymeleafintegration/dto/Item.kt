package com.example.springthymeleafintegration.dto

import com.example.springthymeleafintegration.domain.ItemEntity
import com.example.springthymeleafintegration.domain.Region
import com.example.springthymeleafintegration.enumclass.ItemType
import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class Item(
    val id: Long? = null,

    @field:NotBlank
    val itemName: String? = null,

    @field:NotNull
    @field:Range(min = 1000, max = 1000000)
    val price: Int? = null,

    @field:NotNull
    @field:Max(9999)
    val quantity: Int? = null,

    val open: Boolean? = null,

    val itemType: ItemType? = null,

    val deliveryCode: String? = null,

    val regions: MutableList<String> = mutableListOf()
) {
    fun toEntity(): ItemEntity {

        val itemEntity = ItemEntity(
            itemName = itemName,
            price = price,
            quantity = quantity
        ).apply {
            open = open
            itemType = itemType
            deliveryCode = deliveryCode
        }

        regions.forEach {
            val region = Region(region = it)
            itemEntity.addRegion(region)
        }

        return itemEntity
    }

    fun getTotalPrice() = quantity?.let { price?.times(it) }
}
