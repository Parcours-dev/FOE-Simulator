public class RemoveBuildingCommand implements Command {
    // Manager responsable de la gestion des bâtiments
    private final Manager manager;

    // Bâtiment à supprimer
    private final Building buildingToRemove;

    // Constructeur prenant le manager et le bâtiment à supprimer en paramètres
    public RemoveBuildingCommand(Manager manager, Building buildingToRemove) {
        this.manager = manager;
        this.buildingToRemove = buildingToRemove;
    }

    // Méthode d'exécution de la commande
    @Override
    public void execute() {
        // Appel de la méthode removeBuilding du manager pour supprimer le bâtiment
        manager.removeBuilding(buildingToRemove);
    }
}
