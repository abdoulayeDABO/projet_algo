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

    public void afficher() {
    if (tete == null) {
        System.out.println("0");
        return;
    }

    Maillon m = tete;
    boolean premier = true;

    while (m != null) {
        double coef = m.coefficient;
        int exp = m.exposant;

        // Ignorer les monômes de coefficient 0
        if (coef == 0) {
            m = m.suivant;
            continue;
        }

        // Gérer le signe
        if (premier) {
            if (coef < 0) System.out.print("- ");
        } else {
            if (coef < 0) System.out.print(" - ");
            else          System.out.print(" + ");
        }

        double valeur = Math.abs(coef);

        // Afficher selon l'exposant
        if (exp == 0) {
            System.out.print(valeur);
        } else if (exp == 1) {
            if (valeur != 1) System.out.print(valeur);
            System.out.print("X");
        } else {
            if (valeur != 1) System.out.print(valeur);
            System.out.print("X^" + exp);
        }

        premier = false;
        m = m.suivant;
    }
    System.out.println();
}

}