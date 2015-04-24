package edu.harvard.we99.services.experiments.internal;

import edu.harvard.we99.services.experiments.MemberResource;

/**
 * See the package documentation for details on the internal sub-resource interfaces
 *
 * @author markford
 */
public interface MemberResourceInternal extends MemberResource {
    Long getId();
    void setId(Long id);
}
