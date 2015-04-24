package edu.harvard.we99.services;

import edu.harvard.we99.domain.Compound;
import edu.harvard.we99.domain.lists.Compounds;
import edu.harvard.we99.services.storage.CompoundStorage;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Handles the basic CRUD operations for Compounds
 *
 * @author mford
 */
public class CompoundServiceImpl extends BaseRESTServiceImpl<Compound>  implements CompoundService {

    private final Logger log = LoggerFactory.getLogger(CompoundServiceImpl.class);

    public CompoundServiceImpl(CompoundStorage storage) {
        super(storage);
    }

    @Override
    public Response delete(Long id) {
        deleteImpl(id);
        return Response.ok().build();
    }

    @Override
    public Compounds listAll(Integer page, Integer pageSize, String queryString) {
        try {
            CompoundStorage cs = (CompoundStorage) storage;
            return cs.listAll(page, pageSize, queryString);
        } catch(Exception e) {
            log.error("error listing compounds. Page {} pageSize {} queryString {}",
                    page, pageSize, queryString, e);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    @Override
    public Response upload(InputStream csvFile) {
        StreamFactory sf = StreamFactory.newInstance();
        sf.loadResource("compounds.xml");
        Set<Compound> set = new LinkedHashSet<>();
        int counter = 0;
        try (Reader r = new BufferedReader(new InputStreamReader(csvFile))) {
            BeanReader br = sf.createReader("compounds", r);
            Compound compound;
            CompoundStorage cs = (CompoundStorage) storage;
            while ((compound = (Compound) br.read()) != null) {
                set.add(compound);

                if (set.size()>500) {
                    cs.resolveIds(set);
                    counter += set.size();
                    set.clear();
                }
            }
            if (!set.isEmpty()) {
                cs.resolveIds(set);
                counter += set.size();
            }
        } catch (IOException e) {
            log.error("error processing compounds stream", e);
            throw new WebApplicationException(
                    Response.status(409).entity(e.getMessage()).build()
            );
        } catch (Exception e) {
            log.error("error processing compounds stream", e);
            throw new WebApplicationException(Response.serverError().build());
        }
        return Response.ok().entity("Uploaded " + counter).build();
    }
}
