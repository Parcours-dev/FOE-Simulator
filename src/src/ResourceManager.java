import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceManager {
    private static ResourceManager instance;
    private Map<String, Resource> resources;


    private ResourceManager() {
        resources = new HashMap<>();
        resources.put("Nourriture", new Resource("Nourriture", 400));
        resources.put("Bois", new Resource("Bois", 400));
        resources.put("Pierre", new Resource("Pierre", 400));
        resources.put("Charbon", new Resource("Charbon", 400));
        resources.put("Fer", new Resource("Fer", 200));
        resources.put("Acier", new Resource("Acier", 150));

        resources.put("Population", new Resource("Population", 20));


        // Ajouter d'autres ressources ici
    }


    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public Resource getResource(String name) {
        return resources.get(name);
    }

    public static void setInstance(ResourceManager instance) {
        ResourceManager.instance = instance;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }

    public void setResources(Map<String, Resource> resources) {
        this.resources = resources;
    }

    public void consumeResource(String name, int quantity) {
        Resource resource = resources.get(name);
        resource.setQuantity(resource.getQuantity() - quantity);

        if (resource.getQuantity() <= 0) {
            System.out.println("Tu n'as plus assez de : " + resource.getName() + "\nGame Over mon petit");
            System.exit(0);
        }

    }

    public void produceResource(String name, int quantity) {
        Resource resource = resources.get(name);
        resource.setQuantity(resource.getQuantity() + quantity);
    }

    public void showRessource() {
        for (Map.Entry<String, Resource> entry : resources.entrySet()) {
            String resourceName = entry.getKey();
            int amount = entry.getValue().getQuantity();
            System.out.println(resourceName + " : " + amount);
        }

    }



}
