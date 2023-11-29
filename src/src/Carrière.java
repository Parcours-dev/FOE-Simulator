public class Carrière extends Building {
    // Constructeur de la Carrière
    public Carrière() {
        // Initialisation de la population à 0
        this.population = 0;

        // Limite de population pour la Carrière
        this.populationLimit = 10;

        // Temps de construction en millisecondes
        this.tConstruction = 5000;

        // Coûts en ressources pour construire une Carrière
        this.resourceCosts.put("Nourriture", 50);
        this.resourceCosts.put("Bois", 30);
        this.resourceCosts.put("Pierre", 20);
        this.resourceCosts.put("Fer", 30);

        // Consommation de ressources par la Carrière
        this.resourceConsumption.put("Nourriture", 5);
        this.resourceConsumption.put("Bois", 5);
        this.resourceConsumption.put("Pierre", 5);
        this.resourceConsumption.put("Fer", 5);

        // Production de ressources par la Carrière
        this.resourceProduction.put("Pierre", 120);
    }

    // Redéfinition de la méthode getType pour renvoyer le type spécifique de la Carrière
    @Override
    public String getType() {
        return "Carrière";
    }
}
