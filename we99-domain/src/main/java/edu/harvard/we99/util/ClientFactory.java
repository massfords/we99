package edu.harvard.we99.util;

import com.fasterxml.jackson.jaxrs.cfg.Annotations;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;

import javax.ws.rs.core.MediaType;
import java.net.URL;

/**
 * Wrapper around the CXF factory that presents an interface for any REST
 * service deployed at the container targeted by the URL passed in the ctor.
 *
 * @author mford
 */
public class ClientFactory {
    /**
     * URL to the JAXRS container.
     */
    private final URL address;

    public ClientFactory(URL address) {
        this.address = address;
    }

    public <T> T create(Class<T> serviceClass) {
        JAXRSClientFactoryBean factory = new JAXRSClientFactoryBean();
        factory.setAddress(address.toExternalForm());
        factory.setServiceClass(serviceClass);
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider(
                JacksonUtil.getObjectMapper(),
                new Annotations[]{Annotations.JACKSON, Annotations.JAXB});
        factory.setProvider(provider);
        final Client client = factory.create();
        client.type(MediaType.APPLICATION_JSON_TYPE);
        client.accept(MediaType.APPLICATION_JSON_TYPE);
        return serviceClass.cast(client);
    }
}