#include "polynome.h"
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/* Variables globales du garbage collector */
Monome  *tousLesMaillons           = NULL;
Polynome *polyUtile[MAX_POLY_UTILE];
int       nbPolyUtile               = 0;



/* Cree un nouveau monome et l'ajoute a la liste globale tousLesMaillons */
Monome *nouveauMonome(double coef, int exp) {
    Monome *m = malloc(sizeof(Monome));
    if (!m) { fprintf(stderr, "Memoire insuffisante\n"); exit(EXIT_FAILURE); }

    m->coefficient  = coef;
    m->exposant     = exp;
    m->suivant      = NULL;
    m->utile        = 0;

    /* Ajout en tete de la liste globale pour que le GC puisse le retrouver */
    m->general      = tousLesMaillons;
    tousLesMaillons = m;
    return m;
}

/* Cree un polynome vide (liste vide) */
Polynome *creerPolynome(void) {
    Polynome *p = malloc(sizeof(Polynome));
    if (!p) { fprintf(stderr, "Memoire insuffisante\n"); exit(EXIT_FAILURE); }
    p->tete = NULL;
    return p;
}



/* Insere le terme coef*X^exp dans p en gardant l'ordre decroissant des degres.
   Si un monome du meme degre existe deja, on additionne les coefficients.
   Si le resultat est nul, on retire ce monome de la liste. */
void ajouterOrdreDecroissant(Polynome *p, double coef, int exp) {
    if (coef == 0.0) return;

    /* Avancer jusqu'a trouver un degre <= exp */
    Monome **cur = &p->tete;
    while (*cur != NULL && (*cur)->exposant > exp)
        cur = &(*cur)->suivant;

    if (*cur != NULL && (*cur)->exposant == exp) {
        /* Meme degre : on fusionne les coefficients */
        (*cur)->coefficient += coef;
        if ((*cur)->coefficient == 0.0) {
            /* Le terme devient nul, on le retire de la liste */
            *cur = (*cur)->suivant;
        }
    } else {
        /* Nouveau degre : on insere un maillon a cette position */
        Monome *m  = nouveauMonome(coef, exp);
        m->suivant = *cur;
        *cur       = m;
    }
}



/* Affiche un double sans decimale inutile : 3.0 s'affiche "3" */
static void afficherDouble(double v) {
    if (v == (double)(long)v) printf("%ld", (long)v);
    else                      printf("%g", v);
}

/* Affiche le polynome sous forme lisible, ex : - 4.5*X^5 + 2*X^4 + X + 3 */
void afficher(const Polynome *p) {
    if (p->tete == NULL) { printf("0\n"); return; }

    int premier = 1;
    for (Monome *m = p->tete; m != NULL; m = m->suivant) {
        double c = m->coefficient;
        int    e = m->exposant;
        if (c == 0.0) continue;

        /* Affichage du signe */
        if (premier)  { if (c < 0) printf("- "); }
        else          { printf(c < 0 ? " - " : " + "); }

        double v = fabs(c);

        /* Affichage de la valeur et de la partie variable */
        if (e == 0) {
            afficherDouble(v);
        } else {
            if (v != 1.0) { afficherDouble(v); printf("*"); }
            if (e == 1) printf("X");
            else        printf("X^%d", e);
        }
        premier = 0;
    }
    printf("\n");
}



/* Calcule la valeur numerique du polynome pour x donne
   en sommant chaque terme : coef * x^exp */
double eval(const Polynome *p, double x) {
    double res = 0.0;
    for (Monome *m = p->tete; m != NULL; m = m->suivant)
        res += m->coefficient * pow(x, (double)m->exposant);
    return res;
}



/* Retourne un nouveau polynome R = A + B */
Polynome *plus(const Polynome *a, const Polynome *b) {
    Polynome *r = creerPolynome();
    /* On insere tous les monomes de A puis de B, les doublons de degre
       sont fusionnes automatiquement par ajouterOrdreDecroissant */
    for (Monome *m = a->tete; m != NULL; m = m->suivant)
        ajouterOrdreDecroissant(r, m->coefficient, m->exposant);
    for (Monome *m = b->tete; m != NULL; m = m->suivant)
        ajouterOrdreDecroissant(r, m->coefficient, m->exposant);
    return r;
}

/* Retourne un nouveau polynome R = A - B */
Polynome *moins(const Polynome *a, const Polynome *b) {
    Polynome *r = creerPolynome();
    for (Monome *m = a->tete; m != NULL; m = m->suivant)
        ajouterOrdreDecroissant(r,  m->coefficient, m->exposant);
    /* On inverse le signe de chaque monome de B */
    for (Monome *m = b->tete; m != NULL; m = m->suivant)
        ajouterOrdreDecroissant(r, -m->coefficient, m->exposant);
    return r;
}

/* Retourne un nouveau polynome R = A * B
   Chaque monome de A est multiplie par chaque monome de B */
Polynome *fois(const Polynome *a, const Polynome *b) {
    Polynome *r = creerPolynome();
    for (Monome *ma = a->tete; ma != NULL; ma = ma->suivant)
        for (Monome *mb = b->tete; mb != NULL; mb = mb->suivant)
            /* Les exposants s'additionnent, les coefficients se multiplient */
            ajouterOrdreDecroissant(r,
                ma->coefficient * mb->coefficient,
                ma->exposant    + mb->exposant);
    return r;
}

/* Division euclidienne : A = B * Q + R
   Retourne le quotient Q et ecrit le reste dans *reste.
   Algorithme : on soustrait iterativement B * (terme dominant) au reste courant */
Polynome *quotient(const Polynome *a, const Polynome *b, Polynome **reste) {
    if (b->tete == NULL) {
        fprintf(stderr, "Erreur : division par le polynome nul\n");
        exit(EXIT_FAILURE);
    }

    Polynome *quot = creerPolynome();
    *reste         = creerPolynome();

    /* On part avec le reste egal a A */
    for (Monome *m = a->tete; m != NULL; m = m->suivant)
        ajouterOrdreDecroissant(*reste, m->coefficient, m->exposant);

    int    degB  = b->tete->exposant;
    double coefB = b->tete->coefficient;

    /* Tant que le degre du reste est >= degre de B, on peut encore diviser */
    while ((*reste)->tete != NULL && (*reste)->tete->exposant >= degB) {
        /* Terme dominant du quotient */
        double tc = (*reste)->tete->coefficient / coefB;
        int    te = (*reste)->tete->exposant    - degB;

        ajouterOrdreDecroissant(quot, tc, te);

        /* On soustrait tc*X^te * B au reste */
        for (Monome *mb = b->tete; mb != NULL; mb = mb->suivant)
            ajouterOrdreDecroissant(*reste,
                -(tc * mb->coefficient),
                te + mb->exposant);
    }
    return quot;
}



/* Fonction interne recursive : insere les monomes de ma et mb dans r
   en traitant a chaque appel le monome de plus haut degre restant */
static void plusRecHelper(Polynome *r, Monome *ma, Monome *mb) {
    if (!ma && !mb) return; /* les deux listes sont vides, on s'arrete */

    if (!ma) {
        /* Il ne reste que des monomes de B */
        ajouterOrdreDecroissant(r, mb->coefficient, mb->exposant);
        plusRecHelper(r, NULL, mb->suivant);
    } else if (!mb) {
        /* Il ne reste que des monomes de A */
        ajouterOrdreDecroissant(r, ma->coefficient, ma->exposant);
        plusRecHelper(r, ma->suivant, NULL);
    } else if (ma->exposant > mb->exposant) {
        /* Le monome de A a le degre le plus eleve */
        ajouterOrdreDecroissant(r, ma->coefficient, ma->exposant);
        plusRecHelper(r, ma->suivant, mb);
    } else if (ma->exposant < mb->exposant) {
        /* Le monome de B a le degre le plus eleve */
        ajouterOrdreDecroissant(r, mb->coefficient, mb->exposant);
        plusRecHelper(r, ma, mb->suivant);
    } else {
        /* Meme degre : on additionne les coefficients */
        ajouterOrdreDecroissant(r, ma->coefficient + mb->coefficient, ma->exposant);
        plusRecHelper(r, ma->suivant, mb->suivant);
    }
}

/* Meme logique que plusRecHelper mais on inverse le signe des monomes de B */
static void moinsRecHelper(Polynome *r, Monome *ma, Monome *mb) {
    if (!ma && !mb) return;

    if (!ma) {
        ajouterOrdreDecroissant(r, -mb->coefficient, mb->exposant);
        moinsRecHelper(r, NULL, mb->suivant);
    } else if (!mb) {
        ajouterOrdreDecroissant(r, ma->coefficient, ma->exposant);
        moinsRecHelper(r, ma->suivant, NULL);
    } else if (ma->exposant > mb->exposant) {
        ajouterOrdreDecroissant(r, ma->coefficient, ma->exposant);
        moinsRecHelper(r, ma->suivant, mb);
    } else if (ma->exposant < mb->exposant) {
        ajouterOrdreDecroissant(r, -mb->coefficient, mb->exposant);
        moinsRecHelper(r, ma, mb->suivant);
    } else {
        ajouterOrdreDecroissant(r, ma->coefficient - mb->coefficient, ma->exposant);
        moinsRecHelper(r, ma->suivant, mb->suivant);
    }
}

/* Retourne un nouveau polynome R = A + B (version recursive) */
Polynome *plusRec(const Polynome *a, const Polynome *b) {
    Polynome *r = creerPolynome();
    plusRecHelper(r, a->tete, b->tete);
    return r;
}

/* Retourne un nouveau polynome R = A - B (version recursive) */
Polynome *moinsRec(const Polynome *a, const Polynome *b) {
    Polynome *r = creerPolynome();
    moinsRecHelper(r, a->tete, b->tete);
    return r;
}



/* Enregistre un polynome comme etant encore utile (a appeler avant garbageCollect) */
void enregistrerPolynome(Polynome *p) {
    if (nbPolyUtile < MAX_POLY_UTILE)
        polyUtile[nbPolyUtile++] = p;
}

/* Libere les maillons inutilises en deux phases :
   1. Marquer tous les maillons encore references par un polynome utile
   2. Parcourir tousLesMaillons et liberer ceux non marques */
void garbageCollect(void) {
    /* Phase 1 : marquer les maillons encore en vie */
    for (int i = 0; i < nbPolyUtile; i++)
        for (Monome *m = polyUtile[i]->tete; m != NULL; m = m->suivant)
            m->utile = 1;

    /* Phase 2 : liberer les maillons non marques et effacer les marques */
    Monome **cur = &tousLesMaillons;
    while (*cur != NULL) {
        if ((*cur)->utile == 0) {
            /* Ce maillon n'est plus utilise : on le libere */
            Monome *sup = *cur;
            *cur = sup->general;
            free(sup);
        } else {
            /* Ce maillon est utile : on efface le marquage pour la prochaine fois */
            (*cur)->utile = 0;
            cur = &(*cur)->general;
        }
    }
}
