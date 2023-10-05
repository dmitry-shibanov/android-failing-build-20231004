package nl.fd.app;

import android.app.Application;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import nl.fd.ui.misc.CrashlyticsLoggerHandler;
import pl.brightinventions.slf4android.LoggerConfiguration;

@UtilityClass
@Slf4j
class BuildVariantInitializer {

    void initialize() {
        initializeReleaseVariant();
    }

    //So we can spot which version was loaded through the stack-trace
    private void initializeReleaseVariant() {
        log.info("Initializing release build-type of application");
        LoggerConfiguration.configuration()
                .removeRootLogcatHandler()
                .addHandlerToRootLogger(new CrashlyticsLoggerHandler());

        FirebaseCrashlytics.getInstance()
                .setCrashlyticsCollectionEnabled(true);
    }
}
