import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Building {
    protected int population;
    protected int populationLimit;
    protected int tConstruction;
    protected boolean isBuilt = false;
    protected Map<String, Integer> resourceCosts;
    protected Map<String, Integer> resourceConsumption;
    protected Map<String, Integer> resourceProduction;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    public Building() {
        resourceCosts = new HashMap<>();
        resourceConsumption = new HashMap<>();
        resourceProduction = new HashMap<>();
    }

    public void build() {
        executorService.submit(() -> {
            ResourceManager resourceManager = ResourceManager.getInstance();
            for (Map.Entry<String, Integer> entry : resourceCosts.entrySet()) {
                Resource resource = resourceManager.getResource(entry.getKey());
                if (resource != null) {
                    resourceManager.consumeResource(entry.getKey(), entry.getValue());
                } else {
                    System.out.println("La ressource " + entry.getKey() + " n'existe pas.");
                }
            }
            try {
                Thread.sleep(tConstruction);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isBuilt = true;

            });
        }




    public void consumeResources() {
        if(isBuilt) {
            ResourceManager resourceManager = ResourceManager.getInstance();
            for (Map.Entry<String, Integer> entry : resourceConsumption.entrySet()) {
                String resourceName = entry.getKey();
                int consumptionAmount = entry.getValue();

                // Calculer la consommation proportionnelle à la population
                int proportionalConsumption = (int) ((double) population / populationLimit * consumptionAmount);

                resourceManager.consumeResource(resourceName, proportionalConsumption);
            }
        }
    }

    public void populationConsumption(){
        final int consoDaily = 1;
        ResourceManager resourceManager = ResourceManager.getInstance();
        Resource populationResource = resourceManager.getResource("Population");
        int population = populationResource.getQuantity();

        // Calculer la consommation totale de nourriture
        int totalFoodConsumption = population * consoDaily;

        // Consommer la nourriture
        resourceManager.consumeResource("Nourriture", totalFoodConsumption);
    }



    public void produceResources() {
        if(isBuilt) {
            ResourceManager resourceManager = ResourceManager.getInstance();
            for (Map.Entry<String, Integer> entry : resourceProduction.entrySet()) {
                String resourceName = entry.getKey();
                int productionAmount = entry.getValue();

                // Calculer la consommation proportionnelle à la population
                int proportionalProduction = (int) ((double) population / populationLimit * productionAmount);


                resourceManager.produceResource(resourceName, proportionalProduction);
            }
        }
    }



    public void showRessources(){
        ResourceManager resourceManager = ResourceManager.getInstance();
        resourceManager.showRessource();
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Map<String, Integer> getResourceCosts() {
        return resourceCosts;
    }

    public void setResourceCosts(Map<String, Integer> resourceCosts) {
        this.resourceCosts = resourceCosts;
    }

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

    public void addInhabitant(int habitantNumber) {
        if (this.population < this.populationLimit) {
            this.population = this.population+ habitantNumber;
        } else {
            System.out.println("La limite de population a été atteinte. Impossible d'ajouter un habitant.");
        }
    }

    public void removeInhabitant(int habitantNumber) {
        if (this.population > 0) {
            this.population = this.population - habitantNumber;
        } else {
            System.out.println("Il n'y a pas d'habitants à supprimer.");
        }
    }

    public int getPopulationLimit() {
        return populationLimit;
    }

    public void setPopulationLimit(int populationLimit) {
        this.populationLimit = populationLimit;
    }

}