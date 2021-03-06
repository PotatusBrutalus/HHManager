package server;

import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import server.controllers.*;
import server.restklasser.Bruker;

/**
 * Denne klassen kobles opp mot Googles mail servere og sender mail til en eller flere brukere.
 */
public class Mail {
    private static final String avsender = "officialHouseHoldManager";
    private static final String passord = "HHManagerT6";
    private static final String sub = "Registrert i husholdning i HouseHoldManager";
    private static final String glemtsub = "Nytt passord til HouseHoldManager";
    private static String regards = "\n\nTakk for at du bruker HouseHoldManager." + "\n\nVennlig hilsen,"
            + "\nHouseHoldManagers utviklingsteam <3";

    /**
     * Sender mail til brukere som allerede er registrert i systemet
     * @param eposter ArrayList med epostadressene til brukerne
     * @param hushold String navnet til husholdningen
     */
    public static void sendAllerede(ArrayList<String> eposter, String hushold) {
        if (eposter == null || eposter.size() < 1) return;
        StringBuilder s = new StringBuilder("Velkommen til HousHoldManger, systemet som gir deg en enklere hverdag." +
                "\n\nDu har blitt lagt til i husholdningen " + hushold +
                "\nKlikk lenken for å kommme til HHManagers forside: http://localhost:8080/HHManager");
        sendTilFlere(eposter, s);
    }

    /**
     * Sender mail til brukere som ikke er registrert i systemet fra før.
     * @param brukere ArrayList av brukerne
     * @param hushold String navnet til husholdningen.
     */
    public static void sendUreg(ArrayList<Bruker> brukere, String hushold) {
        if (brukere == null || brukere.size() < 1) return;
        for (Bruker bruker : brukere) {
            String msg = "Velkommen til HousHoldManger, systemet som gir deg en enklere hverdag." +
                    "\n\nDu har blitt lagt til i husholdningen " + hushold + "\nBrukernavn: " + bruker.getEpost() +
                    "\nPassord: " + bruker.getPassord() +
                    "\n\nFølg lenken for å logge inn: http://localhost:8080/HHManager";
            sendTilEn(bruker.getEpost(), msg);
        }
    }

    public static boolean sendGlemtPassord(String email, int brukerId) {
        String msg;
        if (brukerId < 1){
            msg = "Hei!" +
                    "\nDu er ikke registrert i vårt system, vennligst registrer deg på: http://localhost:8080/HHManager/lagbruker.html" + regards;
        } else {
            String pw = BrukerController.nyttTilfeldigPass(brukerId);
            msg = "Velkommen til HouseHoldManger, systemet som gir deg en enklere hverdag." +
                    "\n\nHer er ditt nye genererte passord: " + pw + regards;
        }
        return sendTilEn(email, msg);
    }

    /**
     * Sender mail til alle nye medlemmer i en husholdning, som ER en del av systemet allerede
     *
     * @param eposter ArrayList over alle epostadressene som skal få mail
     * @param msg     String med innholdet i eposten.
     */
    private static void sendTilFlere(ArrayList<String> eposter, StringBuilder msg) {
        if (eposter.size() < 1) return;
        for (String epost :
                eposter) {
            epost.trim().toLowerCase();
        }
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //checks if password is correct to login to email account
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(avsender, passord);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setSubject(sub);
            message.setText(msg.append(regards).toString());

            InternetAddress[] addresses = new InternetAddress[eposter.size()];
            for (int i = 0; i < eposter.size(); i++) {
                addresses[i] = new InternetAddress(eposter.get(i));
            }
            message.addRecipients(Message.RecipientType.BCC, addresses);
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();

        }
    }

    /**
     * Denne metoden genererer et nytt passord og sender det til emailen til en bruker fra selskapets email.
     * @param email er email-adressen til brukeren
     * @param msg det som skal stå i mailen
     **/
    private static boolean sendTilEn(String email, String msg) {
        String out = email.trim().toLowerCase();
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //checks if password is correct to login to email account
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(avsender, passord);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(out));
            message.setSubject(glemtsub);
            message.setText(msg);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
