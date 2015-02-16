package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.services.storage.PlateTypeStorage;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Implementatiuon of the PlateTypeService
 *
 * todo - as with PlateTemplateService, this could be nearly 100% generic, if not at least a base class
 *
 * @author mford
 */
public class PlateTypeServiceImpl implements PlateTypeService {

    /**
     * Storage impl that handles the basic CRUD operations
     */
    private final PlateTypeStorage storage;

    public PlateTypeServiceImpl(PlateTypeStorage storage) {
        this.storage = storage;
    }

    @Override
    public PlateType create(PlateType type) {
        System.out.println("hello from type");
        return storage.create(type);
    }

    @Override
    public PlateType get(Long id) {
        try {
            return storage.get(id);
        } catch(EntityNotFoundException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
    }

    @Override
    public PlateType update(Long id, PlateType type) {
        try {
            return storage.update(id, type);
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
