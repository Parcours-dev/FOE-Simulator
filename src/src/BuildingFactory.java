import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BuildingFactory {
    private static final Map<String, Supplier<Building>> map = new HashMap<>();
    static {
        map.put("Maison", Maison::new);
        map.put("Ferme", Ferme::new);
        map.put("Caserne", Caserne::new);
        map.put("Carrière", Carrière::new);
        map.put("Mine", Mine::new);
        map.put("Scierie", Scierie::new);
        map.put("Forge", Forge::new);
        // map.put("Académie", Academie::new);
    }

    public static Building createBuilding(String type) {
        Supplier<Building> constructor = map.get(type);

        return constructor != null ? constructor.get() : null;
    }
}
