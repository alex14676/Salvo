$(function() {
    loadGamesData();
    loadLeaderboardData();
});

function updateGamesView(data) {

    //Comente lo anterior por las dudas.

    /*let htmlList = data.map(function (game) {
        return  '<li>' + new Date(game.created).toLocaleString() + ' ' + game.gamePlayers.map(function(p) { return p.player.email}).join(', ')
        +'</li>';
    }).join('');*/

    //El codigo es muy similar, solo que en vez de una lista me arme una tabla.

    let htmlTable = data.map(function (game) {
        return   '<tr><td>' + game.id + '</td>' +
                 '<td>' + new Date(game.created).toLocaleString() + '</td>' +
                 game.gamePlayers.map(function(p) { return '<td>' + p.player.nombre + '</td>' + '<td>' + p.player.email + '</td>'}).join('') + '</tr>';
                 }).join('');
    document.getElementById("table-body1").innerHTML = htmlTable;
  //document.getElementById("games-list").innerHTML = htmlList;
}

function updateLeaderboardView(data){

data.sort((a, b) =>  parseFloat(b.score.total)-parseFloat(a.score.total))
console.log(data)
let htmlTable = data.map(function (scores) {
  return   '<tr><td>' + scores.email + '</td>' +
          '<td>' + scores.score.total + '</td>' + '<td>' + scores.score.won + '</td>' + '<td>' + scores.score.lost + '</td>' + '<td>' + scores.score.tied + '</td>'+ '</tr>';}).join('');
document.getElementById("table-body2").innerHTML = htmlTable;
}


// load and display JSON sent by server for /players

function loadGamesData() {
  $.get("/api/games")
      .done(function(data) {
        updateGamesView(data);
      })
      .fail(function( jqXHR, textStatus ) {
        alert( "Failed: " + textStatus );
      });
}

function loadLeaderboardData(){
    $.get("/api/leaderboard")
      .done(function(data) {
        updateLeaderboardView(data);
      })
      .fail(function( jqXHR, textStatus ) {
        alert( "Failed: " + textStatus );
      });
}