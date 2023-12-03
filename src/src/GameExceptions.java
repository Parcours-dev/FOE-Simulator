
public class GameExceptions {

    //Exception levée lorsqu'un indice de bâtiment est invalide.
    public static class InvalidBuildingIndexException extends RuntimeException {
    // Initialise le message d'erreur par défaut pour un indice de bâtiment invalide.
        public InvalidBuildingIndexException() {
            super("Indice de bâtiment invalide. Entrez un nombre valide.");
        }
    }


    // Exception levée lorsqu'un nombre d'habitants est invalide.
    public static class InvalidInhabitantCountException extends RuntimeException {
        //Initialise le message d'erreur par défaut pour un nombre d'habitants invalide.
        public InvalidInhabitantCountException() {
            super("Nombre d'habitants invalide. Entrez un nombre valide.");
        }
    }


    //Exception levée lorsqu'un choix de menu est introuvable.
    public static class MenuChoiceNotFoundException extends RuntimeException {
    //Initialise le message d'erreur par défaut pour un choix de menu introuvable.
        public MenuChoiceNotFoundException() {
            super("Choix de menu introuvable.");
        }
    }
}