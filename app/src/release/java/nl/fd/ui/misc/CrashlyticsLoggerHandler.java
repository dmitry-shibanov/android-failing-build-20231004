package nl.fd.ui.misc;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.logging.Handler;

import pl.brightinventions.slf4android.LogRecord;
import pl.brightinventions.slf4android.MessageValueSupplier;

public class CrashlyticsLoggerHandler extends Handler {
    private MessageValueSupplier messageValueSupplier = new MessageValueSupplier();

    @Override
    public void publish(java.util.logging.LogRecord record) {
        LogRecord logRecord = LogRecord.fromRecord(record);

        StringBuilder messageBuilder = new StringBuilder();
        String tag = record.getLoggerName();
        messageBuilder.append(logRecord.getLogLevel());
        messageBuilder.append("\t");
        messageBuilder.append(tag);
        messageBuilder.append("\t");

        messageValueSupplier.append(logRecord, messageBuilder);

        FirebaseCrashlytics.getInstance().log(messageBuilder.toString());
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }
}
