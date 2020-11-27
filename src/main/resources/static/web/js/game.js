$(function() {
    loadData();
});

function updateView(data) {
    let htmlList = data.map(function (game) {
        return  '<li>' + new Date(game.created).toLocaleString() + ' ' + game.gamePlayers.map(function(p) { return p.player.email}).join(',')  +'</li>';
    }).join('');
  document.getElementById("games-list").innerHTML = htmlList;
}

// load and display JSON sent by server for /players

function loadData() {
    $.get("/api/games")
        .done(function(data) {
          updateView(data);
        })
        .fail(function( jqXHR, textStatus ) {
          alert( "Failed: " + textStatus );
        });
}

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};

function loadData(){
    $.get('/api/game_view/'+getParameterByName('gp'))
        .done(function(data) {
            let playerInfo;
            if(data.gamePlayers[0].id == getParameterByName('gp'))
                playerInfo = [data.gamePlayers[0].player.email,data.gamePlayers[1].player.email];
            else
                playerInfo = [data.gamePlayers[1].player.email,data.gamePlayers[0].player.email];

            $('#playerInfo').text(playerInfo[0] + '(you) vs ' + playerInfo[1]);

            data.ships.forEach(function(shipPiece){
                shipPiece.locations.forEach(function(shipLocation){
                    $('#'+shipLocation).addClass('ship-piece');
                })
            });
        })
        .fail(function( jqXHR, textStatus ) {
          alert( "Failed: " + textStatus );
        });
};