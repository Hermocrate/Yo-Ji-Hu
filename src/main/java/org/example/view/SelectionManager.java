package org.example.view;

import org.example.model.Carte;
import org.example.model.ModePose;
/**
 * Gère la carte actuellement sélectionnée ainsi que l'action
 * que le joueur souhaite effectuer.
 */
public class SelectionManager {

    private static Carte carteSelectionnee;
    private static CarteView vueSelectionnee;
    private static ModePose modePose;

    public static Carte getCarteSelectionnee() {
        return carteSelectionnee;
    }

    public static ModePose getModePose() {
        return modePose;
    }

    public static void setModePose(ModePose modePose) {
        SelectionManager.modePose = modePose;
    }

    public static void selectionner(
            Carte carte,
            CarteView vue
    ) {

        if (vueSelectionnee != null) {
            vueSelectionnee.appliquerStyleNormal();
        }

        carteSelectionnee = carte;
        vueSelectionnee = vue;

        vue.appliquerStyleSelection();
    }

    public static void viderSelection() {

        if (vueSelectionnee != null) {
            vueSelectionnee.appliquerStyleNormal();
        }

        carteSelectionnee = null;
        vueSelectionnee = null;
        modePose = null;
    }
}