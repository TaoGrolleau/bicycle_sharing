
var macarte = null;

var badIcon = new L.Icon({
    iconUrl: 'https://i.ibb.co/phRCF6S/selected-bad.png',
    //iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-icon-2x.png',
    iconSize:    [48, 48],
    iconAnchor:  [24, 48],
    popupAnchor: [24, -32],
    //shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    shadowSize:  [41, 41]
});

var okIcon = new L.Icon({
    iconUrl: 'https://i.ibb.co/1Kqmf4v/station-ok.png',
     //iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-icon-2x.png',
     iconSize:    [48, 48],
     iconAnchor:  [24, 48],
     popupAnchor: [24, -32],
     //shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
     shadowSize:  [41, 41]
});

var goodIcon = new L.Icon({
    iconUrl: 'https://i.ibb.co/P610D5T/station-good.png',
    //iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-icon-2x.png',
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
    macarte = L.map('map').setView([parseFloat(lat_found), parseFloat(lon_found)], 11);
    markerClusters = L.markerClusterGroup();
    // Leaflet ne récupère pas les cartes (tiles) sur un serveur par défaut. Nous devons lui préciser où nous souhaitons les récupérer. Ici, openstreetmap.fr
    L.tileLayer('https://{s}.tile.openstreetmap.fr/osmfr/{z}/{x}/{y}.png', {
        // Il est toujours bien de laisser le lien vers la source des données
        attribution: 'données © OpenStreetMap/ODbL - rendu OSM France',
        minZoom: 15,
        maxZoom: 18
    }).addTo(macarte);


    for (station in listStationName) {
            var averageCapacity = parseFloat(listBikes[station])/parseFloat(listCapacity[station]) * 100;
            var lat_stat = parseFloat(listStationLat[station]);
            var lon_stat = parseFloat(listStationLon[station]);

            if (lat_stat == parseFloat(lat_found) && lon_stat == parseFloat(lon_found)){

                if(averageCapacity <= 10){
                    var marker_found = L.marker([lat_stat, lon_stat] ,{
                        id: listId[station],
                        title: listStationName[station],
                        icon : badIcon
                    });
                }

                if(averageCapacity <=30 && averageCapacity> 10){
                    var marker_found = L.marker([lat_stat, lon_stat] ,{
                        id: listId[station],
                        title: listStationName[station],
                        icon : okIcon
                    });
                }

                if(averageCapacity > 30 ){
                    var marker_found = L.marker([lat_stat, lon_stat] ,{
                        id: listId[station],
                        title: listStationName[station],
                        icon : goodIcon
                    });
                }

                marker_found.addTo(macarte);
                markers.push(marker_found);
            }
            else{
                if(averageCapacity <= 10){
                    var marker = L.marker([lat_stat, lon_stat] ,{
                        id: listId[station],
                        title: listStationName[station],
                        icon : badIcon
                    });
                }

                if(averageCapacity <=30 && averageCapacity> 10){
                    var marker = L.marker([lat_stat, lon_stat] ,{
                        id: listId[station],
                        title: listStationName[station],
                        icon : okIcon
                    });
                }

                if(averageCapacity > 30 ){
                    var marker = L.marker([lat_stat, lon_stat] ,{
                        id: listId[station],
                        title: listStationName[station],
                        icon : goodIcon
                    });
                }

                marker.addTo(macarte);
                markers.push(marker);
            }


        }



}

window.onload = function(){
    // Fonction d'initialisation qui s'exécute lorsque le DOM est chargé
    initMap();
};
