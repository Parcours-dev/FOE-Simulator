import java.util.HashMap;
import java.util.Map;

// Classe ResourceManager
public class ResourceManager {
    private static ResourceManager instance;
    private Map<String, Resource> resources;

    ResourceManager() {
        resources = new HashMap<>();
        resources.put("Nourriture", new Resource("Nourriture", 400));
        resources.put("Bois", new Resource("Bois", 400));
        resources.put("Pierre", new Resource("Pierre", 400));
        resources.put("Charbon", new Resource("Charbon", 400));
        resources.put("Fer", new Resource("Fer", 400));
        resources.put("Acier", new Resource("Acier", 400));

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

    public void consumeResource(String name, int quantity) {
        Resource resource = resources.get(name);
        resource.setQuantity(resource.getQuantity() - quantity);
        if (resource.getQuantity() <= 0) {
            System.out.println("Tu n'as plus assez de : " + resource.getName());
        }
        System.out.println(resource.getName() + " " + resource.getQuantity());
    }

    public void produceResource(String name, int quantity) {
        Resource resource = resources.get(name);
        resource.setQuantity(resource.getQuantity() + quantity);
        System.out.println(resource.getName() + " " + resource.getQuantity());
    }

    @Override
    public String toString() {
        return "ResourceManager{" +
                "resources=" + resources +
                '}';
    }
}