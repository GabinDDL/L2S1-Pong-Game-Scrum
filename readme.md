# Jeu Anniversaire de Pong

## Présentation

Pong World est un jeu de raquettes programmé en Java 17. Le projet est configuré avec Gradle utilisant le plugin JavaFX. Ce jeu est largement inspiré du jeu [Pong](https://fr.wikipedia.org/wiki/Pong), un classique des salles d'arcades dans les années 1970.
Nous vous proposons ici une version améliorée, avec différents modes de jeu et une apparence remasterisée à l'occasion des 50 ans de la sortie du jeu.

Le principe original est simple : un terrain (le "*court*"), deux raquettes et une balle. Le jeu se joue à deux, chaque joueur pouvant déplacer sa raquette sur un axe haut/bas et ayant pour but de ne pas laisser passer la balle derrière sa raquette (sinon, l'adversaire marque un point). La balle se déplace à une vitesse qui varie selon la manière dont elle est frappée par les raquettes et rebondit sur les parois (limites haute et basse de la fenêtre).

Voici les fonctionnalités qui sont implémentées :   
-  2 raquettes contrôlables par deux joueurs sur le même ordinateur dans la même fenêtre (SHIFT/CTRL à gauche, UP/DOWN à droite)  
        **OU**  
      2 joueurs jouant sur deux ordinateurs différents
         **OU**  
      1 joueur contre un bot avec trois niveaux de difficulté
- détection de sortie du terrain par les côtés = but marqué
- gestion d'un compteur de points pour chaque joueur
- gestion des fins de partie avec nombre de points gagnants modifiable  
- fonction pause du jeu et retour à l'écran principal
- physique de la balle réaliste : accélèration ou ralentissement selon si la raquette se déplace dans le même sens qu'elle ou à contresens
- déplacement réaliste des raquettes avec effet de glissement
- gestion complexe des rebonds de la balle sur les différents objets 
- aléas : une deuxième balle qui apparait, augmentation de la vitesse de la raquette, augmentation de l'effet de glissement de la raquette, double point
- choix par le joueur des paramètres d'une partie : nombre de points gagnants avec la règle des deux points, mode bot, mode serveur...
- musique de fond et bruitages, activables/désactivables
- navigation entre différents écrans : écran d'écran d'accueil au lancement du jeu, écran principal, écran d'options, écrans des modes de jeu
- lancement facile du jeu grâce à un installateur

# Instructions

## Télécharger Pong
Pour clôner ce projet, utilisez la commande suivante depuis la console :   
```$ git clone https://gaufre.informatique.univ-paris-diderot.fr/tsoan/pong```  
ou bien   
```$ git clone git@gaufre.informatique.univ-paris-diderot.fr:tsoan/pong.git```

# Exécution, compilation  

## Configuration nécessaire

Avoir une machine virtuelle Java version 17

## Instructions

- Extraire le dossier adapté selon votre système d'exploitation
- Aller dans le dossier bin
- Lancer le script
- Et voilà!

## Exemples

### Exemple avec Windows
- Télécharger `INSTALL\PongWorld-Windows.zip`
- Extraire `<Votre dossier où vous avez téléchargé le .zip>\PongWorld-Windows.zip`
- Exécuter `<Votre dossier où vous avez téléchargé le .zip>\PongWorld-Windows\bin\pong.bat`

### Exemple avec Linux
- Télécharger `INSTALL/PongWorld-Linux.zip`
- Extraire `<Votre dossier où vous avez téléchargé le .zip>/PongWorld-Linux.zip`
- Exécuter `<Votre dossier où vous avez téléchargé le .zip>/PongWorld-Linux/bin/pong`

## Jouer
Une fois sur le menu d'accueil, vous trouverez le panneau d'options, accessible sur la droite. Pour accéder aux différents modes de jeux, descendez à l'aide des flèches. Sélectionnez le mode de jeu auquel vous souhaitez jouer et préparez vous à un match effréné !

### Contrôles  
Si vous jouez à deux sur un même ordinateur, la raquette de gauche est contrôlée par les touches CONTROL et SHIFT, alors que celle de droite est contrôlée par les flèches HAUT et BAS. Si vous jouez en mode serveur à distance, les touches de contrôle sont CONTROL et SHIFT pour les deux joueurs.   
Vous pouvez mettre le jeu en pause avec la touche ECHAP.

## Video  
[Lien vers la vidéo de présentation](https://www.youtube.com/watch?v=4_V38n7mK60)  
  
## Site  
[Lien vers la page de présentation du jeu](https://pongworld.teleporthq.app/)  
  
## Membres  
Fichier contenant les membres du groupe : `membres.txt`
