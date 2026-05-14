package com.mindmatrix.jalsanchaytracker;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mindmatrix.jalsanchaytracker.database.HarvestDao;
import com.mindmatrix.jalsanchaytracker.database.JalSanchayDatabase;
import com.mindmatrix.jalsanchaytracker.di.AppModule_ProvideDatabaseFactory;
import com.mindmatrix.jalsanchaytracker.di.AppModule_ProvideFirebaseAuthFactory;
import com.mindmatrix.jalsanchaytracker.di.AppModule_ProvideFirestoreFactory;
import com.mindmatrix.jalsanchaytracker.di.AppModule_ProvideGeminiApiFactory;
import com.mindmatrix.jalsanchaytracker.di.AppModule_ProvideHarvestDaoFactory;
import com.mindmatrix.jalsanchaytracker.di.AppModule_ProvideMoshiFactory;
import com.mindmatrix.jalsanchaytracker.di.AppModule_ProvideOkHttpFactory;
import com.mindmatrix.jalsanchaytracker.domain.usecase.CalculateWaterCollectedUseCase;
import com.mindmatrix.jalsanchaytracker.network.GeminiApi;
import com.mindmatrix.jalsanchaytracker.repository.AiRepository;
import com.mindmatrix.jalsanchaytracker.repository.AuthRepository;
import com.mindmatrix.jalsanchaytracker.repository.CommunityRepository;
import com.mindmatrix.jalsanchaytracker.repository.HarvestRepository;
import com.mindmatrix.jalsanchaytracker.viewmodel.AiViewModel;
import com.mindmatrix.jalsanchaytracker.viewmodel.AiViewModel_HiltModules;
import com.mindmatrix.jalsanchaytracker.viewmodel.AuthViewModel;
import com.mindmatrix.jalsanchaytracker.viewmodel.AuthViewModel_HiltModules;
import com.mindmatrix.jalsanchaytracker.viewmodel.CommunityViewModel;
import com.mindmatrix.jalsanchaytracker.viewmodel.CommunityViewModel_HiltModules;
import com.mindmatrix.jalsanchaytracker.viewmodel.DashboardViewModel;
import com.mindmatrix.jalsanchaytracker.viewmodel.DashboardViewModel_HiltModules;
import com.mindmatrix.jalsanchaytracker.viewmodel.EntryViewModel;
import com.mindmatrix.jalsanchaytracker.viewmodel.EntryViewModel_HiltModules;
import com.squareup.moshi.Moshi;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

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
public final class DaggerJalSanchayApp_HiltComponents_SingletonC {
  private DaggerJalSanchayApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public JalSanchayApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements JalSanchayApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public JalSanchayApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements JalSanchayApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public JalSanchayApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements JalSanchayApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public JalSanchayApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements JalSanchayApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public JalSanchayApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements JalSanchayApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public JalSanchayApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements JalSanchayApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public JalSanchayApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements JalSanchayApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public JalSanchayApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends JalSanchayApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends JalSanchayApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends JalSanchayApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends JalSanchayApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>of(LazyClassKeyProvider.com_mindmatrix_jalsanchaytracker_viewmodel_AiViewModel, AiViewModel_HiltModules.KeyModule.provide(), LazyClassKeyProvider.com_mindmatrix_jalsanchaytracker_viewmodel_AuthViewModel, AuthViewModel_HiltModules.KeyModule.provide(), LazyClassKeyProvider.com_mindmatrix_jalsanchaytracker_viewmodel_CommunityViewModel, CommunityViewModel_HiltModules.KeyModule.provide(), LazyClassKeyProvider.com_mindmatrix_jalsanchaytracker_viewmodel_DashboardViewModel, DashboardViewModel_HiltModules.KeyModule.provide(), LazyClassKeyProvider.com_mindmatrix_jalsanchaytracker_viewmodel_EntryViewModel, EntryViewModel_HiltModules.KeyModule.provide()));
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_mindmatrix_jalsanchaytracker_viewmodel_EntryViewModel = "com.mindmatrix.jalsanchaytracker.viewmodel.EntryViewModel";

      static String com_mindmatrix_jalsanchaytracker_viewmodel_AuthViewModel = "com.mindmatrix.jalsanchaytracker.viewmodel.AuthViewModel";

      static String com_mindmatrix_jalsanchaytracker_viewmodel_AiViewModel = "com.mindmatrix.jalsanchaytracker.viewmodel.AiViewModel";

      static String com_mindmatrix_jalsanchaytracker_viewmodel_CommunityViewModel = "com.mindmatrix.jalsanchaytracker.viewmodel.CommunityViewModel";

      static String com_mindmatrix_jalsanchaytracker_viewmodel_DashboardViewModel = "com.mindmatrix.jalsanchaytracker.viewmodel.DashboardViewModel";

      @KeepFieldType
      EntryViewModel com_mindmatrix_jalsanchaytracker_viewmodel_EntryViewModel2;

      @KeepFieldType
      AuthViewModel com_mindmatrix_jalsanchaytracker_viewmodel_AuthViewModel2;

      @KeepFieldType
      AiViewModel com_mindmatrix_jalsanchaytracker_viewmodel_AiViewModel2;

      @KeepFieldType
      CommunityViewModel com_mindmatrix_jalsanchaytracker_viewmodel_CommunityViewModel2;

      @KeepFieldType
      DashboardViewModel com_mindmatrix_jalsanchaytracker_viewmodel_DashboardViewModel2;
    }
  }

  private static final class ViewModelCImpl extends JalSanchayApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AiViewModel> aiViewModelProvider;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<CommunityViewModel> communityViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<EntryViewModel> entryViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.aiViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.communityViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.entryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>of(LazyClassKeyProvider.com_mindmatrix_jalsanchaytracker_viewmodel_AiViewModel, ((Provider) aiViewModelProvider), LazyClassKeyProvider.com_mindmatrix_jalsanchaytracker_viewmodel_AuthViewModel, ((Provider) authViewModelProvider), LazyClassKeyProvider.com_mindmatrix_jalsanchaytracker_viewmodel_CommunityViewModel, ((Provider) communityViewModelProvider), LazyClassKeyProvider.com_mindmatrix_jalsanchaytracker_viewmodel_DashboardViewModel, ((Provider) dashboardViewModelProvider), LazyClassKeyProvider.com_mindmatrix_jalsanchaytracker_viewmodel_EntryViewModel, ((Provider) entryViewModelProvider)));
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_mindmatrix_jalsanchaytracker_viewmodel_AiViewModel = "com.mindmatrix.jalsanchaytracker.viewmodel.AiViewModel";

      static String com_mindmatrix_jalsanchaytracker_viewmodel_CommunityViewModel = "com.mindmatrix.jalsanchaytracker.viewmodel.CommunityViewModel";

      static String com_mindmatrix_jalsanchaytracker_viewmodel_EntryViewModel = "com.mindmatrix.jalsanchaytracker.viewmodel.EntryViewModel";

      static String com_mindmatrix_jalsanchaytracker_viewmodel_AuthViewModel = "com.mindmatrix.jalsanchaytracker.viewmodel.AuthViewModel";

      static String com_mindmatrix_jalsanchaytracker_viewmodel_DashboardViewModel = "com.mindmatrix.jalsanchaytracker.viewmodel.DashboardViewModel";

      @KeepFieldType
      AiViewModel com_mindmatrix_jalsanchaytracker_viewmodel_AiViewModel2;

      @KeepFieldType
      CommunityViewModel com_mindmatrix_jalsanchaytracker_viewmodel_CommunityViewModel2;

      @KeepFieldType
      EntryViewModel com_mindmatrix_jalsanchaytracker_viewmodel_EntryViewModel2;

      @KeepFieldType
      AuthViewModel com_mindmatrix_jalsanchaytracker_viewmodel_AuthViewModel2;

      @KeepFieldType
      DashboardViewModel com_mindmatrix_jalsanchaytracker_viewmodel_DashboardViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.mindmatrix.jalsanchaytracker.viewmodel.AiViewModel 
          return (T) new AiViewModel(singletonCImpl.aiRepositoryProvider.get(), singletonCImpl.harvestRepositoryProvider.get());

          case 1: // com.mindmatrix.jalsanchaytracker.viewmodel.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.authRepositoryProvider.get());

          case 2: // com.mindmatrix.jalsanchaytracker.viewmodel.CommunityViewModel 
          return (T) new CommunityViewModel(singletonCImpl.communityRepositoryProvider.get(), singletonCImpl.provideFirebaseAuthProvider.get());

          case 3: // com.mindmatrix.jalsanchaytracker.viewmodel.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.harvestRepositoryProvider.get());

          case 4: // com.mindmatrix.jalsanchaytracker.viewmodel.EntryViewModel 
          return (T) new EntryViewModel(singletonCImpl.harvestRepositoryProvider.get(), new CalculateWaterCollectedUseCase());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends JalSanchayApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends JalSanchayApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends JalSanchayApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<OkHttpClient> provideOkHttpProvider;

    private Provider<Moshi> provideMoshiProvider;

    private Provider<GeminiApi> provideGeminiApiProvider;

    private Provider<AiRepository> aiRepositoryProvider;

    private Provider<JalSanchayDatabase> provideDatabaseProvider;

    private Provider<HarvestRepository> harvestRepositoryProvider;

    private Provider<FirebaseAuth> provideFirebaseAuthProvider;

    private Provider<FirebaseFirestore> provideFirestoreProvider;

    private Provider<AuthRepository> authRepositoryProvider;

    private Provider<CommunityRepository> communityRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private HarvestDao harvestDao() {
      return AppModule_ProvideHarvestDaoFactory.provideHarvestDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideOkHttpProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 2));
      this.provideMoshiProvider = DoubleCheck.provider(new SwitchingProvider<Moshi>(singletonCImpl, 3));
      this.provideGeminiApiProvider = DoubleCheck.provider(new SwitchingProvider<GeminiApi>(singletonCImpl, 1));
      this.aiRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AiRepository>(singletonCImpl, 0));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<JalSanchayDatabase>(singletonCImpl, 5));
      this.harvestRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<HarvestRepository>(singletonCImpl, 4));
      this.provideFirebaseAuthProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseAuth>(singletonCImpl, 7));
      this.provideFirestoreProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseFirestore>(singletonCImpl, 8));
      this.authRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 6));
      this.communityRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<CommunityRepository>(singletonCImpl, 9));
    }

    @Override
    public void injectJalSanchayApp(JalSanchayApp jalSanchayApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.mindmatrix.jalsanchaytracker.repository.AiRepository 
          return (T) new AiRepository(singletonCImpl.provideGeminiApiProvider.get());

          case 1: // com.mindmatrix.jalsanchaytracker.network.GeminiApi 
          return (T) AppModule_ProvideGeminiApiFactory.provideGeminiApi(singletonCImpl.provideOkHttpProvider.get(), singletonCImpl.provideMoshiProvider.get());

          case 2: // okhttp3.OkHttpClient 
          return (T) AppModule_ProvideOkHttpFactory.provideOkHttp();

          case 3: // com.squareup.moshi.Moshi 
          return (T) AppModule_ProvideMoshiFactory.provideMoshi();

          case 4: // com.mindmatrix.jalsanchaytracker.repository.HarvestRepository 
          return (T) new HarvestRepository(singletonCImpl.harvestDao());

          case 5: // com.mindmatrix.jalsanchaytracker.database.JalSanchayDatabase 
          return (T) AppModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 6: // com.mindmatrix.jalsanchaytracker.repository.AuthRepository 
          return (T) new AuthRepository(singletonCImpl.provideFirebaseAuthProvider.get(), singletonCImpl.provideFirestoreProvider.get());

          case 7: // com.google.firebase.auth.FirebaseAuth 
          return (T) AppModule_ProvideFirebaseAuthFactory.provideFirebaseAuth();

          case 8: // com.google.firebase.firestore.FirebaseFirestore 
          return (T) AppModule_ProvideFirestoreFactory.provideFirestore();

          case 9: // com.mindmatrix.jalsanchaytracker.repository.CommunityRepository 
          return (T) new CommunityRepository(singletonCImpl.provideFirestoreProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
