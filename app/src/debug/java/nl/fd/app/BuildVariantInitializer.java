package nl.fd.app;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.Arrays;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import nl.fd.BuildConfig;
import nl.fd.ui.misc.CrashlyticsLoggerHandler;
import pl.brightinventions.slf4android.LoggerConfiguration;

@UtilityClass
@Slf4j
class BuildVariantInitializer {

    void initialize() {
        initializeDebugVariant();
    }

    //So we can spot which version was loaded through the stack-trace
    private void initializeDebugVariant() {
        log.info("Initializing debug build-type of application");

        if (BuildConfig.TEST_DEVICE_IDS.length > 0) {
            MobileAds.setRequestConfiguration(
                    new RequestConfiguration.Builder()
                            .setTestDeviceIds(Arrays.asList(BuildConfig.TEST_DEVICE_IDS))
                            .build()
            );
        }

        LoggerConfiguration.configuration()
                .addHandlerToRootLogger(new CrashlyticsLoggerHandler());
        FirebaseCrashlytics.getInstance()
                .setCrashlyticsCollectionEnabled(true);
    }

}
