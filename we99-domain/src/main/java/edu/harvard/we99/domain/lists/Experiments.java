package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.Experiment;

import java.util.List;

/**
 * @author mford
 */
public class Experiments extends AbstractList<Experiment, Experiments> {
    public Experiments() {}

    public Experiments(Long count, int page, int pageSize, List<Experiment> values) {
        super(count, page, pageSize, values);
    }
}
