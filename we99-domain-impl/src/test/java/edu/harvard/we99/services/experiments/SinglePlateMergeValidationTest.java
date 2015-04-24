package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.WellLabelMapping;
import edu.harvard.we99.domain.WellType;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;

/**
 * @author markford
 */
public class SinglePlateMergeValidationTest {

    private SinglePlateMergeValidation validation = new SinglePlateMergeValidation();

    @Test(expected = WebApplicationException.class)
    public void missingCompound() throws Exception {
        validation.apply(new WellLabelMapping().setWellType(WellType.COMP).setDose(new Dose()));
    }

}
