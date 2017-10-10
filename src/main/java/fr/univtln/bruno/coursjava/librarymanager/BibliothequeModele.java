package fr.univtln.bruno.coursjava.librarymanager;


import fr.univtln.bruno.coursjava.librarymanager.emprunts.Empruntable;
import fr.univtln.bruno.coursjava.librarymanager.exceptions.documents.DocumentInconnuException;
import fr.univtln.bruno.coursjava.librarymanager.exceptions.emprunts.EmpruntImpossibleException;
import fr.univtln.bruno.coursjava.librarymanager.exceptions.emprunts.NonEmpruntableException;
import fr.univtln.bruno.coursjava.librarymanager.exceptions.materiels.MaterielInconnuException;
import fr.univtln.bruno.coursjava.librarymanager.exceptions.personnes.AdherentInconnuException;
import fr.univtln.bruno.coursjava.librarymanager.exceptions.personnes.AuteurInconnuException;
import fr.univtln.bruno.coursjava.librarymanager.exceptions.sauvegarde.SauvegardeException;
import fr.univtln.bruno.coursjava.librarymanager.fondDocumentaire.Document;
import fr.univtln.bruno.coursjava.librarymanager.fondDocumentaire.Livre;
import fr.univtln.bruno.coursjava.librarymanager.matériel.Matériel;
import fr.univtln.bruno.coursjava.librarymanager.matériel.OrdinateurPortable;
import fr.univtln.bruno.coursjava.librarymanager.personne.Personne;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Observable;
import java.util.Set;
import java.util.logging.Logger;

/**
 * La classe Bibliothèque est une facade pour la gestion complète d'une bibliothèque. Elle permet de créer des ressources (Documents, Adhérents, Matériel)
 * et les emprunts.
 */
public class BibliothequeModele<B extends IBibliotheque> extends Observable implements IBibliotheque {
    /**
     * Le logger de la classe courante
     */
    private static Logger logger = Logger.getLogger(BibliothequeModele.class.getName());
    private IBibliotheque bibliotheque;


    public BibliothequeModele(String nom) {
        this.bibliotheque = new Bibliotheque(nom);
    }

    public BibliothequeModele(B bibliotheque) {
        this.bibliotheque = bibliotheque;
    }

    /**
     * Ajoute une personne aux auteurs.
     *
     * @param auteur
     * @return
     */
    public IBibliotheque addAuteur(Personne auteur) {
        bibliotheque.addAuteur(auteur);
        setChanged();
        notifyObservers(new Changement(Changement.Type.ADD, Changement.Domain.AUTEUR));
        return this;
    }

    /**
     * Ajoute un nouveau matériel à la bibliothèque
     *
     * @param matériel
     * @return
     */
    public IBibliotheque add(Matériel matériel) {
        bibliotheque.add(matériel);
        setChanged();
        notifyObservers(new Changement(Changement.Type.ADD, Changement.Domain.MATERIEL));
        return this;
    }

    /**
     * Supprime un matériel de la bibliothèque
     *
     * @param matériel
     * @return
     */
    public IBibliotheque remove(Matériel matériel) {
        bibliotheque.remove(matériel);
        setChanged();
        notifyObservers(new Changement(Changement.Type.REMOVE, Changement.Domain.MATERIEL));
        return this;
    }

    /**
     * Ajoute un Document à la bibliothèque
     *
     * @param document
     * @return
     */
    public IBibliotheque add(Document document) {
        bibliotheque.add(document);
        setChanged();
        notifyObservers(new Changement(Changement.Type.ADD, Changement.Domain.DOCUMENT));
        return this;
    }

    /**
     * Supprime un document de la bibliothèque
     *
     * @param document
     * @return
     */
    public IBibliotheque remove(Document document) {
        bibliotheque.remove(document);
        setChanged();
        notifyObservers(new Changement(Changement.Type.REMOVE, Changement.Domain.DOCUMENT));
        return this;
    }

    /**
     * Ajoute un adhérent à la bibliothèque
     *
     * @param adhérent
     * @return
     */
    public IBibliotheque add(IBibliotheque.Adhérent adhérent) {
        bibliotheque.add(adhérent);
        setChanged();
        notifyObservers(new Changement(Changement.Type.ADD, Changement.Domain.ADHERENT));
        return this;
    }

    /**
     * Supprime un adhérent de la bibliothèque
     *
     * @param adhérent
     * @return
     */
    public IBibliotheque remove(IBibliotheque.Adhérent adhérent) {
        bibliotheque.remove(adhérent);
        setChanged();
        notifyObservers(new Changement(Changement.Type.REMOVE, Changement.Domain.ADHERENT));
        return this;
    }

    @Override
    public String getNom() {
        return bibliotheque.getNom();
    }

    /**
     * Créée un adhérent et l'ajoute à la bibliothèque. C'est une facade de Adhérent.
     *
     * @param email
     * @param prenom
     * @param nom
     * @param statut
     * @return
     */
    public IBibliotheque addAdhérent(String email, String prenom, String nom, IBibliotheque.Adhérent.Statut statut) {
        bibliotheque.addAdhérent(email, prenom, nom, statut);
        setChanged();
        notifyObservers(new Changement(Changement.Type.ADD, Changement.Domain.ADHERENT));
        return this;
    }

    /**
     * Créée un ordinateur portable et l'ajoute à la bibliothèque. C'est une facade de Ordinateur portable.
     *
     * @param marque
     * @param os
     * @return
     */
    public IBibliotheque addOrdinateurPortable(String marque, OrdinateurPortable.Os os) {
        OrdinateurPortable.getInstance(bibliotheque, marque, os);
        setChanged();
        notifyObservers(new Changement(Changement.Type.ADD, Changement.Domain.MATERIEL));
        return this;
    }

    /**
     * Créée un Livre et l'ajoute à la bibliothèque. C'est une facade de Livre.
     *
     * @param isbn
     * @param titre
     * @param auteur
     * @return
     */
    public IBibliotheque addLivre(String isbn, String titre, String auteur) throws AuteurInconnuException {
        Livre.getInstance(bibliotheque, isbn, titre, bibliotheque.getAuteur(auteur));
        setChanged();
        notifyObservers(new Changement(Changement.Type.ADD, Changement.Domain.DOCUMENT));
        return this;
    }

    @Override
    public IBibliotheque addLivre(String s, String s2, IPersonne iPersonne) {
        return null;
    }

    /**
     * Créée un Livre et l'ajoute à la bibliothèque. C'est une facade de Livre.
     *
     * @param isbn
     * @param titre
     * @param auteur
     * @return
     */
    public IBibliotheque addLivre(String isbn, String titre, Personne auteur) {
        Livre.getInstance(bibliotheque, isbn, titre, auteur);
        setChanged();
        notifyObservers(new Changement(Changement.Type.ADD, Changement.Domain.DOCUMENT));
        return this;
    }

    /**
     * retourne un document correspondant à un ISBN.
     *
     * @param isbn
     * @return
     * @throws fr.univtln.bruno.coursjava.librarymanager.exceptions.documents.DocumentInconnuException
     */
    public Document getDocument(String isbn) throws DocumentInconnuException {
        return bibliotheque.getDocument(isbn);
    }

    /**
     * Retourne un matériel correspondant à un id.
     *
     * @param id
     * @return
     * @throws fr.univtln.bruno.coursjava.librarymanager.exceptions.materiels.MaterielInconnuException
     */
    public Matériel getMatériel(int id) throws MaterielInconnuException {
        return bibliotheque.getMatériel(id);
    }

    /**
     * Retourne un matériel correspondant à un id de personne.
     *
     * @param id
     * @return
     * @throws fr.univtln.bruno.coursjava.librarymanager.exceptions.personnes.AdherentInconnuException
     */
    public IBibliotheque.Adhérent getAdhérent(IPersonne.Id id) throws AdherentInconnuException {
        return bibliotheque.getAdhérent(id);
    }

    /**
     * Retourne un matériel correspondant à un id sous forme de string.
     *
     * @param email
     * @return
     * @throws fr.univtln.bruno.coursjava.librarymanager.exceptions.personnes.AdherentInconnuException
     */
    public IBibliotheque.Adhérent getAdhérent(String email) throws AdherentInconnuException {
        return getAdhérent(new Personne.Id(email));
    }

    public IBibliotheque rendreOrdinateur(int id) throws MaterielInconnuException, NonEmpruntableException {
        bibliotheque.rendreOrdinateur(id);
        setChanged();
        notifyObservers(new Changement(Changement.Type.REMOVE, Changement.Domain.EMPRUNT));
        return this;
    }

    public BibliothequeModele rendreLivre(String isbn) throws DocumentInconnuException, NonEmpruntableException {
        bibliotheque.rendreLivre(isbn);
        setChanged();
        notifyObservers(new Changement(Changement.Type.REMOVE, Changement.Domain.EMPRUNT));
        return this;
    }

    public BibliothequeModele rendre(Empruntable empruntable) {
        empruntable.estRendu();
        setChanged();
        notifyObservers(new Changement(Changement.Type.REMOVE, Changement.Domain.EMPRUNT));
        return this;
    }

    public BibliothequeModele emprunterOrdinateur(String emprunteur, int id) throws MaterielInconnuException, NonEmpruntableException, AdherentInconnuException, EmpruntImpossibleException, DocumentInconnuException {
        bibliotheque.emprunterOrdinateur(emprunteur, id);
        setChanged();
        notifyObservers(new Changement(Changement.Type.ADD, Changement.Domain.EMPRUNT));
        return this;
    }

    public BibliothequeModele emprunterLivre(String emprunteur, String isbn) throws MaterielInconnuException, NonEmpruntableException, AdherentInconnuException, EmpruntImpossibleException, DocumentInconnuException {
        bibliotheque.emprunterLivre(emprunteur, isbn);
        setChanged();
        notifyObservers(new Changement(Changement.Type.ADD, Changement.Domain.EMPRUNT));
        return this;
    }

    public BibliothequeModele emprunter(IBibliotheque.Adhérent emprunteur, Empruntable empruntable) throws MaterielInconnuException,
            AdherentInconnuException, NonEmpruntableException, EmpruntImpossibleException {
        bibliotheque.emprunter(emprunteur, empruntable);
        setChanged();
        notifyObservers(new Changement(Changement.Type.ADD, Changement.Domain.EMPRUNT));
        return this;
    }

    /**
     * @return L'ensemble des Id des matériels.
     */
    public Set<Integer> getMaterielIds() {
        return bibliotheque.getMaterielIds();
    }

    @Override
    public IPersonne getAuteur(IPersonne.Id id) throws AuteurInconnuException {
        return null;
    }

    /**
     * @return L'ensemble des Ids des adhérents
     */
    public Set<IPersonne.Id> getAdherentsIds() {
        return bibliotheque.getAdherentsIds();
    }

    @Override
    public Collection<Matériel> getMatériels() {
        return bibliotheque.getMatériels();
    }

    @Override
    public Collection<Adhérent> getAdhérents() {
        return bibliotheque.getAdhérents();
    }

    @Override
    public Collection<Document> getDocuments() {
        return bibliotheque.getDocuments();
    }

    @Override
    public IBibliotheque addAuteur(IPersonne iPersonne) {
        bibliotheque.addAuteur(iPersonne);
        setChanged();
        notifyObservers(new Changement(Changement.Type.ADD, Changement.Domain.AUTEUR));
        return this;
    }

    @Override
    public IPersonne getAuteur(String s) throws AuteurInconnuException {
        return bibliotheque.getAuteur(s);
    }

    /**
     * @return L'ensemble des Ids des auteurs.
     */
    public Set<IPersonne.Id> getAuteursIds() {
        return bibliotheque.getAuteursIds();
    }

    @Override
    public IBibliotheque setNom(String s) {
        return null;
    }

    /**
     * Serialise la bibliotheque dans un fichier.
     *
     * @param filename Le nom du fichier dans lequel sera serialisée la bibliotheque
     * @throws fr.univtln.bruno.coursjava.librarymanager.exceptions.sauvegarde.SauvegardeException
     */
    public void exporter(String filename) throws SauvegardeException {
        bibliotheque.exporter(filename);
    }

    /**
     * Serialise la bibliotheque dans un flux.
     *
     * @param outputStream le flux de destination
     * @throws fr.univtln.bruno.coursjava.librarymanager.exceptions.sauvegarde.SauvegardeException
     */
    public void exporter(OutputStream outputStream) throws SauvegardeException {
        bibliotheque.exporter(outputStream);
    }

    public static class Changement {
        public final Type TYPE;
        public final Domain DOMAIN;

        public Changement(Type TYPE, Domain DOMAIN) {
            this.DOMAIN = DOMAIN;
            this.TYPE = TYPE;
        }

        @Override
        public String toString() {
            return "Changement{" +
                    "TYPE=" + TYPE +
                    ", DOMAIN=" + DOMAIN +
                    '}';
        }

        public enum Type {ADD, REMOVE, UPDATE}

        public enum Domain {DOCUMENT, MATERIEL, ADHERENT, AUTEUR, EMPRUNT}
    }

}
