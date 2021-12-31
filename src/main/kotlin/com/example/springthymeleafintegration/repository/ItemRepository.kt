package com.example.springthymeleafintegration.repository

import com.example.springthymeleafintegration.domain.ItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<ItemEntity, Long> {
}