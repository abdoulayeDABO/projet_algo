public class Polynome {

    Maillon tete; // premier maillon de la liste

    // Constructeur — au départ la liste est vide
    public Polynome() {
        this.tete = null;
    }

    // Ajoute un monôme en tête de liste
    public void ajouterMaillon(double coefficient, int exposant) {
        Maillon nouveau = new Maillon(coefficient, exposant);
        nouveau.suivant = this.tete;
        this.tete = nouveau;
    }
}