# SemWebProject

The Semantic Web project is a large and long practical exercise that consists in integrating all the pieces that have been seen during the first sessions into a consolidated Web application. To make sure you can advance sufficiently fast to cover everything, you are allowed to work by pair.

## Fichiers de données

Strasbourg et Montpellier ont des structures de fichiers XML similaires. [Structure des fichiers XML Files](https://data.montpellier3m.fr/dataset/disponibilite-des-places-velomagg-en-temps-reel)  
Lyon, Rennes et Saint-Etienne sont au format JSON.  
- [Strasbourg](http://velhop.strasbourg.eu/tvcstations.xml)
- [Montpellier](https://data.montpellier3m.fr/sites/default/files/ressources/TAM_MMM_VELOMAG.xml)
- [Lyon](https://download.data.grandlyon.com/wfs/rdata?SERVICE=WFS&VERSION=1.1.0&outputformat=GEOJSON&request=GetFeature&typename=jcd_jcdecaux.jcdvelov&SRSNAME=urn:ogc:def:crs:EPSG::4171)
- [Rennes](https://data.rennesmetropole.fr/api/records/1.0/search/?dataset=etat-des-stations-le-velo-star-en-temps-reel)
- [Paris](https://opendata.paris.fr/api/records/1.0/search/?dataset=velib-disponibilite-en-temps-reel&facet=overflowactivation&facet=creditcard&facet=kioskstate&facet=station_state)
- Stations à [Saint-Etienne](https://saint-etienne-gbfs.klervi.net/gbfs/en/station_information.json)
- Statut des stattions à [Saint-Etienne](https://saint-etienne-gbfs.klervi.net/gbfs/en/station_status.json)

## TO DO 

- Faire un vocabulaire RDF
- Quelles informations on souhaite conserver (Upload dans Fuseki) ?
    * nom de station
    * id station
    * lat
    * lon
    * nb vélos dispos
    * nb vélos utilisés
    * nb total vélos
    * Pour une ville, on liste les stations.
- Requêtes SPARQL permettant de mettre à jour le seveur Fuseki avec les différents fichier
- Site Web (Spring) permettant de choisir des villes
- Réponse en RDFa
- (Créer itinéraire)
- Javascript pour les cartes (OpenStreetMap) et leaflet 

## Infos supplémentaires

Il devrait être possible de rajouter n'importe quelle ville.
