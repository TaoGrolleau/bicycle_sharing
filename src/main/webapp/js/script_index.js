var villes = {
	 "Saint-Etienne": { "lat": 45.439695, "lon": 4.3871779 , "name": "Saint-Etienne" },
	 "Lyon": {"lat": 45.75, "lon": 4.85, "name": "Lyon" },
	 "Montpellier": {"lat": 43.62505, "lon": 3.862038, "name": "Montpellier" },
	 "Rennes": {"lat": 48.0833 , "lon": -1.6833, "name": "Rennes" },
	 "Strasbourg": {"lat": 48.5833, "lon": 7.75, "name": "Strasbourg" },
	 "Paris": {"lat": 48.8534, "lon": 2.3488, "name": "Paris" }
};

var lat = 48.852969;
var lon = 2.349903;

var macarte = null;

var smallIcon = new L.Icon({
    iconUrl: 'https://i.ibb.co/5kgHf7z/station.png',
    //iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-icon-2x.png',
    iconSize:    [48, 48],
    iconAnchor:  [24, 48],
    popupAnchor: [24, -32],
    //shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    shadowSize:  [41, 41]
  });


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

    for (ville in villes) {
        var marker = L.marker([villes[ville].lat, villes[ville].lon] ,{
            name: villes[ville].name,
            icon : smallIcon
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
    location.replace("http://localhost:8080/city?name=" + marker.options.name);
}