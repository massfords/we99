package edu.harvard.we99.jaxb;

import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.PlotPoint;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by HUID 70786729 on 4/5/15.
 */
public class ExperimentPointAdapter extends XmlAdapter<PlotPoint,ExperimentPoint>{

    @Override
    public PlotPoint marshal(ExperimentPoint ep) throws Exception{
             return new PlotPoint(ep.getX(), ep.getY());
    }

    @Override
    public ExperimentPoint unmarshal(PlotPoint pp) throws Exception {
        return new ExperimentPoint().setY(pp.getY()).setX(pp.getX());
    }

}
