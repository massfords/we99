package edu.harvard.we99.security;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter for new user registration to restrict the process to certain email
 * addresses. The pattern could be a regex wildcard or it could restrict emails
 * to a specific domain which might be suitable for a single enterprise.
 *
 * @author mford
 */
public class EmailFilter implements Predicate<String> {

    private final Pattern pattern;

    public EmailFilter(String expression) {
        this.pattern = Pattern.compile(expression);
    }

    @Override
    public boolean test(String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
