public class CabaneEnBois extends Building{
    public CabaneEnBois() {
        this.population = 0;

        this.resourceCosts.put("Bois", 1);

        //pas de consommation

        this.resourceProduction.put("Bois", 2);
        this.resourceProduction.put("Nourriture", 2);

    }
}
