package com.aipostermaker.data.repository

import com.aipostermaker.data.local.PosterDao
import com.aipostermaker.data.local.PosterEntity
import com.aipostermaker.data.local.toDomain
import com.aipostermaker.domain.model.Poster
import com.aipostermaker.domain.usecase.PosterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PosterRepositoryImpl(
    private val dao: PosterDao,
) : PosterRepository {
    override suspend fun insertPoster(title: String, contentJson: String, createdAt: Long): Long {
        return dao.insertPoster(
            PosterEntity(
                title = title,
                contentJson = contentJson,
                createdAt = createdAt,
            ),
        )
    }

    override fun getAllPosters(): Flow<List<Poster>> {
        return dao.getAllPosters().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun deletePoster(id: Int) {
        dao.deletePoster(id)
    }
}

