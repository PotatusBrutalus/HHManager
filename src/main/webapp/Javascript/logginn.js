/**
 * Created by BrageHalse on 11.01.2018.
 */
/**
 * Legger lytter på logg inn-knappen og sjekker om epost og passord er tomme. Feilmelding dersom ett av feltene er
 * tomme. Det opprettes et objekt bruker med epost og passord.
 */
$(document).ready(function () {

    $("#loggInnBtn").on("click", function () {
        var brukerEpost = $("#email").val();
        var passord = $("#password").val();
        if (brukerEpost == "" || passord == "") {
            alert("Du må fylle ut alle feltene");
            return;
        }
        var bruker = {
            epost: brukerEpost,
            passord: passord
        };
        $.ajax({
            url: "server/BrukerService/login",
            type: 'POST',
            data: JSON.stringify(bruker),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (result) {
                var innBruker = (result);
                console.log(innBruker);
                if (innBruker == null) {
                    alert("Feil epost eller passord!");
                    return;
                }else if(innBruker.favHusholdning > 0){
                    localStorage.setItem("bruker", JSON.stringify(innBruker));
                    window.location = "forside.html";
                    return;
                }
                localStorage.setItem("bruker", JSON.stringify(innBruker));
                window.location = "profil.html";
            },
            error: function () {
                alert("serverfeil kall 2 :/")
            }
        })
    });

    /**
     * Lytter på knappen registrere bruker, som sender deg videre til lagbruker.html.
     */

    $("#regBruker").on("click", function () {
        window.location = "lagbruker.html";
    });

});

/**
 * Gjør at du kan trykke enter for å logge inn
 * @param event
 */
function keyCode(event) {
    var x = event.keyCode;
    if (x == 13) {
        $("#loggInnBtn").click();
    }
}