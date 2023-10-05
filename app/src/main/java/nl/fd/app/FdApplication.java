package nl.fd.app;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDexApplication;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FdApplication extends MultiDexApplication implements LifecycleObserver {

    private boolean resumeTimers;

    private FdComponent componentGraph;
    private WebView webView;

    private CompositeDisposable applicationScopedDisposables;

    private final ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(@NonNull Activity activity, Bundle bundle) {
            applicationScopedDisposables = new CompositeDisposable();

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            //not used
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            if (resumeTimers) {
                //resume all layout, parsing, and JavaScript timers for all WebViews
                resumeTimers = false;
                webView.resumeTimers();
            }

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            //not used
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            //not used
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
            //not used
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            applicationScopedDisposables.clear();
        }

    };

    /**
     * Static method for getting a reference to the application instance itself.
     *
     * @param context The activity context.
     * @return The application reference.
     */
    public static FdApplication get(Context context) {
        return (FdApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        if ((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView = new WebView(FdApplication.this);

        webView.getSettings().setSafeBrowsingEnabled(false);

        //WebView don't store cookies
        var cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        BuildVariantInitializer.initialize();

        // Build the Dagger 2 component and execute all injections
        componentGraph = FdComponent.Initializer.init(FdApplication.this);
        component().inject(FdApplication.this);

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    /**
     * Used to detect when app goes to the background
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level >= TRIM_MEMORY_UI_HIDDEN) {

            //pause all layout, parsing, and JavaScript timers for all WebViews
            resumeTimers = true;
            webView.pauseTimers();
        }
    }

    /**
     * Returns the dependency component, which provides the API for injecting into objects.
     *
     * @return The Dagger dependency management component.
     */
    public FdComponent component() {
        return componentGraph;
    }
}
