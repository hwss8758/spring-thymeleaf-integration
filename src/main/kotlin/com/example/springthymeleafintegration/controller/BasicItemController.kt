package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.DeliveryCode
import com.example.springthymeleafintegration.dto.Item
import com.example.springthymeleafintegration.dto.ItemForm
import com.example.springthymeleafintegration.dto.ItemSaveForm
import com.example.springthymeleafintegration.dto.ItemUpdateForm
import com.example.springthymeleafintegration.enumclass.ItemType
import com.example.springthymeleafintegration.repository.ItemRepository
import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
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

    private val logger = KotlinLogging.logger {}

    private fun validateObject(
        item: ItemForm,
        bindingResult: BindingResult
    ) {
        val resultPrice = item.getTotalPrice()
        if (resultPrice != null) {
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", arrayOf(10000, resultPrice), null)
            }
        }

    }

    @ModelAttribute("regions")
    fun regions(): Map<String, String> {

        val regions: LinkedHashMap<String, String> = linkedMapOf()
        regions["SEOUL"] = "서울"
        regions["BUSAN"] = "부산"
        regions["JEJU"] = "제주"

        return regions
    }

    @ModelAttribute("itemTypes")
    fun itemTypes() = ItemType.values()

    @ModelAttribute("deliveryCodes")
    fun deliveryCodes(): List<DeliveryCode> {
        return listOf<DeliveryCode>(
            DeliveryCode("FAST", "빠른배송"),
            DeliveryCode("NORMAL", "일반배송"),
            DeliveryCode("SLOW", "느린배송")
        )
    }

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
    fun addItemUsingValidator(
        @Validated @ModelAttribute("item") item: ItemSaveForm,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {

        // 특정필드가 아닌 복합필드 검증
        validateObject(item, bindingResult)

        if (bindingResult.hasErrors()) {
            logger.info { bindingResult }
            return "basic/addForm"
        }

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
    fun editForm(
        @PathVariable itemId: Long,
        @Validated @ModelAttribute("item") item: ItemUpdateForm,
        bindingResult: BindingResult
    ): String {

        // 특정필드가 아닌 복합필드 검증
        validateObject(item, bindingResult)

        if (bindingResult.hasErrors()) {
            logger.info { bindingResult }
            return "basic/editForm"
        }

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