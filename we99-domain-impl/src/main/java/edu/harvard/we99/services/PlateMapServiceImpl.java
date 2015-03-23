package edu.harvard.we99.services;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.ImportedPlateMap;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.lists.PlateMaps;
import edu.harvard.we99.domain.lists.PlateTypes;
import edu.harvard.we99.services.io.PlateMapCSVReader;
import edu.harvard.we99.services.storage.PlateMapStorage;
import edu.harvard.we99.services.storage.PlateTypeStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.UUID;

/**
 * Implementation of the PlateTemplateService.
 *
 * @author mford
 */
public class PlateMapServiceImpl extends BaseRESTServiceImpl<PlateMap> implements PlateMapService {

    private static final Logger log = LoggerFactory.getLogger(PlateMapServiceImpl.class);

    private final PlateTypeStorage pts;

    public PlateMapServiceImpl(PlateMapStorage storage, PlateTypeStorage pts) {
        super(storage);
        this.pts = pts;
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }

    @Override
    public ImportedPlateMap create(String name, InputStream is) {
        PlateMapCSVReader reader = new PlateMapCSVReader();
        try (Reader r = new BufferedReader(new InputStreamReader(is))) {
            if (name == null) {
                name = UUID.randomUUID().toString();
            }
            PlateMap plateMap = reader.read(r).withName(name);
            PlateMap created = plateMapStorage().create(plateMap);
            PlateTypes list = pts.findGreaterThanOrEqualTo(calcDim(plateMap), 0);
            return new ImportedPlateMap(created, list.getValues());
        } catch (IOException e) {
            log.error("error parsing csv", e);
            throw new WebApplicationException(Response.status(409).build());
        }
    }

    @Override
    public PlateMaps listAll(Integer page) {
        return plateMapStorage().listAll(page);
    }

    private PlateMapStorage plateMapStorage() {
        return (PlateMapStorage) storage;
    }

    /**
     * Returns the minimum dimension we need to encompass this plate
     * @param plateMap
     * @return
     */
    private PlateDimension calcDim(PlateMap plateMap) {
        PlateDimension dim = new PlateDimension(0, 0);
        for(Coordinate coord : plateMap.getWells().keySet()) {
            dim.setRows(Math.max(dim.getRows(), coord.getRow()));
            dim.setCols(Math.max(dim.getCols(), coord.getCol()));
        }
        // the for loop above was building up the dimension based on the max
        // values for rows,cols. However, before returning this as a dimension
        // value we need to increment each of the row and col. This is because
        // the rows,cols are zero based.
        return dim.withRows(dim.getRows()+1).withCols(dim.getCols()+1);
    }
}
