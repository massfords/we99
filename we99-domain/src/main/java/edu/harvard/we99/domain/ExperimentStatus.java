package edu.harvard.we99.domain;

/**
 * @author mford
 */
public enum ExperimentStatus {
    /**
     * Once published, experiments are effectively read only.
     */
    PUBLISHED,

    /**
     * Unpublished experiments can only be seen by their members
     */
    UNPUBLISHED
}
