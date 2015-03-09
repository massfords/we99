package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.Experiment;

import java.util.List;

/**
 * @author mford
 */
public class Experiments extends AbstractList<Experiment> {
    public Experiments() {}

    public Experiments(List<Experiment> values) {
        super(values);
    }
}
