package com.mindmatrix.jalsanchaytracker.di;

import com.mindmatrix.jalsanchaytracker.database.HarvestDao;
import com.mindmatrix.jalsanchaytracker.database.JalSanchayDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideHarvestDaoFactory implements Factory<HarvestDao> {
  private final Provider<JalSanchayDatabase> databaseProvider;

  public AppModule_ProvideHarvestDaoFactory(Provider<JalSanchayDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public HarvestDao get() {
    return provideHarvestDao(databaseProvider.get());
  }

  public static AppModule_ProvideHarvestDaoFactory create(
      Provider<JalSanchayDatabase> databaseProvider) {
    return new AppModule_ProvideHarvestDaoFactory(databaseProvider);
  }

  public static HarvestDao provideHarvestDao(JalSanchayDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideHarvestDao(database));
  }
}
