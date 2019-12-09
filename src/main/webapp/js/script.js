var villes = {
	 "Saint-Etienne": { "lat": 45.439695, "lon": 4.3871779},
	 "Lyon": {"lat": 45.75, "lon": 4.85},
	 "Montpellier": {"lat": 43.62505, "lon": 3.862038},
	 "Rennes": {"lat": 48.0833 , "lon": -1.6833},
	 "Strasbourg": {"lat": 48.5833, "lon": 7.75},
	 "Paris": {"lat": 48.8534, "lon": 2.3488}
};

var lat = 48.852969;
var lon = 2.349903;

var macarte = null;

// Fonction d'initialisation de la carte
function initMap() {
    var markers = [];
    // Créer l'objet "macarte" et l'insèrer dans l'élément HTML qui a l'ID "map"
    macarte = L.map('map').setView([lat, lon], 11);
    markerClusters = L.markerClusterGroup();
    // Leaflet ne récupère pas les cartes (tiles) sur un serveur par défaut. Nous devons lui préciser où nous souhaitons les récupérer. Ici, openstreetmap.fr
    L.tileLayer('https://{s}.tile.openstreetmap.fr/osmfr/{z}/{x}/{y}.png', {
        // Il est toujours bien de laisser le lien vers la source des données
        attribution: 'données © OpenStreetMap/ODbL - rendu OSM France',
        minZoom: 1,
        maxZoom: 20
    }).addTo(macarte);

    for (station in listStationName) {
        var marker = L.marker([parseFloat(listStationLat[station]), parseFloat(listStationLon[station])] ,{
                                                                                                              id: listId[station],
                                                                                                              title: listStationName[station],
                                                                                                              someCustomProperty: 'Adding custom data to this marker!',
                                                                                                              anotherCustomProperty: 'More custom data to this marker!'
                                                                                                          });
        marker.addTo(macarte);
        markers.push(marker);
    }

    var group = new L.featureGroup(markers); // Nous créons le groupe des marqueurs pour adapter le zoom
    group.on('click',onClick);
    macarte.fitBounds(group.getBounds().pad(0.1));// Nous demandons à ce que tous les marqueurs soient visibles, et ajoutons un padding (pad(0.5)) pour que les marqueurs ne soient pas coupés
    macarte.addLayer(markerClusters);
}

window.onload = function(){
    // Fonction d'initialisation qui s'exécute lorsque le DOM est chargé
    initMap();
};

function onClick(e){
    var marker = e.layer;
    location.replace("http://localhost:8080/station?id=" + marker.options.id);
}



