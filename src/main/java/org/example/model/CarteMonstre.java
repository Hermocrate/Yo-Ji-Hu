package org.example.model;

/**
 * Représente une carte Monstre du jeu.
 * Gère ses statistiques et son type.
 */
public class CarteMonstre extends Carte {

    private String type;
    private int nombreEtoiles;
    private int attaque;
    private int defense;

    public CarteMonstre(
            String titre,
            String description,
            String cheminImage,
            EmplacementCarte emplacement,
            boolean faceCachee,
            String type,
            int nombreEtoiles,
            int attaque,
            int defense) {

        super(titre, description, cheminImage, emplacement, faceCachee);

        this.type = type;
        this.nombreEtoiles = nombreEtoiles;
        this.attaque = attaque;
        this.defense = defense;
    }

    public String getType() {
        return type;
    }

    public int getNombreEtoiles() {
        return nombreEtoiles;
    }

    public int getAttaque() {
        return attaque;
    }

    public int getDefense() {
        return defense;
    }


}