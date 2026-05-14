package com.aipostermaker.domain.model

data class Poster(
    val id: Int,
    val title: String,
    val contentJson: String,
    val createdAt: Long,
)

