package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.WellLabelMapping;
import edu.harvard.we99.domain.WellType;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.function.Function;

/**
 * @author markford
 */
class SinglePlateMergeValidation implements Function<WellLabelMapping, Void> {

    @Override
    public Void apply(WellLabelMapping wlm) {
        if (wlm.getWellType() != WellType.EMPTY && wlm.getDose().getCompound() == null) {
            String message = "Dose in well label %s is missing its compound";
            throw new WebApplicationException(
                    Response.status(409)
                            .entity(String.format(message, wlm.getLabel()))
                            .build());
        }
        return null;
    }
}
