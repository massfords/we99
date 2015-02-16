package edu.harvard.we99.services;

import edu.harvard.we99.domain.BaseEntity;
import edu.harvard.we99.services.storage.CRUDStorage;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Base class to handle all of the basic CRUD operations for a web service. We still
 * need specific interfaces for our REST services, but it's likely the case that
 * we could implement most of the basic functionality for CRUD with this single
 * class.
 *
 * @author mford
 */
public abstract class BaseRESTServiceImpl<T extends BaseEntity> {
    protected final CRUDStorage<T> storage;

    public BaseRESTServiceImpl(CRUDStorage<T> storage) {
        this.storage = storage;
    }

    public T create(T type) {
        return storage.create(type);
    }

    public T get(Long id) {
        try {
            return storage.get(id);
        } catch(EntityNotFoundException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
    }

    public T update(Long id, T type) {
        try {
            return storage.update(id, type);
        } catch(EntityNotFoundException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
    }

    public Response delete(Long id) {
        try {
            storage.delete(id);
        } catch(EntityNotFoundException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return Response.ok().build();
    }

}
