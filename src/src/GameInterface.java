import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;

public class GameInterface extends JFrame {
    private static Manager manager;
    private static ResourceManager resourceManager = ResourceManager.getInstance();

    private JPanel buildingsPanel;
    private JPanel gridPanel;
    private Map<String, Color> buildingColors;
    private JPanel resourcesPanel;
    private Map<String, Resource> resources;
    private static Map<String, JLabel> resourceLabels;

    private JPanel infoPanel;
    private JLabel productionLabel;
    private JLabel consumptionLabel;
    private JLabel populationLabel;
    private JButton addInhabitantButton;
    private JButton removeInhabitantButton;

    private JButton destroyButton;
    private AtomicReference<String> selectedBuilding = new AtomicReference<>();
    private boolean destroyMode = false;

    public GameInterface() {
        initializeGUI();
        manager = new Manager();
        initializeResources();
        setupResourceTimer();

        removeInhabitantButton.addActionListener(e -> {
            // Activer ou désactiver le mode destruction
            destroyMode = !destroyMode;
            if (destroyMode) {
                System.out.println("Mode destruction activé. Sélectionnez un bâtiment à détruire.");
            } else {
                System.out.println("Mode destruction désactivé.");
            }
        });
    }

    private void initializeGUI() {
        setTitle("FOE V2 - La concu de Rise of Kingdom");
        setSize(1300, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        buildingsPanel = new JPanel();
        gridPanel = new JPanel();
        add(buildingsPanel, BorderLayout.SOUTH);
        add(gridPanel, BorderLayout.CENTER);

        infoPanel = new JPanel(new GridLayout(5, 1));
        productionLabel = new JLabel("Production: ");
        consumptionLabel = new JLabel("Consommation: ");
        populationLabel = new JLabel("Population: ");
        addInhabitantButton = new JButton("Ajouter Habitant");
        addInhabitantButton.setEnabled(false);
        removeInhabitantButton = new JButton("Supprimer Habitant");
        removeInhabitantButton.setEnabled(false);
        destroyButton = new JButton("Détruire un bâtiment");

        infoPanel.add(productionLabel);
        infoPanel.add(consumptionLabel);
        infoPanel.add(populationLabel);
        infoPanel.add(addInhabitantButton);
        infoPanel.add(removeInhabitantButton);
        infoPanel.add(destroyButton);
        add(infoPanel, BorderLayout.WEST);

        for (String buildingName : BuildingFactory.getBuildingTypes()) {
            JButton buildingButton = new JButton(buildingName);
            buildingButton.addActionListener(e -> selectedBuilding.set(buildingName));
            buildingsPanel.add(buildingButton);
        }

        gridPanel.setLayout(new GridLayout(10, 10));
        for (int i = 0; i < 100; i++) {
            JButton gridButton = new JButton();
            setupGridButton(gridButton);
            gridPanel.add(gridButton);
        }

        resourcesPanel = new JPanel();
        add(resourcesPanel, BorderLayout.NORTH);
        initializeResourceLabels();
    }

    private void setupGridButton(JButton gridButton) {
        Map<String, ImageIcon> buildingIcons = new HashMap<>();
        buildingIcons.put("Maison", new ImageIcon(getClass().getResource("/image/maison.png")));
        buildingIcons.put("Ferme", new ImageIcon(getClass().getResource("/image/ferme.png")));
        buildingIcons.put("Caserne", new ImageIcon(getClass().getResource("/image/caserne.png")));
        buildingIcons.put("Cabanne en Bois", new ImageIcon(getClass().getResource("/image/log-cabin.png")));
        buildingIcons.put("Carrière", new ImageIcon(getClass().getResource("/image/carriere.png")));
        buildingIcons.put("Mine", new ImageIcon(getClass().getResource("/image/mine.png")));
        buildingIcons.put("Scierie", new ImageIcon(getClass().getResource("/image/scierie.png")));
        buildingIcons.put("Forge", new ImageIcon(getClass().getResource("/image/forge.png")));

        // Créer une Map pour stocker la correspondance entre les boutons et les
        // bâtiments
        Map<JButton, Building> buttonBuildingMap = new HashMap<>();

        gridButton.setOpaque(true);
        gridButton.setBorderPainted(true);
        gridButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        gridButton.addActionListener(e -> {
            if (destroyMode && gridButton.getIcon() != null) {
                // Récupérer le bâtiment associé au bouton
                Building building = buttonBuildingMap.get(gridButton);
                if (building != null) {
                    manager.removeBuilding(building);
                    gridButton.setIcon(null);
                    clearBuildingInfo();
                    System.out.println("Le bâtiment a été enlevé du terrain.");
                    System.out.println(manager.getBuildings());
                    addInhabitantButton.setEnabled(false);
                    removeInhabitantButton.setEnabled(false);
                    // Supprimer la correspondance du bouton et du bâtiment de la Map
                    buttonBuildingMap.remove(gridButton);
                }
            } else if (selectedBuilding.get() != null && gridButton.getIcon() == null) {
                System.out.println(selectedBuilding.get() + " est sur le terrain :)");
                ImageIcon originalIcon = buildingIcons.get(selectedBuilding.get());
                Image scaledImage = originalIcon.getImage().getScaledInstance(gridButton.getWidth(),
                        gridButton.getHeight(), Image.SCALE_DEFAULT);
                gridButton.setIcon(new ImageIcon(scaledImage));
                Building building = BuildingFactory.createBuilding(selectedBuilding.get());
                manager.addBuilding(building);
                building.build();
                updateBuildingInfo(building);
                addInhabitantButton.setEnabled(true);
                removeInhabitantButton.setEnabled(true);
                // Ajouter la correspondance du bouton et du bâtiment à la Map
                buttonBuildingMap.put(gridButton, building);
            }else if (gridButton.getIcon() != null) {
                // Un bâtiment est associé à ce bouton
                Building building = buttonBuildingMap.get(gridButton);
                if (building != null) {
                    // Mettre à jour les informations du bâtiment
                    updateBuildingInfo(building);

                    // Activer les boutons "Ajouter Habitant" et "Supprimer Habitant"
                    addInhabitantButton.setEnabled(true);
                    removeInhabitantButton.setEnabled(true);
                }
            }
        });
    }

    private void initializeResources() {
        ResourceManager resourceManager = ResourceManager.getInstance();
        // Map des ressources, associant le nom de la ressource à son instance
        resources = new HashMap<>();
        resources.put(resourceManager.getResource("Nourriture").getName(), resourceManager.getResource("Nourriture"));
        resources.put(resourceManager.getResource("Bois").getName(), resourceManager.getResource("Bois"));
        resources.put(resourceManager.getResource("Pierre").getName(), resourceManager.getResource("Pierre"));
        resources.put(resourceManager.getResource("Charbon").getName(), resourceManager.getResource("Charbon"));
        resources.put(resourceManager.getResource("Fer").getName(), resourceManager.getResource("Fer"));
        resources.put(resourceManager.getResource("Acier").getName(), resourceManager.getResource("Acier"));
        resources.put(resourceManager.getResource("Population").getName(), resourceManager.getResource("Population"));

        resourceLabels = new HashMap<>();
        for (String resourceName : resources.keySet()) {
            JLabel resourceLabel = new JLabel(resourceName + ": " + resources.get(resourceName).getQuantity());
            resourcesPanel.add(resourceLabel);
            resourceLabels.put(resourceName, resourceLabel);
        }

        destroyButton.addActionListener(e -> {
            // Activer ou désactiver le mode destruction
            destroyMode = !destroyMode;
            addInhabitantButton.setEnabled(false);
            removeInhabitantButton.setEnabled(false);
            if (destroyMode) {
                System.out.println("Mode destruction activé. Sélectionnez un bâtiment à détruire.");
            } else {
                System.out.println("Mode destruction désactivé.");
            }

            // Désélectionner tout bâtiment précédemment sélectionné
            selectedBuilding.set(null);
        });

        infoPanel.add(destroyButton);
    }

    private void initializeResourceLabels() {
        Timer timer = new Timer(1000, e -> updateResourceLabels(resourceManager.getResources()));
        timer.start();
    }

    public static void updateResourceLabels(Map<String, Resource> resources) {
        SwingUtilities.invokeLater(() -> {
            for (String resourceName : resources.keySet()) {
                resourceLabels.get(resourceName)
                        .setText(resourceName + ": " + resources.get(resourceName).getQuantity());
            }
        });
    }

    private void updateBuildingInfo(Building building) {
        productionLabel.setText("Production: " + building.getResourceProduction());
        consumptionLabel.setText("Consommation: " + building.getResourceConsumption());
        populationLabel.setText("Population: " + building.getPopulation());

        for (ActionListener listener : addInhabitantButton.getActionListeners()) {
            addInhabitantButton.removeActionListener(listener);
        }
        for (ActionListener listener : removeInhabitantButton.getActionListeners()) {
            removeInhabitantButton.removeActionListener(listener);
        }

        addInhabitantButton.addActionListener(e -> addInhabitant(building));
        removeInhabitantButton.addActionListener(e -> removeInhabitant(building));
    }

    private void clearBuildingInfo() {
        productionLabel.setText("Production: ");
        consumptionLabel.setText("Consommation: ");
        populationLabel.setText("Population: ");

        for (ActionListener listener : addInhabitantButton.getActionListeners()) {
            addInhabitantButton.removeActionListener(listener);
        }
        for (ActionListener listener : removeInhabitantButton.getActionListeners()) {
            removeInhabitantButton.removeActionListener(listener);
        }
    }

    private void addInhabitant(Building building) {
        building.addInhabitant(1);
        updateBuildingInfo(building);
    }

    private void removeInhabitant(Building building) {
        building.removeInhabitant(1);
        updateBuildingInfo(building);
    }

    private void setupResourceTimer() {
        Thread resourceThread = new Thread(() -> {
            while (true) {
                manager.manageResources();
                updateResourceLabels(resourceManager.getResources());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        resourceThread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameInterface().setVisible(true));
    }
}
