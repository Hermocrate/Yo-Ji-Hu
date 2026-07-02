package org.example.model;

public abstract class Carte {

    private String titre;
    private String description;
    private String cheminImage;

    private EmplacementCarte emplacement;
    private boolean faceCachee;


    public Carte(String titre, String description, String cheminImage, EmplacementCarte emplacement, boolean faceCachee) {
        this.titre = titre;
        this.description = description;
        this.cheminImage = cheminImage;
        this.emplacement = emplacement;
        this.faceCachee = faceCachee;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public String getCheminImage() {
        return cheminImage;
    }

    public EmplacementCarte getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(EmplacementCarte emplacement) {
        this.emplacement = emplacement;
    }
}