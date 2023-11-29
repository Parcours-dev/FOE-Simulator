public class Forge extends Building {
    // Constructeur de la Forge
    public Forge() {
        // Initialisation des attributs spécifiques de la Forge
        this.population = 0;
        this.populationLimit = 10;
        this.tConstruction = 10000;

        // Coûts de construction de la Forge
        this.resourceCosts.put("Nourriture", 70);
        this.resourceCosts.put("Bois", 40);
        this.resourceCosts.put("Pierre", 30);
        this.resourceCosts.put("Fer", 50);

        // Consommation de ressources par la Forge
        this.resourceConsumption.put("Nourriture", 30);
        this.resourceConsumption.put("Bois", 30);
        this.resourceConsumption.put("Pierre", 30);
        this.resourceConsumption.put("Fer", 30);

        // Production de ressources par la Forge
        this.resourceProduction.put("Acier", 60);
        this.resourceProduction.put("Charbon", 40);
    }

    // Méthode pour obtenir le type de bâtiment
    @Override
    public String getType() {
        return "Forge";
    }
}
