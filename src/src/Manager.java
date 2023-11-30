import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Manager implements Observer, Observable {
    // Liste des bâtiments gérés par le manager
    private List<Building> buildings;
    // Liste des observateurs du manager
    private List<Observer> observers;

    // Constructeur du manager
    public Manager() {
        // Initialisation des listes
        buildings = new ArrayList<>();
        observers = new ArrayList<>();
    }

    // Méthode pour ajouter un bâtiment au manager
    public void addBuilding(Building building) {
        buildings.add(building);
        building.addObserver(this);
    }

    // Méthode pour supprimer un bâtiment
    public void removeBuilding(Building building) {
        // Récupérer la population du bâtiment avant la destruction
        int buildingPopulation = building.getPopulation();
        ResourceManager resourceManager = ResourceManager.getInstance();

        // Restituer la population à la population disponible dans la ville
        ResourceManager.getInstance().produceResource("Population", buildingPopulation);

        // Restituer les ressources de la construction
        Map<String, Integer> constructionCosts = building.getResourceCosts();
        for (Map.Entry<String, Integer> entry : constructionCosts.entrySet()) {
            String resourceName = entry.getKey();
            int cost = entry.getValue();
            resourceManager.produceResource(resourceName, cost);
        }

        // Supprimer le bâtiment de la liste des bâtiments
        buildings.remove(building);
        building.removeObserver(this);
    }


    // Méthode pour gérer les ressources des bâtiments
    public void manageResources() {
        // Parcourir tous les bâtiments
        for (Building building : buildings) {
            // Appliquer la consommation de population et de ressources
            building.populationConsumption(buildings);
            building.consumeResources();
            building.produceResources();
        }
        // Notifier les observateurs du manager
        notifyObservers();
    }


    // Méthode pour afficher la liste des bâtiments
    public void showBuildings() {
        System.out.println("Liste des bâtiments :");
        for (int i = 0; i < buildings.size(); i++) {
            Building building = buildings.get(i);
            System.out.println(i + 1 + ". " + building.getType());
        }
    }

    // Méthode pour obtenir la liste des bâtiments
    public List<Building> getBuildings() {
        return buildings;
    }

    // Méthode de mise à jour en tant qu'observateur
    @Override
    public void update() {
        // Logique à exécuter en réponse à une mise à jour d'un bâtiment
    }


}
