import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BuildingFactory {
    // Map associant chaque type de bâtiment à un constructeur
    private static final Map<String, Supplier<Building>> buildingConstructors = new HashMap<>();

    // Initialisation statique du map avec les types de bâtiments pris en charge et leurs constructeurs
    static {
        buildingConstructors.put("Maison", Maison::new);
        buildingConstructors.put("Ferme", Ferme::new);
        buildingConstructors.put("Caserne", Caserne::new);
        buildingConstructors.put("Carrière", Carrière::new);
        buildingConstructors.put("Mine", Mine::new);
        buildingConstructors.put("Scierie", Scierie::new);
        buildingConstructors.put("Forge", Forge::new);
        buildingConstructors.put("Cabanne en Bois", CabaneEnBois::new);
    }

    // Crée un nouveau bâtiment du type spécifié
    public static Building createBuilding(String type) {
        Supplier<Building> constructor = buildingConstructors.get(type);
        if (constructor == null) {
            throw new IllegalArgumentException("Type de bâtiment non pris en charge : " + type);
        }
        return constructor.get();
    }


    // Renvoie l'ensemble des types de bâtiments disponibles
    public static List<String> getBuildingTypes() {
        return buildingConstructors.keySet().stream().toList();
    }

    // Renvoie le coût de construction d'un bâtiment du type spécifié
    public static Map<String, Integer> getBuildingCost(String type) {
        Building building = createBuilding(type);
        if (building == null) {
            throw new IllegalArgumentException("Type de bâtiment non pris en charge : " + type);
        }
        return building.getResourceCosts();
    }



}
