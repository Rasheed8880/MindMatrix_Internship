package com.mindmatrix.jalsanchaytracker.viewmodel;

import com.google.firebase.auth.FirebaseAuth;
import com.mindmatrix.jalsanchaytracker.repository.CommunityRepository;
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
public final class CommunityViewModel_Factory implements Factory<CommunityViewModel> {
  private final Provider<CommunityRepository> repositoryProvider;

  private final Provider<FirebaseAuth> authProvider;

  public CommunityViewModel_Factory(Provider<CommunityRepository> repositoryProvider,
      Provider<FirebaseAuth> authProvider) {
    this.repositoryProvider = repositoryProvider;
    this.authProvider = authProvider;
  }

  @Override
  public CommunityViewModel get() {
    return newInstance(repositoryProvider.get(), authProvider.get());
  }

  public static CommunityViewModel_Factory create(Provider<CommunityRepository> repositoryProvider,
      Provider<FirebaseAuth> authProvider) {
    return new CommunityViewModel_Factory(repositoryProvider, authProvider);
  }

  public static CommunityViewModel newInstance(CommunityRepository repository, FirebaseAuth auth) {
    return new CommunityViewModel(repository, auth);
  }
}
