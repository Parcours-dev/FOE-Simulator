public class Ferme extends Building {
    public Ferme() {
        this.population = 0;
        this.populationLimit = 5;
        this.tConstruction = 2000;

        this.resourceCosts.put("Bois", 60);
        this.resourceCosts.put("Pierre", 40);

        this.resourceConsumption.put("Nourriture", 5);

        this.resourceProduction.put("Nourriture", 30);

    }

    @Override
    public String getType() {
        return "Ferme";
    }
}