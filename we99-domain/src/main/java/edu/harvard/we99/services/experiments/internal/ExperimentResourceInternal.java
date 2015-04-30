package edu.harvard.we99.services.experiments.internal;

import edu.harvard.we99.services.experiments.ExperimentResource;

/**
 * See the package documentation for details on the internal sub-resource interfaces
 *
 * @author markford
 */
public interface ExperimentResourceInternal extends ExperimentResource {
    Long getId();
    void setId(Long id);
}
