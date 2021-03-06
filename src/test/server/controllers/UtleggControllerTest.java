package server.controllers;

import org.junit.Test;
import server.restklasser.Oppgjor;
import server.restklasser.Utleggsbetaler;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UtleggControllerTest {
    @Test
    public void getMineOppgjor() throws Exception {
        ArrayList<Oppgjor> oppgjor = UtleggController.getMineOppgjor(2,1);

        ArrayList<Utleggsbetaler> utleggJegSkylder = new ArrayList<>();
        //Sjekk om jeg (brukerId2) skylder bruker1 no
        for (int i = 0; i < oppgjor.size(); i++) {
            if (oppgjor.get(i).getBrukerId() == 1) {
                utleggJegSkylder = oppgjor.get(i).getUtleggJegSkylder();
            }
        }
        for (int i = 0; i < utleggJegSkylder.size(); i++) {
            System.out.println("Skylder bruker 1 "+utleggJegSkylder.get(i).getDelSum()+" kroner for "+utleggJegSkylder.get(i).getBeskrivelse());
            assertEquals(100, (int)utleggJegSkylder.get(i).getDelSum());
        }

        //Finn ut hva folk skylder meg
        ArrayList<Oppgjor> oppgjor2 = UtleggController.getMineOppgjor(1,1);

        if (oppgjor2.get(0).getUtleggDenneSkylderMeg().size() > 0) {
            assertEquals("Karol", oppgjor2.get(0).getNavn());
            assertEquals("Karol", oppgjor2.get(0).getUtleggDenneSkylderMeg().get(0).getNavn());

            assertEquals("Truls", oppgjor2.get(1).getNavn());
            assertEquals("Truls", oppgjor2.get(1).getUtleggDenneSkylderMeg().get(0).getNavn());
        }
    }

    @Test
    public void getMineOppgjorExtraTest() {
        ArrayList<Oppgjor> betalteOppgjor = UtleggController.getMineOppgjor(1,1);
        System.out.println("Skriver ut betalte brukerIder");
        for (Oppgjor item : betalteOppgjor) {
            System.out.println(item.getBrukerId());
        }

        ArrayList<Oppgjor> uBetalteOppgjor = UtleggController.getMineOppgjor(1,0);
        System.out.println("Skriver ut ubetalte brukerIder");
        for (Oppgjor item : uBetalteOppgjor) {
            System.out.println(item.getBrukerId());
        }
    }

}