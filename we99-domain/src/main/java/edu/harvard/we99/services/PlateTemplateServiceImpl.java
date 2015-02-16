package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateTemplate;
import edu.harvard.we99.services.storage.PlateTemplateStorage;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Implementation of the PlateTemplateService.
 *
 * todo - this could be done with a generic base class that handled a generic storage class
 *
 * @author mford
 */
public class PlateTemplateServiceImpl implements PlateTemplateService {

    /**
     * Reference to our storage layer that handles the basic CRUD operations
     */
    private final PlateTemplateStorage storage;

    public PlateTemplateServiceImpl(PlateTemplateStorage storage) {
        this.storage = storage;
    }

    @Override
    public PlateTemplate create(PlateTemplate template) {
        return storage.create(template);
    }

    @Override
    public PlateTemplate get(Long id) {
        try {
            return storage.get(id);
        } catch(EntityNotFoundException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
    }

    @Override
    public PlateTemplate update(Long id, PlateTemplate template) {
        try {
            return storage.update(id, template);
        } catch(EntityNotFoundException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
    }

    @Override
    public Response delete(Long id) {
        try {
            storage.delete(id);
        } catch(EntityNotFoundException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return Response.ok().build();
    }
}
