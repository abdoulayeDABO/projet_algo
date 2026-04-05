package polynomes;

import java.util.Scanner;

public class Analyseur {

    private String texte;
    private int pos;
    private Polynome polynome;

    public Analyseur(String texte) {
        this.texte = texte.replaceAll(" ", "");
        this.pos = 0;
        this.polynome = new Polynome();
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
    private int analyserNaturelValeur() {
    if (!estChiffre()) erreur("chiffre attendu");
    int val = 0;
    while (estChiffre()) {
        val = val * 10 + (caractereCourant() - '0');
        avancer();
    }
    return val;
    }
    private double analyserNombreValeur() {
    double val = analyserNaturelValeur();
    if (caractereCourant() == '.') {
        avancer();
        double frac = 0.1;
        while (estChiffre()) {
            val += (caractereCourant() - '0') * frac;
            frac *= 0.1;
            avancer();
        }
    }
    return val;
    }

    // xpuissance -> 'X' | 'X' '^' naturel
    private void analyserXpuissance() {
    if (caractereCourant() != 'X') erreur("'X' attendu");
    avancer();
    if (caractereCourant() == '^') { avancer(); analyserNaturelValeur(); }
    }

    // monome -> nombre '*' xpuissance | xpuissance | nombre
    private void analyserMonome(double signe) {
    double coefficient = 1.0;
    int exposant = 0;

    if (caractereCourant() == 'X') {
        // Cas : X ou X^n
        coefficient = 1.0;
        avancer();
        if (caractereCourant() == '^') {
            avancer();
            exposant = analyserNaturelValeur();
        } else {
            exposant = 1;
        }
    } else if (estChiffre()) {
        // Cas : nombre seul ou nombre * X^n
        coefficient = analyserNombreValeur();
        if (caractereCourant() == '*') {
            avancer();
            if (caractereCourant() != 'X') erreur("'X' attendu");
            avancer();
            if (caractereCourant() == '^') {
                avancer();
                exposant = analyserNaturelValeur();
            } else {
                exposant = 1;
            }
        } else {
            exposant = 0;
        }
    } else {
        erreur("monome attendu");
    }

    polynome.ajouterMonome(signe * coefficient, exposant);
    }


    // polynome -> [ '-' ] monome { ( '+' | '-' ) monome }
    public void analyserPolynome() {
    double signe = 1.0;
    if (caractereCourant() == '-') {
        signe = -1.0;
        avancer();
    }
    analyserMonome(signe);

    while (caractereCourant() == '+' || caractereCourant() == '-') {
        if (caractereCourant() == '-') signe = -1.0;
        else signe = 1.0;
        avancer();
        analyserMonome(signe);
    }

    if (!finDeTexte()) erreur("caracteres inattendus en fin de polynome");
    }

    public Polynome getPolynome() {
    return polynome;
    }

    public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("=== Analyseur Syntaxique de Polynomes ===");
    System.out.print("Saisissez un polynome : ");
    String saisie = scanner.nextLine();
    Analyseur analyseur = new Analyseur(saisie);
    analyseur.analyserPolynome();
    System.out.println("Syntaxe correcte !");
    System.out.print("Polynome : ");
    analyseur.getPolynome().afficher();
    scanner.close();
    }
}
