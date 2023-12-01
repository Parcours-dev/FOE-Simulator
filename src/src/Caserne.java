public class Caserne extends Building {
    // Constructeur de la Caserne
    public Caserne() {
        // Initialisation de la population à 0
        this.population = 0;

        // Limite de population pour la Caserne
        this.populationLimit = 3;

        // Temps de construction en millisecondes
        this.tConstruction = 5000;

        // Coûts en ressources pour construire une Caserne
        this.resourceCosts.put("Bois", 30);
        this.resourceCosts.put("Pierre", 50);
        this.resourceCosts.put("Fer", 15);
        this.resourceCosts.put("Acier", 15);
        //this.resourceProduction.put("Population", 10);

        // Consommation de ressources par la Caserne
        this.resourceConsumption.put("Acier", 8);
        //this.resourceConsumption.put("Population", 10);

        // Pas de production de ressources par la Caserne
        //this.resourceProduction.put("Guerrier", 3);
    }

    // Redéfinition de la méthode getType pour renvoyer le type spécifique de la Caserne
    @Override
    public String getType() {
        return "Caserne";
    }
}
