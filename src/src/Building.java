import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Building {
    protected int population;
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
                resourceManager.consumeResource(entry.getKey(), entry.getValue());
            }
            try {
                // Simuler le temps de construction en mettant le thread en sommeil
                Thread.sleep(tConstruction); // Pause pour 5 secondes
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

                resourceManager.consumeResource(resourceName, consumptionAmount);
            }
        }
    }


    public void produceResources() {
        if(isBuilt) {
            ResourceManager resourceManager = ResourceManager.getInstance();
            for (Map.Entry<String, Integer> entry : resourceProduction.entrySet()) {
                String resourceName = entry.getKey();
                int productionAmount = entry.getValue();

                resourceManager.produceResource(resourceName, productionAmount);
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
        return resourceConsumption;
    }

    public void setResourceConsumption(Map<String, Integer> resourceConsumption) {
        this.resourceConsumption = resourceConsumption;
    }

    public Map<String, Integer> getResourceProduction() {
        return resourceProduction;
    }

    public void setResourceProduction(Map<String, Integer> resourceProduction) {
        this.resourceProduction = resourceProduction;
    }
}