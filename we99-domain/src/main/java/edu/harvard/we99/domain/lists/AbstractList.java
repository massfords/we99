package edu.harvard.we99.domain.lists;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mford
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractList<T> {
    private List<T> values = new ArrayList<>();

    public AbstractList(List<T> values) {
        this.values.addAll(values);
    }

    public AbstractList() {
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public int size() {return values.size();}
}
