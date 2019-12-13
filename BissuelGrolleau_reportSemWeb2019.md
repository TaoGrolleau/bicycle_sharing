# Semantic Web Project

Made by Quentin  Bissuel and Tao Grolleau

## Semantic Web technologies

We used Semantic Web technologies in order to :
- Generate correct RDFa pages changing with the real time data we have.
- Querying a RDF database to get relevant information about bicycle stations.
- Updating a RDF database to automatically insert data from distant sources.

## Choices we've made

We tried to use SPARQL Generate at first but we had problems to integrate SPARQL Generate in our project.
We then made the choice to develop specific Java parser for each city. So we can't add new city without modifying the source code. 

## Repartition of the work

#### Quentin

- SPARQL Generate files allowing to initiate the database.
- Display of the website.
- JavaScript map using leaflet.
- Generate the RDFa pages with Thymeleaf.
- XML parser for the cities of Montpellier and Strasbourg.
- JSON parser for the city of Saint-Etienne.

#### Tao

- Setup of the RDF database.
- Queries on the RDF database (SELECT).
- Update of the database and of the real time data.
- JSON parsers for the cities of Rennes, Paris and Lyon.
- Spring models, repositories and controllers used for the generation of the HTML pages.

#### Both

- Definition of the RDF vocabulary

## Good aspects of the project

In a user-friendly interface, we display some cities (6) but it is not adaptable enough to easily add new cities.
We show the state of the stations directly on the map : 
- Green means that at most 70% of the bikes are not available.
- Orange means between 70% and 90% of the bikes not available.
- Red means more than 90% are not available.  

We use also real time data that can be refresh using the reload button.
