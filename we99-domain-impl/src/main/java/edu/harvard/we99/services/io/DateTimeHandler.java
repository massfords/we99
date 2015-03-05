package edu.harvard.we99.services.io;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;
import org.joda.time.DateTime;

/**
 * @author mford
 */
public class DateTimeHandler implements TypeHandler {

    @Override
    public Object parse(String text) throws TypeConversionException {
        return new DateTime(text);
    }

    @Override
    public String format(Object value) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Class<?> getType() {
        return DateTime.class;
    }
}
