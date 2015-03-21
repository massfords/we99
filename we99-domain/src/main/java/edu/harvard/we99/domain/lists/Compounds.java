package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.Compound;

import java.util.List;

/**
 * @author mford
 */
public class Compounds extends AbstractList<Compound> {
    public Compounds() {
    }

    public Compounds(Long totalCount, int page, List<Compound> compounds) {
        super(totalCount, page, compounds);
    }
}
