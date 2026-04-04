public class Main {
    public static void main(String[] args) {

        // Créer un polynôme
        Polynome p = new Polynome();

        // Ajouter des monômes manuellement (pour simuler 3X² + 2X - 5)
        p.ajouterMaillon(3.0, 2);   // 3X²
        p.ajouterMaillon(2.0, 1);   // 2X
        p.ajouterMaillon(-5.0, 0);  // -5

        // Parcourir la liste et afficher chaque maillon
        Maillon m = p.tete;
        while (m != null) {
            System.out.println("coefficient=" + m.coefficient + "  exposant=" + m.exposant);
            m = m.suivant;
        }
    }
}