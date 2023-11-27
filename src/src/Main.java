import java.time.format.ResolverStyle;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        ResourceManager resourceManager = ResourceManager.getInstance();

        Building ferme = BuildingFactory.createBuilding("Ferme");
        manager.addBuilding(ferme);
        ferme.addInhabitant(10);


        while (true) {
            manager.manageResources();

            try {
                Thread.sleep(1000);
                System.out.println(ferme.isBuilt);
                resourceManager.showRessource();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}