package polynomes;

public class Operations {

    // ── Utilitaire : copie profonde d'un polynôme ──────────────────────────────
    private static Polynome copier(Polynome p) {
        Polynome copie = new Polynome(null);
        Monome m = p.tete;
        // on parcourt de la tête → on insère en queue pour garder l'ordre
        Monome queue = null;
        while (m != null) {
            Monome nouveau = new Monome(m.coefficient, m.exposant);
            if (copie.tete == null)
                copie.tete = nouveau;
            else
                queue.suivant = nouveau;
            queue = nouveau;
            m = m.suivant;
        }
        return copie;
    }

    // ── Q6 : Addition ──────────────────────────────────────────────────────────
    public static Polynome additionner(Polynome p1, Polynome p2) {
        Monome tete = null, queue = null;
        Monome m1 = p1.tete, m2 = p2.tete;
        while (m1 != null || m2 != null) {
            double coef;
            int exp;
            if (m1 == null) {
                coef = m2.coefficient;
                exp = m2.exposant;
                m2 = m2.suivant;
            } else if (m2 == null) {
                coef = m1.coefficient;
                exp = m1.exposant;
                m1 = m1.suivant;
            } else if (m1.exposant == m2.exposant) {
                coef = m1.coefficient + m2.coefficient;
                exp = m1.exposant;
                m1 = m1.suivant;
                m2 = m2.suivant;
            } else if (m1.exposant > m2.exposant) {
                coef = m1.coefficient;
                exp = m1.exposant;
                m1 = m1.suivant;
            } else {
                coef = m2.coefficient;
                exp = m2.exposant;
                m2 = m2.suivant;
            }
            if (coef != 0) {
                Monome nouveau = new Monome(coef, exp);
                if (tete == null)
                    tete = nouveau;
                else
                    queue.suivant = nouveau;
                queue = nouveau;
            }
        }
        return new Polynome(tete);
    }

    // ── Q6 : Soustraction ─────────────────────────────────────────────────────
    public static Polynome soustraire(Polynome p1, Polynome p2) {
        Monome tete = null, queue = null;
        Monome m1 = p1.tete, m2 = p2.tete;
        while (m1 != null || m2 != null) {
            double coef;
            int exp;
            if (m1 == null) {
                coef = -m2.coefficient;
                exp = m2.exposant;
                m2 = m2.suivant;
            } else if (m2 == null) {
                coef = m1.coefficient;
                exp = m1.exposant;
                m1 = m1.suivant;
            } else if (m1.exposant == m2.exposant) {
                coef = m1.coefficient - m2.coefficient;
                exp = m1.exposant;
                m1 = m1.suivant;
                m2 = m2.suivant;
            } else if (m1.exposant > m2.exposant) {
                coef = m1.coefficient;
                exp = m1.exposant;
                m1 = m1.suivant;
            } else {
                coef = -m2.coefficient;
                exp = m2.exposant;
                m2 = m2.suivant;
            }
            if (coef != 0) {
                Monome nouveau = new Monome(coef, exp);
                if (tete == null)
                    tete = nouveau;
                else
                    queue.suivant = nouveau;
                queue = nouveau;
            }
        }
        return new Polynome(tete);
    }

    // ── Q6 : Multiplication ───────────────────────────────────────────────────
    // Formule du sujet : P×Q = (a*X^n + P') × (b*X^m + Q')
    // = a*b*X^(n+m) + a*X^n×Q' + P'×Q
    public static Polynome multiplier(Polynome p1, Polynome p2) {
        Polynome resultat = new Polynome(null);
        Monome m1 = p1.tete;
        while (m1 != null) {
            Monome m2 = p2.tete;
            while (m2 != null) {
                // On alloue un nouveau maillon pour chaque produit partiel
                resultat.ajouterMonome(
                        m1.coefficient * m2.coefficient,
                        m1.exposant + m2.exposant);
                m2 = m2.suivant;
            }
            m1 = m1.suivant;
        }
        return resultat;
    }

    // ── Q6 : Division euclidienne ─────────────────────────────────────────────
    // Renvoie le quotient ; le reste est mis dans reste[0]
    public static Polynome quotient(Polynome dividende, Polynome diviseur, Polynome[] reste) {
        if (diviseur.tete == null) {
            System.out.println("Erreur : division par le polynome nul");
            System.exit(1);
        }
        Polynome quotient = new Polynome(null);
        Polynome r = copier(dividende); // on travaille sur une copie

        // Tant que deg(reste) >= deg(diviseur)
        while (r.tete != null && r.tete.exposant >= diviseur.tete.exposant) {
            // Terme dominant du quotient
            double coefQ = r.tete.coefficient / diviseur.tete.coefficient;
            int expQ = r.tete.exposant - diviseur.tete.exposant;

            quotient.ajouterMonome(coefQ, expQ); // nouveau maillon alloué

            // Soustraction : reste ← reste - coefQ*X^expQ * diviseur
            Polynome terme = new Polynome(null);
            terme.ajouterMonome(coefQ, expQ);
            Polynome produit = multiplier(terme, diviseur);
            r = soustraire(r, produit);
        }

        reste[0] = r;
        return quotient;
    }

    // À ajouter dans Operations.java

    // ── Q8 : Addition récursive
    // ───────────────────────────────────────────────────
    public static Polynome additionnerRecursif(Polynome p1, Polynome p2) {
        return new Polynome(addRec(p1.tete, p2.tete));
    }

    private static Monome addRec(Monome m1, Monome m2) {
        // Cas de base : les deux listes sont épuisées
        if (m1 == null && m2 == null)
            return null;

        double coef;
        int exp;
        Monome suite1, suite2;

        if (m1 == null) {
            coef = m2.coefficient;
            exp = m2.exposant;
            suite1 = null;
            suite2 = m2.suivant;
        } else if (m2 == null) {
            coef = m1.coefficient;
            exp = m1.exposant;
            suite1 = m1.suivant;
            suite2 = null;
        } else if (m1.exposant > m2.exposant) {
            coef = m1.coefficient;
            exp = m1.exposant;
            suite1 = m1.suivant;
            suite2 = m2;
        } else if (m2.exposant > m1.exposant) {
            coef = m2.coefficient;
            exp = m2.exposant;
            suite1 = m1;
            suite2 = m2.suivant;
        } else {
            // même exposant → addition des coefficients
            coef = m1.coefficient + m2.coefficient;
            exp = m1.exposant;
            suite1 = m1.suivant;
            suite2 = m2.suivant;
        }

        // Appel récursif sur le reste
        Monome reste = addRec(suite1, suite2);

        if (coef == 0)
            return reste; // on ne crée pas de maillon nul

        // Nouveau maillon en tête du résultat
        Monome nouveau = new Monome(coef, exp);
        nouveau.suivant = reste;
        return nouveau;
    }

    // ── Q8 : Soustraction récursive
    // ───────────────────────────────────────────────
    public static Polynome soustraireRecursif(Polynome p1, Polynome p2) {
        return new Polynome(sousRec(p1.tete, p2.tete));
    }

    private static Monome sousRec(Monome m1, Monome m2) {
        if (m1 == null && m2 == null)
            return null;

        double coef;
        int exp;
        Monome suite1, suite2;

        if (m1 == null) {
            coef = -m2.coefficient;
            exp = m2.exposant;
            suite1 = null;
            suite2 = m2.suivant;
        } else if (m2 == null) {
            coef = m1.coefficient;
            exp = m1.exposant;
            suite1 = m1.suivant;
            suite2 = null;
        } else if (m1.exposant > m2.exposant) {
            coef = m1.coefficient;
            exp = m1.exposant;
            suite1 = m1.suivant;
            suite2 = m2;
        } else if (m2.exposant > m1.exposant) {
            coef = -m2.coefficient;
            exp = m2.exposant;
            suite1 = m1;
            suite2 = m2.suivant;
        } else {
            coef = m1.coefficient - m2.coefficient;
            exp = m1.exposant;
            suite1 = m1.suivant;
            suite2 = m2.suivant;
        }

        Monome reste = sousRec(suite1, suite2);
        if (coef == 0)
            return reste;
        Monome nouveau = new Monome(coef, exp);
        nouveau.suivant = reste;
        return nouveau;
    }
}