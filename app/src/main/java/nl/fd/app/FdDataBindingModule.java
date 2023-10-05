package nl.fd.app;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import nl.fd.ui.richtext.ContextAwareSpansBindingAdapter;

@Module
public class FdDataBindingModule {

    @DataBindingScope
    @Provides
    ContextAwareSpansBindingAdapter provideContextAwareSpansBindingAdapter() {
        return new ContextAwareSpansBindingAdapter();
    }

}
