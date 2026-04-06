#ifndef POLYNOME_H
#define POLYNOME_H

/* Un monome represente un terme : coefficient * X^exposant */
typedef struct Monome {
    double        coefficient;
    int           exposant;
    struct Monome *suivant; /* pointeur vers le monome de degre inferieur */
    struct Monome *general; /* chainage global de tous les maillons (GC)  */
    int            utile;   /* marquage garbage collector : 1 = utile     */
} Monome;

/* Un polynome est une liste chainee de monomes tries par degre decroissant.
   tete == NULL signifie le polynome nul. */
typedef struct {
    Monome *tete;
} Polynome;

/* Variables globales pour le garbage collector (Q7) */
#define MAX_POLY_UTILE 256
extern Monome   *tousLesMaillons;
extern Polynome *polyUtile[MAX_POLY_UTILE];
extern int       nbPolyUtile;

Monome   *nouveauMonome(double coef, int exp);
Polynome *creerPolynome(void);
void      ajouterOrdreDecroissant(Polynome *p, double coef, int exp);

void   afficher(const Polynome *p);
double eval(const Polynome *p, double x);

Polynome *plus    (const Polynome *a, const Polynome *b);
Polynome *moins   (const Polynome *a, const Polynome *b);
Polynome *fois    (const Polynome *a, const Polynome *b);
Polynome *quotient(const Polynome *a, const Polynome *b, Polynome **reste);
Polynome *plusRec (const Polynome *a, const Polynome *b);
Polynome *moinsRec(const Polynome *a, const Polynome *b);

void enregistrerPolynome(Polynome *p);
void garbageCollect(void);

#endif
