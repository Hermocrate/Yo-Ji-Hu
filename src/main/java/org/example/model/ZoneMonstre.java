package org.example.model;

/**
 * Emplacement pouvant accueillir un monstre.
 */
public class ZoneMonstre {

    private CarteMonstre carte;
    private PositionCarte position;
    private boolean aDejaAttaque;
    private boolean positionChangeeCeTour;

    public ZoneMonstre() {
        this.carte = null;
        this.position = null;
        this.aDejaAttaque = false;
        this.positionChangeeCeTour = false;
    }

    public CarteMonstre getCarte() {
        return carte;
    }

    public void setCarte(CarteMonstre carte) {
        this.carte = carte;
    }

    public PositionCarte getPosition() {
        return position;
    }

    public boolean isPositionChangeeCeTour() {
        return positionChangeeCeTour;
    }

    public void setPositionChangeeCeTour(boolean positionChangeeCeTour) {
        this.positionChangeeCeTour = positionChangeeCeTour;
    }

    public void setPosition(PositionCarte position) {
        this.position = position;
    }

    public boolean aDejaAttaque() {
        return aDejaAttaque;
    }

    public void setADejaAttaque(boolean aDejaAttaque) {
        this.aDejaAttaque = aDejaAttaque;
    }

    public boolean estVide() {
        return carte == null;
    }

    public void vider() {
        carte = null;
        position = null;
        aDejaAttaque = false;
        positionChangeeCeTour = false;
    }
}