#ifndef ANALYSEUR_H
#define ANALYSEUR_H

/* L'analyseur lit le texte caractere par caractere pour reconnaitre
   un polynome selon la grammaire du sujet (Q1 + Q2). */

#include "polynome.h"

typedef struct {
    const char *texte; /* chaine a analyser   */
    int         pos;   /* position courante   */
} Analyseur;

void      initAnalyseur   (Analyseur *a, const char *texte);
Polynome *analyserPolynome(Analyseur *a);

#endif
