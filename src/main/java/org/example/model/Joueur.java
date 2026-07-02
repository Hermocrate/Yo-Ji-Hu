package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Représente un joueur ainsi que toutes ses cartes.
 */

public class Joueur {

    private String nom;
    private int pointsVie;

    private List<Carte> deck;
    private List<Carte> main;
    private List<Carte> cimetiere;
    private Terrain terrain;

    public Joueur(String nom) {
        this.nom = nom;
        this.pointsVie = 8000;

        this.deck = new ArrayList<>();
        this.main = new ArrayList<>();
        this.cimetiere = new ArrayList<>();
        this.terrain = new Terrain();
    }

    public String getNom() {
        return nom;
    }

    public int getPointsVie() {
        return pointsVie;
    }

    public void setPointsVie(int pointsVie) {
        this.pointsVie = Math.max(pointsVie, 0);
    }

    public void perdrePointsVie(int valeur) {
        pointsVie -= valeur;

        if (pointsVie < 0) {
            pointsVie = 0;
        }
    }

    // Zones appartenant au joueur
    public List<Carte> getDeck() {
        return deck;
    }

    public List<Carte> getMain() {
        return main;
    }

    public List<Carte> getCimetiere() {
        return cimetiere;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    /**
     * Mélange aléatoirement le deck.
     */
    public void melangerDeck() {
        Collections.shuffle(deck);
    }

    /**
     * Pioche la première carte du deck.
     *
     * @return true si la pioche a réussi, false si le deck est vide.
     */
    public boolean piocher() {
        if (deck.isEmpty()) {
            return false;
        }

        Carte carte = deck.remove(0);
        main.add(carte);
        carte.setEmplacement(EmplacementCarte.MAIN);

        while (main.size() > 6) {
            Carte defaussee = main.remove(main.size() - 1);
            cimetiere.add(defaussee);
            defaussee.setEmplacement(EmplacementCarte.CIMETIERE);
        }

        return true;
    }

    public void envoyerAuCimetiere(Carte carte) {
        main.remove(carte);
        cimetiere.add(carte);
        carte.setEmplacement(EmplacementCarte.CIMETIERE);
    }
}