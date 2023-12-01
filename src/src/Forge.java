public class Forge extends Building {
    // Constructeur de la Forge
    public Forge() {
        // Initialisation des attributs spécifiques de la Forge
        this.population = 0;
        this.populationLimit = 5;
        this.tConstruction = 4000;

        // Coûts de construction de la Forge
        this.resourceCosts.put("Bois", 20);
        this.resourceCosts.put("Pierre", 70);
        this.resourceCosts.put("Fer", 10);

        // Consommation de ressources par la Forge
        this.resourceConsumption.put("Charbon", 4);
        this.resourceConsumption.put("Fer", 2);

        // Production de ressources par la Forge
        this.resourceProduction.put("Acier", 2);
    }

    // Méthode pour obtenir le type de bâtiment
    @Override
    public String getType() {
        return "Forge";
    }
}
