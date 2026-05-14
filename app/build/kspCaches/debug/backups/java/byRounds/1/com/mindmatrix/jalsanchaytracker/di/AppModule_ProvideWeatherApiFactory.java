package com.mindmatrix.jalsanchaytracker.di;

import com.mindmatrix.jalsanchaytracker.network.WeatherApi;
import com.squareup.moshi.Moshi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

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
public final class AppModule_ProvideWeatherApiFactory implements Factory<WeatherApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  private final Provider<Moshi> moshiProvider;

  public AppModule_ProvideWeatherApiFactory(Provider<OkHttpClient> okHttpClientProvider,
      Provider<Moshi> moshiProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
    this.moshiProvider = moshiProvider;
  }

  @Override
  public WeatherApi get() {
    return provideWeatherApi(okHttpClientProvider.get(), moshiProvider.get());
  }

  public static AppModule_ProvideWeatherApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider, Provider<Moshi> moshiProvider) {
    return new AppModule_ProvideWeatherApiFactory(okHttpClientProvider, moshiProvider);
  }

  public static WeatherApi provideWeatherApi(OkHttpClient okHttpClient, Moshi moshi) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideWeatherApi(okHttpClient, moshi));
  }
}
