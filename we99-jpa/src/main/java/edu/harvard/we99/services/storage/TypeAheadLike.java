package edu.harvard.we99.services.storage;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import org.apache.commons.lang.StringUtils;

/**
 * @author markford
 */
public class TypeAheadLike {
    private TypeAheadLike() {}

    private static BooleanExpression like(StringPath stringPath, String typeAhead) {
        return stringPath.toUpperCase().like("%" + typeAhead.toUpperCase() + "%");
    }

    public static void applyTypeAhead(JPAQuery query, StringPath stringPath, String typeAhead) {
        if (StringUtils.isNotBlank(typeAhead)) {
            query.where(like(stringPath, typeAhead));
        }
    }
}
