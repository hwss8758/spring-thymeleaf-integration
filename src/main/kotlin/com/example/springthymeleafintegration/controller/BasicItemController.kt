package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.domain.ItemEntity
import com.example.springthymeleafintegration.dto.Item
import com.example.springthymeleafintegration.repository.ItemRepository
import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.annotation.PostConstruct


@Controller
@RequestMapping("/basic/items")
class BasicItemController(private val itemRepository: ItemRepository) {

    @ModelAttribute("regions")
    fun regions(): Map<String, String> {

        val regions: LinkedHashMap<String, String> = linkedMapOf()
        regions["SEOUL"] = "서울"
        regions["BUSAN"] = "부산"
        regions["JEJU"] = "제주"

        return regions
    }

    private val logger = KotlinLogging.logger {}

    @GetMapping
    fun items(model: Model): String {
        val items = itemRepository.findAll()
        model.addAttribute("items", items)
        return "basic/items"
    }

    @GetMapping("/{itemId}")
    fun item(@PathVariable itemId: Long, model: Model): String {
        val item = itemRepository.findById(itemId)
        if (item.isPresent) {
            model.addAttribute("item", item.get().toDto())
        }
        return "basic/item"
    }

    @GetMapping("/add")
    fun addForm(model: Model): String {
        model.addAttribute("item", Item())
        return "basic/addForm"
    }

    @PostMapping("/add")
    fun addItemV7Redirect(item: Item, redirectAttributes: RedirectAttributes): String {

        println("item = ${item}")

        val savedItem = itemRepository.save(item.toEntity())

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)

        return "redirect:/basic/items/{itemId}"
    }

    @GetMapping("/{itemId}/edit")
    fun editForm(@PathVariable itemId: Long, model: Model): String {
        val item = itemRepository.findById(itemId)
        if (item.isPresent) {
            model.addAttribute("item", item.get())
        }
        return "basic/editForm"
    }

    @PostMapping("/{itemId}/edit")
    @Transactional
    fun editForm(@PathVariable itemId: Long, @ModelAttribute item: Item): String {
        val foundItem = itemRepository.findById(itemId)
        if (foundItem.isPresent) {
            val itemToChange = foundItem.get()
            val itemEntity = item.toEntity()
            itemToChange.itemName = itemEntity.itemName
            itemToChange.price = itemEntity.price
            itemToChange.quantity = itemEntity.quantity
            itemToChange.open = itemEntity.open
            itemToChange.itemType = itemEntity.itemType
            itemToChange.deliveryCode = itemEntity.deliveryCode
            itemToChange.clearAndAddRegions(itemEntity.regions)
        }
        return "redirect:/basic/items/{itemId}"
    }

    @PostConstruct
    fun init() {
        itemRepository.save(Item(itemName = "itemA", price = 10000, quantity = 10).toEntity())
        itemRepository.save(Item(itemName = "itemB", price = 20000, quantity = 20).toEntity())
    }
}