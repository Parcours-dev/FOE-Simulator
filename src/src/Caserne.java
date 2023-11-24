public class Caserne extends Building {
    public Caserne() {
        this.population = 0;
        this.resourceCosts.put("Nourriture", 40);
        this.resourceCosts.put("Bois", 20);
        this.resourceCosts.put("Pierre", 10);
        this.resourceCosts.put("Fer", 15);

        this.resourceConsumption.put("Acier", 8);
        this.resourceConsumption.put("Charbon", 10);

        // Pas de production

    }
}