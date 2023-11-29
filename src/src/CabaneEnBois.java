public class CabaneEnBois extends Building {
    // Constructeur de la CabaneEnBois
    public CabaneEnBois() {
        // Initialisation de la population à 0
        this.population = 0;

        // Limite de population pour la CabaneEnBois
        this.populationLimit = 10;

        // Temps de construction en millisecondes
        this.tConstruction = 5000;

        // Coûts en ressources pour construire une CabaneEnBois
        this.resourceCosts.put("Bois", 1);

        // Pas de consommation de ressources

        // Production de ressources par la CabaneEnBois
        this.resourceProduction.put("Bois", 2);
        this.resourceProduction.put("Nourriture", 2);
    }

    // Redéfinition de la méthode getType pour renvoyer le type spécifique de la CabaneEnBois
    @Override
    public String getType() {
        return "CabaneEnBois";
    }
}
