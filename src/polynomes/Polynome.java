package polynomes;

public class Polynome {
    Monome tete; // premier maillon de la liste
    // Constructeur — liste vide au départ

    public Polynome(Monome tete) {
        this.tete = tete;
    }

    // Ajoute un monôme en tête de liste
    public void ajouterMonome(double coefficient, int exposant) {
        Monome nouveau = new Monome(coefficient, exposant);
        nouveau.suivant = this.tete;
        this.tete = nouveau;
    }

    // Affichage du polynôme
    public void afficher() {
        if (tete == null) {
            System.out.println("0");
            return;
        }

        Monome m = tete;
        boolean premier = true;

        while (m != null) {
            double coef = m.coefficient;
            int exp = m.exposant;

            if (coef == 0) {
                m = m.suivant;
                continue;
            }

            // Signe
            if (premier) {
                if (coef < 0)
                    System.out.print("- ");
            } else {
                if (coef < 0)
                    System.out.print(" - ");
                else
                    System.out.print(" + ");
            }

            double valeur = Math.abs(coef);

            // Affichage selon exposant
            if (exp == 0) {
                System.out.print(valeur);
            } else if (exp == 1) {
                if (valeur != 1)
                    System.out.print(valeur);
                System.out.print("X");
            } else {
                if (valeur != 1)
                    System.out.print(valeur);
                System.out.print("X^" + exp);
            }

            premier = false;
            m = m.suivant;
        }
        System.out.println();
    }

}
