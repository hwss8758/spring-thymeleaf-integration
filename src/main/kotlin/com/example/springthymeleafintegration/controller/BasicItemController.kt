package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.domain.Item
import com.example.springthymeleafintegration.repository.ItemRepository
import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.annotation.PostConstruct


@Controller
@RequestMapping("/basic/items")
class BasicItemController(private val itemRepository: ItemRepository) {

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
            model.addAttribute("item", item.get())
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

        logger.info("item.open = ${item.open}")

        val savedItem = itemRepository.save(item)

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
            itemToChange.itemName = item.itemName
            itemToChange.price = item.price
            itemToChange.quantity = item.quantity
            itemToChange.open = item.open
            itemToChange.itemType = item.itemType
            itemToChange.deliveryCode = item.deliveryCode
            itemToChange.clearAndAddRegions(item.regions)
        }
        return "redirect:/basic/items/{itemId}"
    }

    @PostConstruct
    fun init() {
        itemRepository.save(Item("itemA", 10000, 10))
        itemRepository.save(Item("itemB", 20000, 20))
    }
}