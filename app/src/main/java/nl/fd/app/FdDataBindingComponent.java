package nl.fd.app;

import androidx.databinding.DataBindingComponent;
import dagger.Subcomponent;
import nl.fd.ui.activity.HomeActivity;
import nl.fd.ui.richtext.ContextAwareSpansBindingAdapter;

@DataBindingScope
@Subcomponent(modules = FdDataBindingModule.class)
public interface FdDataBindingComponent extends DataBindingComponent {

    ContextAwareSpansBindingAdapter getContextAwareSpansBindingAdapter();

    void inject(HomeActivity activity);

    @Subcomponent.Builder
    interface Builder {
        Builder bindingModule(FdDataBindingModule module);

        FdDataBindingComponent build();
    }

}
