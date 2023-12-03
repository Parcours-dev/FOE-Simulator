public class Ferme extends Building {
    // Constructeur de la Ferme

    public Ferme() {
        // Initialisation des attributs spécifiques de la Ferme
        this.population = 0;
        this.populationLimit = 5;
        this.tConstruction = 2000;

        // Coûts de construction de la Forge
        this.resourceCosts.put("Bois", 60);
        this.resourceCosts.put("Pierre", 40);

        // Consommation de ressources par la Ferme
        this.resourceConsumption.put("Nourriture", 5);

        // Production de ressources par la Ferme
        this.resourceProduction.put("Nourriture", 30);

    }

    // Méthode pour obtenir le type de bâtiment
    @Override
    public String getType() {
        return "Ferme";
    }
}