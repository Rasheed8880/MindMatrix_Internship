package com.mindmatrix.jalsanchaytracker.repository;

import com.mindmatrix.jalsanchaytracker.database.HarvestDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class HarvestRepository_Factory implements Factory<HarvestRepository> {
  private final Provider<HarvestDao> harvestDaoProvider;

  public HarvestRepository_Factory(Provider<HarvestDao> harvestDaoProvider) {
    this.harvestDaoProvider = harvestDaoProvider;
  }

  @Override
  public HarvestRepository get() {
    return newInstance(harvestDaoProvider.get());
  }

  public static HarvestRepository_Factory create(Provider<HarvestDao> harvestDaoProvider) {
    return new HarvestRepository_Factory(harvestDaoProvider);
  }

  public static HarvestRepository newInstance(HarvestDao harvestDao) {
    return new HarvestRepository(harvestDao);
  }
}
