package com.mindmatrix.jalsanchaytracker.viewmodel;

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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<HarvestRepository> harvestRepositoryProvider;

  public DashboardViewModel_Factory(Provider<HarvestRepository> harvestRepositoryProvider) {
    this.harvestRepositoryProvider = harvestRepositoryProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(harvestRepositoryProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<HarvestRepository> harvestRepositoryProvider) {
    return new DashboardViewModel_Factory(harvestRepositoryProvider);
  }

  public static DashboardViewModel newInstance(HarvestRepository harvestRepository) {
    return new DashboardViewModel(harvestRepository);
  }
}
