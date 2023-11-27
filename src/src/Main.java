import java.time.format.ResolverStyle;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();


        // Ajouter des bâtiments et des ressources
        Building ferme = BuildingFactory.createBuilding("Ferme");
        manager.addBuilding(ferme);

        Building maison = BuildingFactory.createBuilding("Maison");
        manager.addBuilding(maison);


        while (true) {
            manager.manageResources();
            // Pause pour simuler le temps réel
            try {
                Thread.sleep(1000);
                System.out.println(ferme.isBuilt);
                System.out.println(maison.isBuilt);

                //manager.showRessources();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}