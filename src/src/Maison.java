// Classe représentant le bâtiment "Maison"
public class Maison extends Building {
    // Constructeur de la Maison
    public Maison() {
        // Initialisation des attributs spécifiques de la Maison
        this.population = 0;
        this.populationLimit = 10;
        this.tConstruction = 3000;

        // Coûts de construction de la Maison
        this.resourceCosts.put("Nourriture", 40);
        this.resourceCosts.put("Bois", 20);
        this.resourceCosts.put("Pierre", 10);
        this.resourceCosts.put("Fer", 20);

        // Consommation de ressources par la Maison
        this.resourceConsumption.put("Bois", 5);
        this.resourceConsumption.put("Nourriture", 15);

        // Production de ressources par la Maison
        this.resourceProduction.put("Population", 1);
    }

    // Méthode pour obtenir le type de bâtiment
    @Override
    public String getType() {
        return "Maison";
    }
}
