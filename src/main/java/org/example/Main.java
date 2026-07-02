package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.data.FabriqueDeck;
import org.example.model.Joueur;
import org.example.model.Partie;
import org.example.view.AccueilView;
import org.example.view.TerrainView;

public class Main extends Application {

    private Stage stage;

    /**
     * Initialise la fenêtre principale et affiche l'écran d'accueil.
     */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        AccueilView accueilView = new AccueilView(this::lancerPartie);

        Scene scene = new Scene(creerEcranAvecBoutons(accueilView));

        stage.setTitle("Yo-Ji-Hu");
        stage.setScene(scene);

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.iconifiedProperty().addListener((obs, ancien, nouveau) -> {
            if (!nouveau) {
                stage.setFullScreen(true);
            }
        });

        stage.show();
    }


    /**
     * Crée une nouvelle partie et initialise les deux joueurs.
     */
    private void lancerPartie() {

        Joueur joueur1 = new Joueur("Joueur 1");
        Joueur joueur2 = new Joueur("Joueur 2");

        joueur1.getDeck().addAll(FabriqueDeck.creerDeckJoueur1());
        joueur2.getDeck().addAll(FabriqueDeck.creerDeckJoueur2());

        Partie partie = new Partie(joueur1, joueur2);
        partie.commencerPartie();

        TerrainView terrainView = new TerrainView(
                partie,
                joueur1,
                joueur2
        );

        stage.getScene().setRoot(
                creerEcranAvecBoutons(terrainView)
        );

        stage.setFullScreen(true);
    }

    private StackPane creerEcranAvecBoutons(Parent contenu) {

        StackPane root = new StackPane();
        root.getChildren().add(contenu);

        Button reduire = new Button("—");
        reduire.setOnAction(event -> stage.setIconified(true));

        Button fermer = new Button("X");
        fermer.setOnAction(event -> Platform.exit());

        reduire.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: rgba(255,255,255,0.20);" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;"
        );

        fermer.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: #dc2626;" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;"
        );

        HBox boutons = new HBox(8, reduire, fermer);
        boutons.setAlignment(Pos.CENTER);
        boutons.setPadding(new Insets(12));
        boutons.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        StackPane.setAlignment(boutons, Pos.TOP_RIGHT);

        root.getChildren().add(boutons);

        return root;
    }

    public static void main(String[] args) {
        launch();
    }
}