package org.example.model;

/**
 * Contient les différentes zones du terrain d'un joueur.
 */
public class Terrain {

    private ZoneMonstre[] zonesMonstres;
    private ZoneMagiePiege[] zonesMagiePiege;

    public Terrain() {

        zonesMonstres = new ZoneMonstre[5];
        zonesMagiePiege = new ZoneMagiePiege[5];

        for (int i = 0; i < 5; i++) {

            zonesMonstres[i] = new ZoneMonstre();
            zonesMagiePiege[i] = new ZoneMagiePiege();
        }
    }

    public ZoneMonstre[] getZonesMonstres() {
        return zonesMonstres;
    }

    public ZoneMagiePiege[] getZonesMagiePiege() {
        return zonesMagiePiege;
    }

    public boolean poserMonstre(CarteMonstre monstre, int position) {

        if (position < 0 || position > 4) {
            return false;
        }

        if (!zonesMonstres[position].estVide()) {
            return false;
        }

        zonesMonstres[position].setCarte(monstre);

        monstre.setEmplacement(
                EmplacementCarte.TERRAIN
        );

        return true;
    }

    public CarteMonstre retirerMonstre(int position) {

        if (position < 0 || position > 4) {
            return null;
        }

        CarteMonstre monstre =
                zonesMonstres[position].getCarte();

        zonesMonstres[position].setCarte(null);

        return monstre;
    }
}