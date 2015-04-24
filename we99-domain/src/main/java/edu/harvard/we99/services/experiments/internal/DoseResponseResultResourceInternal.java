package edu.harvard.we99.services.experiments.internal;

import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.services.experiments.DoseResponseResultResource;

/**
 * See the package documentation for details on the internal sub-resource interfaces
 *
 * @author markford
 */
public interface DoseResponseResultResourceInternal extends DoseResponseResultResource {
    DoseResponseResult addResponseValues();
    void setExperiment(Experiment experiment);
    Experiment getExperiment();
    void setDoseResponseId(Long doseResponseId);
}
