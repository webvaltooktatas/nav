package hu.webvalto.backend.rs;

import hu.webvalto.backend.dao.impl.FelhasznaloDao;
import hu.webvalto.backend.domain.Felhasznalo;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/felhasznalo/{nev}")
public class FelhasznaloRS {

    @Inject
    private FelhasznaloDao felhasznaloDao;

    @Inject
    private Logger logger;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String felhasznaloLekerdezese(@PathParam("nev") String nev) {
        logger.info("Felhasznalo lekerdezese: {}", nev);
        Optional<Felhasznalo> felhasznalo = felhasznaloDao.lekerdezNevAlapjan(nev);
        return felhasznalo.isPresent() ? "LETEZO FELHASZNALO" : "NEM LETEZO FELHASZNALO";
    }

    @Path("json")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response felhasznaloLekerdezeseAsJSON(@PathParam("nev") String nev) {
        logger.info("Felhasznalo lekerdezese: {}", nev);
        Optional<Felhasznalo> felhasznalo = felhasznaloDao.lekerdezNevAlapjan(nev);
        String allapot = felhasznalo.isPresent() ? "{\"message\" : \"LETEZO FELHASZNALO\" }" : "{ \"message\" : \"NEM LETEZO FELHASZNALO\" }";
        return Response.ok().entity(allapot).build();
    }
}
