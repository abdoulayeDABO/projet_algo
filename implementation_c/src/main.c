#include <stdio.h>
#include <stdlib.h>
#include "polynome.h"
#include "analyseur.h"

/* Analyse une expression, affiche le resultat et retourne le polynome */
static Polynome *analyserEtAfficher(const char *expr) {
    Analyseur a;
    initAnalyseur(&a, expr);
    Polynome *p = analyserPolynome(&a);
    printf("  entree   : %s\n", expr);
    printf("  resultat : "); afficher(p);
    return p;
}

int main(void) {

    /* Q1-Q4 : analyse syntaxique, codage et affichage */
    puts("=== Q1-Q4 : Analyseur + codage + affichage ===");
    const char *exemples[] = {
        "- 4.5*X^5 + 2*X^4 + X^3 - X + 123.0",
        "3*X^2 + 2*X + 1",
        "X^3 - X",
        "5"
    };
    int n = sizeof(exemples) / sizeof(*exemples);
    Polynome *poly[4];
    for (int i = 0; i < n; i++) {
        poly[i] = analyserEtAfficher(exemples[i]);
        enregistrerPolynome(poly[i]);
        puts("");
    }

    /* Q5 : evaluation numerique */
    puts("=== Q5 : Evaluation ===");
    printf("  3*X^2 + 2*X + 1  en x=2 → %g  (attendu : 17)\n", eval(poly[1], 2.0));
    printf("  X^3 - X          en x=3 → %g  (attendu : 24)\n\n", eval(poly[2], 3.0));

    /* Q6 : operations arithmetiques */
    puts("=== Q6 : Operations ===");
    Analyseur a1, a2;
    initAnalyseur(&a1, "X^2 + 2*X + 1");
    initAnalyseur(&a2, "X + 1");
    Polynome *p1 = analyserPolynome(&a1);
    Polynome *p2 = analyserPolynome(&a2);
    printf("  P1 = "); afficher(p1);
    printf("  P2 = "); afficher(p2);

    Polynome *somme   = plus(p1, p2);
    printf("  P1 + P2 = "); afficher(somme);

    Polynome *diff    = moins(p1, p2);
    printf("  P1 - P2 = "); afficher(diff);

    Polynome *produit = fois(p1, p2);
    printf("  P1 * P2 = "); afficher(produit);

    Polynome *reste;
    Polynome *quot = quotient(p1, p2, &reste);
    printf("  P1 / P2 = "); afficher(quot);
    printf("  reste   = "); afficher(reste);
    puts("");

    /* Q8 : versions recursives de + et - */
    puts("=== Q8 : Versions recursives ===");
    Polynome *sommeRec = plusRec(p1, p2);
    printf("  P1 +rec P2 = "); afficher(sommeRec);
    Polynome *diffRec  = moinsRec(p1, p2);
    printf("  P1 -rec P2 = "); afficher(diffRec);
    puts("");

    /* Q7 : garbage collector */
    puts("=== Q7 : Garbage collector ===");
    enregistrerPolynome(p1);   enregistrerPolynome(p2);
    enregistrerPolynome(somme); enregistrerPolynome(diff);
    enregistrerPolynome(produit);
    enregistrerPolynome(quot); enregistrerPolynome(reste);
    enregistrerPolynome(sommeRec); enregistrerPolynome(diffRec);
    garbageCollect();
    puts("  Garbage collector execute avec succes.");

    return EXIT_SUCCESS;
}
