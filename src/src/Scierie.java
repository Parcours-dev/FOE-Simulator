// Classe représentant une Scierie, sous-classe de Building
public class Scierie extends Building {
    // Constructeur de la Scierie
    public Scierie() {
        // Initialisation des attributs de la classe parent (Building)
        this.population = 0;
        this.populationLimit = 10;
        this.tConstruction = 4000;

        // Coûts de construction de la Scierie
        this.resourceCosts.put("Bois", 40);
        this.resourceCosts.put("Pierre", 60);

        // Consommation de ressources par la Scierie
        this.resourceConsumption.put("Bois", 5);

        // Production de ressources par la Scierie
        this.resourceProduction.put("Bois", 20);
    }

    // Méthode pour obtenir le type de la Scierie
    @Override
    public String getType() {
        return "Scierie";
    }
}
