public class Caserne extends Building {
    // Constructeur de la Caserne
    public Caserne() {
        // Initialisation de la population à 0
        this.population = 0;

        // Limite de population pour la Caserne
        this.populationLimit = 10;

        // Temps de construction en millisecondes
        this.tConstruction = 5000;

        // Coûts en ressources pour construire une Caserne
        this.resourceCosts.put("Nourriture", 40);
        this.resourceCosts.put("Bois", 20);
        this.resourceCosts.put("Pierre", 10);
        this.resourceCosts.put("Fer", 15);

        // Consommation de ressources par la Caserne
        this.resourceConsumption.put("Acier", 8);
        this.resourceConsumption.put("Charbon", 10);

        // Pas de production de ressources par la Caserne

    }

    // Redéfinition de la méthode getType pour renvoyer le type spécifique de la Caserne
    @Override
    public String getType() {
        return "Caserne";
    }
}
