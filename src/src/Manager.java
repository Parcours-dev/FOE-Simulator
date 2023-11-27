import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Manager {
    private List<Building> buildings;

    public Manager() {
        buildings = new ArrayList<>();
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public void removeBuilding(Building building) {
        buildings.remove(building);
    }

    public void manageResources() {
        for (Building building : buildings) {
            building.consumeResources();
            building.produceResources();
        }
    }

    public void buildBuilding() {
        for (Building building : buildings) {
            building.build();
        }
    }
    public void showBuildings() {
        for (Building building : buildings) {
            System.out.println(building);
        }
    }

    public void showRessources() {
        for (Building building : buildings) {
            building.showRessources();
        }

    }
}