package com.example.springthymeleafintegration.dto

import com.example.springthymeleafintegration.domain.ItemEntity
import com.example.springthymeleafintegration.enumclass.ItemType
import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ItemSaveForm(
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
) : ItemForm {
    override fun getTotalPrice() = quantity?.let { price?.times(it) }

    fun toEntity(): ItemEntity {
        val item = Item(
            itemName = itemName,
            price = price,
            quantity = quantity
        )

        return item.toEntity()
    }
}