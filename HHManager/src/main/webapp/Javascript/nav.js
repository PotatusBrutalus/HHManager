$(document).ready(function (){
    $(function(){
        $("#navbar").load("nav.html");
        $("#modaldiv").load("lagnyhusstand.html");

    });

    $('body').on('click', 'a#bildenav', function() {
        window.location="forside.html"
    });

    $('body').on('click', 'a#gjøremålsknapp', function() {
        window.location="gjøremål.html"
    });
    $('body').on('click', 'a#kalenderknapp', function() {
        window.location="kalender.html"
    });
    $('body').on('click', 'a#handlelisteknapp', function() {
        window.location="handlelister.html"
    });
}); $('body').on('click', 'a#bildeknapp', function() {
    window.location="forside.html"
});
