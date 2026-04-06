package polynomes;

public class Polynome {
    Monome tete;

    public Polynome(Monome tete) {
        this.tete = tete;
    }

    // Q4 : insertion triée par degré décroissant + fusion des mêmes degrés
    public void ajouterMonome(double coefficient, int exposant) {
        if (coefficient == 0)
            return;

        // Cas 1 : liste vide ou degré plus grand que la tête
        if (tete == null || exposant > tete.exposant) {
            Monome nouveau = new Monome(coefficient, exposant);
            nouveau.suivant = tete;
            tete = nouveau;
            return;
        }

        // Cas 2 : même degré que la tête → fusion
        if (tete.exposant == exposant) {
            tete.coefficient += coefficient;
            if (tete.coefficient == 0)
                tete = tete.suivant;
            return;
        }

        // Cas 3 : parcours pour trouver la bonne position
        Monome courant = tete;
        while (courant.suivant != null && courant.suivant.exposant > exposant) {
            courant = courant.suivant;
        }

        if (courant.suivant != null && courant.suivant.exposant == exposant) {
            // Fusion avec un monôme existant de même degré
            courant.suivant.coefficient += coefficient;
            if (courant.suivant.coefficient == 0)
                courant.suivant = courant.suivant.suivant;
        } else {
            // Insertion entre courant et courant.suivant
            Monome nouveau = new Monome(coefficient, exposant);
            nouveau.suivant = courant.suivant;
            courant.suivant = nouveau;
        }
    }

    // Q3 : Affichage (inchangé)
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
            if (premier) {
                if (coef < 0)
                    System.out.print("- ");
            } else {
                System.out.print(coef < 0 ? " - " : " + ");
            }
            double valeur = Math.abs(coef);
            if (exp == 0)
                System.out.print(valeur);
            else if (exp == 1) {
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

    // Q5 : Évaluation du polynôme en x
    public double eval(double x) {
        double resultat = 0;
        Monome m = tete;
        while (m != null) {
            resultat += m.coefficient * Math.pow(x, m.exposant);
            m = m.suivant;
        }
        return resultat;
    }
}