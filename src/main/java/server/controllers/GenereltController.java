package server.controllers;

import server.database.ConnectionPool;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Generell controller klasse. Her legges metoder hvis det viser seg at noe kode dupliserer seg i to av controllerklassene
 */
public class GenereltController {

    /**
     * Henter verdien fra én celle. Cellens innhold må være en string i databasen.
     * Lager en generell select-setning, men bruker bare id som identifikasjon.
     * Se variabelen "sqlsetning" for mal.
     *
     * @param kolonne Navnet på kolonnene i tabellen vi henter data fra
     * @param tabell Navnet på tabellen i databasen vi henter data fra
     * @param id Attributt i tabellen som må hete id og unikt identifisere raden
     * @return String. verdien til cellen fra select-setningen.
     */
    static String getString(String kolonne, String tabell, int id) {
        String sqlsetning = "SELECT "+ kolonne + " from "+ tabell + " where " + tabell + "id=?";
        try(Connection connection = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlsetning)){
            preparedStatement.setString(1, Integer.toString(id));
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getString(kolonne);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * En mer generell selectsetning som ikke må bruke id for å identifisere
     * @param kolonne man vil hente
     * @param tabell man vil hente fra
     * @param wherekolonne kolonnen man bruker til å identifisere raden
     * @param wheredata dataen som skal være i wherekolonnnen
     * @return stringen man ville hente ut
     */
    static String getString(String kolonne, String tabell, String wherekolonne, String wheredata) {
        String sqlsetning = "select " + kolonne + " from " + tabell + " where " + wherekolonne + "=?";
        try(Connection connection = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlsetning)) {
            preparedStatement.setString(1, wheredata);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getString(kolonne);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Henter verdien fra én celle. Cellens innhold må være en int i databasen.
     * Lager en generell select-setning.
     *
     * @param kolonne Navnet på kolonnene i tabellen vi henter data fra
     * @param tabell Navnet på tabellen i databasen vi henter data fra
     * @param whereData Attributt i tabellen som unikt identifiserer raden
     * @return Integer. verdien til cellen fra select-setningen.
     */
    static int getInt(String kolonne, String tabell, String wherekolonne, String whereData) {
        String sqlsetning = "SELECT "+ kolonne + " from "+ tabell + " where " + wherekolonne + "=?";
        try(Connection connection = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlsetning)){
            preparedStatement.setString(1, whereData);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(kolonne);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Henter verdien fra én celle. Cellens innhold må være en sql Date i databasen.
     * Lager en generell select-setning, men bruker bare id som identifikasjon.
     *
     * @param kolonne Navnet på kolonnene i tabellen vi henter data fra
     * @param tabell Navnet på tabellen i databasen vi henter data fra
     * @param id Attributt i tabellen som må hete id og unikt identifisere raden
     * @return SQL Date. verdien til cellen fra select-setningen.
     */
    static Date getDate(String kolonne, String tabell, int id) {
        String sqlsetning = "SELECT "+ kolonne + " from "+ tabell + " where " + tabell + "id=?";
        try(Connection connection = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlsetning)){
            preparedStatement.setString(1, Integer.toString(id));
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getDate(kolonne);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sletter en rad i databasen. Raden finnes vha. tabellnavn, kolonnenavn og en unikt identifiserbar
     * entitetsId.
     * @param tabell Navnet på tabellen i databasen vi henter data fra
     * @param id Attributt i tabellen som må hete id og unikt identifisere raden
     * @return True hvis vi ikke fikk exceptions.
     */
    static boolean slettRad(String tabell, int id) {
        String kollonnenavn = tabell+"Id"; //Genererer et navn for id-kolonnen basert på tabellenavnet
        String sqlsetning = "DELETE FROM "+tabell+" WHERE "+kollonnenavn+" = "+id+"";
        try(Connection connection = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlsetning)){
            try {
                preparedStatement.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Setter gjemt = 1, (dvs. true) på en rad i databasen. Dette betyr at denne raden IKKE
     * vil hentes ut når man gjør vanlige kall. (Denne sjekken må legges inn i andre SQL-kall)
     * @param tabell Navnet på tabellen i databasen vi henter data fra
     * @param id Attributt i tabellen som må hete id og unikt identifisere raden
     * @return True hvis vi ikke fikk exceptions.
     */
    static boolean gjemRad(String tabell, int id) {
        String kollonnenavn = tabell+"Id"; //Genererer et navn for id-kolonnen basert på tabellenavnet
        String sqlsetning = "UPDATE handleliste SET gjemt = 1 WHERE "+kollonnenavn+" = "+id+"";
        try(Connection connection = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlsetning)){
            try {
                preparedStatement.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Setter gjemt = 0, (dvs. false) på en rad i databasen. Dette betyr at denne raden
     * vil hentes ut når man gjør vanlige kall.
     * @param tabell Navnet på tabellen i databasen vi henter data fra
     * @param id Attributt i tabellen som må hete id og unikt identifisere raden
     * @return True hvis vi ikke fikk exceptions.
     */
    static boolean visRad(String tabell, int id) {
        String kollonnenavn = tabell+"Id"; //Genererer et navn for id-kolonnen basert på tabellenavnet
        String sqlsetning = "UPDATE handleliste SET gjemt=0 WHERE "+kollonnenavn+" = "+id+"";
        try(Connection connection = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlsetning)){
            try {
                preparedStatement.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Oppdaterer/endrer én celle.
     * Må bruke id.
     * Kan brukes til alt selv om det 'setData' er en String.
     *
     * @param tabell som skal oppdateres
     * @param kolonneOppdater kolonne som skal oppdateres
     * @param setData dataen som skal endres
     * @param whereId id til etiteten som skal oppdateres
     */
    static boolean update(String tabell, String kolonneOppdater, String setData, int whereId) {
        String q = "update " + tabell + " set " + kolonneOppdater + "=? where " + tabell + "Id=?";
        try(Connection connection = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(q)) {
            preparedStatement.setString(1, setData);
            preparedStatement.setInt(2, whereId);
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}


