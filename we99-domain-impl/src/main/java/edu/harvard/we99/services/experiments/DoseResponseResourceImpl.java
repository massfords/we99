package edu.harvard.we99.services.experiments;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Dose;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.ExperimentPoint;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.Well;
import edu.harvard.we99.domain.lists.DoseResponseResults;
import edu.harvard.we99.domain.lists.Plates;
import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.services.storage.CompoundStorage;
import edu.harvard.we99.services.storage.DoseResponseResultStorage;
import edu.harvard.we99.services.storage.PlateStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author alan orcharton
 */
public abstract class DoseResponseResourceImpl implements DoseResponseResource {

    private static final Logger log = LoggerFactory.getLogger(DoseResponseResourceImpl.class);

    private final PlateStorage plateStorage;
    private final CompoundStorage compoundStorage;
    private final DoseResponseResultStorage doseResponseResultStorage;
    private Experiment experiment;


    public DoseResponseResourceImpl(PlateStorage plateStorage, CompoundStorage compoundStorage,
                                    DoseResponseResultStorage doseResponseResultStorage){

        this.plateStorage = plateStorage;
        this.compoundStorage = compoundStorage;
        this.doseResponseResultStorage = doseResponseResultStorage;
    }

    protected abstract DoseResponseResultResource createDoseResponseResultResource();

    @Override
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
                for(Iterator<Dose> it = doses.iterator(); it.hasNext();){
                    Dose d = it.next();
                    if(d.getCompound() != null){
                        compoundList.put(d.getCompound().getId(), d.getCompound());
                    }
                }
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
        /*
            There's a lot going on here:
            - list all of the results
            - for each result:
             -- create a DoseResponseResultResource that...
             -- get the same result which had at the start of the forEach
             -- arrange its ExperimentPoints into a Map<plateId,List<points>>
             -- for each plateId, get its PlateResults (sort of, see comment in DoseResponseResultImpl)
             -- lost track of the rest in DoseResponseResultImpl line 138

             A couple of concerns...
             - there's a lot of being fetched from the storage layer and then
             written back and this all happens in different transactions.
             - another approach would be to fetch all of the data you need into
             this tier, perform calculations on it, and then write it back.
             - this way, it would all happen in a single transaction
             - another option would be to push this all to the storage layer and
               work with the JPA entities directly. My hope was to implement all
               of the functions using the domain objects but if that proves too
               costly then perhaps the entities should be use.
         */
        doseResponseResultStorage.createAll(experiment.getId());
        DoseResponseResults drResults = doseResponseResultStorage.getAll(experiment.getId());
       // drResults.getValues().forEach(result -> { DoseResponseResultResource resultResource = getDoseResponseResults(result.getId());
        //                                            resultResource.addResponseValues();  } );
        DoseResponseResults doseResponseResults = doseResponseResultStorage.listAll(experiment.getId(), page, pageSize, typeAhead);
        return doseResponseResults;
    }

    @Override
    public DoseResponseResultResource getDoseResponseResults(Long doseResponseId) {
        DoseResponseResultResource drr = createDoseResponseResultResource();
        drr.setExperiment(experiment);
        drr.setDoseResponseId(doseResponseId);
        return drr;
    }

    @Override
    public DoseResponseResult list() {
        return doseResponseResultStorage.get(1L);
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
