package org.example.data;

import org.example.model.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Fabrique les decks prédéfinis utilisés par le jeu.
 */
public class FabriqueDeck {

    private static final String DOSSIER_IMAGES =
            "/org/";

    public static List<Carte> creerDeckJoueur1() {
        List<Carte> deck = new ArrayList<>();

        deck.add(monstre("Homer Simpson", "Toh!!", "Normal", 0, 1900, 1200));
        deck.add(monstre("Tintin", "Allez Milou !", "Reporter", 0, 1900, 1400));
        deck.add(monstre("Milou", "Wouf !", "Chien", 0, 1700, 1500));
        deck.add(monstre("Patrick", "Oh", "Etoile", 0, 800, 2000));
        deck.add(monstre("Donald Duck", "Palooka!", "Cannard", 0, 1800, 1000));
        deck.add(monstre("Demetrian Titus", "Ave Imperator !", "Space Marine", 1, 3000, 2500));

        deck.add(monstreEffet("Geralt De Riv", "Détruit un monstre adverse.", "Sorceleur", 0, 2000, 2000, EffetMonstre.DETRUIRE_MONSTRE_ADVERSE));
        deck.add(monstreEffet("Ciri", "Pioche 2 puis défausse 1.", "Sorceleur", 0, 1400, 1600, EffetMonstre.PIOCHER_2_DEFAUSSER_1));
        deck.add(monstreEffet("Dr House", "Gagne 1000 PV.", "Docteur", 0, 1200, 1800, EffetMonstre.GAGNER_1000_PV));
        deck.add(monstreEffet("Juste Henry", "Compte comme deux sacrifices.", "Forgeron", 0, 1000, 1000, EffetMonstre.COMPTE_DOUBLE_SACRIFICE));
        deck.add(monstreEffet("Peregrin Touque", "Inflige 500 dégâts.", "Pyro", 0, 1500, 900, EffetMonstre.INFLIGER_500));
        deck.add(monstreEffet("Legolas", "Peut attaquer directement les PV.", "Elfe", 1, 2500, 2100, EffetMonstre.ATTAQUE_DIRECTE_PV));

        deck.add(magie("LeviOsa pas leviosAaa", "Piochez 1 carte.", EffetCarte.PIOCHER_1));
        deck.add(magie("Bourg Palette", "Gagnez 500 Points de Vie.", EffetCarte.GAGNER_500_PV));
        deck.add(magie("Fus Ro Dah", "Infligez 300 dégâts à l'adversaire.", EffetCarte.INFLIGER_300));
        deck.add(magie("Kamehameha", "Détruisez un monstre adverse.", EffetCarte.DETRUIRE_MONSTRE_ADVERSE));
        deck.add(magie("Hadoken", "Détruisez une carte Magie ou Piège adverse.", EffetCarte.DETRUIRE_MAGIE_PIEGE_ADVERSE));

        deck.add(piege("Choc", "Détruisez un monstre adverse.", EffetCarte.DETRUIRE_MONSTRE_ADVERSE));
        deck.add(piege("Contre Choc", "Infligez 300 dégâts à l'adversaire.", EffetCarte.INFLIGER_300));
        deck.add(piege("Temporisation", "Gagnez 500 Points de Vie.", EffetCarte.GAGNER_500_PV));

        return deck;
    }

    public static List<Carte> creerDeckJoueur2() {
        List<Carte> deck = new ArrayList<>();

        deck.add(monstre("Moe Szyslak", "Allo c'est Moe", "Normal", 0, 1800, 0));
        deck.add(monstre("Scooby Doo", "I'm so scared", "Chien", 0, 1800, 1200));
        deck.add(monstre("Voldemort", "Avada Kedavra", "Démon", 0, 1000, 500));
        deck.add(monstre("Daffy Duck", "Coin", "Canard", 0, 1300, 1400));
        deck.add(monstre("Idéfix", "Wuf", "Chien", 0, 1600, 0));
        deck.add(monstre("Obélix", "Ils sont fous ces romains", "Gaulois", 1, 3000, 2800));

        deck.add(monstreEffet("Astérix", "Détruit une magie ou piège adverse.", "Gaulois", 0, 1500, 1500, EffetMonstre.DETRUIRE_MAGIE_PIEGE_ADVERSE));
        deck.add(monstreEffet("Scar", "Inflige 500 dégâts.", "Lion", 0, 1400, 1000, EffetMonstre.INFLIGER_500));
        deck.add(monstreEffet("Mandark", "Gagne 1000 PV.", "Normal", 0, 1000, 1900, EffetMonstre.GAGNER_1000_PV));
        deck.add(monstreEffet("Elric de Melniboné", "Compte comme deux sacrifices.", "Albinos", 0, 2100, 400, EffetMonstre.COMPTE_DOUBLE_SACRIFICE));
        deck.add(monstreEffet("Schwarzenegger", "Détruit un monstre adverse.", "Guerrier", 0, 2000, 1000, EffetMonstre.DETRUIRE_MONSTRE_ADVERSE));
        deck.add(monstreEffet("Gaunter O Dimm", "Peut attaquer directement les PV.", "?", 1, 2500, 2500, EffetMonstre.ATTAQUE_DIRECTE_PV));

        deck.add(magie("Renforts", "Piochez 1 carte.", EffetCarte.PIOCHER_1));
        deck.add(magie("Dol Blathanna", "Gagnez 500 Points de Vie.", EffetCarte.GAGNER_500_PV));
        deck.add(magie("Assassin", "Infligez 300 dégâts à l'adversaire.", EffetCarte.INFLIGER_300));
        deck.add(magie("Chasse sauvage", "Détruisez un monstre adverse.", EffetCarte.DETRUIRE_MONSTRE_ADVERSE));
        deck.add(magie("Necronomicon", "Détruisez une carte Magie ou Piège adverse.", EffetCarte.DETRUIRE_MAGIE_PIEGE_ADVERSE));

        deck.add(piege("Avada Kedavra", "Détruisez un monstre adverse.", EffetCarte.DETRUIRE_MONSTRE_ADVERSE));
        deck.add(piege("Météorite", "Infligez 300 dégâts à l'adversaire.", EffetCarte.INFLIGER_300));
        deck.add(piege("Lilas et Groseilles", "Gagnez 500 Points de Vie.", EffetCarte.GAGNER_500_PV));

        return deck;
    }

    private static CarteMonstre monstre(
            String titre,
            String description,
            String type,
            int sacrifice,
            int attaque,
            int defense
    ) {
        return new CarteMonstre(
                titre,
                description,
                cheminImage(titre),
                EmplacementCarte.DECK,
                false,
                type,
                sacrifice,
                attaque,
                defense
        );
    }

    private static CarteMonstreEffet monstreEffet(
            String titre,
            String description,
            String type,
            int sacrifice,
            int attaque,
            int defense,
            EffetMonstre effet
    ) {
        return new CarteMonstreEffet(
                titre,
                description,
                cheminImage(titre),
                EmplacementCarte.DECK,
                false,
                type,
                sacrifice,
                attaque,
                defense,
                effet
        );
    }

    private static CarteMagie magie(
            String titre,
            String description,
            EffetCarte effet
    ) {
        return new CarteMagie(
                titre,
                description,
                cheminImage(titre),
                EmplacementCarte.DECK,
                false,
                description,
                effet
        );
    }

    private static CartePiege piege(
            String titre,
            String description,
            EffetCarte effet
    ) {
        return new CartePiege(
                titre,
                description,
                cheminImage(titre),
                EmplacementCarte.DECK,
                false,
                description,
                effet
        );
    }

    /**
     * Transforme un titre de carte en nom de fichier.
     */
    private static String cheminImage(String titre) {
        return DOSSIER_IMAGES + nettoyerNomImage(titre) + ".png";
    }

    private static String nettoyerNomImage(String titre) {
        String nom = titre.toLowerCase();

        nom = Normalizer.normalize(nom, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        nom = nom
                .replace("œ", "oe")
                .replace("æ", "ae")
                .replaceAll("[^a-z0-9]", "");

        return nom;
    }
}