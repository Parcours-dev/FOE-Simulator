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

            // Pause pour simuler le temps réel
            try {
                Thread.sleep(1000);
                manager.showRessources();
                System.out.print("\033[H\033[2J");
                System.out.flush();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}