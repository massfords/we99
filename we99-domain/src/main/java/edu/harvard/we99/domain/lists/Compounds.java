package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.Compound;

import java.util.List;

/**
 * @author mford
 */
public class Compounds extends AbstractList<Compound> {
    public Compounds() {
    }

    public Compounds(List<Compound> compounds) {
        super(compounds);
    }
}
