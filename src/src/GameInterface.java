import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

    private JTable buildingTable;
    private DefaultTableModel buildingTableModel;

    public GameInterface() {
        initializeGUI();
        manager = new Manager();
        initializeResources();
        setupResourceTimer();

        removeInhabitantButton.addActionListener(e -> {
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
        add(buildingsPanel, BorderLayout.PAGE_END);
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
        add(infoPanel, BorderLayout.LINE_END);

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
        add(resourcesPanel, BorderLayout.PAGE_START);
        initializeResourceLabels();

        buildingTable = createBuildingTable();
        add(buildingTable, BorderLayout.LINE_START);
    }

    private void setupGridButton(JButton gridButton) {
        Map<String, ImageIcon> buildingIcons = new HashMap<>();
        buildingIcons.put("Maison", new ImageIcon(getClass().getResource("/image/maison.jpg")));
        buildingIcons.put("Ferme", new ImageIcon(getClass().getResource("/image/ferme.jpg")));
        buildingIcons.put("Caserne", new ImageIcon(getClass().getResource("/image/caserne.jpg")));
        buildingIcons.put("Cabanne en Bois", new ImageIcon(getClass().getResource("/image/cabane_bois.jpg")));
        buildingIcons.put("Carrière", new ImageIcon(getClass().getResource("/image/carriere.jpg")));
        buildingIcons.put("Mine", new ImageIcon(getClass().getResource("/image/mine.jpg")));
        buildingIcons.put("Scierie", new ImageIcon(getClass().getResource("/image/scierie.jpg")));
        buildingIcons.put("Forge", new ImageIcon(getClass().getResource("/image/forge.jpg")));

        Map<JButton, Building> buttonBuildingMap = new HashMap<>();

        gridButton.setOpaque(true);
        gridButton.setBorderPainted(true);
        gridButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        gridButton.addActionListener(e -> {
            if (destroyMode && gridButton.getIcon() != null) {
                Building building = buttonBuildingMap.get(gridButton);
                if (building != null) {
                    manager.removeBuilding(building);
                    gridButton.setIcon(null);
                    clearBuildingInfo();
                    System.out.println("Le bâtiment a été enlevé du terrain.");
                    System.out.println(manager.getBuildings());
                    addInhabitantButton.setEnabled(false);
                    removeInhabitantButton.setEnabled(false);
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
                buttonBuildingMap.put(gridButton, building);
            } else if (gridButton.getIcon() != null) {
                Building building = buttonBuildingMap.get(gridButton);
                if (building != null) {
                    updateBuildingInfo(building);
                    addInhabitantButton.setEnabled(true);
                    removeInhabitantButton.setEnabled(true);
                }
            }
        });
    }

    private JTable createBuildingTable() {
        String[] columnNames = {"Nom bâtiment", "Nb habitants/travailleurs", "Mat constructions", "Temps de construction (en ms)"};
        buildingTableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(buildingTableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (String buildingName : BuildingFactory.getBuildingTypes()) {
            Building building = BuildingFactory.createBuilding(buildingName);
            Object[] rowData = {
                    buildingName,
                    building.getPopulationLimit(),
                    formatMaterials(building.getResourceCosts()),
                    building.gettConstruction()
            };
            buildingTableModel.addRow(rowData);
        }

        return table;
    }

    private String formatMaterials(Map<String, Integer> materials) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Integer> entry : materials.entrySet()) {
            result.append(entry.getValue()).append(" ").append(entry.getKey()).append("  ");
        }
        return result.toString();
    }

    private String formatResources(Map<String, Integer> resources) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            result.append(entry.getValue()).append(" ").append(entry.getKey()).append("  ");
        }
        return result.toString();
    }

    private void initializeResources() {
        ResourceManager resourceManager = ResourceManager.getInstance();
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
            destroyMode = !destroyMode;
            addInhabitantButton.setEnabled(false);
            removeInhabitantButton.setEnabled(false);
            if (destroyMode) {
                System.out.println("Mode destruction activé. Sélectionnez un bâtiment à détruire.");
            } else {
                System.out.println("Mode destruction désactivé.");
            }

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
