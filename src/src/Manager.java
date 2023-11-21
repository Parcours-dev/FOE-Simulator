import java.util.ArrayList;
import java.util.List;

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

    @Override
    public String toString() {
        return "Manager{" +
                "buildings=" + buildings +
                '}';
    }
}