package edu.harvard.we99.domain.results;

/**
 * @author mford
 */
public enum ResultStatus {
    /**
     * All wells and samples are included by default
     */
    INCLUDED,
    /**
     * Excluded wells/samples don't contribute to our analytics
     */
    EXCLUDED
}
