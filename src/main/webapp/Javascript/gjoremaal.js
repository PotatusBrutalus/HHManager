/**
 * Definerer variabler
 */
var minBruker = JSON.parse(localStorage.getItem("bruker"));
var bruker;
var utforerId = minBruker.brukerId;
var minegjoremal = minBruker.gjoremal;
var fellesgjoremal;
var husholdningId = localStorage.getItem("husholdningId");
var varselListe;
var husholdning;
var id;

/**
 * Funksjonen henter husholdningsdata fra database.
 */

function gethhData() {
    $.getJSON("server/hhservice/" + husholdningId + "/husholdningData", function (data) {
        husholdning = data;

    });
}

/**
 * Funksjonen kalles når et nytt gjøremål skal opprettes og det skrives et nytt gjøremål inn
 * i tekstboksen fellesGjoremaal.
 */

$(document).on("click", ".valgtMedlem", function () {
    id = $(this).attr('value');
    $("#droppknapp").text($(this).text());
});


function hentMedlemmer() {
    var medlemmer = husholdning.medlemmer;
    for (var j = 0, leng = medlemmer.length; j < leng; j++) {
        var medlemnavn = medlemmer[j].navn;
        var hhBrukerId = medlemmer[j].brukerId;
        var epost = medlemmer[j].epost;
        if (!medlemnavn) medlemnavn = epost;
        if (hhBrukerId !== minBruker.brukerId) {
            $("#medlemmer").append('<li class ="valgtMedlem" id="medlem' + hhBrukerId + '"role="presentation" value="' + hhBrukerId + '"><a  role="menuitem" tabindex="-1" href="#">' + medlemnavn + '</a></li>');

        }
    }
}

function hentFellesGjoremal() {
    for (var i = 0, len = fellesgjoremal.length; i < len; i++) {
        var fellesnavn = he.encode(fellesgjoremal[i].beskrivelse);
        var frist = fellesgjoremal[i].frist;
        if (!frist) frist= ' ';
        var gjoremalId = fellesgjoremal[i].gjoremalId;

        $("#fellesGjoremaal").append('<li class="list-group-item ">' + '<b>' + fellesnavn + '</b>' +
            ",  " + frist +
            '<input id="checkboxid2' + gjoremalId + '" type="checkbox" class="all pull-right"></li>');
    }
}

/**
 *  Henter felles gjøremål fra gjoremalservice
 */

function hentFellesGjoremalData() {
    $.getJSON("server/gjoremalservice/" + husholdningId, function (data) {
        fellesgjoremal = data;
    });
}

/**
 * Lar deg hente dine egne gjøremål som opprettes med en id, beskrivelse og en frist.
 * Deretter legges den til på gjøremålsiden med en checkbox.
 */
function hentMinegjoremal() {
    for (var i = 0, len = minegjoremal.length; i < len; i++) {
        var gjoremalId = minegjoremal[i].gjoremalId;
        var beskrivelse = he.encode(minegjoremal[i].beskrivelse);
        var frist = minegjoremal[i].frist;
        if (!frist) frist = '';

        $("#mineGjoremaal").append('<li class="list-group-item minegjoremalliste" value="' + gjoremalId + '">' + '<b>' + beskrivelse + '</b>' +
            ",  " + frist +
            '<input id="checkboxid' + gjoremalId + '" type="checkbox" class="all pull-right"></li>');
    }
}

/**
 * Funksjonene kalles i starten av document.ready(). En timeout lar det gå millisekunder før
 * fellesgjøremål kan hentes. Dette fordi minegjøremål hentes fra localstorage og felles hentes fra
 * database.
 */
$(document).ready(function () {
    gethhData();
    hentFellesGjoremalData();
    hentMinegjoremal();
    setTimeout(function () {
        hentFellesGjoremal();
    }, 300);
    setTimeout(function () {
        hentMedlemmer();
    }, 300);

    /**
     * Sender hvilke gjøremål som er krysset av til database og oppdaterer listen av gjøremål.
     */
    $("body").on("click", "#refresh2", function () {
        for (var i = 0, len = fellesgjoremal.length; i < len; i++) {
            var gjoremal = fellesgjoremal[i];
            var gjoremalId = fellesgjoremal[i].gjoremalId;
            var fullfort = document.getElementById("checkboxid2" + gjoremalId).checked;
            if (fullfort) {

                $.ajax({
                    url: "server/gjoremalservice/fullfortfelles",
                    type: 'PUT',
                    data: JSON.stringify(gjoremal),
                    contentType: 'application/json; charset=utf-8',
                    dataType: 'json',
                    success: function (result) {
                        var data = JSON.parse(result); // gjør string til json-objekt
                        if (data) {
                            var index = fellesgjoremal.indexOf(gjoremal);
                            fellesgjoremal.splice(index, 1);
                            localStorage.setItem("bruker", JSON.stringify(minBruker));
                            alert("Det gikk bra!");
                        } else {
                            alert("feil!");
                        }
                        window.location = "gjoremaal.html";
                    },
                    error: function () {
                        $('#errorModal').modal('show');
                    }
                });
            }
        }
    });

    /**
     * Et klikk på refreshknappen på siden lar deg oppdatere gjøremålene slik at de som er huket
     * av forsvinner
     */

    $("body").on("click", "#refresh", function () {
        var ffListe = [];
        for (var i = 0, len = minegjoremal.length; i < len; i++) {
            var gjoremal = minegjoremal[i];
            var gjoremalId = minegjoremal[i].gjoremalId;
            var fullfort = document.getElementById("checkboxid" + gjoremalId).checked;
            if (fullfort) {
                ffListe.push(i);
            }
        }
        for (var j = ffListe.length - 1; j >= 0; j--) {
            gjoremal = minegjoremal[ffListe[j]];
            $.ajax({
                url: "server/gjoremalservice/fullfort",
                type: 'PUT',
                data: JSON.stringify(gjoremal),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                success: function (result) {
                    var data = JSON.parse(result); // gjør string til json-objekt
                    if (data) {
                    } else {
                        alert("feil!");
                    }

                },
                error: function () {
                    $('#errorModal').modal('show');
                }
            });
        }
        setTimeout(function () {
            for (var h = ffListe.length - 1; h >= 0; h--) {
                minegjoremal.splice(ffListe[h], 1);
            }
            minBruker.gjoremal = minegjoremal;
            localStorage.setItem("bruker", JSON.stringify(minBruker));
            window.location = "gjoremaal.html";

        }, 200)

    });

    /**
     * Lar deg lagre gjøremålet i databasen når det klikkes på knappen. Lagres med en beskrivelse,
     * en utførerId, frist og en husholdningsid for hvilken husholdning det skal vises i.
     */
    $("body").on("click", "#lagreGjoremal", function () {
        var beskrivelse = $("#gjoremalInput").val();
        var utforerid = id;
        var frist = $("#dato").val();
        var husholdningId = localStorage.getItem("husholdningId");

        if (id == "null") {
            id = 0;
        }
        var gjoremal = {
            beskrivelse: beskrivelse,
            hhBrukerId: utforerid,
            frist: frist,
            husholdningId: husholdningId
        };
        if (beskrivelse == "") {
            ($("#feil2").fadeIn(200)).delay(2500).fadeOut(2500);
            return;
        }

        if (!id){
            $("#feilmedlem").fadeIn();
            return;
        }
        /**
         * Dersom beskrivelsen av gjøremålet ikke er tomt skal det være et ajax-kall til gjøremål-
         * service under metoden nyttfellesgoremal.
         */
        $.ajax({
            url: "server/gjoremalservice/nyttfellesgjoremal",
            type: 'POST',
            data: JSON.stringify(gjoremal),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (result) {
                var data = JSON.parse(result); // gjør string til json-objekt
                if (data) {
                    window.location = "gjoremaal.html";
                } else {
                    $("#datafeilfelles").fadeIn();
                }
            },
            error: function () {
                $("#datafeilfelles").fadeIn();
            }
        });
    });

    /**
     * Lagrer egne gjøremål på samme måte som felles gjøremål.
     */
    $("body").on("click", "#lagreMineGjoremal", function () {
        var beskrivelse =$("#mineGjoremalInput").val();
        var frist = $("#minDato").val();
        var husholdningId = localStorage.getItem("husholdningId");

        var gjoremal = {
            hhBrukerId: minBruker.brukerId,
            beskrivelse: beskrivelse,
            frist: frist,
            husholdningId: parseInt(husholdningId)
        };

        if (beskrivelse == "") {
            $("#feil").fadeIn();
            return;
        }
        $.ajax({
            url: "server/gjoremalservice/nyttgjoremal",
            type: 'POST',
            data: JSON.stringify(gjoremal),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (result) {
                var data = JSON.parse(result); // gjør string til json-objekt
                if (data > 0) {
                    var gjoremal2 = {
                        gjoremalId: data,
                        husholdningId: parseInt(husholdningId),
                        hhBrukerId: minBruker.brukerId,
                        fullfort: false,
                        beskrivelse: beskrivelse,
                        frist: frist
                    };
                    minBruker.gjoremal.push(gjoremal2);
                    localStorage.setItem("bruker", JSON.stringify(minBruker));
                    window.location = "gjoremaal.html";
                } else {
                   $("#datafeil").fadeIn();
                }
            },
            error: function () {
                $("#datafeil").fadeIn();
            }
        });
    });
});