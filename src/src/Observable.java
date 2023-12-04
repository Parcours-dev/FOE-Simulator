import java.util.ArrayList;
import java.util.List;

public interface Observable {
    // Liste d'observateurs (initialisée par défaut avec une liste vide)
    List<Observer> observers = new ArrayList<>();

    // Méthode par défaut pour ajouter un observateur
    default void addObserver(Observer observer) {
        observers.add(observer);
    }

    // Méthode par défaut pour supprimer un observateur
    default void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    // Méthode par défaut pour notifier tous les observateurs
    default void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}

