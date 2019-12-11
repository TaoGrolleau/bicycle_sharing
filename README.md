# SemWebProject

The Semantic Web project is a large and long practical exercise that consists in integrating all the pieces that have been seen during the first sessions into a consolidated Web application. To make sure you can advance sufficiently fast to cover everything, you are allowed to work by pair.

## Setup

Apache Fuseki (create dataset /bicycle-sharing)

## Running

At first go to localhost:8080/ (initialise the database)
Redirect to index 

## What you can see

Possible refresh
Click on the map or the list

## Getting data

We're getting data from these cities :
- [Strasbourg](http://velhop.strasbourg.eu/tvcstations.xml)
- [Montpellier](https://data.montpellier3m.fr/sites/default/files/ressources/TAM_MMM_VELOMAG.xml)
- [Lyon](https://download.data.grandlyon.com/wfs/rdata?SERVICE=WFS&VERSION=1.1.0&outputformat=GEOJSON&request=GetFeature&typename=jcd_jcdecaux.jcdvelov&SRSNAME=urn:ogc:def:crs:EPSG::4171)
- [Rennes](https://data.rennesmetropole.fr/api/records/1.0/search/?dataset=etat-des-stations-le-velo-star-en-temps-reel)
- [Paris](https://opendata.paris.fr/api/records/1.0/search/?dataset=velib-disponibilite-en-temps-reel&facet=overflowactivation&facet=creditcard&facet=kioskstate&facet=station_state)
- Stations in [Saint-Etienne](https://saint-etienne-gbfs.klervi.net/gbfs/en/station_information.json)
- Station states in [Saint-Etienne](https://saint-etienne-gbfs.klervi.net/gbfs/en/station_status.json)

## Using librairies

- Java Spring
- HTML generated using Thymeleaf
- Bootstrap
- Javascript pour les cartes (OpenStreetMap) et [leaflet](https://leafletjs.com/) 

