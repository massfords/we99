package edu.harvard.we99.domain;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Populates the database with some initial data. We'll only execute this if
 * we just created the database which is a property available on the
 * inspector passed in.
 *
 * @author mford
 */
public class DbPopulator {
    public DbPopulator(EntityManagerFactory emf,
                       DbVersionInspector inspector) throws Exception {
        if (!inspector.isDbInitRequired()) {
            // no sample data required, leave now
            return;
        }

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            StreamFactory sf = StreamFactory.newInstance();
            sf.loadResource("sample-data/import.xml");

            loadData(em, sf, "/sample-data/plate-types.csv", "plateTypes");
            loadData(em, sf, "/sample-data/compounds.csv", "compounds");
            loadData(em, sf, "/sample-data/protocols.csv", "protocols");

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    private void loadData(EntityManager em, StreamFactory sf, String csvFile, String streamName) throws IOException {
        try (Reader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(csvFile)))) {
            BeanReader br = sf.createReader(streamName, r);
            Object entity;
            while ((entity = br.read()) != null) {
                em.persist(entity);
            }
        }
    }
}
