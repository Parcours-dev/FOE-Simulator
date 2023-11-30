import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Classe gérant les ressources du jeu
public class ResourceManager {
    // Instance unique de la classe (singleton)
    private static ResourceManager instance;

    // Map des ressources, associant le nom de la ressource à son instance
    private Map<String, Resource> resources;

    // Population disponible dans la ville
    private int availablePopulation;

    // Constructeur privé initialisant les ressources et la population disponible
    private ResourceManager() {
        resources = new HashMap<>();
        resources.put("Nourriture", new Resource("Nourriture", 400));
        resources.put("Bois", new Resource("Bois", 400));
        resources.put("Pierre", new Resource("Pierre", 400));
        resources.put("Charbon", new Resource("Charbon", 400));
        resources.put("Fer", new Resource("Fer", 200));
        resources.put("Acier", new Resource("Acier", 150));
        resources.put("Population", new Resource("Population", 20));

        this.availablePopulation = resources.get("Population").getQuantity();
        // Ajouter d'autres ressources ici
    }

    // Méthode statique pour obtenir l'instance unique de la classe (singleton)
    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    // Méthode pour obtenir une ressource spécifique
    public Resource getResource(String name) {
        return resources.get(name);
    }

    // Méthode pour définir l'instance unique de la classe (singleton)
    public static void setInstance(ResourceManager instance) {
        ResourceManager.instance = instance;
    }

    // Méthode pour obtenir la map des ressources
    public Map<String, Resource> getResources() {
        return resources;
    }

    // Méthode pour définir la map des ressources
    public void setResources(Map<String, Resource> resources) {
        this.resources = resources;
    }

    // Méthode pour consommer une quantité donnée d'une ressource
    public void consumeResource(String resourceName, int amount) {
        Resource resource = resources.get(resourceName);
        if (resource != null) {
            resource.setQuantity(resource.getQuantity() - amount);
            if (resource.getQuantity() < 0) {
                System.out.println("La quantité de " + resourceName + " est devenue négative.");
                Main.gameOver();
            }
        } else {
            System.out.println("La ressource " + resourceName + " n'existe pas.");
        }
    }

    // Méthode pour produire une quantité donnée d'une ressource
    public void produceResource(String name, int quantity) {
        Resource resource = resources.get(name);
        resource.setQuantity(resource.getQuantity() + quantity);
    }

    // Méthode pour afficher les quantités de toutes les ressources
    public void showResource() {
        for (Map.Entry<String, Resource> entry : resources.entrySet()) {
            String resourceName = entry.getKey();
            int amount = entry.getValue().getQuantity();
            System.out.println(resourceName + " : " + amount);
        }
    }

    // Méthode pour obtenir la population disponible dans la ville
    public int getAvailablePopulation() {
        return availablePopulation;
    }

    // Méthode pour définir la population disponible dans la ville
    public void setAvailablePopulation(int availablePopulation) {
        this.availablePopulation = availablePopulation;
    }

    // Dans la classe ResourceManager
    public int getTotalPopulation(List<Building> buildingList) {
        int totalPopulation = availablePopulation;

        // Ajoutez ici la population des bâtiments en parcourant la liste des bâtiments
        for (Building building : buildingList) {
            totalPopulation += building.getPopulation();
        }

        return totalPopulation;
    }


}
