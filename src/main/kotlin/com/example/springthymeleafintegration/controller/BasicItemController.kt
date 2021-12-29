package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.domain.Item
import com.example.springthymeleafintegration.repository.ItemRepository
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.annotation.PostConstruct


@Controller
@RequestMapping("/basic/items")
class BasicItemController(private val itemRepository: ItemRepository) {

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
    fun addForm(): String = "basic/addForm"

    //    @PostMapping("/add")
    fun addItemV1(
        @RequestParam itemName: String,
        @RequestParam price: Int,
        @RequestParam quantity: Int,
        model: Model
    ): String {
        val item = Item(itemName, price, quantity)
        itemRepository.save(item)
        model.addAttribute("item", item)

        return "basic/item"
    }

    //    @PostMapping("/add")
    fun addItemV2(
        @ModelAttribute("item") item: Item,
        model: Model
    ): String {
        itemRepository.save(item)
        model.addAttribute("item", item) // ModelAttribute 사용 시 자동 추가, 생략 가능
        return "basic/item"
    }

    //    @PostMapping("/add")
    fun addItemV3(@ModelAttribute("item") item: Item): String {
        itemRepository.save(item)
        return "basic/item"
    }

    //    @PostMapping("/add")
    fun addItemV4(@ModelAttribute item: Item): String {
        itemRepository.save(item)
        return "basic/item"
    }

    //    @PostMapping("/add")
    fun addItemV5(item: Item): String {
        itemRepository.save(item)
        return "basic/item"
    }

    //    @PostMapping("/add")
    fun addItemV6Redirect(item: Item): String {
        itemRepository.save(item)
        return "redirect:/basic/items/${item.id}"
    }

    @PostMapping("/add")
    fun addItemV7Redirect(item: Item, redirectAttributes: RedirectAttributes): String {
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
        }
        return "redirect:/basic/items/{itemId}"
    }

    @PostConstruct
    fun init() {
        itemRepository.save(Item("itemA", 10000, 10))
        itemRepository.save(Item("itemB", 20000, 20))
    }
}