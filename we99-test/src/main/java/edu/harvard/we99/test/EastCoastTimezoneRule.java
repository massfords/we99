package edu.harvard.we99.test;

import org.joda.time.DateTimeZone;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.TimeZone;

/**
 * @author mford
 */
public class EastCoastTimezoneRule extends TestWatcher {
    private final DateTimeZone tz;
    private final DateTimeZone original;

    public EastCoastTimezoneRule() {
        this.tz = DateTimeZone.forID("US/Eastern");
        this.original = DateTimeZone.getDefault();
    }

    public void install() {
        DateTimeZone.setDefault(tz);
        TimeZone.setDefault(tz.toTimeZone());
    }

    public void restore() {
        DateTimeZone.setDefault(original);
        TimeZone.setDefault(original.toTimeZone());
    }

    @Override
    protected void starting(Description description) {
        install();
    }

    @Override
    protected void finished(Description description) {
        restore();
    }
}
