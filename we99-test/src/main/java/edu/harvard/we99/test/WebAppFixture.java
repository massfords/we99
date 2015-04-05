package edu.harvard.we99.test;

import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * A wrapper around the Jetty server. It's nice to be able to run one or more
 * unit tests from within the IDE or command line w/o having to fire up an
 * external JVM and attach to it.
 *
 * All of the heavy lifting here is done by Jetty. This class just makes it easy
 * to pass in a few config files and it'll assemble the web app and start the
 * server.
 *
 * @author mford
 */
public class WebAppFixture {
    private final Server server;
    private final WebAppContext context;

    public WebAppFixture(int port, File webxml, File webappDir, String contextPath, URL jettyEnvXml, URL webXmlOverride)
            throws Exception {
        if (!webxml.isFile()) {
            throw new IllegalArgumentException("web.xml does not exist:" + webxml.getAbsolutePath());
        }
        if (!webappDir.isDirectory()) {
            throw new IllegalArgumentException("webapp dir does not exist:" + webappDir.getAbsolutePath());
        }
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        server = new Server(port);
        context = new WebAppContext();
        context.setDescriptor(webxml.getAbsolutePath());
        context.setResourceBase(webappDir.getAbsolutePath());
        context.setContextPath(contextPath);
        context.setParentLoaderPriority(true);
        context.setThrowUnavailableOnStartupException(true);

        if (jettyEnvXml != null) {
            EnvConfiguration envConfiguration = new EnvConfiguration();
            envConfiguration.setJettyEnvXml(jettyEnvXml);

            // add it as one of our configurations. This is awkward since I need to instantiate all of the
            // existing configurations.
            List<Configuration> configs = new ArrayList<>();
            configs.add(envConfiguration);
            String[] configClasses = context.getConfigurationClasses();
            if (configClasses == null || configClasses.length == 0) {
                configClasses = new String[]{
                        // need to have these as a minimum or the web app
                        // won't be processed and the servlets won't be added
                        WebInfConfiguration.class.getName(),
                        WebXmlConfiguration.class.getName(),
                        MetaInfConfiguration.class.getName(),
                        FragmentConfiguration.class.getName()
                };
            }
            for (String s : configClasses) {
                configs.add((Configuration) Class.forName(s).newInstance());
            }
            context.setConfigurations(configs.toArray(new Configuration[configs.size()]));
        }

        if (webXmlOverride != null) {
            context.addOverrideDescriptor(webXmlOverride.toURI().toString());
        }
        server.setHandler(context);
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
        server.destroy();
        assertTrue(context.isStopped());
    }
}
