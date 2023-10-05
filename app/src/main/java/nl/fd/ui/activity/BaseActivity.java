package nl.fd.ui.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.window.layout.WindowMetricsCalculator;

import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import lombok.extern.slf4j.Slf4j;
import nl.fd.app.FdApplication;

@Slf4j
public abstract class BaseActivity extends AppCompatActivity {

    public static final int MIN_LANDSCAPE_HEIGHT = 600;

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FdApplication.get(BaseActivity.this).component().inject(BaseActivity.this);

        compositeDisposable = new CompositeDisposable();

        setupAllowedOrientation();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    protected void setupAllowedOrientation() {
        var windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this);
        final float heightDp = windowMetrics.getBounds().height()/((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        final float widthDp = windowMetrics.getBounds().width()/((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        if (Math.min(widthDp, heightDp) < MIN_LANDSCAPE_HEIGHT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                var outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    Optional.ofNullable(getSystemService(Context.INPUT_METHOD_SERVICE))
                            .map(InputMethodManager.class::cast)
                            .ifPresent(imm -> imm.hideSoftInputFromWindow(v.getWindowToken(), 0));
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}
