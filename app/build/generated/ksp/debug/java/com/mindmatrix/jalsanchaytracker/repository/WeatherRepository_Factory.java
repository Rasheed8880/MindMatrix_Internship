package com.mindmatrix.jalsanchaytracker.repository;

import com.mindmatrix.jalsanchaytracker.network.WeatherApi;
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
public final class WeatherRepository_Factory implements Factory<WeatherRepository> {
  private final Provider<WeatherApi> weatherApiProvider;

  public WeatherRepository_Factory(Provider<WeatherApi> weatherApiProvider) {
    this.weatherApiProvider = weatherApiProvider;
  }

  @Override
  public WeatherRepository get() {
    return newInstance(weatherApiProvider.get());
  }

  public static WeatherRepository_Factory create(Provider<WeatherApi> weatherApiProvider) {
    return new WeatherRepository_Factory(weatherApiProvider);
  }

  public static WeatherRepository newInstance(WeatherApi weatherApi) {
    return new WeatherRepository(weatherApi);
  }
}
