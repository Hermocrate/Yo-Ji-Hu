package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
/**
 * Écran d'accueil affiché avant le début d'une partie.
 */
public class AccueilView extends BorderPane {

    // Présentation du jeu
    public AccueilView(Runnable lancerPartie) {

        setPadding(new Insets(40));
        setStyle(
                "-fx-background-color: radial-gradient(center 50% 30%, radius 75%, #4c1d95, #111827 55%, #020617);"
        );

        VBox contenu = new VBox(26);
        contenu.setAlignment(Pos.CENTER);

        Label titre = new Label("YO-JI-HU");
        titre.setStyle(
                "-fx-font-size: 82px;" +
                        "-fx-font-weight: 900;" +
                        "-fx-text-fill: linear-gradient(to bottom, #fef08a, #f97316, #991b1b);" +
                        "-fx-effect: dropshadow(gaussian, black, 30, 0.8, 0, 6);"
        );

        Label sousTitre = new Label("DUEL DE CARTES PARODIQUE");
        sousTitre.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #e5e7eb;" +
                        "-fx-letter-spacing: 4px;"
        );

        HBox zoneCartes = new HBox(28);
        zoneCartes.setAlignment(Pos.CENTER);
        zoneCartes.getChildren().addAll(
                creerCarteType("MONSTRE", "#F5DEB3", "ATK / DEF", "⚔"),
                creerCarteType("MONSTRE SPE", "#C89F76", "Pouvoir spécial", "✦"),
                creerCarteType("MAGIE", "#008B8B", "Sorts", "◆"),
                creerCarteType("PIÈGE", "#5B2C6F", "Réactions", "!")
        );

        VBox panneau = creerPanneauInformations();

        // Bouton de lancement
        Button jouer = new Button("COMMENCER LE DUEL");
        jouer.setStyle(
                "-fx-font-size: 25px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #111827;" +
                        "-fx-background-color: linear-gradient(to bottom, #fde047, #f97316);" +
                        "-fx-background-radius: 16;" +
                        "-fx-padding: 16 70;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, black, 22, 0.55, 0, 6);"
        );
        jouer.setOnAction(e -> lancerPartie.run());

        Label copyright = new Label(
                "© HERMOCRATE"
        );
        copyright.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #cbd5e1;" +
                        "-fx-opacity: 0.85;"
        );

        contenu.getChildren().addAll(
                titre,
                sousTitre,
                zoneCartes,
                panneau,
                jouer,
                copyright
        );

        setCenter(contenu);
    }

    private VBox creerPanneauInformations() {

        VBox panneau = new VBox(14);
        panneau.setAlignment(Pos.CENTER_LEFT);
        panneau.setMaxWidth(900);
        panneau.setPadding(new Insets(24));

        panneau.setStyle(
                "-fx-background-color: rgba(15,23,42,0.86);" +
                        "-fx-background-radius: 24;" +
                        "-fx-border-color: linear-gradient(to right, #facc15, #f97316, #7c3aed);" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 24;" +
                        "-fx-effect: dropshadow(gaussian, black, 28, 0.45, 0, 8);"
        );

        Label titre = new Label("RÈGLES ESSENTIELLES");
        titre.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #fef3c7;"
        );

        Label regles = new Label(
                "• Chaque joueur commence avec 8000 PV et 5 cartes en main.\n" +
                        "• Tour : Pioche → Main Phase 1 → Battle Phase → Main Phase 2 → End Phase.\n" +
                        "• Les cartes de la main se jouent uniquement pendant les Main Phases.\n" +
                        "• Les attaques se font uniquement pendant la Battle Phase.\n" +
                        "• Les monstres à étoile nécessitent un sacrifice pour être invoqué.\n" +
                        "• Deck vide à la pioche ou PV à 0 = défaite."
        );
        regles.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: #f8fafc;" +
                        "-fx-line-spacing: 5px;"
        );

        Label mention = new Label(
                "Mention : Yo-Ji-Hu est un jeu de cartes parodique, volontairement simplifié et incomplet, " +
                        "créé uniquement dans le cadre d’un projet d’apprentissage. Toute copie, redistribution " +
                        "ou réutilisation non autorisée est interdite."
        );
        mention.setWrapText(true);
        mention.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #cbd5e1;" +
                        "-fx-font-style: italic;"
        );

        panneau.getChildren().addAll(titre, regles, mention);

        return panneau;
    }

    private VBox creerCarteType(String nom, String couleur, String texteBas, String symbole) {

        VBox carte = new VBox(9);
        carte.setAlignment(Pos.TOP_CENTER);
        carte.setPadding(new Insets(12));
        carte.setPrefSize(150, 215);
        carte.setMaxSize(150, 215);

        carte.setStyle(
                "-fx-background-color: " + couleur + ";" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 2.5;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, black, 24, 0.55, 0, 7);"
        );

        Label titre = new Label(nom);
        titre.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: black;"
        );

        StackPane illustration = new StackPane();
        illustration.setPrefSize(120, 95);
        illustration.setStyle(
                "-fx-background-color: rgba(255,255,255,0.42);" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-background-radius: 6;" +
                        "-fx-border-radius: 6;"
        );

        Label icone = new Label(symbole);
        icone.setStyle(
                "-fx-font-size: 48px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: black;" +
                        "-fx-effect: dropshadow(gaussian, white, 8, 0.45, 0, 0);"
        );

        illustration.getChildren().add(icone);

        Label description = new Label(texteBas);
        description.setWrapText(true);
        description.setAlignment(Pos.CENTER);
        description.setMaxWidth(120);
        description.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: black;"
        );

        Region separation = new Region();
        separation.setPrefHeight(1);
        separation.setStyle("-fx-background-color: rgba(0,0,0,0.55);");

        carte.getChildren().addAll(
                titre,
                illustration,
                separation,
                description
        );

        return carte;
    }
}