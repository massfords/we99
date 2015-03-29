package edu.harvard.we99.domain.lists;

import edu.harvard.we99.domain.Protocol;

import java.util.List;

/**
 * @author mford
 */
public class Protocols extends AbstractList<Protocol> {
    public Protocols() {}
    public Protocols(Long count, int page, int pageSize, List<Protocol> values) {
        super(count, page, pageSize, values);
    }
}
