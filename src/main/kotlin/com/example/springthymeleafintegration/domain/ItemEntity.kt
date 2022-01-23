package com.example.springthymeleafintegration.domain

import com.example.springthymeleafintegration.dto.Item
import com.example.springthymeleafintegration.enumclass.ItemType
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "Item")
class ItemEntity(
    var itemName: String? = null,
    var price: Int? = null,
    var quantity: Int? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var open: Boolean? = null

    var itemType: ItemType? = null

    var deliveryCode: String? = null

    @OneToMany(mappedBy = "item", cascade = [CascadeType.ALL])
    val regions: MutableList<Region> = mutableListOf()


    constructor(itemName: String?, price: Int?, quantity: Int?, open: Boolean?) : this(itemName, price, quantity) {
        this.open = open
    }

    fun clearAndAddRegions(_regions: List<Region>) {
        regions.clear()
        _regions.forEach { addRegion(it) }
    }

    fun addRegion(region: Region) {
        regions.add(region)
        region.item = this
    }

    override fun toString(): String {
        return "Item(itemName=$itemName, price=$price, quantity=$quantity, id=$id, open=$open, regions=$regions, itemType=$itemType, deliveryCode=$deliveryCode)"
    }

    fun toDto(): Item {
        val item = Item(
            id = id,
            itemName = itemName,
            price = price,
            quantity = quantity,
            open = open,
            itemType = itemType,
            deliveryCode = deliveryCode
        )

        regions.map(Region::region).toList().forEach {
            item.regions.add(it)
        }

        return item
    }

}
