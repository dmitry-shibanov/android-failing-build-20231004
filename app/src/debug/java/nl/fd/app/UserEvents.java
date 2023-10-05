package nl.fd.app;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserEvents {

    public void setUser(String userId) {
        FirebaseCrashlytics.getInstance().setUserId(userId);
    }

    public void setKey(String key, String value) {
        FirebaseCrashlytics.getInstance().setCustomKey(key, value);
    }

    public void setKey(String key, int value) {
        FirebaseCrashlytics.getInstance().setCustomKey(key, value);
    }

}
