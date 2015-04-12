package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.*;
import edu.harvard.we99.domain.results.*;
import edu.harvard.we99.security.Role;
import edu.harvard.we99.security.RoleName;
import edu.harvard.we99.security.User;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.joda.time.DateTime;

/**
 * @author mford
 */
public class Mappers {
    private Mappers() {}

    public static final BoundMapperFacade<CompoundEntity,Compound> COMPOUND;
    public static final BoundMapperFacade<UserEntity,User> USERS;
    public static final BoundMapperFacade<DoseResponseResultEntity,DoseResponseResult> DOSERESPONSES;
    public static final BoundMapperFacade<ExperimentEntity,Experiment> EXPERIMENTS;
    public static final BoundMapperFacade<PlateEntity,Plate> PLATES;
    public static final BoundMapperFacade<PlateMapEntity,PlateMap> PLATEMAP;
    public static final BoundMapperFacade<WellMapEntity,WellMap> WELLMAP;
    public static final BoundMapperFacade<PlateTypeEntity,PlateType> PLATETYPE;
    public static final BoundMapperFacade<ProtocolEntity,Protocol> PROTOCOL;
    public static final BoundMapperFacade<PlateResultEntity,PlateResult> PLATERESULT;
    public static final BoundMapperFacade<WellEntity,Well> WELL;
    public static final BoundMapperFacade<WellResultsEntity,WellResults> WELLRESULT;
    public static final BoundMapperFacade<LabelEntity,Label> LABEL;
    public static final BoundMapperFacade<ExperimentPointEntity,ExperimentPoint> EXPERIMENTPOINT;
    public static final BoundMapperFacade<CurveFitPointEntity,CurveFitPoint> CURVEFITPOINT;
    public static final BoundMapperFacade<HillFitParameterEntity,HillFitParameter> HILLFITPARAMETER;


    private static final MapperFactory mapperFactory =
            new DefaultMapperFactory.Builder().build();


    static {
        mapperFactory.getConverterFactory().registerConverter(
                new PassThroughConverter(DateTime.class));
        mapperFactory.getConverterFactory().registerConverter(
                new PassThroughConverter(Amount.class));
        mapperFactory.getConverterFactory().registerConverter(
                new PassThroughConverter(Coordinate.class));
        mapperFactory.getConverterFactory().registerConverter(
                new PassThroughConverter(RoleName.class));
        mapperFactory.getConverterFactory().registerConverter(
                new PassThroughConverter(PlateDimension.class));



        mapperFactory
                .classMap(HillFitParameterEntity.class, HillFitParameter.class)
                .mapNullsInReverse(false)
                .fieldAToB("bottom","bottom")
                .byDefault()
                .register();

        mapperFactory
                .classMap(CurveFitPointEntity.class, CurveFitPoint.class)
                .mapNullsInReverse(false)
                .byDefault()
                .register();

        mapperFactory
                .classMap(CompoundEntity.class, Compound.class)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
        mapperFactory
                .classMap(UserEntity.class, User.class)
                .mapNullsInReverse(false)
                .fieldAToB("role", "role")
                .fieldAToB("experiments{key}", "experiments{}")
                .exclude("password")
                .byDefault()
                .register();
        mapperFactory
                .classMap(RoleEntity.class, Role.class)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
        mapperFactory
                .classMap(ExperimentEntity.class, Experiment.class)
                .mapNullsInReverse(false)
                .fieldAToB("labels", "labels")
                .fieldAToB("status", "status")
                .fieldAToB("protocol", "protocol")
                .byDefault()
                .register();
        mapperFactory
                .classMap(PlateEntity.class, Plate.class)
                .mapNullsInReverse(false)
                .fieldAToB("wells", "wells")
                .fieldAToB("labels", "labels")
                .fieldAToB("experiment.id", "experimentId")
                .fieldAToB("plateType", "plateType")
                .fieldAToB("wellCount", "wellCount")
                .byDefault()
                .register();
        mapperFactory
                .classMap(PlateMapEntity.class, PlateMap.class)
                .mapNullsInReverse(false)
                .fieldAToB("wells", "wells")
                .byDefault()
                .register();
        mapperFactory
                .classMap(WellEntity.class, Well.class)
                .mapNullsInReverse(false)
                .fieldAToB("labels", "labels")
                .byDefault()
                .register();
        mapperFactory
                .classMap(WellResultsEntity.class, WellResults.class)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
        mapperFactory
                .classMap(WellMapEntity.class, WellMap.class)
                .mapNullsInReverse(false)
                .fieldAToB("labels", "labels")
                .byDefault()
                .register();
        mapperFactory
                .classMap(PlateTypeEntity.class, PlateType.class)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
        mapperFactory
                .classMap(ProtocolEntity.class, Protocol.class)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
        mapperFactory
                .classMap(PlateResultEntity.class, PlateResult.class)
                .mapNullsInReverse(false)
                .fieldAToB("wellResults", "wellResults")
                .fieldAToB("plate", "plate")
                .byDefault()
                .register();
        mapperFactory
                .classMap(PlateMetricsEntity.class, PlateMetrics.class)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
        mapperFactory
                .classMap(LabelEntity.class, Label.class)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
        mapperFactory
                .classMap(DoseResponseResultEntity.class, DoseResponseResult.class)
                .mapNullsInReverse(false)
                .fieldAToB("compound", "compound")
                .fieldAToB("fitParameter", "fitParameter")
                .byDefault()
                .register();
        mapperFactory
                .classMap(ExperimentPointEntity.class, ExperimentPoint.class)
                .mapNullsInReverse(false)
                .fieldAToB("associatedDoseResponseResult", "associatedDoseResponseResult")
                .fieldAToB("associatedPlate", "associatedPlate")
                .fieldAToB("associatedWell","associatedWell")
                .byDefault()
                .register();


        COMPOUND = mapperFactory.getMapperFacade(CompoundEntity.class, Compound.class);
        EXPERIMENTS = mapperFactory.getMapperFacade(ExperimentEntity.class, Experiment.class);
        DOSERESPONSES = mapperFactory.getMapperFacade(DoseResponseResultEntity.class,DoseResponseResult.class);
        PLATES = mapperFactory.getMapperFacade(PlateEntity.class, Plate.class);
        USERS = mapperFactory.getMapperFacade(UserEntity.class, User.class);
        PLATEMAP = mapperFactory.getMapperFacade(PlateMapEntity.class, PlateMap.class);
        WELLMAP = mapperFactory.getMapperFacade(WellMapEntity.class, WellMap.class);
        PLATETYPE = mapperFactory.getMapperFacade(PlateTypeEntity.class, PlateType.class);
        PROTOCOL = mapperFactory.getMapperFacade(ProtocolEntity.class, Protocol.class);
        PLATERESULT = mapperFactory.getMapperFacade(PlateResultEntity.class, PlateResult.class);
        WELL = mapperFactory.getMapperFacade(WellEntity.class, Well.class);
        WELLRESULT = mapperFactory.getMapperFacade(WellResultsEntity.class, WellResults.class);
        LABEL = mapperFactory.getMapperFacade(LabelEntity.class, Label.class);
        EXPERIMENTPOINT = mapperFactory.getMapperFacade(ExperimentPointEntity.class,ExperimentPoint.class);
        CURVEFITPOINT = mapperFactory.getMapperFacade(CurveFitPointEntity.class, CurveFitPoint.class);
        HILLFITPARAMETER = mapperFactory.getMapperFacade(HillFitParameterEntity.class, HillFitParameter.class);

    }
}
