package com.example.springthymeleafintegration.repository

import com.example.springthymeleafintegration.domain.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {
}