package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.services.io.PlateMapCSVReader;
import edu.harvard.we99.services.storage.CRUDStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Implementation of the PlateTemplateService.
 *
 * @author mford
 */
public class PlateMapServiceImpl extends BaseRESTServiceImpl<PlateMap> implements PlateMapService {

    private static final Logger log = LoggerFactory.getLogger(PlateMapServiceImpl.class);

    public PlateMapServiceImpl(CRUDStorage<PlateMap> storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }

    @Override
    public PlateMap prototype(InputStream is) {
        PlateMapCSVReader reader = new PlateMapCSVReader();
        try (Reader r = new BufferedReader(new InputStreamReader(is))) {
            return reader.read(r);
        } catch (IOException e) {
            log.error("error parsing csv", e);
            throw new WebApplicationException(Response.status(409).build());
        }
    }
}
