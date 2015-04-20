package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.WellType;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.Sample;
import edu.harvard.we99.domain.results.WellResults;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by HUID 70786729 on 4/19/15.
 */
public class ExperimentPointsFunction implements Function<Map<Long,List<WellResults>>, List<ExperimentPoint>> {

    private final DoseResponseResult doseResponse;
    private final Set<Long> plateIds;



    public  ExperimentPointsFunction(DoseResponseResult doseResponse,Set<Long> plateIds ){
            this.doseResponse = doseResponse;
            this.plateIds = plateIds;

    }


    @Override
    public List<ExperimentPoint> apply(Map<Long, List<WellResults>> longWellResultsMap) {

        List<ExperimentPoint> newpoints = new ArrayList<>();
        for(Long id : plateIds){
            Map<Coordinate, List<Sample>> cord = new HashMap<>();
            List<WellResults> plateResults = longWellResultsMap.get(id);
            plateResults.forEach(wr -> cord.put(wr.getCoordinate(), wr.getSamples()));
            List<Dose> epdoses = doseResponse.getDoses();
            Map<Long, Dose> epdosesmap = new HashMap<>();
            epdoses.forEach(d -> epdosesmap.put(d.getId(),d));
            List<ExperimentPoint> dosepoints = doseResponse.getExperimentPoints();
            for( ExperimentPoint ep : dosepoints){
                if(ep.getPlateId() == id) {
                    ExperimentPoint npoint = new ExperimentPoint(id, ep.getDoseId());
                    npoint.setX(epdosesmap.get(ep.getDoseId()).getAmount().getNumber());
                    cord.keySet().forEach(wellcoord -> {
                        if (wellcoord.equals(epdosesmap.get(ep.getDoseId()).getWell().getCoordinate())) {
                            npoint.setY(cord.get(wellcoord).get(0).getValue());
                        }
                    });
                    newpoints.add(npoint);
                }
            }

        }

        return newpoints;
    }
}
