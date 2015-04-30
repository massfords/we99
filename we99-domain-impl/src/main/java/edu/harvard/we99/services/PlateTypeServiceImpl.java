package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.lists.PlateTypes;
import edu.harvard.we99.services.storage.PlateTypeStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Implementation of the PlateTypeService
 *
 * @author mford
 */
public class PlateTypeServiceImpl extends BaseRESTServiceImpl<PlateType> implements PlateTypeService {

    private static final Logger log = LoggerFactory.getLogger(PlateTypeServiceImpl.class);

    public PlateTypeServiceImpl(PlateTypeStorage storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        try {
            deleteImpl(id);
            return Response.ok().build();
        } catch (PersistenceException e) {
            log.debug("PlateType {} could not be deleted", id, e);
            return Response.status(409).build();
        }
    }

    @Override
    public PlateTypes listAll(Integer page, Integer pageSize, String typeAhead) {
        try {
            PlateTypeStorage ps = (PlateTypeStorage) storage;
            return ps.listAll(page, pageSize, typeAhead);
        } catch(Exception e) {
            log.error("error listing plate types. Page {}, pageSize {}, query {}",
                    page, pageSize, typeAhead, e);
            throw new WebApplicationException(Response.serverError().build());
        }
    }
}