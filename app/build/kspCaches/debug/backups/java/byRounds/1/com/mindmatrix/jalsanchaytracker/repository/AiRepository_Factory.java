package com.mindmatrix.jalsanchaytracker.repository;

import com.mindmatrix.jalsanchaytracker.network.GeminiApi;
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
public final class AiRepository_Factory implements Factory<AiRepository> {
  private final Provider<GeminiApi> geminiApiProvider;

  public AiRepository_Factory(Provider<GeminiApi> geminiApiProvider) {
    this.geminiApiProvider = geminiApiProvider;
  }

  @Override
  public AiRepository get() {
    return newInstance(geminiApiProvider.get());
  }

  public static AiRepository_Factory create(Provider<GeminiApi> geminiApiProvider) {
    return new AiRepository_Factory(geminiApiProvider);
  }

  public static AiRepository newInstance(GeminiApi geminiApi) {
    return new AiRepository(geminiApi);
  }
}
