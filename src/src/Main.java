import java.time.format.ResolverStyle;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        ResourceManager resourceManager = ResourceManager.getInstance();

        Building ferme = BuildingFactory.createBuilding("Ferme");
        manager.addBuilding(ferme);
        ferme.addInhabitant(10);
        ferme.build();



        while (true) {
            manager.manageResources();

            try {
                Thread.sleep(1000);
                resourceManager.showRessource();
                System.out.println(ferme.isBuilt);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}