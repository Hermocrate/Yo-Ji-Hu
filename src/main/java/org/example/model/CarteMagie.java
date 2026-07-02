package org.example.model;

/**
 * Carte permettant d'appliquer un effet immédiat ou différé.
 */
public class CarteMagie extends Carte {

    private String effet;
    private EffetCarte effetCarte;

    public CarteMagie(
            String titre,
            String description,
            String cheminImage,
            EmplacementCarte emplacement,
            boolean faceCachee,
            String effet
    ) {
        this(
                titre,
                description,
                cheminImage,
                emplacement,
                faceCachee,
                effet,
                EffetCarte.PIOCHER_1
        );
    }

    public CarteMagie(
            String titre,
            String description,
            String cheminImage,
            EmplacementCarte emplacement,
            boolean faceCachee,
            String effet,
            EffetCarte effetCarte
    ) {
        super(titre, description, cheminImage, emplacement, faceCachee);
        this.effet = effet;
        this.effetCarte = effetCarte;
    }

    public String getEffet() {
        return effet;
    }

    public EffetCarte getEffetCarte() {
        return effetCarte;
    }
}