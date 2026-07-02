package org.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Gère le déroulement complet d'une partie.
 */
public class Partie {

    // Informations générales de la carte
    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueurActif;
    private PhaseTour phaseActuelle;

    private boolean invocationNormaleEffectuee;
    private Joueur gagnant;

    private Joueur joueurAttaquantSelectionne;
    private int indexMonstreAttaquant;

    private int numeroTour;

    private Joueur joueurInvocationEnAttente;
    private CarteMonstre monstreAInvoquer;
    private int zoneInvocation;
    private PositionCarte positionInvocation;
    private int sacrificesNecessaires;
    private List<Integer> zonesSacrificesSelectionnees;

    public Partie(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.joueurActif = joueur1;
        this.phaseActuelle = PhaseTour.PHASE_TIRAGE;
        this.invocationNormaleEffectuee = false;
        this.gagnant = null;
        this.joueurAttaquantSelectionne = null;
        this.indexMonstreAttaquant = -1;
        this.numeroTour = 1;

        this.joueurInvocationEnAttente = null;
        this.monstreAInvoquer = null;
        this.zoneInvocation = -1;
        this.positionInvocation = null;
        this.sacrificesNecessaires = 0;
        this.zonesSacrificesSelectionnees = new ArrayList<>();
    }

    public void commencerPartie() {
        joueur1.melangerDeck();
        joueur2.melangerDeck();

        for (int i = 0; i < 5; i++) {
            joueur1.piocher();
            joueur2.piocher();
        }

        joueurActif = joueur1;
        phaseActuelle = PhaseTour.PHASE_TIRAGE;
    }

    // Gestion des tours
    public void phaseSuivante() {
        if (gagnant != null) return;

        annulerInvocationEnAttente();

        switch (phaseActuelle) {
            case PHASE_TIRAGE -> {
                boolean succes = joueurActif.piocher();

                if (!succes) {
                    terminerPartie(getAdversaire(joueurActif));
                    return;
                }

                phaseActuelle = PhaseTour.MAIN_PHASE_1;
            }

            case MAIN_PHASE_1 -> {
                reinitialiserAttaques(joueurActif);
                phaseActuelle = PhaseTour.BATTLE_PHASE;
            }

            case BATTLE_PHASE -> {
                viderSelectionAttaque();
                phaseActuelle = PhaseTour.MAIN_PHASE_2;
            }

            case MAIN_PHASE_2, END_PHASE -> {
                reinitialiserPositions(joueurActif);
                changerJoueur();
                numeroTour++;
                phaseActuelle = PhaseTour.PHASE_TIRAGE;
                invocationNormaleEffectuee = false;
                viderSelectionAttaque();
            }
        }
    }

    private void changerJoueur() {
        joueurActif = getAdversaire(joueurActif);
    }

    private void reinitialiserAttaques(Joueur joueur) {
        for (ZoneMonstre zone : joueur.getTerrain().getZonesMonstres()) {
            zone.setADejaAttaque(false);
        }
    }

    private void reinitialiserPositions(Joueur joueur) {
        for (ZoneMonstre zone : joueur.getTerrain().getZonesMonstres()) {
            zone.setPositionChangeeCeTour(false);
        }
    }

    public boolean peutChangerPosition(Joueur joueur, int indexZone) {
        if (gagnant != null) return false;
        if (invocationEnAttente()) return false;
        if (joueur != joueurActif) return false;

        if (phaseActuelle != PhaseTour.MAIN_PHASE_1
                && phaseActuelle != PhaseTour.MAIN_PHASE_2) {
            return false;
        }

        ZoneMonstre zone =
                joueur.getTerrain()
                        .getZonesMonstres()[indexZone];

        if (zone.estVide()) return false;
        if (zone.isPositionChangeeCeTour()) return false;

        return true;
    }

    public void changerPosition(Joueur joueur, int indexZone) {
        if (!peutChangerPosition(joueur, indexZone)) return;

        ZoneMonstre zone =
                joueur.getTerrain()
                        .getZonesMonstres()[indexZone];

        if (zone.getPosition() == PositionCarte.ATTAQUE_RECTO) {
            zone.setPosition(PositionCarte.DEFENSE_RECTO);
        } else {
            zone.setPosition(PositionCarte.ATTAQUE_RECTO);
        }

        zone.setPositionChangeeCeTour(true);
    }

    public boolean peutJouerCarteDepuisMain(Joueur joueur) {
        return joueur == joueurActif &&
                (phaseActuelle == PhaseTour.MAIN_PHASE_1 ||
                        phaseActuelle == PhaseTour.MAIN_PHASE_2);
    }

    public boolean peutActiverMagiePiege(Joueur joueur, int indexZone) {

        ZoneMagiePiege zone =
                joueur.getTerrain()
                        .getZonesMagiePiege()[indexZone];

        if (zone.estVide()) {
            return false;
        }

        Carte carte = zone.getCarte();

        if (carte instanceof CarteMagie) {
            return joueur == joueurActif
                    && zone.getTourPose() < numeroTour
                    && (
                    phaseActuelle == PhaseTour.MAIN_PHASE_1
                            || phaseActuelle == PhaseTour.MAIN_PHASE_2
            );
        }

        if (carte instanceof CartePiege) {
            return zone.getTourPose() < numeroTour;
        }

        return false;
    }

    public boolean activerMagiePiege(Joueur joueur, int indexZone) {

        if (!peutActiverMagiePiege(joueur, indexZone)) {
            return false;
        }

        ZoneMagiePiege zone =
                joueur.getTerrain()
                        .getZonesMagiePiege()[indexZone];

        Carte carte = zone.getCarte();

        if (carte instanceof CarteMagie magie) {
            appliquerEffetCarte(joueur, magie.getEffetCarte());
        }

        if (carte instanceof CartePiege piege) {
            appliquerEffetCarte(joueur, piege.getEffetCarte());
        }

        zone.vider();

        carte.setEmplacement(EmplacementCarte.CIMETIERE);
        joueur.getCimetiere().add(carte);

        return true;
    }

    public void activerCarteDepuisMain(Joueur joueur, Carte carte) {

        if (carte instanceof CarteMagie magie) {
            appliquerEffetCarte(joueur, magie.getEffetCarte());
        }

        if (carte instanceof CartePiege piege) {
            appliquerEffetCarte(joueur, piege.getEffetCarte());
        }

        carte.setEmplacement(EmplacementCarte.CIMETIERE);
        joueur.getCimetiere().add(carte);
    }

    private void appliquerEffetCarte(Joueur joueur, EffetCarte effet) {

        Joueur adversaire = getAdversaire(joueur);

        switch (effet) {

            case PIOCHER_1 -> {
                boolean succes = joueur.piocher();

                if (!succes) {
                    terminerPartie(adversaire);
                }
            }

            case GAGNER_500_PV -> {
                joueur.setPointsVie(
                        joueur.getPointsVie() + 500
                );
            }

            case INFLIGER_300 -> {
                infligerDegats(adversaire, 300);
            }

            case DETRUIRE_MONSTRE_ADVERSE -> {
                detruirePremierMonstre(adversaire);
            }

            case DETRUIRE_MAGIE_PIEGE_ADVERSE -> {
                detruirePremiereMagiePiege(adversaire);
            }
        }
    }

    private void appliquerEffetMonstre(Joueur joueur, CarteMonstreEffet monstre) {
        Joueur adversaire = getAdversaire(joueur);

        switch (monstre.getEffet()) {
            case DETRUIRE_MONSTRE_ADVERSE -> detruirePremierMonstre(adversaire);

            case PIOCHER_2_DEFAUSSER_1 -> {
                joueur.piocher();
                joueur.piocher();

                if (!joueur.getMain().isEmpty()) {
                    Carte defaussee =
                            joueur.getMain()
                                    .remove(joueur.getMain().size() - 1);

                    defaussee.setEmplacement(EmplacementCarte.CIMETIERE);
                    joueur.getCimetiere().add(defaussee);
                }
            }

            case GAGNER_1000_PV -> joueur.setPointsVie(
                    joueur.getPointsVie() + 1000
            );

            case COMPTE_DOUBLE_SACRIFICE -> {
            }

            case INFLIGER_500 -> infligerDegats(adversaire, 500);

            case DETRUIRE_MAGIE_PIEGE_ADVERSE -> detruirePremiereMagiePiege(adversaire);

            case ATTAQUE_DIRECTE_PV -> {
            }
        }
    }

    private boolean detruirePremierMonstre(Joueur joueur) {
        ZoneMonstre[] zones = joueur.getTerrain().getZonesMonstres();

        for (int i = 0; i < zones.length; i++) {
            if (!zones[i].estVide()) {
                envoyerMonstreAuCimetiere(joueur, i);
                return true;
            }
        }

        return false;
    }

    private boolean detruirePremiereMagiePiege(Joueur joueur) {
        ZoneMagiePiege[] zones = joueur.getTerrain().getZonesMagiePiege();

        for (ZoneMagiePiege zone : zones) {
            if (!zone.estVide()) {
                Carte carte = zone.getCarte();

                zone.vider();

                carte.setEmplacement(EmplacementCarte.CIMETIERE);
                joueur.getCimetiere().add(carte);

                return true;
            }
        }

        return false;
    }

    public int calculerNombreSacrifices(CarteMonstre monstre) {
        if (monstre.getNombreEtoiles() <= 0) {
            return 0;
        }

        return 1;
    }

    private int valeurSacrifice(ZoneMonstre zone) {
        if (zone.estVide()) {
            return 0;
        }

        CarteMonstre monstre = zone.getCarte();

        if (monstre instanceof CarteMonstreEffet effet
                && effet.getEffet() == EffetMonstre.COMPTE_DOUBLE_SACRIFICE) {
            return 2;
        }

        return 1;
    }

    public boolean peutSacrifierPour(Joueur joueur, CarteMonstre monstre, int zoneDestination) {
        int sacrificesNecessaires = calculerNombreSacrifices(monstre);
        int totalDisponible = 0;

        ZoneMonstre[] zones = joueur.getTerrain().getZonesMonstres();

        for (int i = 0; i < zones.length; i++) {
            if (i != zoneDestination) {
                totalDisponible += valeurSacrifice(zones[i]);
            }
        }

        return totalDisponible >= sacrificesNecessaires;
    }

    public boolean commencerInvocationAvecSacrifices(
            Joueur joueur,
            CarteMonstre monstre,
            int zoneDestination,
            PositionCarte position
    ) {
        if (!peutInvoquer(joueur)) return false;
        if (!joueur.getMain().contains(monstre)) return false;

        ZoneMonstre zone =
                joueur.getTerrain()
                        .getZonesMonstres()[zoneDestination];

        if (!zone.estVide()) return false;

        int nbSacrifices = calculerNombreSacrifices(monstre);

        if (nbSacrifices == 0) {
            return invoquerMonstreDirectement(
                    joueur,
                    monstre,
                    zoneDestination,
                    position
            );
        }

        if (!peutSacrifierPour(joueur, monstre, zoneDestination)) {
            return false;
        }

        joueurInvocationEnAttente = joueur;
        monstreAInvoquer = monstre;
        zoneInvocation = zoneDestination;
        positionInvocation = position;
        sacrificesNecessaires = nbSacrifices;
        zonesSacrificesSelectionnees.clear();

        return true;
    }

    private boolean invoquerMonstreDirectement(
            Joueur joueur,
            CarteMonstre monstre,
            int zoneDestination,
            PositionCarte position
    ) {
        ZoneMonstre zone =
                joueur.getTerrain()
                        .getZonesMonstres()[zoneDestination];

        joueur.getMain().remove(monstre);

        zone.setCarte(monstre);
        zone.setPosition(position);
        zone.setPositionChangeeCeTour(true);

        monstre.setEmplacement(EmplacementCarte.TERRAIN);

        marquerInvocationNormale();

        if (monstre instanceof CarteMonstreEffet effet) {
            appliquerEffetMonstre(joueur, effet);
        }

        return true;
    }

    public void selectionnerSacrifice(Joueur joueur, int indexZone) {
        if (!invocationEnAttente()) return;
        if (joueur != joueurInvocationEnAttente) return;
        if (indexZone == zoneInvocation) return;

        ZoneMonstre zone =
                joueur.getTerrain()
                        .getZonesMonstres()[indexZone];

        if (zone.estVide()) return;

        if (zonesSacrificesSelectionnees.contains(indexZone)) {
            zonesSacrificesSelectionnees.remove(Integer.valueOf(indexZone));
        } else {
            zonesSacrificesSelectionnees.add(indexZone);
        }

        if (totalSacrificesSelectionnes(joueur) >= sacrificesNecessaires) {
            terminerInvocationAvecSacrifices();
        }
    }

    private int totalSacrificesSelectionnes(Joueur joueur) {
        int total = 0;

        for (int index : zonesSacrificesSelectionnees) {
            total += valeurSacrifice(
                    joueur.getTerrain()
                            .getZonesMonstres()[index]
            );
        }

        return total;
    }

    private void terminerInvocationAvecSacrifices() {
        for (int index : zonesSacrificesSelectionnees) {
            ZoneMonstre zone =
                    joueurInvocationEnAttente
                            .getTerrain()
                            .getZonesMonstres()[index];

            if (!zone.estVide()) {
                CarteMonstre sacrifice = zone.getCarte();

                zone.vider();

                sacrifice.setEmplacement(EmplacementCarte.CIMETIERE);
                joueurInvocationEnAttente.getCimetiere().add(sacrifice);
            }
        }

        invoquerMonstreDirectement(
                joueurInvocationEnAttente,
                monstreAInvoquer,
                zoneInvocation,
                positionInvocation
        );

        annulerInvocationEnAttente();
    }

    public boolean invocationEnAttente() {
        return joueurInvocationEnAttente != null
                && monstreAInvoquer != null
                && zoneInvocation >= 0;
    }

    public boolean estZoneSacrificeSelectionnee(
            Joueur joueur,
            int indexZone
    ) {
        return joueur == joueurInvocationEnAttente
                && zonesSacrificesSelectionnees.contains(indexZone);
    }

    public boolean estZoneSacrifiable(
            Joueur joueur,
            int indexZone
    ) {
        if (!invocationEnAttente()) return false;
        if (joueur != joueurInvocationEnAttente) return false;
        if (indexZone == zoneInvocation) return false;

        return !joueur.getTerrain()
                .getZonesMonstres()[indexZone]
                .estVide();
    }

    public void annulerInvocationEnAttente() {
        joueurInvocationEnAttente = null;
        monstreAInvoquer = null;
        zoneInvocation = -1;
        positionInvocation = null;
        sacrificesNecessaires = 0;
        zonesSacrificesSelectionnees.clear();
    }

    public boolean peutInvoquer(Joueur joueur) {
        return peutJouerCarteDepuisMain(joueur)
                && !invocationNormaleEffectuee
                && !invocationEnAttente();
    }

    public void marquerInvocationNormale() {
        invocationNormaleEffectuee = true;
    }

    public boolean estBattlePhase() {
        return phaseActuelle == PhaseTour.BATTLE_PHASE;
    }

    public boolean peutSelectionnerAttaquant(
            Joueur joueur,
            int indexZone
    ) {
        if (!estBattlePhase()) return false;
        if (joueur != joueurActif) return false;

        ZoneMonstre zone =
                joueur.getTerrain()
                        .getZonesMonstres()[indexZone];

        if (zone.estVide()) return false;
        if (zone.aDejaAttaque()) return false;

        return zone.getPosition() == PositionCarte.ATTAQUE_RECTO;
    }

    public void selectionnerAttaquant(
            Joueur joueur,
            int indexZone
    ) {
        if (!peutSelectionnerAttaquant(
                joueur,
                indexZone
        )) {
            return;
        }

        joueurAttaquantSelectionne = joueur;
        indexMonstreAttaquant = indexZone;
    }

    public boolean attaquantSelectionne() {
        return joueurAttaquantSelectionne != null
                && indexMonstreAttaquant >= 0;
    }

    public boolean estAttaquantSelectionne(
            Joueur joueur,
            int indexZone
    ) {
        return joueur == joueurAttaquantSelectionne
                && indexZone == indexMonstreAttaquant;
    }

    public void attaquerMonstre(
            Joueur defenseur,
            int indexCible
    ) {
        if (!attaquantSelectionne()) return;
        if (defenseur == joueurAttaquantSelectionne) return;

        ZoneMonstre zoneAttaquant =
                joueurAttaquantSelectionne
                        .getTerrain()
                        .getZonesMonstres()[indexMonstreAttaquant];

        ZoneMonstre zoneDefenseur =
                defenseur
                        .getTerrain()
                        .getZonesMonstres()[indexCible];

        if (zoneAttaquant.estVide()
                || zoneDefenseur.estVide()) {

            return;
        }

        if (zoneAttaquant.aDejaAttaque()) {
            viderSelectionAttaque();
            return;
        }

        CarteMonstre attaquant =
                zoneAttaquant.getCarte();

        CarteMonstre defenseurMonstre =
                zoneDefenseur.getCarte();

        int atk = attaquant.getAttaque();

        if (zoneDefenseur.getPosition()
                == PositionCarte.ATTAQUE_RECTO) {

            int atkAdverse =
                    defenseurMonstre.getAttaque();

            if (atk > atkAdverse) {

                envoyerMonstreAuCimetiere(
                        defenseur,
                        indexCible
                );

                infligerDegats(
                        defenseur,
                        atk - atkAdverse
                );

            } else if (atk < atkAdverse) {

                envoyerMonstreAuCimetiere(
                        joueurAttaquantSelectionne,
                        indexMonstreAttaquant
                );

                infligerDegats(
                        joueurAttaquantSelectionne,
                        atkAdverse - atk
                );

            } else {

                envoyerMonstreAuCimetiere(
                        defenseur,
                        indexCible
                );

                envoyerMonstreAuCimetiere(
                        joueurAttaquantSelectionne,
                        indexMonstreAttaquant
                );
            }

        } else {

            int defAdverse =
                    defenseurMonstre.getDefense();

            if (atk > defAdverse) {

                envoyerMonstreAuCimetiere(
                        defenseur,
                        indexCible
                );

            } else if (atk < defAdverse) {

                infligerDegats(
                        joueurAttaquantSelectionne,
                        defAdverse - atk
                );
            }
        }

        if (!zoneAttaquant.estVide()) {
            zoneAttaquant.setADejaAttaque(true);
        }

        viderSelectionAttaque();
    }

    public boolean peutAttaquerDirectement(Joueur defenseur) {
        if (!attaquantSelectionne()) return false;
        if (defenseur == joueurAttaquantSelectionne) return false;

        if (estPremierTourDuPremierJoueur()) {
            return false;
        }

        ZoneMonstre zoneAttaquant =
                joueurAttaquantSelectionne
                        .getTerrain()
                        .getZonesMonstres()[indexMonstreAttaquant];

        if (zoneAttaquant.estVide()) return false;
        if (zoneAttaquant.aDejaAttaque()) return false;

        CarteMonstre attaquant = zoneAttaquant.getCarte();

        if (terrainMonstreVide(defenseur)) {
            return true;
        }

        return attaquant instanceof CarteMonstreEffet effet
                && effet.getEffet() == EffetMonstre.ATTAQUE_DIRECTE_PV;
    }

    public void attaqueDirecte(Joueur defenseur) {
        if (!attaquantSelectionne()) return;
        if (defenseur == joueurAttaquantSelectionne) return;
        if (estPremierTourDuPremierJoueur()) {
            return;
        }

        ZoneMonstre zoneAttaquant =
                joueurAttaquantSelectionne
                        .getTerrain()
                        .getZonesMonstres()[indexMonstreAttaquant];

        if (zoneAttaquant.estVide()) return;

        if (zoneAttaquant.aDejaAttaque()) {
            viderSelectionAttaque();
            return;
        }

        CarteMonstre attaquant =
                zoneAttaquant.getCarte();

        boolean peutAttaquerDirectement =
                terrainMonstreVide(defenseur);

        if (attaquant instanceof CarteMonstreEffet effet
                && effet.getEffet()
                == EffetMonstre.ATTAQUE_DIRECTE_PV) {

            peutAttaquerDirectement = true;
        }

        if (!peutAttaquerDirectement) {
            return;
        }

        infligerDegats(
                defenseur,
                attaquant.getAttaque()
        );

        zoneAttaquant.setADejaAttaque(true);
        viderSelectionAttaque();
    }

    private boolean terrainMonstreVide(Joueur joueur) {
        for (ZoneMonstre zone :
                joueur.getTerrain()
                        .getZonesMonstres()) {

            if (!zone.estVide()) return false;
        }

        return true;
    }

    private void envoyerMonstreAuCimetiere(
            Joueur joueur,
            int indexZone
    ) {
        ZoneMonstre zone =
                joueur.getTerrain()
                        .getZonesMonstres()[indexZone];

        if (zone.estVide()) return;

        CarteMonstre monstre =
                zone.getCarte();

        zone.vider();

        monstre.setEmplacement(
                EmplacementCarte.CIMETIERE
        );

        joueur.getCimetiere().add(monstre);
    }

    private void infligerDegats(
            Joueur joueur,
            int degats
    ) {
        joueur.perdrePointsVie(degats);

        if (joueur.getPointsVie() <= 0) {
            terminerPartie(
                    getAdversaire(joueur)
            );
        }
    }

    public boolean peutAttaquerMonstre(Joueur defenseur, int indexCible) {
        if (!attaquantSelectionne()) return false;
        if (defenseur == joueurAttaquantSelectionne) return false;

        ZoneMonstre zoneDefenseur =
                defenseur.getTerrain()
                        .getZonesMonstres()[indexCible];

        if (zoneDefenseur.estVide()) return false;

        ZoneMonstre zoneAttaquant =
                joueurAttaquantSelectionne.getTerrain()
                        .getZonesMonstres()[indexMonstreAttaquant];

        if (zoneAttaquant.estVide()) return false;
        if (zoneAttaquant.aDejaAttaque()) return false;

        return true;
    }

    private boolean estPremierTourDuPremierJoueur() {
        return numeroTour == 1 && joueurActif == joueur1;
    }

    public boolean peutAttaquerDirectementSurZoneVide(Joueur defenseur, int indexZone) {
        return false;
    }

    public void terminerPartie(Joueur gagnant) {
        this.gagnant = gagnant;
        System.out.println(
                gagnant.getNom()
                        + " gagne la partie !"
        );
    }

    public void viderSelectionAttaque() {
        joueurAttaquantSelectionne = null;
        indexMonstreAttaquant = -1;
    }

    public Joueur getAdversaire(Joueur joueur) {
        return joueur == joueur1
                ? joueur2
                : joueur1;
    }

    public Joueur getJoueurActif() {
        return joueurActif;
    }

    public PhaseTour getPhaseActuelle() {
        return phaseActuelle;
    }

    public Joueur getGagnant() {
        return gagnant;
    }

    public int getNumeroTour() {
        return numeroTour;
    }
}