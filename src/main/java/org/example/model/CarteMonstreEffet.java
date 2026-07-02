package org.example.model;

/**
 * Monstre possédant un effet déclenché lors de son invocation.
 */
public class CarteMonstreEffet extends CarteMonstre {

    // Effet propre au monstre
    private EffetMonstre effet;

    public CarteMonstreEffet(
            String titre,
            String description,
            String cheminImage,
            EmplacementCarte emplacement,
            boolean faceCachee,
            String type,
            int nombreEtoiles,
            int attaque,
            int defense,
            EffetMonstre effet
    ) {
        super(
                titre,
                description,
                cheminImage,
                emplacement,
                faceCachee,
                type,
                nombreEtoiles,
                attaque,
                defense
        );

        this.effet = effet;
    }

    public EffetMonstre getEffet() {
        return effet;
    }
}