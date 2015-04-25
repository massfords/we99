package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.Compound;

import java.util.List;

/**
 * @author mford
 */
public class Compounds extends AbstractList<Compound,Compounds> {
    public Compounds() {
    }

    public Compounds(Long totalCount, int page, int pageSize, List<Compound> compounds) {
        super(totalCount, page, pageSize, compounds);
    }
}
