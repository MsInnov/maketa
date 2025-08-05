# maketa

Application Android à destination des recruteurs

Il s’agit d’une application e-commerce permettant d’acheter et de vendre des produits.

Fonctionnalités :
 - Connexion
 - Inscription
 - Ajouter des produits aux favoris
 - Vendre un produit
 - Ajouter des produits au panier
 - Acheter les produits du panier
 - Filtrer la liste des produits par catégorie et par prix
   
L’application s’interface avec une API gratuite et figée : aucune donnée sur le serveur ne peut être ajoutée, modifiée ou supprimée.
Cela entraîne les limitations suivantes :
 - Pas de nouvelle inscription
 - Pas d’ajout de produit à la liste en cas de vente
 - Pas de suppression de produit de la liste en cas d’achat
 - Identifiant et mot de passe pré-renseignés pour faciliter l’utilisation de l’application
   
Stack technique :
 - Kotlin
 - Jetpack Compose
 - ROOM
 - Dagger Hilt
 - Retrofit2
 - Coil
 - MOCKK (tests unitaires)
 - Turbine (tests unitaires)
 - Remote Config (Firebase)
   
Architecture :
 - Clean Architecture
 - Principes SOLID
 - MVI
