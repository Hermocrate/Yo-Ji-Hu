package org.example.model;

/**
 * Carte activable à partir du tour suivant sa pose.
 */
public class CartePiege extends Carte {

    private String effet;
    private EffetCarte effetCarte;

    public CartePiege(
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
                EffetCarte.INFLIGER_300
        );
    }

    public CartePiege(
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