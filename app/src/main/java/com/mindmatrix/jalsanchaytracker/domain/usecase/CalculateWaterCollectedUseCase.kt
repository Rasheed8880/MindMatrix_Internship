package com.mindmatrix.jalsanchaytracker.domain.usecase

import javax.inject.Inject

class CalculateWaterCollectedUseCase @Inject constructor() {
    operator fun invoke(rainfallMm: Double, areaSqm: Double, runoffCoefficient: Double): Double {
        return rainfallMm * areaSqm * runoffCoefficient
    }
}
