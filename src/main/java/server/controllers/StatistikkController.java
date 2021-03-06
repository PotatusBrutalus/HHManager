package server.controllers;

import server.database.ConnectionPool;
import server.restklasser.Nyhetsinnlegg;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 *
 * Created by BrageHalse on 19.01.2018.
 */

/**
 * Klasse for å beregne antall nyhetsinnlegg skrevet av hvilket medlem, som skal vises på statistikksiden.
 */
public class StatistikkController {

    public static ArrayList<List<String>> getNyhetsatistikk(int husholdningId){
        ArrayList<List<String>> nyhetstatistikk = new ArrayList<List<String>>();
        String query = "SELECT COUNT(nyhetsinnleggId) antall, navn FROM nyhetsinnlegg LEFT JOIN bruker ON forfatterId=brukerId WHERE husholdningId = "+husholdningId+" AND dato>DATE_ADD(NOW(), INTERVAL -1 MONTH ) GROUP BY forfatterId";

        try (Connection con = ConnectionPool.getConnection()){
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                ArrayList<String> liste1  = new ArrayList<>();
                liste1.add(Integer.toString(rs.getInt("antall")));
                liste1.add(rs.getString("navn"));
                nyhetstatistikk.add(liste1);
            }
            return nyhetstatistikk;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoden teller antall gjøremål gjort om hvem som har gjort de.
     * @param husholdningId
     * @return
     */
    public static ArrayList<List<String>> getGjoremalstatistikk(int husholdningId){
        ArrayList<List<String>> gjoremalstatistikk = new ArrayList<List<String>>();
        String query = "SELECT COUNT(gjøremålId) antal, navn FROM gjoremal LEFT JOIN bruker ON utførerId = brukerId WHERE husholdningId = "+husholdningId+" AND fullført = 1 GROUP BY utførerId";

        try(Connection con = ConnectionPool.getConnection()){
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                ArrayList<String> list = new ArrayList<>();
                list.add(Integer.toString(rs.getInt("antal")));
                list.add(rs.getString("navn"));
                gjoremalstatistikk.add(list);
            }
            return gjoremalstatistikk;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lager en arraylist over hvem som har lagt ut mest
     * @param husholdningId
     * @return
     */
    public static ArrayList<List<String>> getUtleggstatistikk(int husholdningId){
        ArrayList<List<String>> utleggstatistikk = new ArrayList<List<String>>();
        String query = "";
        return utleggstatistikk;
    }

    /**
     * Får opp varkjøpsstatistikken i husholdningen gitt husholdningsid.
     * @param husholdningId
     * @return null
     */
    public static ArrayList<List<String>> getVarekjopstatistikk(int husholdningId){
        ArrayList<List<String>> varestatistikk = new ArrayList<>();
        String query = "SELECT COUNT(vareId) antallVarer, bruker.navn FROM vare LEFT JOIN handleliste ON vare.handlelisteId = handleliste.handlelisteId LEFT JOIN bruker ON kjøperId = brukerId WHERE husholdningId = "+husholdningId+" AND kjopt=1 AND vare.datoKjøpt>DATE_ADD(NOW(), INTERVAL -1 MONTH)  GROUP BY kjøperId;";
        try(Connection con = ConnectionPool.getConnection()){
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ArrayList<String> list = new ArrayList<>();
                list.add(Integer.toString(rs.getInt("antallVarer")));
                list.add(rs.getString("navn"));
                varestatistikk.add(list);
            }
            return varestatistikk;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
