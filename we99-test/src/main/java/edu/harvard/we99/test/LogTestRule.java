package edu.harvard.we99.test;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Adds sysouts before and after a test runs. This is designed to be used in
 * Suite Tests where it's difficult to associate the output from the log
 * with the test that's running. Case in point, the WebAppIT test has lots of
 * output and it's hard to see which statements go with which tests.
 *
 * @author markford
 */
public class LogTestRule extends TestWatcher {
    @Override
    protected void starting(Description description) {
        System.out.println("----------------------------------------------------");
        System.out.format("Starting %s.%s\n", description.getClassName(), description.getMethodName());
        System.out.println("----------------------------------------------------");
    }

    @Override
    protected void finished(Description description) {
        System.out.println("----------------------------------------------------");
        System.out.format("Ending %s.%s\n", description.getClassName(), description.getMethodName());
        System.out.println("----------------------------------------------------");
    }
}
