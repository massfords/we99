package edu.harvard.we99.services;

import edu.harvard.we99.domain.PlateTemplate;
import edu.harvard.we99.services.storage.CRUDStorage;

import javax.ws.rs.core.Response;

/**
 * Implementation of the PlateTemplateService.
 *
 * @author mford
 */
public class PlateTemplateServiceImpl extends BaseRESTServiceImpl<PlateTemplate> implements PlateTemplateService {

    public PlateTemplateServiceImpl(CRUDStorage<PlateTemplate> storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }
}
