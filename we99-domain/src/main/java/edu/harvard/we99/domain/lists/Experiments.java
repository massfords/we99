package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.Experiment;

import java.util.List;

/**
 * @author mford
 */
public class Experiments extends AbstractList<Experiment> {
    public Experiments() {}

    public Experiments(Long count, int page, List<Experiment> values) {
        super(count, page, values);
    }
}
