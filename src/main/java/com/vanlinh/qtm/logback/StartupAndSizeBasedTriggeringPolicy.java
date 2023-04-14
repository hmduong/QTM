package com.vanlinh.qtm.logback;

import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;

import java.io.File;

public class StartupAndSizeBasedTriggeringPolicy<E> extends SizeBasedTriggeringPolicy<E>
{
    private boolean policyStarted = false;

    public StartupAndSizeBasedTriggeringPolicy() {
    }

    public boolean isTriggeringEvent(File file, E event) {
        if (!this.policyStarted) {
            this.policyStarted = true;
            if (file.exists() && file.length() > 0L) {
                return true;
            }
        }

        return super.isTriggeringEvent(file, event);
    }
}