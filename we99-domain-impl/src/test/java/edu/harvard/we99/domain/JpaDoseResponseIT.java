package edu.harvard.we99.domain;

import edu.harvard.we99.domain.results.DoseResponseResult;
import edu.harvard.we99.domain.results.EPointStatusChange;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.ResultStatus;
import edu.harvard.we99.domain.results.WellResults;
import edu.harvard.we99.domain.results.analysis.CurveFitParametersFunction;
import edu.harvard.we99.domain.results.analysis.CurveFitPointsFunction;
import edu.harvard.we99.domain.results.analysis.ExperimentPointsFunction;
import edu.harvard.we99.domain.results.analysis.PlateNormalizationForDoseResponseFunction;
import edu.harvard.we99.services.ExperimentService;
import edu.harvard.we99.services.PlateTypeService;
import edu.harvard.we99.services.experiments.DoseResponseResource;
import edu.harvard.we99.services.experiments.DoseResponseResultResource;
import edu.harvard.we99.services.experiments.PlateResource;
import edu.harvard.we99.services.experiments.PlatesResource;
import edu.harvard.we99.services.storage.CompoundStorage;
import edu.harvard.we99.services.storage.DoseResponseResultStorage;
import edu.harvard.we99.services.storage.PlateStorage;
import edu.harvard.we99.services.storage.ResultStorage;
import edu.harvard.we99.services.storage.entities.DoseEntity;
import edu.harvard.we99.services.storage.entities.DoseResponseResultEntity;
import edu.harvard.we99.services.storage.entities.ExperimentEntity;
import edu.harvard.we99.services.storage.entities.WellEntity;
import edu.harvard.we99.test.EastCoastTimezoneRule;
import edu.harvard.we99.test.Scrubbers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.test.BaseFixture.name;
import static edu.harvard.we99.test.BaseFixture.stream;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertEquals;

/**
 * @author alan orcharton
 */
public class JpaDoseResponseIT extends JpaSpringFixture {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    private ExperimentService experimentService;

    @Inject
    private CompoundStorage compoundStorage;

    @Inject
    private PlateTypeService plateTypeService;

    @Inject
    private DoseResponseResultStorage doseResponseResultStorage;

    @Inject
    private PlateStorage plateStorage;

    @Inject
    private ResultStorage resultStorage;

    @Rule
    public TestRule eastCoastTimezoneRule = new EastCoastTimezoneRule();

    @Rule
    public AuthenticatedUserRule authenticatedUserRule =
            new AuthenticatedUserRule("we99.2015@gmail.com", this);


    @Test
    public void testKnockOutExperimentPoint() throws Exception {


        Double[] positive = {138.478,144.927,144.34,145.219,145.654,145.924,144.77,140.504,
                138.814,142.404,144.539,145.242,142.971,139.508,145.795,141.695};

        Double[] negative = {30.046,29.32,31.546,26.173,29.214,25.645,27.317,30.404,
                30.939,27.316,25.647,27.482,25.806,30.73,29.419,27.189};

        Double[] wellResults = {110.794,82.435,53.098,34.547,24.925,21.977,21.68,25.515,24.101,
                28.793,28.194};

        Double[] wellDoses = {0.00003,9.49E-06,3.00E-06,9.51E-07,3.01E-07,
                9.52E-08,3.01E-08,9.53E-09,3.02E-09,9.55E-10,3.02E-10};

        Double[] responses = {71.79121535497015,47.08544254001027,21.527656402541446,5.3664098321291975,-3.0160770688737975,-5.584313546129757,
                -5.843053788241366,-2.502081301716058,-3.7339287843618965,0.35364433344168644,-0.16819204711337463};


        Compound c = new Compound("Popcorn");
        compoundStorage.create(c);

        Experiment exp = experimentService.create(
                new Experiment(name("drxp")).setProtocol(new Protocol(name("proto")))
        );
        PlateType pt = plateTypeService.create(
                new PlateType()
                        .setName(name("DoseResponsePlate"))
                        .setDim(new PlateDimension(16, 24))
                        .setManufacturer(name("Man")));
        PlatesResource pr = experimentService.getExperiment(exp.getId()).getPlates();
        Plate plate = pr.create(
                new Plate()
                        .setName(name("Plate"))
                        .withWells(makeDoseCompoundWells(16, 24, c, wellDoses))
                        .setPlateType(pt)
        );
        PlateResource plates = pr.getPlates(plate.getId());
        PlateResult plateResult = plates.getPlateResult().uploadResults("csv", stream("/DoseResponsePlateIT/results-single-dr.csv"));

        //doseResponseResultStorage.createAll(exp.getId());

        DoseResponseResource drr = experimentService.getExperiment(exp.getId()).getDoseResponses();
        drr.generateAllResults(0, 100, "");

        DoseResponseResult popcornDR = doseResponseResultStorage.getByCompoundName("Popcorn");

        DoseResponseResultResource drrr = drr.getDoseResponseResults(popcornDR.getId());

        //assert the results
        DoseResponseResult aResult = drrr.get();

        Function<String, String> scrubber = Scrubbers.iso8601
                .andThen(Scrubbers.uuid)
                .andThen(Scrubbers.pkey)
                .andThen(Scrubbers.xpId)
                .andThen(Scrubbers.xvalue)
                .andThen(Scrubbers.yvalue)
                .andThen(Scrubbers.yvalueend)
                .andThen(Scrubbers.logxvalue)
                .andThen(Scrubbers.fitvalue);
        assertJsonEquals(load("/DoseResponsePlateIT/single-response-all-points.json"),
                toJsonString(aResult), scrubber);

        //remove a point
        Dose d = aResult.getDoses().iterator().next();
        EPointStatusChange eps = new EPointStatusChange()
                .setDoseId(d.getId()).setPlateId(plate.getId()).setStatus(ResultStatus.EXCLUDED);
        //DoseResponseResult resultAfter = drrr.updateStatus(eps);
        DoseResponseResult resultAfter = drr.KoPointAndReCalc(eps);

        //assert first point is excluded
        assertJsonEquals(load("/DoseResponsePlateIT/single-response-one-excluded.json"),
                toJsonString(resultAfter), scrubber);



    }


    @Test
    public void testCreationOfDoseResponseResult() throws Exception {

        Double[] positive = {138.478,144.927,144.34,145.219,145.654,145.924,144.77,140.504,
                138.814,142.404,144.539,145.242,142.971,139.508,145.795,141.695};

        Double[] negative = {30.046,29.32,31.546,26.173,29.214,25.645,27.317,30.404,
                30.939,27.316,25.647,27.482,25.806,30.73,29.419,27.189};

        Double[] wellResults = {110.794,82.435,53.098,34.547,24.925,21.977,21.68,25.515,24.101,
                28.793,28.194};

        Double[] wellDoses = {0.00003,9.49E-06,3.00E-06,9.51E-07,3.01E-07,
                9.52E-08,3.01E-08,9.53E-09,3.02E-09,9.55E-10,3.02E-10};

        Double[] responses = {71.79121535497015,47.08544254001027,21.527656402541446,5.3664098321291975,-3.0160770688737975,-5.584313546129757,
                -5.843053788241366,-2.502081301716058,-3.7339287843618965,0.35364433344168644,-0.16819204711337463};

        Compound c = new Compound("Sugar");
        compoundStorage.create(c);

        Experiment exp = experimentService.create(
                new Experiment(name("drxp")).setProtocol(new Protocol(name("proto")))
        );
        PlateType pt = plateTypeService.create(
                new PlateType()
                        .setName(name("DoseResponsePlate"))
                        .setDim(new PlateDimension(16, 24))
                        .setManufacturer(name("Man")));
        PlatesResource pr = experimentService.getExperiment(exp.getId()).getPlates();
        Plate plate = pr.create(
                new Plate()
                        .setName(name("Plate"))
                        .withWells(makeDoseCompoundWells(16, 24, c, wellDoses))
                        .setPlateType(pt)
        );
        PlateResource plates = pr.getPlates(plate.getId());
        PlateResult plateResult = plates.getPlateResult().uploadResults("csv", stream("/DoseResponsePlateIT/results-single-dr.csv"));

        doseResponseResultStorage.createAll(exp.getId());

        //TypedQuery<DoseResponseResultEntity> query2 = em.createQuery("select d from DoseResponseResultEntity d where d.compound.name='Sugar'", DoseResponseResultEntity.class);
        //List<DoseResponseResultEntity> drentities = query2.getResultList();
        DoseResponseResult drResult = doseResponseResultStorage.getByCompoundName("Sugar");
        Set<Long> plateIds = doseResponseResultStorage.getPlateIds(drResult.getId());

        Map<Long,List<WellResults>> resultByPlate = new HashMap<>();
        for(Long id : plateIds){

            Plate plate1 = plateStorage.get(id);
            PlateResult plateResult2 = resultStorage.getByPlateId(id);
            Map<Coordinate, WellResults> wellResults2 = plateResult2.getWellResults();
            //resultByPlate.put(id,wrList);
            PlateNormalizationForDoseResponseFunction pnf = new PlateNormalizationForDoseResponseFunction(plate1);
            List<WellResults> wrList = new ArrayList<>(wellResults2.values());
            List<WellResults> normalized = pnf.apply(wrList);
            resultByPlate.put(id, normalized);
            //System.out.println("Hello");
            //ExperimentPointsFunction epf = new ExperimentPointsFunction(current,plateIds);

        }

        ExperimentPointsFunction epf = new ExperimentPointsFunction(drResult,plateIds);
        List<ExperimentPoint> newPoints = epf.apply(resultByPlate);
        CurveFitParametersFunction cfp = new CurveFitParametersFunction();
        List<FitParameter> fit = cfp.apply(newPoints);
        CurveFitPointsFunction cfpf = new CurveFitPointsFunction(fit,39,FitEquation.HILLEQUATION);
        List<CurveFitPoint> curvePoints = cfpf.apply(newPoints);
        Map<String,FitParameter> fitMap = new HashMap<>();
        drResult.setCurveFitPoints(curvePoints);
        for(FitParameter f : fit){
           fitMap.put(f.getName(), f);
        }

        drResult.setFitParameterMap(fitMap);
        doseResponseResultStorage.update(drResult.getId(),drResult);



    }


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
    public void testGettingPlateIds() throws Exception {
        doseResponseResultStorage.createAll(1L);
        Set<Long> ids = doseResponseResultStorage.getPlateIds(1L);


    }


    @Test
    public void testGettingDoseandDoseResponseForKOPoints() throws Exception {



       /* Map<String,Long>  koPointIdMap = new HashMap<>();
        Long experimentId = 6L;
        Long doseId = 1457L;
        ExperimentEntity ee = em.find(ExperimentEntity.class, experimentId);
        DoseEntity de = em.find(DoseEntity.class, doseId);

        doseResponseResultStorage.createAll(experimentId);

        TypedQuery<Object[]> query2 = em.createQuery("select distinct drre.Id, do.plateId from DoseResponseResultEntity AS drre JOIN drre.doses as do where drre.experiment.id=:id and do.id=:doid",Object[].class);
        query2.setParameter("id", experimentId);
        query2.setParameter("doid",doseId);
        List<Object[]> doseResponse = query2.getResultList();


        Long targetDoseResponse;
        Long targetPlateId;
        if( doseResponse.size() > 0){

           Object[] DrIdandPlateId = doseResponse.get(0);
            if( DrIdandPlateId.length == 2){
                targetDoseResponse = (Long) DrIdandPlateId[0];
                targetPlateId = (Long) DrIdandPlateId[1];
                koPointIdMap.put("drresultid", targetDoseResponse);
                koPointIdMap.put("plateid",targetPlateId);
            }
        }
      */





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


    private static Well[] makeDoseCompoundWells(int rowCount, int colCount, Compound c1, Double[] doses) {
        Set<Well> wells = new HashSet<>();

        Queue<Dose> doseQueue = new LinkedList<>();
        for (Double d: doses){
            Dose aDose = new Dose();
            aDose.setAmount(new Amount(d, DoseUnit.MICROMOLAR));
            aDose.setCompound(c1);
            doseQueue.add(aDose);

        }


        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {


                if(col == 0){
                    wells.add(
                            new Well(row,col)
                                    .setType(WellType.POSITIVE)
                    );
                }
                else if (col == 1){
                    if(!doseQueue.isEmpty()) {
                        Dose d = (Dose) doseQueue.remove();
                        Set<Dose> ds = new HashSet<>();
                        ds.add(d);
                        wells.add(
                                new Well(row, col)
                                        .setType(WellType.COMP)
                                        .setContents(ds)

                        );
                    } else {
                        wells.add(
                                new Well(row, col)
                                        .setType(WellType.EMPTY)
                        );

                    }
                } else if(col == 23){
                    wells.add(
                            new Well(row, col)
                                    .setType(WellType.NEGATIVE)
                    );

                } else {
                    wells.add(
                            new Well(row, col)
                                    .setType(WellType.EMPTY)
                    );

                }
            }
        }
        assertEquals(rowCount * colCount, wells.size());

        return wells.toArray(new Well[wells.size()]);
    }


//    protected static Well[] makeDoseCompoundWells(int rowCount, int colCount, Compound c1, Compound c2) {
//        Set<Well> wells = new HashSet<>();
//
//
//
//        Dose dose1 = new Dose()
//                .setCompound(c1)
//                .setAmount(new Amount(575, DoseUnit.MICROMOLAR));
//        Set<Dose> set1 = new HashSet<>();
//        set1.add(dose1);
//
//
//
//        Dose dose2 = new Dose()
//                .setCompound(c2)
//                .setAmount(new Amount(220, DoseUnit.MICROMOLAR));
//        Set<Dose> set2 = new HashSet<>();
//        set2.add(dose2);
//
//        for (int row = 0; row < rowCount; row++) {
//            for (int col = 0; col < colCount; col++) {
//
//                if (row % 2 == 0) {
//                    wells.add(
//                            new Well(row, col)
//                                    .setType(WellType.COMP)
//                                    .setContents(set1)
//
//
//                    );
//                } else {
//
//                    wells.add(
//                            new Well(row, col)
//                                    .setType(WellType.COMP)
//                                    .setContents(set1)
//
//                    );
//                }
//            }
//        }
//        assertEquals(rowCount * colCount, wells.size());
//
//        return wells.toArray(new Well[wells.size()]);
//    }
}
