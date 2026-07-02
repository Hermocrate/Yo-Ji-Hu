package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import org.example.model.*;
/**
 * Représentation graphique d'une zone Magie/Piège.
 */
public class ZoneMagiePiegeView extends StackPane {

    public ZoneMagiePiegeView(
            int index,
            Joueur joueur,
            TerrainView terrainView,
            Partie partie
    ) {
        setPrefSize(80, 120);
        setMinSize(80, 120);
        setMaxSize(80, 120);
        setAlignment(Pos.CENTER);

        ZoneMagiePiege zone = joueur.getTerrain().getZonesMagiePiege()[index];

        if (zone.estVide()) {
            Label label = new Label("Magie\nPiège");
            label.setAlignment(Pos.CENTER);
            label.setStyle(
                    "-fx-text-fill: rgba(255,255,255,0.70);" +
                            "-fx-font-size: 11px;" +
                            "-fx-font-weight: bold;"
            );

            getChildren().add(label);

            if (peutRecevoirCarte(joueur, partie)) {
                appliquerStyleSurbrillance();
            } else {
                appliquerStyleNormal();
            }

        } else {
            Carte carte = zone.getCarte();

            if (zone.isFaceCachee()) {
                DosCarteView dosCarte = new DosCarteView();

                dosCarte.setOnMouseClicked(event -> {
                    new CarteView(carte).afficherCartePleinEcran();
                    event.consume();
                });

                getChildren().add(dosCarte);
            } else {
                getChildren().add(new CarteView(carte));
            }

            if (partie.peutActiverMagiePiege(joueur, index)) {
                appliquerStyleActivable();
            } else {
                appliquerStyleNormal();
            }
        }

        setOnMouseClicked(event -> {

            if (zone.estVide()) {
                poserOuActiverDepuisMain(
                        joueur,
                        index,
                        terrainView,
                        partie
                );
                return;
            }

            ouvrirMenuCartePosee(
                    joueur,
                    index,
                    terrainView,
                    partie,
                    event.getScreenX(),
                    event.getScreenY()
            );
        });
    }

    private void poserOuActiverDepuisMain(
            Joueur joueur,
            int index,
            TerrainView terrainView,
            Partie partie
    ) {
        Carte carte = SelectionManager.getCarteSelectionnee();

        if (!(carte instanceof CarteMagie)
                && !(carte instanceof CartePiege)) {
            return;
        }

        if (!peutRecevoirCarte(joueur, partie)) {
            return;
        }

        ZoneMagiePiege zone =
                joueur.getTerrain()
                        .getZonesMagiePiege()[index];

        joueur.getMain().remove(carte);

        // Magie activée depuis la main : elle va directement au cimetière
        if (carte instanceof CarteMagie
                && SelectionManager.getModePose() == ModePose.MAGIE_RECTO) {

            partie.activerCarteDepuisMain(joueur, carte);

            SelectionManager.viderSelection();
            terrainView.actualiser();
            return;
        }

        // Magie verso ou piège verso : posé sur le terrain
        zone.setCarte(carte);
        zone.setFaceCachee(true);
        zone.setTourPose(partie.getNumeroTour());

        carte.setEmplacement(EmplacementCarte.TERRAIN);

        SelectionManager.viderSelection();
        terrainView.actualiser();
    }

    private void ouvrirMenuCartePosee(
            Joueur joueur,
            int index,
            TerrainView terrainView,
            Partie partie,
            double x,
            double y
    ) {
        ContextMenu menu = new ContextMenu();

        if (partie.peutActiverMagiePiege(joueur, index)) {
            MenuItem activer = new MenuItem("Activer");

            activer.setOnAction(event -> {
                partie.activerMagiePiege(joueur, index);
                terrainView.actualiser();
            });

            menu.getItems().add(activer);
        }

        if (!menu.getItems().isEmpty()) {
            menu.show(this, x, y);
        }
    }

    private boolean peutRecevoirCarte(
            Joueur joueur,
            Partie partie
    ) {
        if (joueur != partie.getJoueurActif()) {
            return false;
        }

        if (!partie.peutJouerCarteDepuisMain(joueur)) {
            return false;
        }

        Carte carte = SelectionManager.getCarteSelectionnee();
        ModePose mode = SelectionManager.getModePose();

        if (carte instanceof CarteMagie) {
            return mode == ModePose.MAGIE_RECTO
                    || mode == ModePose.MAGIE_VERSO;
        }

        if (carte instanceof CartePiege) {
            return mode == ModePose.PIEGE_VERSO;
        }

        return false;
    }

    private void appliquerStyleNormal() {
        setStyle(
                "-fx-border-color: rgba(255,255,255,0.45);" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-background-color: rgba(255,255,255,0.12);"
        );
    }

    private void appliquerStyleSurbrillance() {
        setStyle(
                "-fx-border-color: orange;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-background-color: #fff3b0;"
        );
    }

    private void appliquerStyleActivable() {
        setStyle(
                "-fx-border-color: #facc15;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-background-color: rgba(250,204,21,0.16);" +
                        "-fx-effect: dropshadow(gaussian, #facc15, 14, 0.45, 0, 0);"
        );
    }
}