package com.example.springthymeleafintegration.domain

import com.example.springthymeleafintegration.enumclass.ItemType
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

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

    var open: Boolean? = null

    @OneToMany(mappedBy = "item", cascade = [CascadeType.ALL])
    val regions: MutableList<Region> = mutableListOf()

    var itemType: ItemType? = null

    var deliveryCode: String? = null

    fun addRegion(region: Region) {
        regions.add(region)
        region.item = this
    }
}
