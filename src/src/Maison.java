public class Maison extends Building {
    public Maison() {
            this.population = 0;
            this.resourceCosts.put("Nourriture", 20);
            this.resourceCosts.put("Bois", 100);
            this.resourceCosts.put("Pierre", 30);
            this.resourceCosts.put("Fer", 20);
        }

    @Override
    public void consumeResources() {

    }

    @Override
    public void produceResources() {

    }
}