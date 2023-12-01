public class Carrière extends Building {
    // Constructeur de la Carrière
    public Carrière() {
        // Initialisation de la population à 0
        this.population = 0;

        // Limite de population pour la Carrière
        this.populationLimit = 20;

        // Temps de construction en millisecondes
        this.tConstruction = 2000;

        // Coûts en ressources pour construire une Carrière
        this.resourceCosts.put("Bois", 50);

        // Consommation de ressources par la Carrière


        // Production de ressources par la Carrière
        this.resourceProduction.put("Pierre", 10);
    }

    // Redéfinition de la méthode getType pour renvoyer le type spécifique de la Carrière
    @Override
    public String getType() {
        return "Carrière";
    }
}
