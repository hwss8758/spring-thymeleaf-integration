package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.domain.Member
import com.example.springthymeleafintegration.repository.MemberRepository
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.annotation.PostConstruct

@Controller
@RequestMapping("/members")
class MemberController(private val memberRepository: MemberRepository) {

    @GetMapping("/add")
    fun addForm(@ModelAttribute member: Member) = "members/addMemberForm"

    @PostMapping("/add")
    fun save(@Validated @ModelAttribute member: Member, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) return "members/addMemberForm"
        memberRepository.save(member)
        return "redirect:/"
    }

    @PostConstruct
    fun init() {
        memberRepository.save(Member(loginId = "test", password = "test!", name = "테스터"))
    }
}