package com.mindmatrix.jalsanchaytracker.viewmodel;

import com.mindmatrix.jalsanchaytracker.domain.usecase.CalculateWaterCollectedUseCase;
import com.mindmatrix.jalsanchaytracker.repository.HarvestRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class EntryViewModel_Factory implements Factory<EntryViewModel> {
  private final Provider<HarvestRepository> harvestRepositoryProvider;

  private final Provider<CalculateWaterCollectedUseCase> calculateWaterCollectedProvider;

  public EntryViewModel_Factory(Provider<HarvestRepository> harvestRepositoryProvider,
      Provider<CalculateWaterCollectedUseCase> calculateWaterCollectedProvider) {
    this.harvestRepositoryProvider = harvestRepositoryProvider;
    this.calculateWaterCollectedProvider = calculateWaterCollectedProvider;
  }

  @Override
  public EntryViewModel get() {
    return newInstance(harvestRepositoryProvider.get(), calculateWaterCollectedProvider.get());
  }

  public static EntryViewModel_Factory create(Provider<HarvestRepository> harvestRepositoryProvider,
      Provider<CalculateWaterCollectedUseCase> calculateWaterCollectedProvider) {
    return new EntryViewModel_Factory(harvestRepositoryProvider, calculateWaterCollectedProvider);
  }

  public static EntryViewModel newInstance(HarvestRepository harvestRepository,
      CalculateWaterCollectedUseCase calculateWaterCollected) {
    return new EntryViewModel(harvestRepository, calculateWaterCollected);
  }
}
