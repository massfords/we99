package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.*;
import edu.harvard.we99.domain.results.*;
import edu.harvard.we99.domain.results.analysis.*;
import edu.harvard.we99.services.storage.DoseResponseResultStorage;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.ResultStorage;

import javax.persistence.PersistenceException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author alan orcharton
 */
public abstract class DoseResponseResultImpl implements DoseResponseResultResource {

    private Long doseResponseResultId;
    private final PlateStorage plateStorage;
    private final DoseResponseResultStorage doseResponseResultStorage;
    private final ResultStorage resultStorage;
    private Experiment experiment;

    public DoseResponseResultImpl(PlateStorage plateStorage, DoseResponseResultStorage doseResponseStorage, ResultStorage resultStorage){
        this.plateStorage = plateStorage;
        this.doseResponseResultStorage = doseResponseStorage;
        this.resultStorage = resultStorage;
    }






    /**
     * Gets the Y value - the sample result from the wells that make up the experiment points
     * Finds the plates that make up the dose response points and then takes the sample from
     * the well.
     * todo:Note  This takes the first sample. Assuming that Dose response is only for a single sample plate
     * Saves the DoseResponseResult experiment points back to the database
     */

    public DoseResponseResult addResponseValues(){

        DoseResponseResult currentResult = get();
        Set<Long> plateIds = doseResponseResultStorage.getPlateIds(doseResponseResultId);

        Map<Long,List<WellResults>> resultByPlate = new HashMap<>();
        for (Long id : plateIds){
            Plate plate = plateStorage.get(id);
            PlateResult plateResult = resultStorage.getByPlateId(id);
            Map<Coordinate, WellResults> wellResults = plateResult.getWellResults();
            PlateNormalizationForDoseResponseFunction pnf = new PlateNormalizationForDoseResponseFunction(plate);
            List<WellResults> wrList = new ArrayList<>(wellResults.values());
            List<WellResults> normalized = pnf.apply(wrList);
            resultByPlate.put(id, normalized);
        }
        computeResults(resultByPlate, plateIds, currentResult);



        return currentResult;
    }

    private DoseResponseResult computeResults(Map<Long,List<WellResults>> resultsByPlate, Set<Long> plateIds, DoseResponseResult current){

        //DoseResponseResult current = this.get();
        ExperimentPointsFunction epf = new ExperimentPointsFunction(current,plateIds);
        List<ExperimentPoint> newPoints = epf.apply(resultsByPlate);

        current.setExperimentPoints(newPoints);
        CurveFitParametersFunction cfp = new CurveFitParametersFunction();
        List<FitParameter> fit = cfp.apply(newPoints);
        CurveFitPointsFunction cfpf = new CurveFitPointsFunction(fit,39,FitEquation.HILLEQUATION);
        List<CurveFitPoint> curvePoints = cfpf.apply(newPoints);
        Map<String,FitParameter> fitMap = new HashMap<>();
        current.setCurveFitPoints(curvePoints);
        for(FitParameter f : fit){
            fitMap.put(f.getName(), f);
        }

        current.setFitParameterMap(fitMap);
        return doseResponseResultStorage.update(current.getId(),current);

        //calculateCurveFit();



    }

    //no longer used to calculate curve fit remove soon
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
        DoseResponseResult doseResponseResult = getDoseResponseResult();
        if( doseResponseResult == null || !doseResponseResult.getExperiment().getId().equals(experiment.getId())){
            throw new WebApplicationException(Response.status(404).build());
        }
        return doseResponseResult;
    }

    @Override
    public DoseResponseResult updateStatus(EPointStatusChange ePointstatusChange) {

        DoseResponseResult result;
        try {
            Long doseId = ePointstatusChange.getDoseId();
            Long plateId = ePointstatusChange.getPlateId();
            Dose d = doseResponseResultStorage.getDose(doseResponseResultId, doseId);
            Coordinate cord = d.getWell().getCoordinate();
            resultStorage.updateStatus(plateId, cord, ePointstatusChange.getStatus());

            result = addResponseValues();
        } catch (Exception e) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return result;
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

    private DoseResponseResult getDoseResponseResult() {

        DoseResponseResult doseResponseResult;
        try {
            doseResponseResult= doseResponseResultStorage.get(doseResponseResultId);

        } catch(PersistenceException e) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return doseResponseResult;
    }

}
