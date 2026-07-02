package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
/**
 * Affiche le dos d'une carte.
 */
public class DosCarteView extends StackPane {

    public DosCarteView() {

        setPrefSize(80, 120);
        setMinSize(80, 120);
        setMaxSize(80, 120);

        setAlignment(Pos.CENTER);

        setStyle(
                "-fx-background-color: radial-gradient(center 50% 45%, radius 80%, #312e81, #111827, #020617);" +
                        "-fx-border-color: linear-gradient(to bottom, #fde68a, #d97706, #78350f);" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, black, 12, 0.45, 0, 3);"
        );

        StackPane cadreInterieur = new StackPane();
        cadreInterieur.setPrefSize(62, 98);
        cadreInterieur.setMaxSize(62, 98);
        cadreInterieur.setStyle(
                "-fx-border-color: rgba(253,230,138,0.85);" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 7;" +
                        "-fx-background-radius: 7;" +
                        "-fx-background-color: radial-gradient(center 50% 50%, radius 70%, rgba(250,204,21,0.25), rgba(30,41,59,0.35));"
        );

        Label logo = new Label("YJH");
        logo.setStyle(
                "-fx-font-family: 'Palatino Linotype';" +
                        "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: linear-gradient(to bottom, #fff7c2, #facc15, #b45309);" +
                        "-fx-effect: dropshadow(gaussian, black, 6, 0.7, 1, 1);"
        );

        Label sousTexte = new Label("YO-JI-HU");
        sousTexte.setTranslateY(28);
        sousTexte.setStyle(
                "-fx-font-family: 'Georgia';" +
                        "-fx-font-size: 8px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #fde68a;"
        );

        cadreInterieur.getChildren().addAll(logo, sousTexte);

        getChildren().add(cadreInterieur);
    }
}