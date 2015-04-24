package edu.harvard.we99.services.experiments.internal;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.services.experiments.DoseResponseResource;

/**
 * See the package documentation for details on the internal sub-resource interfaces
 *
 * @author markford
 */
public interface DoseResponseResourceInternal extends DoseResponseResource{
    void setExperiment(Experiment experiment);
    Experiment getExperiment();
}
