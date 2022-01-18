package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.DeliveryCode
import com.example.springthymeleafintegration.dto.Item
import com.example.springthymeleafintegration.enumclass.ItemType
import com.example.springthymeleafintegration.repository.ItemRepository
import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
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

    //    @PostMapping("/add")
    fun addItemV7Redirect(
        @ModelAttribute item: Item,
        redirectAttributes: RedirectAttributes,
        model: Model
    ): String {
        if (validationFields(item, model)) return "basic/addForm"

        val savedItem = itemRepository.save(item.toEntity())

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)

        return "redirect:/basic/items/{itemId}"
    }

    @PostMapping("/add")
    fun addItemUsingBindingResult(
        @ModelAttribute item: Item,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        if (validationUsingBindingResult(item, bindingResult)) return "basic/addForm"

        val savedItem = itemRepository.save(item.toEntity())

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)

        return "redirect:/basic/items/{itemId}"
    }

    private fun validationUsingBindingResult(item: Item, bindingResult: BindingResult): Boolean {
        //검증로직
        if (!StringUtils.hasText(item.itemName)) {
            bindingResult.addError(FieldError("item", "itemName", "상품 이름은 필수입니다."))
        }

        if (item.price == null ||
            item.price < 1000 ||
            item.price > 1000000
        ) {
            bindingResult.addError(FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."))
        }

        if (item.quantity == null || item.quantity >= 9999) {
            bindingResult.addError(FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."))
        }

        // 특정필드가 아닌 복합필드 검증
        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price * item.quantity
            if (resultPrice < 10000) {
                bindingResult.addError(ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = $resultPrice"))
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        return bindingResult.hasErrors()
    }

    private fun validationFields(item: Item, model: Model): Boolean {
        //검증 오류 결과를 보관
        val errors = hashMapOf<String, String>()

        //검증로직
        if (!StringUtils.hasText(item.itemName)) {
            errors["itemName"] = "상품 이름은 필수입니다."
        }

        if (item.price == null ||
            item.price < 1000 ||
            item.price > 1000000
        ) {
            errors["price"] = "가격은 1,000 ~ 1,000,000 까지 허용합니다."
        }

        if (item.quantity == null || item.quantity >= 9999) {
            errors["quantity"] = "수량은 최대 9,999 까지 허용합니다."
        }

        // 특정필드가 아닌 복합필드 검증
        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price * item.quantity
            if (resultPrice < 10000) {
                errors["globalError"] = "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = $resultPrice"
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (errors.isNotEmpty()) {
            model.addAttribute("errors", errors)
            return true
        }

        return false
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