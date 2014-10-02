package fr.univtln.bruno.exemple.bibliotheque;

import fr.univtln.bruno.exemple.bibliotheque.exceptions.personnes.AuteurInconnuException;
import fr.univtln.bruno.exemple.bibliotheque.matériel.OrdinateurPortable;
import fr.univtln.bruno.exemple.bibliotheque.personne.Personne;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by bruno on 02/10/14.
 */
public class App {
    public static void main(String[] args) {

        BibliothequeModele bibliothèque = null;

        //Initialisation de la bibliothèque
        try {
            bibliothèque = new BibliothequeModele<Bibliotheque>("Ma Bibliothèque");

            bibliothèque.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    System.out.println(((IBibliotheque)o).getNom()+ " -> " + ((BibliothequeModele.Changement)arg));
                }
            });

            bibliothèque.addAuteur(new Personne("marie.durand@test.fr", "marie", "durand"))
                    .addAuteur(new Personne("jean.martin@test.fr", "jean", "martin"))
                    .addAdhérent("pierre.dupond@test.fr", "pierre", "dupond", Bibliotheque.Adhérent.Statut.ENSEIGNANT)
                    .addAdhérent("marc.durand@test.fr", "marc", "durand", Bibliotheque.Adhérent.Statut.ETUDIANT)
                    .addOrdinateurPortable("Vaio", OrdinateurPortable.Os.LINUX)
                    .addOrdinateurPortable("Dell", OrdinateurPortable.Os.WINDOWS)
                    .addOrdinateurPortable("Macbook Pro", OrdinateurPortable.Os.MACOS)
                    .addLivre("123-XY", "Mon livre 1", "marie.durand@test.fr")
                    .addLivre("A", "Mon livre 1", "marie.durand@test.fr")
                    .addLivre("B", "Mon livre B", "marie.durand@test.fr")
                    .addLivre("C", "Mon livre C", "marie.durand@test.fr")
                    .addLivre("D", "Mon livre D", "marie.durand@test.fr");
        } catch (AuteurInconnuException e) {
            System.err.println(e.getClass() + " : " +
                    e.getMessage());
        }


    }

}
