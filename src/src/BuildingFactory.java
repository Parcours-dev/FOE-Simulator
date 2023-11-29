import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        // Ajouter d'autres types de bâtiments ici
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
    public static Set<String> getBuildingTypes() {
        return buildingConstructors.keySet();
    }
}
