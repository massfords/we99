package edu.harvard.we99.util;

import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * The persistence.xml file for JPA requires a DataSource and this class helps in
 * taking the DataSource (or any other object) and installing it in a JNDI
 * InitialContext so it can be referenced by its path.
 *
 * This allows for the construction of a simple InitialContext for our integration
 * tests.
 *
 * @author mford
 */
public class ContextBinder {
    public ContextBinder(String path, Object object) throws NamingException {
        SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        Context context = new InitialContext();
        try {
            context.bind(path,object);
        } finally {
            context.close();
        }
    }
}
