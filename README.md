# maketa
Application Android à destination des recruteurs

l'application est une app e-commerce, qui propose d'acheter et de vendre des produits.

Fonctionnalités :
 - Connexion
 - S'enregistrer
 - Ajouter des produits en favoris
 - Vendre un produit
 - Ajouter des produits en panier
 - Acheter les produits du panier
 - Filtrer la liste des produits par catégorie et par prix.

l'app s'interface avec une API gratuite figé, aucune des informations du serveur ne peuvent être ajoutés, modifiés ou supprimés.
Il y a donc des limitations : 
 - Pas de nouvelle inscription
 - Pas d'ajout de produit de la liste en cas de vente
 - Pas de supression de la liste des produits en cas d'achat
 - Utilisateur et password pré-affiché pour faciliter l'utilisation de l'app

Stack technique : 
 - Kotlin
 - Jetpack Compose
 - ROOM
 - Dagger Hilt
 - Retrofit2
 - Coil
 - MOCKK (Test Unitaire)
 - Turbine (Test Unitaire)
 - Remote Config (Firebase)

Architecture :
 - Clean Architecture
 - Principe SOLID
 - MVI
