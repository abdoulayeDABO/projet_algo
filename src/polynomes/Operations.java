package polynomes;
public class Operations {
    //fonction d'addition de deux polynomes
   public static Polynome additionner(Polynome p1, Polynome p2) {
        Monome tete = null;
        //cette variable nous permet de savoir ou rattacher le prochain monome resultat
        Monome queue = null;
        //on recupere les têtes des deux polynomes a additionner pour pouvoir les parcourir simultanement
        Monome m1 = p1.tete;
        Monome m2 = p2.tete;
        // tant que l'un des deux polynomes n'est pas encore parcouru entierement
        while (m1 != null || m2 != null) {
         double coef;
         int exp;
            //si on a fini de parcourir p1, on prend p2
            if (m1 == null) {
                coef = m2.coefficient; exp = m2.exposant; m2 = m2.suivant;
            } else if (m2 == null) {
            // si on a fini de parcourir p2, on prend p1 
                coef = m1.coefficient; exp = m1.exposant; m1 = m1.suivant;
            } else if (m1.exposant == m2.exposant) {
            //si ils ont le meme degre : on additionne les coefficients
                coef = m1.coefficient + m2.coefficient;
                exp = m1.exposant;
                m1 = m1.suivant; m2 = m2.suivant;

            } else if (m1.exposant > m2.exposant) {
            // si m1 a le plus grand degre,on prend m1 et on avance dans m1
                coef = m1.coefficient; exp = m1.exposant; m1 = m1.suivant;
            } else {
            // si p2 a le plus grand degre, on prend p2 et on avance dans p2
                coef = m2.coefficient; exp = m2.exposant; m2 = m2.suivant;
            }
            // on ignore les coefficients nuls car ils n'ont pas d'impact sur le resultat
            if (coef != 0) {
            //on cree un nouveau monome avec le coefficient et l'exposant calcules pour ce degre
                Monome nouveau = new Monome(coef, exp);
            //si la tete est nulle,cad on est au debut de la liste, on initialise la tete avec le nouveau monome
                if (tete == null) tete = nouveau;
            //sinon on ajoute ce monome a la fin de notre polynome resultat
                else queue.suivant = nouveau;
                queue = nouveau;
            }
        }
        //on retourne le Polynome resultat
        return new Polynome(tete);
    }
    //fonction de soustraction de deux polynomes
    public static Polynome soustraire(Polynome p1, Polynome p2){
        Monome tete = null;
        Monome queue = null;
        Monome m1 = p1.tete;
        Monome m2 = p2.tete;
        while (m1!=null || m2!=null) {
            double coef;
            int exp;
            //on va appliquer la meme logique que pour l'addition, mais en soustrayant les coefficients de p2 au lieu de les additionner
            if (m1 == null) {
                coef = -m2.coefficient; exp = m2.exposant; m2 = m2.suivant;
            } else if (m2 == null) {
                coef = m1.coefficient; exp = m1.exposant; m1 = m1.suivant;
            } else if (m1.exposant == m2.exposant) {
                coef = m1.coefficient - m2.coefficient;
                exp = m1.exposant;
                m1 = m1.suivant; 
                m2 = m2.suivant;
            } else if (m1.exposant > m2.exposant) {
                coef = m1.coefficient; exp = m1.exposant; m1 = m1.suivant;
            } else {
                coef = -m2.coefficient; exp = m2.exposant; m2 = m2.suivant;
            }
            //si on a un coefficient non nul, on cree un nouveau monome et on l'ajoute a la fin de notre polynome resultat
            if (coef != 0) {
                Monome nouveau = new Monome(coef, exp);
                if (tete == null) tete = nouveau;
                else queue.suivant = nouveau;
                queue = nouveau;
            }
        }
        //
        return new Polynome(tete);
    }
//test des fonctions d'addition et de soustraction de polynomes
   public static void main(String[] args) {
    //sachant que la fonction de tri n'a pas encore ete implementee, on va creer les polynomes de test en ajoutant les monomes dans l'ordre decroissant des exposants pour faciliter les calculs
        Polynome p1 = new Polynome(null);
        p1.ajouterMonome(1, 0); // 1
        p1.ajouterMonome(2, 1); // 2X
        p1.ajouterMonome(3, 2); // 3X^2

        Polynome p2 = new Polynome(null);
        p2.ajouterMonome(4, 0); // 4
        p2.ajouterMonome(-3, 2); // -3X^2
        p2.ajouterMonome(5, 3); // 5X^3

        System.out.println("P1 : ");
        p1.afficher();
        System.out.println("P2 : ");
        p2.afficher();

        Polynome somme = additionner(p1, p2);
        System.out.println("P1 + P2 : ");
        somme.afficher();

        Polynome difference = soustraire(p1, p2);
        System.out.println("P1 - P2 : ");
        difference.afficher();
 }
} 
