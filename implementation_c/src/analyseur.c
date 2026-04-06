#include "analyseur.h"
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>


/* Retourne le caractere courant sans avancer */
static char courant(const Analyseur *a) {
    return a->texte[a->pos];
}

/* Passe au caractere suivant */
static void avancer(Analyseur *a) {
    a->pos++;
}

/* Retourne 1 si on a atteint la fin de la chaine */
static int finDeTexte(const Analyseur *a) {
    return a->texte[a->pos] == '\0';
}

/* Saute les espaces et tabulations */
static void ignorerEspaces(Analyseur *a) {
    while (!finDeTexte(a) && isspace((unsigned char)courant(a)))
        avancer(a);
}

/* Affiche un message d'erreur avec la position et arrete le programme */
static void erreur(const char *msg, int pos) {
    fprintf(stderr, "Erreur syntaxique a la position %d : %s\n", pos, msg);
    exit(EXIT_FAILURE);
}



/* naturel → chiffre { chiffre }
   Lit une suite de chiffres et retourne la valeur entiere */
static int analyserNaturel(Analyseur *a) {
    if (!isdigit((unsigned char)courant(a)))
        erreur("chiffre attendu", a->pos);

    int n = 0;
    while (!finDeTexte(a) && isdigit((unsigned char)courant(a))) {
        n = n * 10 + (courant(a) - '0');
        avancer(a);
    }
    return n;
}

/* nombre → naturel [ '.' { chiffre } ]
   Lit un nombre entier ou decimal et retourne sa valeur */
static double analyserNombre(Analyseur *a) {
    double n = (double)analyserNaturel(a);

    /* Partie decimale optionnelle apres le point */
    if (!finDeTexte(a) && courant(a) == '.') {
        avancer(a);
        double dec = 0.1;
        while (!finDeTexte(a) && isdigit((unsigned char)courant(a))) {
            n  += (courant(a) - '0') * dec;
            dec *= 0.1;
            avancer(a);
        }
    }
    return n;
}

/* xpuissance → 'X' [ '^' naturel ]
   Lit la variable X avec son exposant optionnel.
   Retourne 1 si X est seul (X^1), sinon retourne l'exposant lu. */
static int analyserXpuissance(Analyseur *a) {
    if (courant(a) != 'X')
        erreur("'X' attendu", a->pos);
    avancer(a);

    /* Exposant optionnel apres '^' */
    if (!finDeTexte(a) && courant(a) == '^') {
        avancer(a);
        return analyserNaturel(a);
    }
    return 1;
}

/* monome → nombre '*' xpuissance | xpuissance | nombre
   Lit un monome et ecrit son coefficient dans *coef et son degre dans *exp.
   Trois formes possibles : "4.5*X^3", "X^3", "4.5" */
static void analyserMonome(Analyseur *a, double *coef, int *exp) {
    ignorerEspaces(a);

    if (courant(a) == 'X') {
        /* Forme : xpuissance seule → coefficient implicite = 1 */
        *coef = 1.0;
        *exp  = analyserXpuissance(a);

    } else if (isdigit((unsigned char)courant(a))) {
        double n = analyserNombre(a);
        ignorerEspaces(a);

        if (!finDeTexte(a) && courant(a) == '*') {
            /* Forme : nombre '*' xpuissance */
            avancer(a);
            ignorerEspaces(a);
            *coef = n;
            *exp  = analyserXpuissance(a);
        } else {
            /* Forme : nombre seul → terme constant de degre 0 */
            *coef = n;
            *exp  = 0;
        }
    } else {
        erreur("monome attendu (chiffre ou 'X')", a->pos);
    }
}



/* Initialise l'analyseur sur la chaine texte */
void initAnalyseur(Analyseur *a, const char *texte) {
    a->texte = texte;
    a->pos   = 0;
}

/* polynome → [ '-' ] monome { ( '+' | '-' ) monome }
   Analyse la chaine entiere et construit le polynome correspondant.
   Arrete le programme avec un message si la syntaxe est incorrecte. */
Polynome *analyserPolynome(Analyseur *a) {
    Polynome *p = creerPolynome();
    double coef, signe = 1.0;
    int    exp;

    ignorerEspaces(a);

    /* Signe moins optionnel devant le premier monome */
    if (!finDeTexte(a) && courant(a) == '-') {
        signe = -1.0;
        avancer(a);
        ignorerEspaces(a);
    }

    /* Premier monome obligatoire */
    analyserMonome(a, &coef, &exp);
    ajouterOrdreDecroissant(p, signe * coef, exp);

    /* Monomes suivants separes par '+' ou '-' */
    while (1) {
        ignorerEspaces(a);
        if (finDeTexte(a)) break;

        if      (courant(a) == '+') { signe =  1.0; avancer(a); }
        else if (courant(a) == '-') { signe = -1.0; avancer(a); }
        else    erreur("'+' ou '-' attendu", a->pos);

        ignorerEspaces(a);
        analyserMonome(a, &coef, &exp);
        ajouterOrdreDecroissant(p, signe * coef, exp);
    }

    return p;
}
