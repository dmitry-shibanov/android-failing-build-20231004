package nl.fd.ui.fragment;

import androidx.fragment.app.Fragment;
import io.reactivex.disposables.CompositeDisposable;

public class BaseFragment extends Fragment {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }
}
