// Classe représentant une Scierie, sous-classe de Building
public class Scierie extends Building {
    // Constructeur de la Scierie
    public Scierie() {
        // Initialisation des attributs de la classe parent (Building)
        this.population = 0;
        this.populationLimit = 10;
        this.tConstruction = 5000;

        // Coûts de construction de la Scierie
        this.resourceCosts.put("Nourriture", 50);
        this.resourceCosts.put("Bois", 30);
        this.resourceCosts.put("Pierre", 20);
        this.resourceCosts.put("Fer", 30);

        // Consommation de ressources par la Scierie
        this.resourceConsumption.put("Nourriture", 5);
        this.resourceConsumption.put("Bois", 5);
        this.resourceConsumption.put("Pierre", 5);
        this.resourceConsumption.put("Fer", 5);

        // Production de ressources par la Scierie
        this.resourceProduction.put("Bois", 120);
    }

    // Méthode pour obtenir le type de la Scierie
    @Override
    public String getType() {
        return "Scierie";
    }
}
