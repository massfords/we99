package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.Well;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.Sample;
import edu.harvard.we99.domain.results.WellResults;
import edu.harvard.we99.domain.results.analysis.CurveFit;
import edu.harvard.we99.services.storage.DoseResponseResultStorage;
import edu.harvard.we99.services.storage.PlateStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
      /*
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
          */
        return null;
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
        doseResponseResultStorage.getPlateIds(experiment, doseResponseResultId);

       /*
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
        plateIds.forEach(id -> {    // what's going on with PlateResultResource prr here? You're creating it,
                                    // assigning some stuff on it and then disregarding it
                                    PlateResultResource prr =createPlateResultResource();
                                    prr.setPlateId(id);
                                    prr.setExperiment(experiment);
                                    // I made this more obvious now. I don't think prr == prr2
                                    // is what you want.
                                    //PlateResultResource prr2 = createPlateResultResource();
                                    //assert prr != prr2; // this assertion will fail
                                    resultResources.put(id, prr);
                                });


        plateIdPointsMap.forEach((pid, list) -> {
            PlateResultResource plateResultResource = resultResources.get(pid);
            Map<Coordinate, WellResults> wr = plateResultResource.get().getWellResults();
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

        //return get();
        return calculateCurveFit();
        */
        return null;
    }

    private DoseResponseResult calculateCurveFit(){

        DoseResponseResult currentResult = get();
        DoseResponseResult curvePointResult = CurveFit.fitCurve(currentResult);
        //if the python script could not be found the result will be null
        if(curvePointResult == null) {
            curvePointResult = currentResult;
        } else {
            curvePointResult = doseResponseResultStorage.updateCurveFitPoints(doseResponseResultId,curvePointResult);
        }
        return curvePointResult;

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
