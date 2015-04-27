package edu.harvard.we99.domain;

/**
 * The different types of wells in our system
 *
 * @author mford
 */
public enum WellType {
    /**
     * Indicates the well will have a compound that we want to measure
     */
    COMP,

    /**
     * A positive control for the analysis
     */
    POSITIVE,

    /**
     * A negative control for the analysis
     */
    NEGATIVE,

    /**
     * An empty well (may not be likely given the cost of plate analysis)
     */
    EMPTY
}
