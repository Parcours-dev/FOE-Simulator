import java.time.format.ResolverStyle;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();


        // Ajouter des bâtiments et des ressources
        Building ferme = BuildingFactory.createBuilding("Ferme");
        manager.addBuilding(ferme);



        while (true) {
            manager.manageResources();
            manager.showRessources();

            // Pause pour simuler le temps réel
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}