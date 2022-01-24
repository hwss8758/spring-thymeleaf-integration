package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.dto.ItemSaveForm
import mu.KotlinLogging
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ValidationItemApiController {

    private val logger = KotlinLogging.logger {}

    @PostMapping("/validation/api/items/add")
    fun addItem(@RequestBody @Validated form: ItemSaveForm, bindingResult: BindingResult): Any {
        logger.info { "API START" }

        if (bindingResult.hasErrors()) {
            logger.info { "검증 오류 발생 errors = $bindingResult" }
            return bindingResult.allErrors
        }

        logger.info { "API END" }

        return form
    }
}