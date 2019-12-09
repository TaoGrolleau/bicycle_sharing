
var macarte = null;

// Fonction d'initialisation de la carte
function initMap() {
    var markers = [];
    // Créer l'objet "macarte" et l'insèrer dans l'élément HTML qui a l'ID "map"
    macarte = L.map('map').setView([parseFloat(lat), parseFloat(lon)], 11);
    markerClusters = L.markerClusterGroup();
    // Leaflet ne récupère pas les cartes (tiles) sur un serveur par défaut. Nous devons lui préciser où nous souhaitons les récupérer. Ici, openstreetmap.fr
    L.tileLayer('https://{s}.tile.openstreetmap.fr/osmfr/{z}/{x}/{y}.png', {
        // Il est toujours bien de laisser le lien vers la source des données
        attribution: 'données © OpenStreetMap/ODbL - rendu OSM France',
        minZoom: 1,
        maxZoom: 20
    }).addTo(macarte);


    var marker = L.marker([parseFloat(lat), parseFloat(lon)]);
    marker.addTo(macarte);
    markers.push(marker);


    var group = new L.featureGroup(markers); // Nous créons le groupe des marqueurs pour adapter le zoom
    macarte.fitBounds(group.getBounds().pad(0.5));// Nous demandons à ce que tous les marqueurs soient visibles, et ajoutons un padding (pad(0.5)) pour que les marqueurs ne soient pas coupés
    macarte.addLayer(markerClusters);
}

window.onload = function(){
    // Fonction d'initialisation qui s'exécute lorsque le DOM est chargé
    initMap();
};
