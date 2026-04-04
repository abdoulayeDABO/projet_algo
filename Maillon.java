public class Maillon {
    double coefficient;
    int    exposant;
    Maillon suivant;

    // Constructeur
    Maillon(double coefficient, int exposant) {
        this.coefficient = coefficient;
        this.exposant    = exposant;
        this.suivant     = null;
    }
}