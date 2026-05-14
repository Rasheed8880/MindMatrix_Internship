package com.mindmatrix.jalsanchaytracker.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class CalculateWaterCollectedUseCase_Factory implements Factory<CalculateWaterCollectedUseCase> {
  @Override
  public CalculateWaterCollectedUseCase get() {
    return newInstance();
  }

  public static CalculateWaterCollectedUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CalculateWaterCollectedUseCase newInstance() {
    return new CalculateWaterCollectedUseCase();
  }

  private static final class InstanceHolder {
    private static final CalculateWaterCollectedUseCase_Factory INSTANCE = new CalculateWaterCollectedUseCase_Factory();
  }
}
