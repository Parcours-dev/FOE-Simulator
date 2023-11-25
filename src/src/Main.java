import java.time.format.ResolverStyle;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();


        // Ajouter des bâtiments et des ressources
        Building ferme = BuildingFactory.createBuilding("Ferme");
        manager.addBuilding(ferme);

//test

        while (true) {
            manager.manageResources();
            // Pause pour simuler le temps réel
            try {
                Thread.sleep(2000);
                System.out.println(ferme.isBuilt);
                manager.showRessources();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}