package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.WellLabelMapping;
import edu.harvard.we99.domain.WellType;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.function.Function;

/**
 * @author markford
 */
class CompoundlessPlateValidation implements Function<WellLabelMapping, Void> {

    @Override
    public Void apply(WellLabelMapping wlm) {
        if (wlm.getWellType() == WellType.EMPTY) {
            if (wlm.getDose() != null) {
                throw new WebApplicationException(
                        Response.status(409)
                                .entity("Wells marked as empty cannot contain a compound")
                                .build());
            }
        } else {
            if (wlm.getDose() == null) {
                throw new WebApplicationException(
                        Response.status(409)
                                .entity("Non-empty wells must have a Dose")
                                .build());
            }
            if (wlm.getDose().getAmount() == null) {
                String message = "Dose in well label %s is missing its amount";
                throw new WebApplicationException(
                        Response.status(409)
                                .entity(String.format(message, wlm.getLabel()))
                                .build());
            }
        }
        return null;
    }
}
