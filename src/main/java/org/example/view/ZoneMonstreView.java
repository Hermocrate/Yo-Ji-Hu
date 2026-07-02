package org.example.view;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.example.model.*;
/**
 * Représentation graphique d'une zone Monstre.
 */
public class ZoneMonstreView extends StackPane {

    public ZoneMonstreView(
            int index,
            Joueur joueur,
            TerrainView terrainView,
            Partie partie
    ) {
        setPrefSize(80, 120);
        setMinSize(80, 120);
        setMaxSize(80, 120);
        setAlignment(Pos.CENTER);

        ZoneMonstre zone =
                joueur.getTerrain()
                        .getZonesMonstres()[index];

        if (zone.estVide()) {

            Label label = new Label("Monstre");

            label.setStyle(
                    "-fx-text-fill: rgba(255,255,255,0.70);" +
                            "-fx-font-size: 11px;" +
                            "-fx-font-weight: bold;"
            );

            getChildren().add(label);

            if (peutRecevoirCarte(joueur, partie, index)) {
                appliquerStyleSurbrillance();
            } else if (partie.peutAttaquerDirectementSurZoneVide(joueur, index)) {
                appliquerStyleCibleAttaque();
            } else {
                appliquerStyleNormal();
            }

        } else {

            CarteMonstre monstre = zone.getCarte();

            if (zone.getPosition() == PositionCarte.DEFENSE_VERSO) {
                DosCarteView dosCarte = new DosCarteView();
                dosCarte.setRotate(90);
                getChildren().add(dosCarte);
            } else {
                CarteView carteView = new CarteView(monstre);

                if (zone.getPosition() == PositionCarte.DEFENSE_RECTO) {
                    carteView.setRotate(90);
                }

                getChildren().add(carteView);
            }

            if (partie.estZoneSacrificeSelectionnee(joueur, index)) {
                appliquerStyleSacrificeSelectionne();
            } else if (partie.estZoneSacrifiable(joueur, index)) {
                appliquerStyleSacrifiable();
            } else if (partie.estAttaquantSelectionne(joueur, index)) {
                appliquerStyleAttaquantSelectionne();
            } else if (partie.peutAttaquerMonstre(joueur, index)) {
                appliquerStyleCibleAttaque();
            } else if (partie.peutSelectionnerAttaquant(joueur, index)) {
                appliquerStylePeutAttaquer();
            } else if (zone.aDejaAttaque()) {
                appliquerStyleDejaAttaque();
            } else {
                appliquerStyleNormal();
            }
        }

        setOnMouseClicked(event -> {

            if (partie.invocationEnAttente()) {
                partie.selectionnerSacrifice(joueur, index);
                terrainView.actualiser();
                return;
            }

            if (partie.estBattlePhase()) {
                gererBattlePhase(index, joueur, terrainView, partie);
                return;
            }

            if (zone.estVide()) {
                gererInvocation(index, joueur, terrainView, partie);
                return;
            }

            ouvrirMenuMonstre(
                    index,
                    joueur,
                    terrainView,
                    partie,
                    event.getScreenX(),
                    event.getScreenY()
            );
        });
    }

    private void ouvrirMenuMonstre(
            int index,
            Joueur joueur,
            TerrainView terrainView,
            Partie partie,
            double x,
            double y
    ) {
        if (joueur != partie.getJoueurActif()) return;

        ContextMenu menu = new ContextMenu();

        if (partie.peutChangerPosition(joueur, index)) {
            MenuItem changerPosition = new MenuItem("Changer position");

            changerPosition.setOnAction(event -> {
                partie.changerPosition(joueur, index);
                terrainView.actualiser();
            });

            menu.getItems().add(changerPosition);
        }

        if (!menu.getItems().isEmpty()) {
            menu.show(this, x, y);
        }
    }

    private void gererBattlePhase(
            int index,
            Joueur joueur,
            TerrainView terrainView,
            Partie partie
    ) {
        ZoneMonstre zone =
                joueur.getTerrain()
                        .getZonesMonstres()[index];

        if (!zone.estVide()
                && joueur == partie.getJoueurActif()) {

            partie.selectionnerAttaquant(joueur, index);
            terrainView.actualiser();
            return;
        }

        if (!zone.estVide()
                && joueur != partie.getJoueurActif()) {

            if (!partie.peutAttaquerMonstre(joueur, index)) return;

            jouerAnimationImpact(() -> {
                partie.attaquerMonstre(joueur, index);
                terrainView.actualiser();
            });

            return;
        }

    }

    private void jouerAnimationImpact(Runnable actionFin) {

        ScaleTransition scale = new ScaleTransition(Duration.millis(90), this);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(1.35);
        scale.setToY(1.35);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);

        javafx.animation.TranslateTransition shake =
                new javafx.animation.TranslateTransition(Duration.millis(60), this);

        shake.setFromX(-8);
        shake.setToX(8);
        shake.setAutoReverse(true);
        shake.setCycleCount(6);

        javafx.animation.ParallelTransition attaque =
                new javafx.animation.ParallelTransition(scale, shake);

        attaque.setOnFinished(event -> {
            setScaleX(1);
            setScaleY(1);
            setTranslateX(0);
            setTranslateY(0);
            actionFin.run();
        });

        attaque.play();
    }

    private void gererInvocation(
            int index,
            Joueur joueur,
            TerrainView terrainView,
            Partie partie
    ) {
        ZoneMonstre zone =
                joueur.getTerrain()
                        .getZonesMonstres()[index];

        if (!zone.estVide()) return;

        Carte carte = SelectionManager.getCarteSelectionnee();

        if (!(carte instanceof CarteMonstre monstre)) return;

        if (!peutRecevoirCarte(joueur, partie, index)) {
            afficherMessage(
                    "Invocation impossible",
                    "Pas assez de sacrifices ou mauvaise phase."
            );
            return;
        }

        PositionCarte position;

        if (SelectionManager.getModePose() == ModePose.ATTAQUE) {
            position = PositionCarte.ATTAQUE_RECTO;
        } else if (SelectionManager.getModePose() == ModePose.DEFENSE_RECTO) {
            position = PositionCarte.DEFENSE_RECTO;
        } else {
            position = PositionCarte.DEFENSE_VERSO;
        }

        boolean succes =
                partie.commencerInvocationAvecSacrifices(
                        joueur,
                        monstre,
                        index,
                        position
                );

        if (!succes) {
            afficherMessage(
                    "Invocation impossible",
                    "Pas assez de monstres à sacrifier."
            );
            return;
        }

        if (!partie.invocationEnAttente()) {
            SelectionManager.viderSelection();
        }

        terrainView.actualiser();
    }

    private boolean peutRecevoirCarte(
            Joueur joueur,
            Partie partie,
            int index
    ) {
        if (joueur != partie.getJoueurActif()) return false;

        if (!(SelectionManager.getCarteSelectionnee()
                instanceof CarteMonstre monstre)) {
            return false;
        }

        if (SelectionManager.getModePose() != ModePose.ATTAQUE
                && SelectionManager.getModePose() != ModePose.DEFENSE_RECTO
                && SelectionManager.getModePose() != ModePose.DEFENSE_CACHEE) {
            return false;
        }

        if (!partie.peutInvoquer(joueur)) return false;

        return partie.peutSacrifierPour(joueur, monstre, index);
    }

    private void afficherMessage(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(titre);
        alert.setContentText(message);
        alert.showAndWait();
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

    private void appliquerStylePeutAttaquer() {
        setStyle(
                "-fx-border-color: #22c55e;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-background-color: rgba(34,197,94,0.18);" +
                        "-fx-effect: dropshadow(gaussian, #22c55e, 12, 0.45, 0, 0);"
        );
    }

    private void appliquerStyleCibleAttaque() {
        setStyle(
                "-fx-border-color: #ef4444;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-background-color: rgba(239,68,68,0.18);" +
                        "-fx-effect: dropshadow(gaussian, #ef4444, 12, 0.45, 0, 0);"
        );
    }

    private void appliquerStyleSacrifiable() {
        setStyle(
                "-fx-border-color: purple;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-background-color: #ead7ff;"
        );
    }

    private void appliquerStyleSacrificeSelectionne() {
        setStyle(
                "-fx-border-color: darkviolet;" +
                        "-fx-border-width: 5;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-background-color: #d0a2ff;"
        );
    }

    private void appliquerStyleAttaquantSelectionne() {
        setStyle(
                "-fx-border-color: red;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-background-color: rgba(239,68,68,0.18);"
        );
    }

    private void appliquerStyleDejaAttaque() {
        setStyle(
                "-fx-border-color: gray;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-background-color: rgba(148,163,184,0.18);"
        );
    }
}