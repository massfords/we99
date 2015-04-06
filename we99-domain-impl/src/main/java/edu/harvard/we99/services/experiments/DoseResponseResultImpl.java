package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.*;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.Sample;
import edu.harvard.we99.domain.results.WellResults;
import edu.harvard.we99.services.storage.DoseResponseResultStorage;
import edu.harvard.we99.services.storage.PlateStorage;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by HUID 70786729 on 4/1/15.
 */
public abstract class DoseResponseResultImpl implements DoseResponseResultResource {

    private Long doseResponseResultId;
    private final PlateStorage plateStorage;
    private final DoseResponseResultStorage doseResponseResultStorage;
    private Experiment experiment;




    public DoseResponseResultImpl(PlateStorage plateStorage, DoseResponseResultStorage doseResponseStorage){
        this.plateStorage = plateStorage;
        this.doseResponseResultStorage = doseResponseStorage;
    }

    /*
    @Override
    public DoseResponseResult create(List<Plate> plates){

        List<Long> wellIds = new ArrayList<>();
        for (Plate p : plates){
            Plate aPlate = plateStorage.get(p.getId());
            for(Well w : aPlate.getWells().values()){
                Set<Dose> doses = w.getContents();
                for (Iterator<Dose> it = doses.iterator(); it.hasNext(); ) {
                    Dose d = it.next();
                    DoseResponseResult drr = new DoseResponseResult()
                            .setCompound(d.getCompound());
                    doseResponseResultStorage.create(drr);
                    wellIds.add(w.getId());

                    }
                }

            }

        return null;

    }
    */


    @Override
    public DoseResponseResult create(Compound compound, List<Plate> plates) {


        DoseResponseResult drr = new DoseResponseResult()
                .setCompound(compound);
        DoseResponseResult result = doseResponseResultStorage.create(drr);
        for (Plate p : plates){
            Plate aPlate = plateStorage.get(p.getId());
            for(Well w : aPlate.getWells().values()){
                Set<Dose> doses = w.getContents();
                for(Iterator<Dose> it = doses.iterator(); it.hasNext();){
                    Dose d = it.next();
                    if(d.getCompound().equals(compound)){
                        ExperimentPoint ep = new ExperimentPoint()
                                .setAssociatedDoseResponseResult(result)
                                .setAssociatedPlate(aPlate)
                                .setAssociatedWell(w)
                                .setX(d.getAmount().getNumber());

                        doseResponseResultStorage.addExperimentPoint(result.getId(),ep);


                    }

                }
            }
        }


        return doseResponseResultStorage.get(result.getId());

    }

    /**
     * Gets the Y value - the sample result from the wells that make up the experiment points
     * Finds the plates that make up the dose response points and then takes the sample from
     * the well.
     * todo:Note  This takes the first sample. Assuming that Dose response is only for a single sample plate
     * Saves the DoseResponseResult experiment points back to the database
     */
    @Override
    public DoseResponseResult addResponseValues(){

        DoseResponseResult currentResult = get();
        //get the plates
        List<ExperimentPoint> points = currentResult.getExperimentPoints();
        Map<Long,List<ExperimentPoint>> plateIdPointsMap = new HashMap<>();
        for(ExperimentPoint p : points){
            Long plateId = p.getAssociatedPlate().getId();
            List<ExperimentPoint> pointsByPlate = plateIdPointsMap.get(plateId);
            if(pointsByPlate == null){
                List<ExperimentPoint> newPointsByPlate = new ArrayList<>();
                plateIdPointsMap.put(plateId, newPointsByPlate);
                newPointsByPlate.add(p);
            } else {
                pointsByPlate.add(p);
            }

        }

        Set<Long> plateIds = plateIdPointsMap.keySet();
        Map<Long,PlateResultResource> resultResources = new HashMap<>();
        plateIds.forEach(id -> {    PlateResultResource prr =createPlateResultResource();
                                    prr.setPlateId(id);
                                    prr.setExperiment(experiment);
                                    resultResources.put(id, createPlateResultResource());
                                    });


        plateIdPointsMap.forEach((pid, list) -> {
            Map<Coordinate, WellResults> wr = resultResources.get(pid).get().getWellResults();
            list.forEach(point -> {
                WellResults results = wr.get(point.getAssociatedWell().getCoordinate());
                if (results != null) {
                    Sample s = results.getSamples().get(0);
                    point.setY(s.getValue());
                }
            });
        });

         plateIdPointsMap.values()
                 .forEach(list -> doseResponseResultStorage.updateAllExperimentPoints(doseResponseResultId, list));

        return get();
    }



    public abstract PlateResultResource createPlateResultResource();

    @Override
    public DoseResponseResult get() {
        return doseResponseResultStorage.get(doseResponseResultId);
    }

    @Override
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;

    }

    @Override
    public Experiment getExperiment() {
        return this.experiment;
    }

    @Override
    public void setDoseResponseId(Long doseResponseId) {
        this.doseResponseResultId = doseResponseId;

    }

}
