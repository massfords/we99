package edu.harvard.we99.services;

import edu.harvard.we99.domain.lists.PlateTypes;
import edu.harvard.we99.util.ClientFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author markford
 */
public class PlateTypeST {
    private static PlateTypeService pts;

    @BeforeClass
    public static void init() throws Exception {
        URL url = new URL(WebAppIT.WE99_URL);
        ClientFactory cf = new ClientFactory(url, WebAppIT.WE99_EMAIL, WebAppIT.WE99_PW);

        pts = cf.create(PlateTypeService.class);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        pts = null;
    }

    @Test
    public void listPlateTypes() throws Exception {
        PlateTypes pt = pts.listAll(null, null, null);
        // don't care so much about the actual results, but there should be at
        // least one with a plateCount
        AtomicLong sum = new AtomicLong(0);
        pt.getValues().stream().filter(
                pte->pte.getPlateCount()>0)
                .forEach(
                        pte->sum.addAndGet(pte.getPlateCount()));
        assertTrue(sum.get()>0);
    }

    @Test
    public void typeAhead() throws Exception {
        PlateTypes pt = pts.listAll(null, null, null);
        pt = pts.listAll(null, null, pt.getValues().get(0).getName());
        assertEquals(1, pt.size());
    }
}
