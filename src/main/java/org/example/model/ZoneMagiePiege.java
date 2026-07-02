package org.example.model;

/**
 * Emplacement réservé aux cartes Magie et Piège.
 */
public class ZoneMagiePiege {

    private Carte carte;
    private boolean faceCachee;
    private int tourPose;

    public ZoneMagiePiege() {
        this.carte = null;
        this.faceCachee = false;
        this.tourPose = -1;
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }

    public boolean isFaceCachee() {
        return faceCachee;
    }

    public void setFaceCachee(boolean faceCachee) {
        this.faceCachee = faceCachee;
    }

    public int getTourPose() {
        return tourPose;
    }

    public void setTourPose(int tourPose) {
        this.tourPose = tourPose;
    }

    public boolean estVide() {
        return carte == null;
    }

    public void vider() {
        carte = null;
        faceCachee = false;
        tourPose = -1;
    }
}