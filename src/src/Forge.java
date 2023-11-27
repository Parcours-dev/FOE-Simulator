public class Forge extends Building {
    public Forge() {
        this.population = 0;
        this.populationLimit = 10;
        this.tConstruction = 10000;

        this.resourceCosts.put("Nourriture", 70);
        this.resourceCosts.put("Bois", 40);
        this.resourceCosts.put("Pierre", 30);
        this.resourceCosts.put("Fer", 50);

        this.resourceConsumption.put("Nourriture", 30);
        this.resourceConsumption.put("Bois", 30);
        this.resourceConsumption.put("Pierre", 30);
        this.resourceConsumption.put("Fer", 30);

        this.resourceProduction.put("Acier", 60);
        this.resourceProduction.put("Charbon", 40);
    }
}