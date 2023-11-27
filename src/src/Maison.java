public class Maison extends Building {
    public Maison() {
        this.population = 0;
        this.tConstruction = 3000;
        this.resourceCosts.put("Nourriture", 40);
        this.resourceCosts.put("Bois", 20);
        this.resourceCosts.put("Pierre", 10);
        this.resourceCosts.put("Fer", 20);


        this.resourceConsumption.put("Bois", 5);
        this.resourceConsumption.put("Nourriture", 15);


        // Pas de production

    }
}
