package edu.harvard.we99.domain;

import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.Sample;
import edu.harvard.we99.services.ExperimentService;
import edu.harvard.we99.services.PlateTypeService;
import edu.harvard.we99.services.experiments.DoseResponseResource;
import edu.harvard.we99.services.experiments.PlatesResource;
import edu.harvard.we99.services.storage.CompoundStorage;
import edu.harvard.we99.services.storage.DoseResponseResultStorage;
import edu.harvard.we99.services.storage.entities.*;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;
import javax.persistence.TypedQuery;

import java.util.*;

import static edu.harvard.we99.test.BaseFixture.name;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author alan orcharton
 */
public class JpaDoseResponseIPT extends JpaSpringFixture {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    private ExperimentService experimentService;

    @Inject
    private CompoundStorage compoundStorage;

    @Inject
    private PlateTypeService plateTypeService;

    @Inject
    private DoseResponseResultStorage doseResponseResultStorage;

    @Rule
    public EastCoastTimezoneRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    @Rule
    public AuthenticatedUserRule authenticatedUserRule =
            new AuthenticatedUserRule("we99.2015@gmail.com", this);



    @Test
    public void testCreatingAllResponseResults() throws Exception {

        TypedQuery<WellEntity> query7 = em.createQuery("select w from WellEntity w", WellEntity.class);
        List<WellEntity> experimentEntities7 = query7.getResultList();

        TypedQuery<ExperimentEntity> query = em.createQuery("select e from ExperimentEntity e", ExperimentEntity.class);
        List<ExperimentEntity> experimentEntities = query.getResultList();

        doseResponseResultStorage.createAll(experimentEntities.get(0).getId());

        TypedQuery<DoseResponseResultEntity> query2 = em.createQuery("select d from DoseResponseResultEntity d", DoseResponseResultEntity.class);
        List<DoseResponseResultEntity> drentities = query2.getResultList();

    }
    @Test
    public void testGettingAllCompoundsByExperiment() throws Exception{

        beginTx();



        TypedQuery<Object[]> query2 = em.createQuery("select pe.id, wid  from PlateEntity AS pe JOIN pe.wells as wid where pe.experiment.name=:name",Object[].class);
        query2.setParameter("name", "experiment uno");
        List<Object[]> experiment2 = query2.getResultList();
        experiment2.size();

        TypedQuery<Object[]> query3 = em.createQuery("select de from DoseEntity AS de where de.compound.name='Acetaldehyde'",Object[].class);
        List<Object[]> experiment3 = query3.getResultList();
        experiment3.size();


        Map<WellEntity,Long> wellEntityMap = new HashMap<>();
        for( Object[] results : experiment2){
            Long id = (Long) results[0];
            WellEntity we = (WellEntity) results[1];
            wellEntityMap.put(we,id);

        }

        Map<DoseEntity, Long> dosePlateMap = new HashMap<>();
        Iterator it = wellEntityMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Long plateId = (Long)pair.getValue();
            WellEntity wellEntity = (WellEntity) pair.getKey();
            Set<DoseEntity> doses = wellEntity.getContents();
            Object[] dosearray = doses.toArray();
            if (dosearray.length > 0){
                DoseEntity d = (DoseEntity) dosearray[0];
                dosePlateMap.put(d,plateId);
            }

            it.remove(); // avoids a ConcurrentModificationException
        }

        commitTx();

        //query.setParameter("roleName", RoleName.BuiltIn.Admin.asName());

        //TypeQuery<Object[]> query4 = em.createQuery("select we from PlateEntity.wells JOIN pe.wells where de.well.id = pe.  pe.experiment.name='experiment uno'",Object[].class);
       // List<Object[]> experiment4 = query4.getResultList();

       // TypedQuery<Object[]> query5 = em.createQuery("select de from DoseEntity de where de.well EXISTS (select pe.wells from PlateEntity pe where pe.experiment.name='experiment uno')", Object[].class);
       // List<Object[]> experiment5 = query5.getResultList();
    }

    /*
    @Test
    public void createDoseResponseWithFitParameter() throws Exception{

        Compound c2 = new Compound().setName("Smarties");
        Compound newCompound = compoundStorage.create(c2);

        Experiment exp = experimentService.create(
                new Experiment(name("xp"))
                        .setProtocol(new Protocol(name("protocol"))));

        FitParameter fp = new FitParameter("top",130.0,ParameterStatus.FLOAT);

        DoseResponseResult drr = new DoseResponseResult().setExperiment(exp).setCompound(newCompound);

        DoseResponseResult storedDrr = doseResponseResultStorage.create(drr);
        DoseResponseResult withParam = doseResponseResultStorage.addFitParameter(storedDrr.getId(), fp);

        assertNotNull(withParam.getFitEquation());
        assertNotNull(withParam.getFitParameterMap());


    }

    @Test
    public void validateDoseResponseWithFitParameter() throws Exception{

        Compound c2 = new Compound().setName("WhamBar");
        compoundStorage.create(c2);
        Experiment exp = experimentService.create(
                new Experiment(name("xp"))
                        .setProtocol(new Protocol(name("protocol"))));
        PlateType pt = plateTypeService.create(
                new PlateType()
                        .setName(name("PlateType"))
                        .setDim(new PlateDimension(5, 5))
                        .setManufacturer(name("Man")));
        PlatesResource pr = experimentService.getExperiment(exp.getId()).getPlates();
        Plate plate = pr.create(
                new Plate()
                        .setName(name("Plate"))
                        .withWells(makeDoseCompoundWells(5, 5, c2, c2))
                        .setPlateType(pt)
        );

        List<Plate> platesfordr = new ArrayList<>();
        DoseResponseResource drResource = experimentService.getExperiment(exp.getId()).getDoseResponses();
        drResource.create();



    }
      */

    protected static Well[] makeDoseCompoundWells(int rowCount, int colCount, Compound c1, Compound c2) {
        Set<Well> wells = new HashSet<>();



        Dose dose1 = new Dose()
                .setCompound(c1)
                .setAmount(new Amount(575, DoseUnit.MICROMOLAR));
        Set<Dose> set1 = new HashSet<>();
        set1.add(dose1);



        Dose dose2 = new Dose()
                .setCompound(c2)
                .setAmount(new Amount(220, DoseUnit.MICROMOLAR));
        Set<Dose> set2 = new HashSet<>();
        set2.add(dose2);

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {

                if (row % 2 == 0) {
                    wells.add(
                            new Well(row, col)
                                    .setType(WellType.COMP)
                                    .setContents(set1)


                    );
                } else {

                    wells.add(
                            new Well(row, col)
                                    .setType(WellType.COMP)
                                    .setContents(set1)

                    );
                }
            }
        }
        assertEquals(rowCount * colCount, wells.size());

        return wells.toArray(new Well[wells.size()]);
    }
}
