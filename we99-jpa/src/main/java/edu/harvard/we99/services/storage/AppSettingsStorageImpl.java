package edu.harvard.we99.services.storage;

import com.mysema.query.jpa.impl.JPAQuery;
import edu.harvard.we99.services.storage.entities.AppSetting;
import edu.harvard.we99.services.storage.entities.QAppSetting;
import edu.harvard.we99.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;

import static edu.harvard.we99.util.JacksonUtil.toJsonString;

/**
 * @author mford
 */
public class AppSettingsStorageImpl implements AppSettingsStorage {

    private static final Logger log = LoggerFactory.getLogger(AppSettingsStorageImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public <T> T load(Class<T> setting) {
        AppSetting as = getAppSetting(setting);
        T t = null;
        try {
            if (as != null) {
                t = JacksonUtil.fromString(as.getJson(), setting);
            }
            return t;
        } catch(Exception e) {
            log.error("error getting setting {}", setting.getSimpleName(), e);
            return null;
        }
    }

    @Override
    @Transactional
    public <T> void store(Class<T> setting, T t) throws IOException {
        AppSetting as = getAppSetting(setting);
        if (as == null) {
            insert(t);
        } else {
            as.setJson(toJsonString(t));
            em.merge(as);
        }
    }

    private  <T> void insert(T t) throws IOException {
        AppSetting as = new AppSetting();
        as.setName(t.getClass().getSimpleName());
        as.setJson(toJsonString(t));
        em.persist(as);
    }

    private AppSetting getAppSetting(Class<?> setting) {

        JPAQuery query = new JPAQuery(em);
        QAppSetting appSetting = QAppSetting.appSetting;
        query.from(appSetting).where(appSetting.name.eq(setting.getSimpleName()));

        return query.uniqueResult(appSetting);
    }
}
