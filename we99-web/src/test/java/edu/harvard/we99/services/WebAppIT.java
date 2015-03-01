package edu.harvard.we99.services;

import edu.harvard.we99.test.WebAppFixture;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.File;

/**
 * @author mford
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        PlateTemplateST.class,
        CreateAccountServiceST.class,
        ForgotPasswordServiceST.class,
        ManageUserServiceST.class,
        GuestAccessST.class
})
public class WebAppIT {
    public static final String WE99_URL = "http://localhost:8080/we99/services/rest";
    public static final String WE99_EMAIL = "we99.2015@gmail.com";
    public static final String WE99_PW = "pass";

    private static WebAppFixture webAppFixture;

    /**
     * Starts the web app running before any of our tests are started.
     * @throws Exception
     */
    @BeforeClass
    public static void initWebApp() throws Exception {

        for(File file : new File("target").listFiles(pathname -> {
            return pathname.getName().endsWith(".db");
        })) {
            FileUtils.forceDelete(file);
        }


        File testWebXml = new File("src/main/webapp/WEB-INF/web.xml");
        webAppFixture = new WebAppFixture(
                8080,
                testWebXml,
                new File("src/main/webapp"),
                "/we99",
                WebAppIT.class.getResource("/jetty/jetty-env.xml")
        );
        webAppFixture.start();
    }

    /**
     * Stop the webapp when we're done
     * @throws Exception
     */
    @AfterClass
    public static void destroy() throws Exception {
        webAppFixture.stop();
    }

}
