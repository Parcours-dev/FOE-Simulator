// Classe représentant une mine
public class Mine extends Building {
    // Constructeur de la mine
    public Mine() {
        // Initialisation des attributs de la classe parent (Building)
        this.population = 0;
        this.populationLimit = 10;
        this.tConstruction = 5000;

        // Coûts de construction de la mine
        this.resourceCosts.put("Nourriture", 50);
        this.resourceCosts.put("Bois", 30);
        this.resourceCosts.put("Pierre", 20);
        this.resourceCosts.put("Fer", 30);

        // Consommation de ressources par la mine
        this.resourceConsumption.put("Nourriture", 5);
        this.resourceConsumption.put("Bois", 5);
        this.resourceConsumption.put("Pierre", 5);
        this.resourceConsumption.put("Fer", 5);

        // Production de ressources par la mine
        this.resourceProduction.put("Fer", 120);
    }

    // Méthode pour obtenir le type de la mine
    @Override
    public String getType() {
        return "Mine";
    }
}
