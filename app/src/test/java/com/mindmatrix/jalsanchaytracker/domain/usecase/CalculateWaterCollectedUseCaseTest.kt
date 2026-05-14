package com.mindmatrix.jalsanchaytracker.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateWaterCollectedUseCaseTest {
    private val useCase = CalculateWaterCollectedUseCase()

    @Test
    fun calculatesWaterCollectedInLiters() {
        val result = useCase(rainfallMm = 40.0, areaSqm = 80.0, runoffCoefficient = 0.85)
        assertEquals(2720.0, result, 0.001)
    }
}
