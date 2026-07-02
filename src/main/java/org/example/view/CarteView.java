package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.model.*;
/**
 * Représentation graphique d'une carte.
 */
public class CarteView extends VBox {

    private final Carte carte;
    private final Joueur proprietaire;
    private final Partie partie;
    private final TerrainView terrainView;

    public CarteView(Carte carte) {
        this(carte, null, null, null);
    }

    public CarteView(Carte carte, Joueur proprietaire, Partie partie, TerrainView terrainView) {
        this.carte = carte;
        this.proprietaire = proprietaire;
        this.partie = partie;
        this.terrainView = terrainView;

        setPrefSize(75, 105);
        setMinSize(75, 105);
        setMaxSize(75, 105);
        setPadding(new Insets(3));
        setSpacing(2);
        setAlignment(Pos.TOP_CENTER);

        construireCarte();
        appliquerStyleNormal();

        setOnMouseClicked(event -> ouvrirMenu(event.getScreenX(), event.getScreenY()));
    }

    private void construireCarte() {
        Label titre = new Label(carte.getTitre());
        titre.setWrapText(true);
        titre.setMaxWidth(68);
        titre.setAlignment(Pos.CENTER);
        titre.setStyle("-fx-font-weight: bold; -fx-font-size: 8px;");

        StackPane illustration = creerIllustration(66, 42);

        Label texte = new Label(carte.getDescription());
        texte.setWrapText(true);
        texte.setMaxWidth(66);
        texte.setPrefHeight(18);
        texte.setStyle(
                "-fx-font-size: 6px;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 1;" +
                        "-fx-background-color: rgba(255,255,255,0.35);"
        );

        getChildren().addAll(titre, illustration, texte);

        if (carte instanceof CarteMonstre monstre) {
            if (monstre.getNombreEtoiles() > 0) {
                Label sacrifice = new Label("★ Sacrifice");
                sacrifice.setStyle(
                        "-fx-font-size: 7px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: #7c2d12;"
                );
                getChildren().add(sacrifice);
            }

            Label stats = new Label("ATK " + monstre.getAttaque() + " / DEF " + monstre.getDefense());
            stats.setStyle("-fx-font-size: 6px; -fx-font-weight: bold;");
            getChildren().add(stats);
        }
    }

    private StackPane creerIllustration(double largeur, double hauteur) {
        StackPane illustration = new StackPane();

        illustration.setPrefSize(largeur, hauteur);
        illustration.setMinSize(largeur, hauteur);
        illustration.setMaxSize(largeur, hauteur);

        illustration.setStyle(
                "-fx-border-color: black;" +
                        "-fx-border-width: 1;" +
                        "-fx-background-color: rgba(255,255,255,0.35);"
        );

        if (carte.getCheminImage() == null || carte.getCheminImage().isBlank()) {
            illustration.getChildren().add(new Label("Image"));
            return illustration;
        }

        var url = CarteView.class.getResource(carte.getCheminImage());

        if (url == null) {
            illustration.getChildren().add(new Label("Image\nintrouvable"));
            System.out.println("Image introuvable : " + carte.getCheminImage());
            return illustration;
        }

        Image image = new Image(url.toExternalForm());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(largeur);
        imageView.setFitHeight(hauteur);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);


        illustration.getChildren().add(imageView);

        return illustration;
    }

    private void ouvrirMenu(double x, double y) {
        ContextMenu menu = new ContextMenu();

        MenuItem agrandir = new MenuItem("Agrandir");
        agrandir.setOnAction(e -> afficherCartePleinEcran());
        menu.getItems().add(agrandir);

        if (cartePeutAvoirAction()) {

            if (carte instanceof CarteMonstre && peutJouerMonstre()) {
                MenuItem attaque = new MenuItem("Jouer en attaque");
                attaque.setOnAction(e -> {
                    SelectionManager.selectionner(carte, this);
                    SelectionManager.setModePose(ModePose.ATTAQUE);
                    terrainView.actualiser();
                });

                MenuItem defenseRecto = new MenuItem("Jouer en défense face recto");
                defenseRecto.setOnAction(e -> {
                    SelectionManager.selectionner(carte, this);
                    SelectionManager.setModePose(ModePose.DEFENSE_RECTO);
                    terrainView.actualiser();
                });

                MenuItem defenseCachee = new MenuItem("Jouer en défense face cachée");
                defenseCachee.setOnAction(e -> {
                    SelectionManager.selectionner(carte, this);
                    SelectionManager.setModePose(ModePose.DEFENSE_CACHEE);
                    terrainView.actualiser();
                });

                menu.getItems().addAll(
                        attaque,
                        defenseRecto,
                        defenseCachee
                );
            }

            if (carte instanceof CarteMagie && peutPoserMagiePiege()) {
                MenuItem activer = new MenuItem("Activer / poser recto");
                activer.setOnAction(e -> {
                    SelectionManager.selectionner(carte, this);
                    SelectionManager.setModePose(ModePose.MAGIE_RECTO);
                    terrainView.actualiser();
                });

                MenuItem poser = new MenuItem("Poser face cachée");
                poser.setOnAction(e -> {
                    SelectionManager.selectionner(carte, this);
                    SelectionManager.setModePose(ModePose.MAGIE_VERSO);
                    terrainView.actualiser();
                });

                menu.getItems().addAll(activer, poser);
            }

            if (carte instanceof CartePiege && peutPoserMagiePiege()) {
                MenuItem poser = new MenuItem("Poser face cachée");
                poser.setOnAction(e -> {
                    SelectionManager.selectionner(carte, this);
                    SelectionManager.setModePose(ModePose.PIEGE_VERSO);
                    terrainView.actualiser();
                });

                menu.getItems().add(poser);
            }
        }

        menu.show(this, x, y);
    }

    private boolean cartePeutAvoirAction() {
        return proprietaire != null
                && partie != null
                && terrainView != null
                && carte.getEmplacement() == EmplacementCarte.MAIN
                && partie.peutJouerCarteDepuisMain(proprietaire);
    }

    private boolean peutJouerMonstre() {
        if (!(carte instanceof CarteMonstre monstre)) return false;
        if (!partie.peutInvoquer(proprietaire)) return false;

        ZoneMonstre[] zones = proprietaire.getTerrain().getZonesMonstres();

        for (int i = 0; i < zones.length; i++) {
            if (zones[i].estVide()
                    && partie.peutSacrifierPour(proprietaire, monstre, i)) {
                return true;
            }
        }

        return false;
    }

    private boolean peutPoserMagiePiege() {
        ZoneMagiePiege[] zones = proprietaire.getTerrain().getZonesMagiePiege();

        for (ZoneMagiePiege zone : zones) {
            if (zone.estVide()) return true;
        }

        return false;
    }

    public void appliquerStyleNormal() {
        String bordure = cartePeutAvoirAction()
                && (
                (carte instanceof CarteMonstre && peutJouerMonstre())
                        || ((carte instanceof CarteMagie || carte instanceof CartePiege) && peutPoserMagiePiege())
        )
                ? "#facc15"
                : "black";

        int largeur = bordure.equals("#facc15") ? 3 : 2;

        setStyle(
                "-fx-border-color: " + bordure + ";" +
                        "-fx-border-width: " + largeur + ";" +
                        "-fx-background-color: " + getCouleurFond() + ";" +
                        "-fx-effect: " + (bordure.equals("#facc15")
                        ? "dropshadow(gaussian, #facc15, 12, 0.45, 0, 0);"
                        : "none;")
        );
    }

    public void appliquerStyleSelection() {
        setStyle(
                "-fx-border-color: red;" +
                        "-fx-border-width: 4;" +
                        "-fx-background-color: " + getCouleurFond() + ";"
        );
    }

    public void afficherCartePleinEcran() {
        Stage stage = new Stage();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: rgba(0,0,0,0.92);");

        VBox grandeCarte = new VBox(12);
        grandeCarte.setAlignment(Pos.TOP_CENTER);
        grandeCarte.setPadding(new Insets(20));
        grandeCarte.setPrefSize(420, 650);
        grandeCarte.setMaxSize(420, 650);
        grandeCarte.setStyle(
                "-fx-border-color: black;" +
                        "-fx-border-width: 4;" +
                        "-fx-background-color: " + getCouleurFond() + ";"
        );

        Label titre = new Label(carte.getTitre());
        titre.setWrapText(true);
        titre.setMaxWidth(370);
        titre.setAlignment(Pos.CENTER);
        titre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        StackPane illustration = creerIllustration(360, 260);

        Label texte = new Label(creerTexteDetails());
        texte.setWrapText(true);
        texte.setMaxWidth(360);
        texte.setPrefHeight(160);
        texte.setPadding(new Insets(10));
        texte.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 2;" +
                        "-fx-background-color: rgba(255,255,255,0.45);"
        );
        texte.setOnMouseClicked(event -> afficherTextePleinEcran());

        grandeCarte.getChildren().addAll(titre, illustration, texte);

        Button fermer = new Button("Fermer");
        fermer.setOnAction(event -> stage.close());

        HBox haut = new HBox(fermer);
        haut.setAlignment(Pos.TOP_RIGHT);

        root.setTop(haut);
        root.setCenter(grandeCarte);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void afficherTextePleinEcran() {
        Stage stage = new Stage();

        TextArea texte = new TextArea(creerTexteDetails());
        texte.setWrapText(true);
        texte.setEditable(false);
        texte.setStyle("-fx-font-size: 22px;");

        Scene scene = new Scene(texte, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Texte - " + carte.getTitre());
        stage.show();
    }

    private String creerTexteDetails() {
        String texte = carte.getDescription();

        if (carte instanceof CarteMonstre monstre) {
            texte += "\n\nType : " + monstre.getType();

            if (monstre.getNombreEtoiles() > 0) {
                texte += "\n★ Monstre à sacrifice";
            } else {
                texte += "\nMonstre sans sacrifice";
            }

            texte += "\nATK : " + monstre.getAttaque();
            texte += "\nDEF : " + monstre.getDefense();
        }


        return texte;
    }

    private String getCouleurFond() {
        if (carte instanceof CarteMonstreEffet) return "#C89F76";
        if (carte instanceof CarteMonstre) return "#F5DEB3";
        if (carte instanceof CarteMagie) return "#008B8B";
        if (carte instanceof CartePiege) return "#5B2C6F";
        return "#FFFFFF";
    }

    public Carte getCarte() {
        return carte;
    }
}