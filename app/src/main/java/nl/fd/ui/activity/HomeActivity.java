package nl.fd.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.core.splashscreen.SplashScreen;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.slf4j.Logger;

import java.util.Objects;
import java.util.Optional;

import nl.fd.R;
import nl.fd.app.FdApplication;
import nl.fd.app.FdDataBindingComponent;
import nl.fd.ui.PageRouter;
import nl.fd.ui.PageUrlParser;
import nl.fd.ui.fragment.HomeFragment;
import nl.fd.ui.richtext.ContextAwareSpansBindingAdapter;

public class HomeActivity extends BaseActivity /*implements NavigationBarView.OnItemSelectedListener, NavigationBarView.OnItemReselectedListener*/ {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(HomeActivity.class);
    private static final String SELECTED_FRAGMENT_KEY = "selectedFragment";
    private static final String HOME_TAG = "home_tag";

    private ContextAwareSpansBindingAdapter contextAwareBindingAdapter;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment activeFragment;

    private boolean needsRefresh;

    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            var handled = false;
            if (!handled) {
                this.setEnabled(false);
                HomeActivity.super.getOnBackPressedDispatcher().onBackPressed();
                this.setEnabled(true);
            }
        }
    };

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, @Nullable Bundle data) {
        if (context == null) {
            return;
        }

        var intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (data != null) {
            intent.putExtras(data);
        }

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Handle the splash screen transition.
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        FdDataBindingComponent bindingComponent = FdApplication.get(HomeActivity.this).component().dataBindingBuilder().build();
        bindingComponent.inject(HomeActivity.this);
        DataBindingUtil.setDefaultComponent(bindingComponent);
        contextAwareBindingAdapter = bindingComponent.getContextAwareSpansBindingAdapter();

        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        setContentView(R.layout.ac_home);

        loadFragment(getOrCreateFragment(HOME_TAG));

        openUrlFromDeepLink(getIntent());
    }

    private Fragment initializeFragment(String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = HomeFragment.newInstance();
        transaction.add(R.id.fragment_container, fragment, tag).hide(fragment);
        transaction.commitNow();
        return fragment;
    }

    @Override
    protected void onDestroy() {
        Optional.ofNullable(contextAwareBindingAdapter)
                .ifPresent(ContextAwareSpansBindingAdapter::onDestroy);
        // clear cached wab pages
        super.onDestroy();
    }

    private boolean selectHomePageTab(Uri uri) {
        loadFragment(getOrCreateFragment(HOME_TAG));
        return true;
    }

    private void openUrlFromDeepLink(Intent intent) {
        var uri = Optional.ofNullable(intent).map(Intent::getData).orElse(null);
        if (uri != null) {
            boolean homePageTabSelected = selectHomePageTab(uri);

            if (!homePageTabSelected) {
                loadFragment(getOrCreateFragment(HOME_TAG));
                if (!openInBrowserIfNotSupported(uri)) {
                    PageRouter.open(this, uri.toString());
                }
            }
        }
    }

    private boolean openInBrowserIfNotSupported(Uri uri) {
        if (PageUrlParser.parse(uri.toString()).getPageType() == PageUrlParser.PageType.EXTERNAL) {
            var browserIntent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER);
            browserIntent.setData(uri);
            startActivity(browserIntent);
            return true;
        }
        return false;
    }

    public static Intent getHomeIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Nullable
    private Fragment getFragment(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    private Fragment getOrCreateFragment(String tag) {
        var fragment = getFragment(tag);
        if (fragment == null) {
            fragment = initializeFragment(tag);
        }
        return fragment;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (activeFragment != null) {
            transaction.hide(activeFragment);
        }
        transaction.show(fragment).commitAllowingStateLoss();
        activeFragment = fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needsRefresh) {
            refreshFragments();
            needsRefresh = false;
        }
    }

    private void refreshFragments() {
        for (Fragment fragment : fragmentManager.getFragments()) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
        fragmentManager.executePendingTransactions();

        loadFragment(getOrCreateFragment(HOME_TAG));
    }
}
