package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.domain.Amount;
import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.Coordinate;
import edu.harvard.we99.domain.Experiment;
import edu.harvard.we99.domain.Label;
import edu.harvard.we99.domain.Plate;
import edu.harvard.we99.domain.PlateDimension;
import edu.harvard.we99.domain.PlateMap;
import edu.harvard.we99.domain.PlateType;
import edu.harvard.we99.domain.Protocol;
import edu.harvard.we99.domain.Well;
import edu.harvard.we99.domain.WellMap;
import edu.harvard.we99.domain.results.PlateMetrics;
import edu.harvard.we99.domain.results.PlateResult;
import edu.harvard.we99.domain.results.WellResults;
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
                .fieldAToB("experiment", "experiment")
                .fieldAToB("labels", "labels")
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

        COMPOUND = mapperFactory.getMapperFacade(CompoundEntity.class, Compound.class);
        EXPERIMENTS = mapperFactory.getMapperFacade(ExperimentEntity.class, Experiment.class);
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
    }
}
