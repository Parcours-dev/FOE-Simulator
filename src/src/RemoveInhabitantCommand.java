public class RemoveInhabitantCommand implements Command {
    // Bâtiment concerné par la suppression d'habitant
    private final Building building;

    // Nombre d'habitants à supprimer
    private final int inhabitantsToRemove;

    // Constructeur prenant le bâtiment et le nombre d'habitants à supprimer en paramètres
    public RemoveInhabitantCommand(Building building, int inhabitantsToRemove) {
        this.building = building;
        this.inhabitantsToRemove = inhabitantsToRemove;
    }

    // Méthode d'exécution de la commande
    @Override
    public void execute() {
        // Appel de la méthode removeInhabitant du bâtiment pour supprimer des habitants
        building.removeInhabitant(inhabitantsToRemove);
    }
}
