package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Amount;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.DoseUnit;
import edu.harvard.we99.domain.WellLabelMapping;
import edu.harvard.we99.domain.WellType;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;

/**
 * @author markford
 */
public class CompoundlessPlateValidationTest {

    private CompoundlessPlateValidation validator = new CompoundlessPlateValidation();

    @Test(expected = WebApplicationException.class)
    public void emptyWellWithCompound() throws Exception {
        validator.apply(new WellLabelMapping()
                .setWellType(WellType.EMPTY)
                .setDose(new Dose().setAmount(new Amount(1, DoseUnit.MICROMOLAR))
                        .setCompound(new Compound())));
    }

    @Test(expected = WebApplicationException.class)
    public void nonEmptyWellMustHaveCompound() throws Exception {
        validator.apply(new WellLabelMapping()
                .setWellType(WellType.COMP));
    }

    @Test(expected = WebApplicationException.class)
    public void missingAmount() throws Exception {
        validator.apply(new WellLabelMapping()
                .setWellType(WellType.COMP)
                .setDose(new Dose().setCompound(new Compound())));
    }
}
