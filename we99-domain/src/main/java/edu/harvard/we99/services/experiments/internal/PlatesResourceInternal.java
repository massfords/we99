package edu.harvard.we99.services.experiments.internal;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.services.experiments.PlatesResource;

/**
 * See the package documentation for details on the internal sub-resource interfaces
 *
 * @author markford
 */
public interface PlatesResourceInternal extends PlatesResource {
    void setExperiment(Experiment experiment);
    Experiment getExperiment();
}
