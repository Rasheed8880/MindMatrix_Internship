package com.aipostermaker.data.local

import com.aipostermaker.domain.model.Poster

fun PosterEntity.toDomain(): Poster =
    Poster(
        id = id,
        title = title,
        contentJson = contentJson,
        createdAt = createdAt,
    )

