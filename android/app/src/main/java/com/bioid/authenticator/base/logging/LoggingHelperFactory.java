package com.bioid.authenticator.base.logging;

import androidx.annotation.NonNull;

import com.example.flutter_app_bioid_integration.BuildConfig;


/**
 * Factory to obtain {@link LoggingHelper} instances.
 */
public final class LoggingHelperFactory {

    /**
     * Creates a new LoggingHelper.
     */
    public static LoggingHelper create(@NonNull Class clazz) {
        if (BuildConfig.DEBUG) {
            return new AndroidLoggingHelper(clazz);
        } else {
            return new NopLoggingHelper();
        }
    }
}
