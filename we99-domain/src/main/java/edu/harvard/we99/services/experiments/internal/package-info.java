/**
 * The purpose of this package is to separate the REST sub-resource interfaces
 * from implementation specific ones that require additional methods to setup the
 * state of the sub-resource before returning it through the CXF framework.
 *
 * Examples are returning the sub-resource for an Experiment and installing the
 * experiment id into the sub-resource so it knows what instance it should be
 * working with.
 *
 * The benefit of having this separation is that the non-REST methods we need to
 * establish this state won't propagate to the client proxies when we use the
 * CXF factory bean.
 *
 * @author markford
 */
package edu.harvard.we99.services.experiments.internal;