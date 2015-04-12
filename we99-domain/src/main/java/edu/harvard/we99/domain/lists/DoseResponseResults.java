package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.results.DoseResponseResult;

import java.util.List;

/**
 * @author alan orcharton
 */
public class DoseResponseResults extends AbstractList<DoseResponseResult>{

    public DoseResponseResults(){}
    public DoseResponseResults(long count, int page, int pageSize, List<DoseResponseResult> values) {
        super(count,page,pageSize,values);
    }

}
