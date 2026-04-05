public class Main {
    public static void main(String[] args) {

        Polynome p = new Polynome();
        p.ajouterMaillon(3.0, 2);
        p.ajouterMaillon(2.0, 1);
        p.ajouterMaillon(-5.0, 0);

        System.out.print("Mon polynôme : ");
        p.afficher();
    }
}