package nl.fd.app;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = FdDataBindingComponent.class)
class AppModule {
    private final Application application;

    AppModule(Application app) {
        application = app;
    }

    /**
     * Provide ApplicationContext
     */
    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

}
