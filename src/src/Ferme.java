public class Ferme extends Building {
    public Ferme() {
        this.population = 0;
        this.resourceCosts.put("Nourriture", 50);
        this.resourceCosts.put("Bois", 30);
        this.resourceCosts.put("Pierre", 20);
        this.resourceCosts.put("Fer", 30);

        this.resourceConsumption.put("Nourriture", 5);
        this.resourceConsumption.put("Bois", 5);
        this.resourceConsumption.put("Pierre", 5);
        this.resourceConsumption.put("Fer", 5);

        this.resourceProduction.put("Nourriture", 10);

    }
}