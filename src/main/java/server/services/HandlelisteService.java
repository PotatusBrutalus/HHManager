package server.services;

import server.controllers.GenereltController;
import server.controllers.HandlelisteController;
import server.restklasser.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

@Path("/handleliste")
public class HandlelisteService {

    /**
     * Tar imot navn på handlelisten fra klienten, samt handlelistens innhold (JSON)
     * @param handleliste Et handlelisteobjekt med all nødvendig informasjon
     * @return int Handlelistens ID
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public int lagHandleliste(Handleliste handleliste) {
        return HandlelisteController.lagHandleliste(handleliste);
    }

    /**
     * Tar imot IDen til handlelisten som skal slettes fra klienten (i URLen)
     * @param handlelisteId Unikt identifiserbar handlelisteId som er lagret på serveren
     * @return True hvis slettingen lykkes, false hvis ikke.
     */
    @DELETE
    @Path("/{handlelisteId}")
    public boolean slett(@PathParam("handlelisteId") int handlelisteId) {
        return HandlelisteController.gjemHandleliste(handlelisteId);
    }

    /**
     * Tar imot en int-array der hver element sitt tall er indeksen til elementet som skal checkes/uncheckes i listen over
     * varer i handlelisten. Så checker eller unchecker metoden feltenene (gjør dem true/false) på databasen avhengig av hva
     * de var før.
     * @param handlelisteId Unikt identifiserbar handlelisteId som er lagret på serveren
     * @param checkedUnchecked Array med int der hver int-verdi refererer til indeksen til et element i handlelisten som skal checkes/uncheckes
     *                         avhengig av hva det var før.
     * @return boolean[] Array som er like lang som vare-arrayen og sier hvilken av checkboksene som er checked. True = checked, false = unchecked.
     */

    /**
     * Tar imot en int-array der hver element sitt tall er indeksen til elementet som skal checkes/uncheckes i listen over
     * varer i handlelisten. Så checker eller unchecker metoden feltenene (gjør dem true/false) på databasen avhengig av hva
     * de var før.
     * @param pubEllerPriv Hvis true, skal listen være offentlig, hvis false, skal den være private
     * @return boolean Nåværende
     */

    /**
     * Henter egen handleliste gitt handlelisteid og brukerid.
     * @param husholdningId
     * @param brukerId
     * @return
     */
    @GET
    @Path("/{husholdningId}/{brukerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Handleliste> getHandlelister(@PathParam("husholdningId") int husholdningId, @PathParam("brukerId") int brukerId) {
        return HandlelisteController.getHandlelister(husholdningId, brukerId);
    }

    /**
     *     Returnerer ID til varen som ble lagt inn
     */
    @POST
    @Path("/nyVare")
    @Consumes(MediaType.APPLICATION_JSON)
    public int leggTilVare(Vare vare) {
        return HandlelisteController.leggInnVare(vare);
    }


    /**
     * Henter sist brukte handleliste fra database og viser det på forsiden.
     * @param brukerId BrukerId'en som sendes inn
     * @param husholdningId Husholdningen som vises på forsiden
     * @return
     */
    @GET
    @Path("/forsideListe/{husholdningId}/{brukerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Handleliste hentForsideListe(@PathParam("husholdningId") int husholdningId, @PathParam("brukerId") int brukerId){
        return HandlelisteController.getForsideListe(husholdningId, brukerId);
    }
    /**
     * Setter varer med rett Id som kjøpt
     *
     */
    @PUT
    @Path("/kjoptVarer")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean settKjopt(ArrayList<Vare> varer){
        return HandlelisteController.setKjopt(varer);
    }

    @PUT
    @Path("/endreOffentlig")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean endreOffentlig(Handleliste handleliste){
        return HandlelisteController.endreOffentlig(handleliste);
    }
}
