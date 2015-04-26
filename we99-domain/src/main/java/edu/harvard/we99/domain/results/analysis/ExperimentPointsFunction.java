package edu.harvard.we99.domain.results.analysis;

import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.WellType;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.ResultStatus;
import edu.harvard.we99.domain.results.Sample;
import edu.harvard.we99.domain.results.WellResults;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author alan orcharton
 */
public class ExperimentPointsFunction implements Function<Map<Long,List<WellResults>>, List<ExperimentPoint>> {

    private final DoseResponseResult doseResponse;
    private final Set<Long> plateIds;



    public  ExperimentPointsFunction(DoseResponseResult doseResponse,Set<Long> plateIds ){
            this.doseResponse = doseResponse;
            this.plateIds = plateIds;

    }

    @Override
    public List<ExperimentPoint> apply(Map<Long, List<WellResults>> longWellResultsMap){

        List<ExperimentPoint> newpoints = new ArrayList<>();
        for(Long id : plateIds) {
            Map<Coordinate, List<Sample>> cord = new HashMap<>();    //stores samples results by coordinate
            List<WellResults> plateResults = longWellResultsMap.get(id);   //gets list well results for a plate
            List<WellResults> includedResults = plateResults.stream()
                                .filter(wresult -> wresult.getResultStatus() == ResultStatus.INCLUDED)
                                .collect(Collectors.toList());
            includedResults.forEach(wr -> cord.put(wr.getCoordinate(), wr.getSamples()));   //map sample to co-ordinate
            List<Dose> epdoses = doseResponse.getDoses();         // Gets the Doses for the DR
            for(Dose d : epdoses){
                if(d.getPlateId() == id){
                    ExperimentPoint npoint = new ExperimentPoint(id,d.getId());
                    npoint.setX(d.getAmount().getNumber());
                    cord.keySet().forEach(wellcoord -> {
                        if (wellcoord.equals(d.getWell().getCoordinate())) {
                            npoint.setY(cord.get(wellcoord).get(0).getNormalized());
                        }
                    });
                    newpoints.add(npoint);
                }
            }


        }

        return newpoints;

    }


    public List<ExperimentPoint> apply2(Map<Long, List<WellResults>> longWellResultsMap) {

        List<ExperimentPoint> newpoints = new ArrayList<>();
        for(Long id : plateIds){
            Map<Coordinate, List<Sample>> cord = new HashMap<>();    //stores samples results by coordinate
            List<WellResults> plateResults = longWellResultsMap.get(id);   //gets list well results for a plate
            plateResults.forEach(wr -> cord.put(wr.getCoordinate(), wr.getSamples()));   //map sample to co-ordinate
            List<Dose> epdoses = doseResponse.getDoses();         // Gets the Doses for the DR
            Map<Long, Dose> epdosesmap = new HashMap<>();         // store dose per dose id
            epdoses.forEach(d -> epdosesmap.put(d.getId(),d));
            List<ExperimentPoint> dosepoints = doseResponse.getExperimentPoints();   //gets all the Experiment points
            for( ExperimentPoint ep : dosepoints){
                if(ep.getPlateId() == id) {
                    ExperimentPoint npoint = new ExperimentPoint(id, ep.getDoseId());
                    npoint.setX(epdosesmap.get(ep.getDoseId()).getAmount().getNumber());
                    cord.keySet().forEach(wellcoord -> {
                        if (wellcoord.equals(epdosesmap.get(ep.getDoseId()).getWell().getCoordinate())) {
                            npoint.setY(cord.get(wellcoord).get(0).getNormalized());      //protectet against no well at co-ord
                        }
                    });
                    newpoints.add(npoint);
                }
            }

        }

        return newpoints;
    }
}
