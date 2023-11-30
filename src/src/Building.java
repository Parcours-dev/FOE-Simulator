import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Building implements Observable, Observer {
    private static final int CONSO_DAILY = 0;

    // Propriétés de base du bâtiment
    protected int population;
    protected int populationLimit;
    protected int tConstruction;
    protected boolean isBuilt = false;

    // Coûts, consommations et productions de ressources
    protected Map<String, Integer> resourceCosts = new HashMap<>();
    protected Map<String, Integer> resourceConsumption = new HashMap<>();
    protected Map<String, Integer> resourceProduction = new HashMap<>();

    // ExecutorService pour gérer les threads
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Liste des observateurs
    private List<Observer> observers = new ArrayList<>();

    // Constructeur
    public Building() {
        // L'initialisation a été déplacée à la déclaration des champs
    }

    // Méthode pour construire le bâtiment de manière asynchrone
    public void build() {
        executorService.submit(() -> {
            ResourceManager resourceManager = ResourceManager.getInstance();
            handleResourceCosts(resourceManager);
            sleep(tConstruction);
            isBuilt = true;
            notifyObservers();
        });
    }

    // Méthode privée pour gérer les coûts de ressources lors de la construction
    private void handleResourceCosts(ResourceManager resourceManager) {
        resourceCosts.forEach((resourceName, amount) -> {
            Resource resource = resourceManager.getResource(resourceName);
            if (resource != null) {
                resourceManager.consumeResource(resourceName, amount);
            } else {
                System.out.println("La ressource " + resourceName + " n'existe pas.");
            }
        });
    }

    // Méthode privée pour mettre en pause le thread pendant une durée spécifiée
    private void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour consommer les ressources en fonction de la population
    public void consumeResources() {
        if (isBuilt) {
            ResourceManager resourceManager = ResourceManager.getInstance();
            resourceConsumption.forEach((resourceName, consumptionAmount) -> {
                int proportionalConsumption = (int) ((double) population / populationLimit * consumptionAmount);
                resourceManager.consumeResource(resourceName, proportionalConsumption);
            });
            notifyObservers();
        }
    }

    // Dans la classe Building
    public void populationConsumption(List<Building> buildingList) {
        ResourceManager resourceManager = ResourceManager.getInstance();
        int totalFoodConsumption = resourceManager.getTotalPopulation(buildingList) * CONSO_DAILY;
        resourceManager.consumeResource("Nourriture", totalFoodConsumption);
    }



    // Méthode pour produire des ressources en fonction de la population
    public void produceResources() {
        if (isBuilt) {
            ResourceManager resourceManager = ResourceManager.getInstance();
            resourceProduction.forEach((resourceName, productionAmount) -> {
                int proportionalProduction = (int) ((double) population / populationLimit * productionAmount);
                resourceManager.produceResource(resourceName, proportionalProduction);
            });
        }
    }

    // Méthode pour afficher les ressources
    public void showResources() {
        ResourceManager resourceManager = ResourceManager.getInstance();
        resourceManager.showResource();
    }

    // Getter et Setter pour la population
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    // Getter et Setter pour les coûts de ressources
    public Map<String, Integer> getResourceCosts() {
        return resourceCosts;
    }

    public void setResourceCosts(Map<String, Integer> resourceCosts) {
        this.resourceCosts = resourceCosts;
    }

    // Getter et Setter pour la consommation de ressources
    public Map<String, Integer> getResourceConsumption() {
        Map<String, Integer> proportionalConsumption = new HashMap<>();
        if (isBuilt) {
            for (Map.Entry<String, Integer> entry : resourceConsumption.entrySet()) {
                String resourceName = entry.getKey();
                int consumptionAmount = entry.getValue();

                // Calculer la consommation proportionnelle à la population
                int proportionalAmount = (int) ((double) population / populationLimit * consumptionAmount);
                proportionalConsumption.put(resourceName, proportionalAmount);
            }
        }
        return proportionalConsumption;
    }

    public void setResourceConsumption(Map<String, Integer> resourceConsumption) {
        this.resourceConsumption = resourceConsumption;
    }

    // Getter et Setter pour la production de ressources
    public Map<String, Integer> getResourceProduction() {
        Map<String, Integer> proportionalProduction = new HashMap<>();
        if (isBuilt) {
            for (Map.Entry<String, Integer> entry : resourceProduction.entrySet()) {
                String resourceName = entry.getKey();
                int productionAmount = entry.getValue();

                // Calculer la production proportionnelle à la population
                int proportionalAmount = (int) ((double) population / populationLimit * productionAmount);
                proportionalProduction.put(resourceName, proportionalAmount);
            }
        }
        return proportionalProduction;
    }

    public void setResourceProduction(Map<String, Integer> resourceProduction) {
        this.resourceProduction = resourceProduction;
    }

    // Méthode pour ajouter un habitant
    public void addInhabitant(int habitantNumber) {
        int availablePopulation = ResourceManager.getInstance().getAvailablePopulation();

        if (habitantNumber > 0) {
            if (habitantNumber <= availablePopulation) {
                this.population += habitantNumber;
                ResourceManager.getInstance().consumeResource("Population", habitantNumber);
                ResourceManager.getInstance().setAvailablePopulation(availablePopulation - habitantNumber);
            } else {
                System.out.println("Il n'y a pas assez d'habitants disponibles dans la ville.");
            }
        } else {
            System.out.println("Veuillez entrer un nombre d'habitants positif.");
        }
    }


    // Méthode pour supprimer un habitant
    public void removeInhabitant(int habitantNumber) {
        if (habitantNumber > 0) {
            int remainingPopulation = this.population - habitantNumber;
            if (remainingPopulation >= 0) {
                this.population = remainingPopulation;

                // Restituer la population en tant que ressource "Population"
                ResourceManager.getInstance().produceResource("Population", habitantNumber);
            } else {
                System.out.println("Il n'y a pas suffisamment d'habitants à supprimer.");
            }
        } else {
            System.out.println("Veuillez entrer un nombre d'habitants positif.");
        }
    }


    // Getter et Setter pour la limite de population
    public int getPopulationLimit() {
        return populationLimit;
    }

    public void setPopulationLimit(int populationLimit) {
        this.populationLimit = populationLimit;
    }

    // Méthode abstraite pour obtenir le type de bâtiment
    public abstract String getType();

    // Implémentation des méthodes de l'interface Observable
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    // Implémentation de la méthode de l'interface Observer
    @Override
    public void update() {
        // Logique à exécuter en réponse à une mise à jour du manager
    }

}
