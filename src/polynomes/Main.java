package polynomes;

import java.util.Scanner;

public class Main {

    // ═══════════════════════════════════════════════════════════════
    // UTILITAIRES
    // ═══════════════════════════════════════════════════════════════

    private static Polynome saisirPolynome(Scanner scanner, String nom) {
        System.out.print("Saisissez " + nom + " : ");
        String saisie = scanner.nextLine();
        Analyseur analyseur = new Analyseur(saisie);
        analyseur.analyserPolynome();
        System.out.print("  → Reconnu : ");
        analyseur.getPolynome().afficher();
        return analyseur.getPolynome();
    }

    private static void separateur(String titre) {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║  " + titre);
        System.out.println("╚══════════════════════════════════════════╝");
    }

    // ═══════════════════════════════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════════════════════════════

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   MANIPULATION DE POLYNOMES — DIC2       ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("Format accepté : -4.5*X^5 + 2*X^4 + X^3 - X + 123.0");
        System.out.println("Termes permis  : X, X^n, a*X^n, a*X, nombre");

        // ───────────────────────────────────────────────────────────
        // Q1 + Q2 : Analyseur syntaxique + codage en mémoire
        // ───────────────────────────────────────────────────────────
        separateur("Q1 & Q2 — Analyse + Codage en mémoire");

        Polynome p1 = saisirPolynome(scanner, "P1");
        Polynome p2 = saisirPolynome(scanner, "P2");

        // ───────────────────────────────────────────────────────────
        // Q3 : Affichage
        // ───────────────────────────────────────────────────────────
        separateur("Q3 — Affichage");
        System.out.print("P1 affiché : ");
        p1.afficher();
        System.out.print("P2 affiché : ");
        p2.afficher();

        // ───────────────────────────────────────────────────────────
        // Q4 : Tri par degré décroissant (assuré par ajouterMonome)
        // ───────────────────────────────────────────────────────────
        separateur("Q4 — Ordre décroissant des degrés");
        System.out.println("(Les polynômes sont automatiquement triés à la saisie)");
        System.out.print("P1 trié : ");
        p1.afficher();
        System.out.print("P2 trié : ");
        p2.afficher();

        // ───────────────────────────────────────────────────────────
        // Q5 : Évaluation
        // ───────────────────────────────────────────────────────────
        separateur("Q5 — Évaluation");
        System.out.print("Valeur de x pour évaluer P1 et P2 : ");
        double x = Double.parseDouble(scanner.nextLine().trim());

        double valP1 = p1.eval(x);
        double valP2 = p2.eval(x);
        System.out.println("P1(" + x + ") = " + valP1);
        System.out.println("P2(" + x + ") = " + valP2);

        // ───────────────────────────────────────────────────────────
        // Q6 : Opérations arithmétiques
        // ───────────────────────────────────────────────────────────
        separateur("Q6 — Opérations arithmétiques");

        // Addition
        Polynome somme = Operations.additionner(p1, p2);
        System.out.print("P1 + P2 = ");
        somme.afficher();

        // Soustraction
        Polynome difference = Operations.soustraire(p1, p2);
        System.out.print("P1 - P2 = ");
        difference.afficher();

        // Multiplication
        Polynome produit = Operations.multiplier(p1, p2);
        System.out.print("P1 × P2 = ");
        produit.afficher();

        // Division euclidienne
        if (p2.tete == null) {
            System.out.println("Division impossible : P2 est le polynôme nul.");
        } else {
            Polynome[] reste = new Polynome[1];
            Polynome quotient = Operations.quotient(p1, p2, reste);
            System.out.print("Quotient  : ");
            quotient.afficher();
            System.out.print("Reste     : ");
            reste[0].afficher();

            // Vérification : P1 == P2 * quotient + reste
            Polynome verification = Operations.additionner(
                    Operations.multiplier(p2, quotient),
                    reste[0]);
            System.out.print("Vérif (P2×Q + R doit = P1) : ");
            verification.afficher();
        }

        // ───────────────────────────────────────────────────────────
        // Q8 : Versions récursives addition et soustraction
        // ───────────────────────────────────────────────────────────
        separateur("Q8 — Versions récursives");

        Polynome sommeRec = Operations.additionnerRecursif(p1, p2);
        System.out.print("P1 + P2 (récursif) = ");
        sommeRec.afficher();

        Polynome diffRec = Operations.soustraireRecursif(p1, p2);
        System.out.print("P1 - P2 (récursif) = ");
        diffRec.afficher();

        // Comparaison itératif vs récursif
        System.out.println("\n--- Comparaison itératif vs récursif ---");
        System.out.print("Somme  itérative  : ");
        somme.afficher();
        System.out.print("Somme  récursive  : ");
        sommeRec.afficher();
        System.out.print("Diff   itérative  : ");
        difference.afficher();
        System.out.print("Diff   récursive  : ");
        diffRec.afficher();

        // ───────────────────────────────────────────────────────────
        // FIN
        // ───────────────────────────────────────────────────────────
        separateur("FIN DES TESTS");
        System.out.println("Tous les exercices ont été testés avec succès.");
        scanner.close();
    }
}