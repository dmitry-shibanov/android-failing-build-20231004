package nl.fd.app;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import nl.fd.ui.activity.BaseActivity;
import nl.fd.ui.activity.HomeActivity;
import nl.fd.ui.fragment.HomeFragment;

@Singleton
@Component(modules = {AppModule.class})
public interface FdComponent {

    FdDataBindingComponent.Builder dataBindingBuilder();

    //Activities
    void inject(BaseActivity activity);

    void inject(HomeFragment homeFragment);

    // Others
    void inject(FdApplication application);

    /**
     * An initializer that creates the graph from an application.
     */
    final class Initializer {
        private Initializer() {
        } // No instances.

        static FdComponent init(Application application) {
            // Don't worry if you see an error here, DaggerCharlyConcertsComponent is generated while building.
            return DaggerFdComponent.builder()
                    .appModule(new AppModule(application))
                    .build();
        }
    }
}
