import java.util.InputMismatchException;

public class GameExceptions {

    public static class InvalidBuildingIndexException extends RuntimeException {
        public InvalidBuildingIndexException() {
            super("Indice de bâtiment invalide. Entrez un nombre valide.");
        }
    }


    public static class InvalidInhabitantCountException extends RuntimeException {
        public InvalidInhabitantCountException() {
            super("Nombre d'habitants invalide. Entrez un nombre valide.");
        }
    }

    public static class MenuChoiceNotFoundException extends RuntimeException {
        public MenuChoiceNotFoundException() {
            super("Choix de menu introuvable.");
        }
    }
}
