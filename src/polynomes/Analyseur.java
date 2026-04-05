package polynomes;

import java.util.Scanner;

public class Analyseur {

    private String texte;
    private int pos;

    public Analyseur(String texte) {
        this.texte = texte.replaceAll(" ", "");
        this.pos = 0;
    }

    private char caractereCourant() {
        if (pos < texte.length())
            return texte.charAt(pos);
        return '\0';
    }

    private void avancer() { pos++; }

    private boolean finDeTexte() { return pos >= texte.length(); }

    private void erreur(String message) {
        System.out.println("Erreur a la position " + pos + " : " + message);
        System.exit(1);
    }

    private boolean estChiffre() {
        return caractereCourant() >= '0' && caractereCourant() <= '9';
    }

    // naturel -> chiffre { chiffre }
    private void analyserNaturel() {
        if (!estChiffre()) erreur("chiffre attendu");
        while (estChiffre()) avancer();
    }

    // nombre -> naturel [ '.' { chiffre } ]
    private void analyserNombre() {
        analyserNaturel();
        if (caractereCourant() == '.') {
            avancer();
            while (estChiffre()) avancer();
        }
    }

    // xpuissance -> 'X' | 'X' '^' naturel
    private void analyserXpuissance() {
        if (caractereCourant() != 'X') erreur("'X' attendu");
        avancer();
        if (caractereCourant() == '^') { avancer(); analyserNaturel(); }
    }

    // monome -> nombre '*' xpuissance | xpuissance | nombre
    private void analyserMonome() {
        if (caractereCourant() == 'X') {
            analyserXpuissance();
        } else if (estChiffre()) {
            analyserNombre();
            if (caractereCourant() == '*') { avancer(); analyserXpuissance(); }
        } else {
            erreur("monome attendu");
        }
    }

    // polynome -> [ '-' ] monome { ( '+' | '-' ) monome }
    public void analyserPolynome() {
        if (caractereCourant() == '-') avancer();
        analyserMonome();
        while (caractereCourant() == '+' || caractereCourant() == '-') {
            avancer();
            analyserMonome();
        }
        if (!finDeTexte()) erreur("caracteres inattendus en fin de polynome");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Analyseur Syntaxique de Polynomes ===");
        System.out.print("Saisissez un polynome : ");
        String saisie = scanner.nextLine();
        Analyseur analyseur = new Analyseur(saisie);
        analyseur.analyserPolynome();
        System.out.println("Le polynome \"" + saisie + "\" est syntaxiquement correct.");
        scanner.close();
    }
}
