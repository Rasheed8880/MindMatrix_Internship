package com.mindmatrix.jalsanchaytracker.viewmodel;

import com.mindmatrix.jalsanchaytracker.repository.AiRepository;
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
public final class AiViewModel_Factory implements Factory<AiViewModel> {
  private final Provider<AiRepository> aiRepositoryProvider;

  private final Provider<HarvestRepository> harvestRepositoryProvider;

  public AiViewModel_Factory(Provider<AiRepository> aiRepositoryProvider,
      Provider<HarvestRepository> harvestRepositoryProvider) {
    this.aiRepositoryProvider = aiRepositoryProvider;
    this.harvestRepositoryProvider = harvestRepositoryProvider;
  }

  @Override
  public AiViewModel get() {
    return newInstance(aiRepositoryProvider.get(), harvestRepositoryProvider.get());
  }

  public static AiViewModel_Factory create(Provider<AiRepository> aiRepositoryProvider,
      Provider<HarvestRepository> harvestRepositoryProvider) {
    return new AiViewModel_Factory(aiRepositoryProvider, harvestRepositoryProvider);
  }

  public static AiViewModel newInstance(AiRepository aiRepository,
      HarvestRepository harvestRepository) {
    return new AiViewModel(aiRepository, harvestRepository);
  }
}
