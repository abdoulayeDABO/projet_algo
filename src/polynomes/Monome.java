package polynomes;

public class Monome {
    double coefficient;
    int    exposant;
    Monome suivant;

    public Monome(double coefficient, int exposant) {
        this.coefficient = coefficient;
        this.exposant    = exposant;
        this.suivant     = null;
    }
}