package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.dto.Item
import com.example.springthymeleafintegration.repository.ItemRepository
import mu.KotlinLogging
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Deprecated("삭제된 로직 모음")
class DeprecatedController(private val itemRepository: ItemRepository) {

    private val logger = KotlinLogging.logger {}

    //    @PostMapping("/add")
    @Deprecated("삭제된 함수")
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

    //    @PostMapping("/add")
    @Deprecated("삭제된 함수")
    fun addItemUsingBindingResult(
        @ModelAttribute item: Item,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        if (validationUsingBindingResultV4(item, bindingResult)) return "basic/addForm"

        val savedItem = itemRepository.save(item.toEntity())

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)

        return "redirect:/basic/items/{itemId}"
    }

    @Deprecated("삭제된 함수")
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

    @Deprecated("삭제된 함수")
    private fun validationUsingBindingResultV2(item: Item, bindingResult: BindingResult): Boolean {
        //검증로직
        if (!StringUtils.hasText(item.itemName)) {
            bindingResult.addError(FieldError("item", "itemName", item.itemName, false, null, null, "상품 이름은 필수입니다."))
        }

        if (item.price == null ||
            item.price < 1000 ||
            item.price > 1000000
        ) {
            bindingResult.addError(
                FieldError(
                    "item",
                    "price",
                    item.price,
                    false,
                    null,
                    null,
                    "가격은 1,000 ~ 1,000,000 까지 허용합니다."
                )
            )
        }

        if (item.quantity == null || item.quantity >= 9999) {
            bindingResult.addError(
                FieldError(
                    "item",
                    "quantity",
                    item.quantity,
                    false,
                    null,
                    null,
                    "수량은 최대 9,999 까지 허용합니다."
                )
            )
        }

        // 특정필드가 아닌 복합필드 검증
        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price * item.quantity
            if (resultPrice < 10000) {
                bindingResult.addError(
                    ObjectError(
                        "item",
                        null,
                        null,
                        "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = $resultPrice"
                    )
                )
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        return bindingResult.hasErrors()
    }

    @Deprecated("삭제된 함수")
    private fun validationUsingBindingResultV3(item: Item, bindingResult: BindingResult): Boolean {
        //검증로직
        if (!StringUtils.hasText(item.itemName)) {
            bindingResult.addError(
                FieldError(
                    "item",
                    "itemName",
                    item.itemName,
                    false,
                    arrayOf("required.item.itemName"),
                    null,
                    null
                )
            )
        }

        if (item.price == null ||
            item.price < 1000 ||
            item.price > 1000000
        ) {
            bindingResult.addError(
                FieldError(
                    "item",
                    "price",
                    item.price,
                    false,
                    arrayOf("range.item.price"),
                    arrayOf(1000, 1000000),
                    null
                )
            )
        }

        if (item.quantity == null || item.quantity >= 9999) {
            bindingResult.addError(
                FieldError(
                    "item",
                    "quantity",
                    item.quantity,
                    false,
                    arrayOf("max.item.quantity"),
                    arrayOf(9999),
                    "수량은 최대 9,999 까지 허용합니다."
                )
            )
        }

        // 특정필드가 아닌 복합필드 검증
        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price * item.quantity
            if (resultPrice < 10000) {
                bindingResult.addError(
                    ObjectError(
                        "item",
                        arrayOf("totalPriceMin"),
                        arrayOf(10000, resultPrice),
                        null
                    )
                )
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        return bindingResult.hasErrors()
    }

    @Deprecated("삭제된 함수")
    private fun validationUsingBindingResultV4(item: Item, bindingResult: BindingResult): Boolean {
        //검증로직
        if (!StringUtils.hasText(item.itemName)) {
            bindingResult.rejectValue("itemName", "required")
        }

        if (item.price == null ||
            item.price < 1000 ||
            item.price > 1000000
        ) {
            bindingResult.rejectValue("price", "range", arrayOf(1000, 1000000), null)
        }

        if (item.quantity == null || item.quantity >= 9999) {
            bindingResult.rejectValue("quantity", "max", arrayOf(9999), null)
        }

        // 특정필드가 아닌 복합필드 검증
        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price * item.quantity
            if (resultPrice < 10000) {
                bindingResult.reject(
                    "totalPriceMin",
                    arrayOf(10000, resultPrice),
                    null
                )
            }
        }

        logger.info { bindingResult }

        //검증에 실패하면 다시 입력 폼으로
        return bindingResult.hasErrors()
    }

    @Deprecated("삭제된 함수")
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
}