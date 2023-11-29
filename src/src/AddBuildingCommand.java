public class AddBuildingCommand implements Command {
    private final Manager manager;
    private final String buildingType;

    public AddBuildingCommand(Manager manager, String buildingType) {
        this.manager = manager;
        this.buildingType = buildingType;
    }

    @Override
    public void execute() {
        // Créer un nouveau bâtiment en utilisant la fabrique
        Building newBuilding = BuildingFactory.createBuilding(buildingType);

        // Ajouter le nouveau bâtiment au gestionnaire
        manager.addBuilding(newBuilding);

        // Construire le nouveau bâtiment
        newBuilding.build();

        // Afficher un message pour informer de la construction du bâtiment
        System.out.println("Nouveau bâtiment de type " + buildingType + " en cours de construction !");
    }
}
