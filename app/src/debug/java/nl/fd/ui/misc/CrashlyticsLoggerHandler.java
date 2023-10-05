package nl.fd.ui.misc;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.logging.Handler;

import pl.brightinventions.slf4android.LogRecord;
import pl.brightinventions.slf4android.MessageValueSupplier;

public class CrashlyticsLoggerHandler extends Handler {
    private final MessageValueSupplier messageValueSupplier = new MessageValueSupplier();

    @Override
    public void publish(java.util.logging.LogRecord recordToLog) {
        var logRecord = LogRecord.fromRecord(recordToLog);

        var messageBuilder = new StringBuilder();
        String tag = recordToLog.getLoggerName();
        messageBuilder.append(logRecord.getLogLevel());
        messageBuilder.append("\t");
        messageBuilder.append(tag);
        messageBuilder.append("\t");

        messageValueSupplier.append(logRecord, messageBuilder);

        FirebaseCrashlytics.getInstance().log(messageBuilder.toString());
    }

    @Override
    public void close() {
        //Nothing to close
    }

    @Override
    public void flush() {
        //Nothing to flush
    }
}
