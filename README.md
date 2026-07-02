Yo-Ji-Hu est un jeu de cartes inspiré de Yu-Gi-Oh!, développé en Java et JavaFX dans le cadre d'un projet d'apprentissage. Il s'agit d'une parodie humoristique mettant en scène des personnages issus de différentes œuvres de fiction et de la culture populaire.

⚠️ Ce projet est volontairement non officiel, parodique et réalisé à des fins éducatives. Il ne doit pas être copié, redistribué ou utilisé à des fins commerciales.

Yo-Ji-Hu propose des duels à deux joueurs avec :

Monstres normaux
Monstres à effet
Cartes Magie
Cartes Piège
Système de sacrifices
Phases de tour
Position attaque / défense
Combat entre monstres
Attaques directes
Points de vie
Cimetière consultable
Deck consultable
Écran de victoire
Illustrations personnalisées


Technologies utilisées:

Java 21
JavaFX
Maven
IntelliJ IDEA


Règles du jeu

Chaque joueur commence avec :

8000 Points de Vie
Un deck de 20 cartes
Une main de 5 cartes

Composition d'un deck :

6 monstres normaux
6 monstres à effet
5 cartes Magie
3 cartes Piège
Phases du tour
Phase de pioche
Main Phase 1
Battle Phase
Main Phase 2
Fin du tour
Monstres
Invocation normale

Un joueur peut effectuer une seule invocation normale par tour.

Les monstres possédant une étoile  nécessitent :

1 sacrifice
Positions possibles
Attaque face recto
Défense face recto
Défense face cachée

Une fois par tour, un monstre peut changer de position :

Attaque ↔ Défense
Monstres à effet

Les monstres à effet possèdent l'un des effets suivants :

Détruire un monstre adverse
Piocher 2 cartes puis défausser 1 carte
Gagner 1000 Points de Vie
Compter comme deux sacrifices
Infliger 500 dégâts
Détruire une Magie ou un Piège adverse
Attaquer directement les Points de Vie adverses
Cartes Magie

Une carte Magie peut :

Être activée immédiatement
Être posée face cachée

Effets disponibles :

Piocher 1 carte
Gagner 500 Points de Vie
Infliger 300 dégâts
Détruire un monstre adverse
Détruire une Magie ou un Piège adverse

Une carte Magie activée est envoyée au cimetière après sa résolution.

Cartes Piège

Les cartes Piège :

Sont toujours posées face cachée
Ne peuvent être activées qu'à partir du tour suivant

Effets disponibles :

Détruire un monstre adverse
Infliger 300 dégâts
Gagner 500 Points de Vie

Une fois activées, elles sont envoyées au cimetière.

Conditions de victoire

Un joueur gagne si :

Les Points de Vie de son adversaire atteignent 0.
Son adversaire doit piocher alors que son deck est vide.
Son adversaire abandonne.
Interface

L'interface comprend :

Terrain des deux joueurs
Deck et cimetière visibles
Main cachée du joueur adverse
Agrandissement des cartes
Animation des attaques
Animation des changements de phase
Animation de mélange du deck
Écran de victoire
Illustrations

Les illustrations des cartes sont chargées depuis :

src/main/resources/org/

Le nom de chaque image correspond au titre de la carte

Les caractères spéciaux, espaces et accents sont automatiquement supprimés.

Lancement du projet

Cloner le dépôt :

git clone <url_du_projet>

Se placer dans le dossier :

cd Yo-Ji-Hu

Compiler :

mvn clean install

Lancer :

mvn javafx:run
Avertissement

Yo-Ji-Hu est une œuvre parodique développée dans le cadre d'un apprentissage du langage Java et de JavaFX.

Les personnages, références et univers représentés appartiennent à leurs ayants droit respectifs.

Ce projet est :

non officiel ;
sans but lucratif ;
purement éducatif ;
volontairement humoristique ;
interdit à la redistribution ou à la commercialisation sans autorisation.
© HERMOCRATE – 2025

Projet personnel réalisé dans le cadre de l'apprentissage de Java et JavaFX.