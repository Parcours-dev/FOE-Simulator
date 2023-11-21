import java.time.format.ResolverStyle;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        ResourceManager r1 = new ResourceManager();

        // Ajouter des bâtiments et des ressources
        Building ferme = BuildingFactory.createBuilding("Ferme");
        ferme.build();
        manager.addBuilding(ferme);


        while (true) {
            manager.manageResources();

            // Pause pour simuler le temps réel
            try {
                Thread.sleep(1000);
                ferme.consumeResources();
                System.out.println(r1.getResource("Bois"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}