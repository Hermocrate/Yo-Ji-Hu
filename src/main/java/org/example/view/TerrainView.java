package org.example.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.model.*;
import java.util.List;
/**
 * Vue principale représentant le terrain de jeu.
 */
public class TerrainView extends BorderPane {

    private Joueur joueur1;
    private Joueur joueur2;
    private Partie partie;
    private String messageAnimation;

    public TerrainView(Partie partie, Joueur joueur1, Joueur joueur2) {
        this.partie = partie;
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;

        setPadding(new Insets(14));
        setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 80%, #1e293b, #020617);");

        messageAnimation = "DÉBUT DU DUEL";
        reconstruireInterface();
    }

    // ====================
    // Construction de l'interface
    // ====================
    private void reconstruireInterface() {

        if (partie.getGagnant() != null) {
            setTop(null);
            setBottom(null);
            setCenter(creerEcranVictoire());
            return;
        }

        setTop(creerJoueurHaut(joueur2));
        setCenter(creerCentre());
        setBottom(creerJoueurBas(joueur1));
    }

    private VBox creerEcranVictoire() {
        VBox box = new VBox(30);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(50));

        Label titre = new Label("VICTOIRE");
        titre.setStyle(
                "-fx-font-family: 'Palatino Linotype';" +
                        "-fx-font-size: 82px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: linear-gradient(to bottom,#fff7c7,#facc15,#f59e0b);" +
                        "-fx-effect: dropshadow(gaussian, black, 18, 0.9, 4, 4);"
        );

        Label gagnant = new Label(partie.getGagnant().getNom() + " gagne le duel");
        gagnant.setStyle(
                "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Button quitter = new Button("QUITTER");
        quitter.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(to bottom, #facc15, #f97316);" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 12 50;" +
                        "-fx-cursor: hand;"
        );
        quitter.setOnAction(e -> Platform.exit());

        box.getChildren().addAll(titre, gagnant, quitter);
        return box;
    }

    private BorderPane creerCentre() {
        BorderPane centre = new BorderPane();
        centre.setPadding(new Insets(8));

        centre.setCenter(creerAnnoncePhase());

        VBox boutonBox = new VBox(creerBoutonPiece());
        boutonBox.setAlignment(Pos.CENTER_RIGHT);
        boutonBox.setPadding(new Insets(0, 10, 0, 30));

        centre.setRight(boutonBox);
        return centre;
    }

    // ====================
    // Gestion des animations
    // ====================
    private StackPane creerAnnoncePhase() {
        StackPane box = new StackPane();
        box.setPrefSize(900, 150);

        Label label = new Label(messageAnimation == null ? "" : messageAnimation);
        label.setAlignment(Pos.CENTER);

        label.setStyle(
                "-fx-font-family: 'Palatino Linotype';" +
                        "-fx-font-size: 52px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: linear-gradient(to bottom,#fff7c7,#facc15,#f59e0b);" +
                        "-fx-effect: dropshadow(gaussian, black, 12, 0.9, 4, 4);"
        );

        box.getChildren().add(label);

        if (messageAnimation != null && !messageAnimation.isBlank()) {
            FadeTransition fade = new FadeTransition(Duration.millis(1700), label);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.setAutoReverse(true);
            fade.setCycleCount(2);

            ScaleTransition scale = new ScaleTransition(Duration.millis(850), label);
            scale.setFromX(0.5);
            scale.setFromY(0.5);
            scale.setToX(1.15);
            scale.setToY(1.15);
            scale.setAutoReverse(true);
            scale.setCycleCount(2);

            new ParallelTransition(fade, scale).play();
            messageAnimation = null;
        }

        return box;
    }

    private Button creerBoutonPiece() {
        Button bouton = new Button(getTexteBoutonPhase());

        bouton.setPrefSize(125, 125);
        bouton.setMinSize(125, 125);
        bouton.setMaxSize(125, 125);
        bouton.setWrapText(true);
        bouton.setAlignment(Pos.CENTER);

        bouton.setStyle(
                "-fx-background-radius: 100;" +
                        "-fx-border-radius: 100;" +
                        "-fx-background-color: radial-gradient(center 50% 35%, radius 70%, #fff7a8, #facc15, #ca8a04);" +
                        "-fx-border-color: #fff7a8;" +
                        "-fx-border-width: 4;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3b2600;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, #facc15, 28, 0.75, 0, 0);"
        );

        bouton.setOnAction(event -> {
            Joueur joueurAvant = partie.getJoueurActif();

            partie.phaseSuivante();

            Joueur joueurApres = partie.getJoueurActif();

            if (partie.getGagnant() != null) {
                actualiser();
                return;
            }

            if (joueurAvant != joueurApres) {
                messageAnimation = "NOUVEAU TOUR\n" + joueurApres.getNom();
            } else {
                messageAnimation = getNomPhaseLisible(partie.getPhaseActuelle());
            }

            actualiser();
        });

        return bouton;
    }

    private String getTexteBoutonPhase() {
        return switch (partie.getPhaseActuelle()) {
            case PHASE_TIRAGE -> "PIOCHER";
            case MAIN_PHASE_1 -> "BATTLE\nPHASE";
            case BATTLE_PHASE -> "MAIN\nPHASE 2";
            case MAIN_PHASE_2, END_PHASE -> "FIN DU\nTOUR";
        };
    }

    private String getNomPhaseLisible(PhaseTour phase) {
        return switch (phase) {
            case PHASE_TIRAGE -> "PHASE DE TIRAGE";
            case MAIN_PHASE_1 -> "MAIN PHASE 1";
            case BATTLE_PHASE -> "BATTLE PHASE";
            case MAIN_PHASE_2 -> "MAIN PHASE 2";
            case END_PHASE -> "END PHASE";
        };
    }

    // ====================
    // Interface des joueurs
    // ====================
    private VBox creerJoueurHaut(Joueur joueur) {
        VBox zone = creerPanneauJoueur(joueur);
        zone.getChildren().addAll(
                creerMain(joueur),
                creerInfosJoueur(joueur),
                creerTerrainHaut()
        );
        return zone;
    }

    private VBox creerJoueurBas(Joueur joueur) {
        VBox zone = creerPanneauJoueur(joueur);
        zone.getChildren().addAll(
                creerTerrainBas(),
                creerInfosJoueur(joueur),
                creerMain(joueur)
        );
        return zone;
    }

    private VBox creerPanneauJoueur(Joueur joueur) {
        VBox zone = new VBox(8);
        zone.setAlignment(Pos.CENTER);
        zone.setPadding(new Insets(10));

        if (joueur == partie.getJoueurActif()) {
            zone.setStyle(
                    "-fx-background-color: rgba(34,197,94,0.16);" +
                            "-fx-background-radius: 22;" +
                            "-fx-border-color: #22c55e;" +
                            "-fx-border-radius: 22;" +
                            "-fx-border-width: 2.5;"
            );
        } else {
            zone.setStyle(
                    "-fx-background-color: rgba(15,23,42,0.68);" +
                            "-fx-background-radius: 22;" +
                            "-fx-border-color: rgba(255,255,255,0.18);" +
                            "-fx-border-radius: 22;" +
                            "-fx-border-width: 1.5;"
            );
        }

        return zone;
    }

    private Label creerInfosJoueur(Joueur joueur) {
        Label infos = new Label(joueur.getNom() + "     " + joueur.getPointsVie() + " PV");

        if (partie.peutAttaquerDirectement(joueur)) {
            infos.setStyle(
                    "-fx-font-size: 19px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-color: rgba(239,68,68,0.35);" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-color: #ef4444;" +
                            "-fx-border-width: 3;" +
                            "-fx-border-radius: 12;" +
                            "-fx-padding: 6 22;" +
                            "-fx-effect: dropshadow(gaussian, #ef4444, 18, 0.65, 0, 0);" +
                            "-fx-cursor: hand;"
            );

            infos.setOnMouseClicked(event -> {
                partie.attaqueDirecte(joueur);
                actualiser();
            });

        } else {
            infos.setStyle(
                    "-fx-font-size: 19px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-color: rgba(0,0,0,0.30);" +
                            "-fx-background-radius: 12;" +
                            "-fx-padding: 6 22;"
            );
        }

        return infos;
    }

    private GridPane creerTerrainHaut() {
        GridPane grille = creerGrille();

        for (int i = 0; i < 5; i++) {
            grille.add(new ZoneMagiePiegeView(i, joueur2, this, partie), i, 0);
        }

        grille.add(creerDeck(joueur2), 5, 0);
        grille.add(creerCimetiere(joueur2), 6, 0);

        for (int i = 0; i < 5; i++) {
            grille.add(new ZoneMonstreView(i, joueur2, this, partie), i, 1);
        }

        grille.add(creerZoneVide(), 5, 1);
        grille.add(creerZoneVide(), 6, 1);

        return grille;
    }

    private GridPane creerTerrainBas() {
        GridPane grille = creerGrille();

        for (int i = 0; i < 5; i++) {
            grille.add(new ZoneMonstreView(i, joueur1, this, partie), i, 0);
        }

        grille.add(creerZoneVide(), 5, 0);
        grille.add(creerZoneVide(), 6, 0);

        for (int i = 0; i < 5; i++) {
            grille.add(new ZoneMagiePiegeView(i, joueur1, this, partie), i, 1);
        }

        grille.add(creerDeck(joueur1), 5, 1);
        grille.add(creerCimetiere(joueur1), 6, 1);

        return grille;
    }

    private GridPane creerGrille() {
        GridPane grille = new GridPane();

        grille.setHgap(16);
        grille.setVgap(12);
        grille.setMaxWidth(Double.MAX_VALUE);

        for (int i = 0; i < 7; i++) {
            ColumnConstraints colonne = new ColumnConstraints();
            colonne.setPercentWidth(100.0 / 7.0);
            colonne.setHalignment(javafx.geometry.HPos.CENTER);
            grille.getColumnConstraints().add(colonne);
        }

        return grille;
    }

    private HBox creerMain(Joueur joueur) {
        HBox main = new HBox(7);
        main.setAlignment(Pos.CENTER);
        main.setPadding(new Insets(2));

        boolean cacherMain = joueur != partie.getJoueurActif();

        for (Carte carte : joueur.getMain()) {
            if (cacherMain) {
                main.getChildren().add(new DosCarteView());
            } else {
                main.getChildren().add(new CarteView(carte, joueur, partie, this));
            }
        }

        return main;
    }

    private Label creerZone(String texte) {
        Label zone = new Label(texte);

        zone.setPrefSize(80, 120);
        zone.setMinSize(80, 120);
        zone.setMaxSize(80, 120);
        zone.setAlignment(Pos.CENTER);

        zone.setStyle(
                "-fx-border-color: rgba(255,255,255,0.45);" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-background-color: rgba(255,255,255,0.12);" +
                        "-fx-text-fill: rgba(255,255,255,0.80);" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;"
        );

        return zone;
    }

    private Region creerZoneVide() {
        Region region = new Region();
        region.setPrefSize(80, 120);
        region.setMinSize(80, 120);
        region.setMaxSize(80, 120);
        return region;
    }

    private StackPane creerDeck(Joueur joueur) {
        StackPane deck = new StackPane();
        deck.setPrefSize(80, 120);
        deck.setMinSize(80, 120);
        deck.setMaxSize(80, 120);

        if (joueur.getDeck().isEmpty()) {
            Label deckVide = creerZone("Deck\n0");

            deckVide.setStyle(
                    "-fx-border-color: #ef4444;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 10;" +
                            "-fx-background-radius: 10;" +
                            "-fx-background-color: rgba(239,68,68,0.22);" +
                            "-fx-text-fill: #fecaca;" +
                            "-fx-font-size: 12px;" +
                            "-fx-font-weight: bold;"
            );

            deck.getChildren().add(deckVide);
        } else {
            DosCarteView dosCarte = new DosCarteView();
            Label nombre = creerBadgeNombre(joueur.getDeck().size());

            deck.getChildren().addAll(dosCarte, nombre);
            StackPane.setAlignment(nombre, Pos.BOTTOM_RIGHT);
        }

        deck.setOnMouseClicked(event -> ouvrirMenuDeck(joueur, deck, event.getScreenX(), event.getScreenY()));
        deck.setStyle("-fx-cursor: hand;");

        return deck;
    }

    private void ouvrirMenuDeck(Joueur joueur, StackPane deck, double x, double y) {
        ContextMenu menu = new ContextMenu();

        MenuItem voirDeck = new MenuItem("Visualiser le deck");
        voirDeck.setOnAction(event -> ouvrirDeck(joueur));

        MenuItem abandonner = new MenuItem("Abandonner");
        abandonner.setOnAction(event -> {
            partie.terminerPartie(partie.getAdversaire(joueur));
            actualiser();
        });

        menu.getItems().addAll(voirDeck, abandonner);
        menu.show(deck, x, y);
    }

    private void ouvrirDeck(Joueur joueur) {
        ouvrirListeCartes(
                "Deck de " + joueur.getNom(),
                joueur.getDeck(),
                () -> {
                    joueur.melangerDeck();
                    messageAnimation = "DECK MÉLANGÉ";

                    actualiser();

                    RotateTransition rotate = new RotateTransition(Duration.millis(90), this);
                    rotate.setFromAngle(-2);
                    rotate.setToAngle(2);
                    rotate.setAutoReverse(true);
                    rotate.setCycleCount(8);

                    rotate.setOnFinished(event -> {
                        setRotate(0);
                        setTranslateX(0);
                        setTranslateY(0);
                    });

                    rotate.play();
                }
        );
    }

    private StackPane creerCimetiere(Joueur joueur) {
        StackPane cimetiere = new StackPane();
        cimetiere.setPrefSize(80, 120);
        cimetiere.setMinSize(80, 120);
        cimetiere.setMaxSize(80, 120);

        if (joueur.getCimetiere().isEmpty()) {
            cimetiere.getChildren().add(creerZone("Cimetière\n0"));
        } else {
            DosCarteView dosCarte = new DosCarteView();
            Label nombre = creerBadgeNombre(joueur.getCimetiere().size());

            cimetiere.getChildren().addAll(dosCarte, nombre);
            StackPane.setAlignment(nombre, Pos.BOTTOM_RIGHT);
        }

        cimetiere.setOnMouseClicked(event -> ouvrirCimetiere(joueur));
        cimetiere.setStyle("-fx-cursor: hand;");

        return cimetiere;
    }

    private Label creerBadgeNombre(int nombreCartes) {
        Label nombre = new Label(String.valueOf(nombreCartes));

        nombre.setStyle(
                "-fx-font-size: 16;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: rgba(0,0,0,0.82);" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 3 7;"
        );

        return nombre;
    }

    private void ouvrirCimetiere(Joueur joueur) {
        ouvrirListeCartes(
                "Cimetière de " + joueur.getNom(),
                joueur.getCimetiere(),
                null
        );
    }

    private void ouvrirListeCartes(String titreFenetre, List<Carte> cartesListe, Runnable actionFermeture) {
        Stage fenetre = new Stage();

        if (getScene() != null && getScene().getWindow() != null) {
            fenetre.initOwner(getScene().getWindow());
        }

        VBox contenu = new VBox(18);
        contenu.setPadding(new Insets(25));
        contenu.setAlignment(Pos.CENTER);
        contenu.setStyle(
                "-fx-background-color: radial-gradient(center 50% 50%, radius 80%, #312e81, #020617);"
        );

        Label titre = new Label(titreFenetre);
        titre.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        HBox cartes = new HBox(12);
        cartes.setAlignment(Pos.CENTER_LEFT);
        cartes.setPadding(new Insets(10));

        if (cartesListe.isEmpty()) {
            Label vide = new Label("Aucune carte.");
            vide.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
            cartes.getChildren().add(vide);
        } else {
            for (Carte carte : cartesListe) {
                cartes.getChildren().add(new CarteView(carte));
            }
        }

        ScrollPane scrollPane = new ScrollPane(cartes);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(900, 230);

        Button fermer = new Button("Fermer");
        fermer.setOnAction(event -> fenetre.close());

        contenu.getChildren().addAll(titre, scrollPane, fermer);

        Scene scene = new Scene(contenu, 1000, 380);

        fenetre.setTitle(titreFenetre);
        fenetre.setScene(scene);

        if (actionFermeture != null) {
            fenetre.setOnHidden(event -> actionFermeture.run());
        }

        fenetre.show();
    }

    public void actualiser() {
        reconstruireInterface();
    }
}