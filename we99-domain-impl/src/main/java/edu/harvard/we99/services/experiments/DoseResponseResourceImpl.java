package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.Well;
import edu.harvard.we99.domain.lists.DoseResponseResults;
import edu.harvard.we99.domain.lists.Plates;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.EPointStatusChange;
import edu.harvard.we99.services.storage.DoseResponseResultStorage;
import edu.harvard.we99.services.storage.PlateStorage;

import javax.annotation.Generated;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author alan orcharton
 */
public abstract class DoseResponseResourceImpl implements DoseResponseResource {

    private final PlateStorage plateStorage;
    private final DoseResponseResultStorage doseResponseResultStorage;
    private Experiment experiment;


    public DoseResponseResourceImpl(PlateStorage plateStorage,
                                    DoseResponseResultStorage doseResponseResultStorage){

        this.plateStorage = plateStorage;
        this.doseResponseResultStorage = doseResponseResultStorage;
    }

    protected abstract DoseResponseResultResource createDoseResponseResultResource();


    public DoseResponseResult create() {
        Plates plates = plateStorage.getAll(experiment.getId());
        List<Plate> plateList = plates.getValues();
        Map<Long,Compound> allCompounds = getCompoundsFromPlates(plateList);
        List<Compound> compounds =  new ArrayList<>(allCompounds.values());
        DoseResponseResult drr = createForCompound(compounds.get(0),plateList);
        return drr;
    }

    public Map<Long,Compound> getCompoundsFromPlates(List<Plate> plateList){

        Map<Long, Compound> compoundList = new HashMap<>();
        for(Plate p : plateList){
            Plate aPlate = plateStorage.get(p.getId());
            for(Well w : aPlate.getWells().values()){
                Set<Dose> doses = w.getContents();
                doses.stream()
                        .filter(d -> d.getCompound() != null)
                        .forEach(d -> compoundList.put(d.getCompound().getId(), d.getCompound()));
            }
        }
        return compoundList;
    }

    @Override
    public DoseResponseResult createForCompound(Compound compound, List<Plate> plates) {
        /*
        DoseResponseResult drr = new DoseResponseResult().setCompound(compound).setExperiment(experiment);
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

    @Override
    public DoseResponseResults generateAllResults(Integer page, Integer pageSize, String typeAhead) {

        doseResponseResultStorage.createAll(experiment.getId());

        DoseResponseResults drResults = doseResponseResultStorage.getAll(experiment.getId());
        drResults.getValues().forEach(result -> { DoseResponseResultResource resultResource = getDoseResponseResults(result.getId());
                                                    resultResource.addResponseValues();  } );
        DoseResponseResults doseResponseResults = doseResponseResultStorage.listAll(experiment.getId(), page, pageSize, typeAhead);
        return doseResponseResults;
    }

    @Override
    public DoseResponseResult KoPointAndReCalc(EPointStatusChange ePointstatusChange){

        DoseResponseResult result = null;
        try{
            Long doseId = ePointstatusChange.getDoseId();
            Long doseResponseId = doseResponseResultStorage.getKOPointDrAndPlateId(experiment.getId(), doseId);

            DoseResponseResultResource drrr = getDoseResponseResults(doseResponseId);

            result = drrr.updateStatus(ePointstatusChange);
        } catch (Exception e) {
            throw new WebApplicationException(Response.status(404).build());
        }

        return result;


    }

    @Override
    public DoseResponseResultResource getDoseResponseResults(Long doseResponseId) {
        DoseResponseResultResource drr = createDoseResponseResultResource();
        drr.setExperiment(experiment);
        drr.setDoseResponseId(doseResponseId);
        return drr;
    }


    public DoseResponseResults list(Integer page, Integer pageSize, String typeAhead) {

        return doseResponseResultStorage.listAll(experiment.getId(), page, pageSize, typeAhead);
    }

    @Override
    @Generated(value = "generated by IDE")
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }


    @Override
    @Generated(value = "generated by IDE")
    public Experiment getExperiment() {
        return experiment;
    }

}
