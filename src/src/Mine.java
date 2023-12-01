// Classe représentant une mine
public class Mine extends Building {
    // Constructeur de la mine
    public Mine() {
        // Initialisation des attributs de la classe parent (Building)
        this.population = 0;
        this.populationLimit = 20;
        this.tConstruction = 3000;

        // Coûts de construction de la mine
        this.resourceCosts.put("Bois", 20);
        this.resourceCosts.put("Pierre", 80);

        // Consommation de ressources par la mine
        this.resourceConsumption.put("Bois", 5);

        // Production de ressources par la mine
        this.resourceProduction.put("Pierre", 5);
        this.resourceProduction.put("Charbon", 4);
        this.resourceProduction.put("Fer", 3);
    }

    // Méthode pour obtenir le type de la mine
    @Override
    public String getType() {
        return "Mine";
    }
}
