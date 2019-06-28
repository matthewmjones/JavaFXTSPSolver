package uk.ac.mdx;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.RejectedExecutionException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Main controller for the GUI
 * @author Matthew M. Jones
 */
public class FXMLGUIController implements Initializable {

    private static final double     CITY_SIZE                       = 6.0;
    private static final double     PATH_LINE_WIDTH                 = 3.0;
    private static final int        NUMBER_OF_VERTICAL_GRID_LINES   = 23;
    private static final int        NUMBER_OF_HORIZONTAL_GRID_LINES = 23;
    private static final double     CANVAS_RATIO_WIDTH              =
            NUMBER_OF_VERTICAL_GRID_LINES > NUMBER_OF_HORIZONTAL_GRID_LINES ? (double) NUMBER_OF_VERTICAL_GRID_LINES / (double) NUMBER_OF_HORIZONTAL_GRID_LINES : 1;
    private static final double     CANVAS_RATIO_HEIGHT             =
            NUMBER_OF_VERTICAL_GRID_LINES < NUMBER_OF_HORIZONTAL_GRID_LINES ? (double) NUMBER_OF_HORIZONTAL_GRID_LINES / (double) NUMBER_OF_VERTICAL_GRID_LINES : 1;

    private static Path currentPath;

    boolean snapToGrid = true;
    Stage settingsStage = new Stage();

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    SplitPane theMainSplitPane = new SplitPane();

    @FXML
    private MenuItem showCitiesMenuItem;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private ChoiceBox<String> algorithmChoiceBox;

    @FXML
    private ProgressBar runningProgressBar;

    @FXML
    private ProgressIndicator runningProgressIndicator;

    @FXML
    private Slider numberOfCitiesSlider;

    @FXML
    private Button createNewButton;

    @FXML
    private Button saveCreatedProblemBtn;

    @FXML
    private CheckBox snapToGridCB;

    @FXML
    private Label lengthLabel;

    @FXML
    private Canvas backgroundCanvas;

    @FXML
    private Canvas pathCanvas;

    @FXML
    private Canvas cityCanvas;


    private static HashMap<String, Integer> algHM;

    private static final TSPProblem theTSPProblem = TSPProblem.getProblem();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        settingsStage.initStyle(StageStyle.UNIFIED);

        saveCreatedProblemBtn.setVisible(false);
        snapToGridCB.setVisible(false);


        algorithmChoiceBox.getItems().addAll("Brute Force", "Nearest Neighbour", "Simulated Annealing", "Genetic Algorithm", "Two-opt", "Three-opt");
        algorithmChoiceBox.getSelectionModel().select(0);

        algHM = new HashMap<>();

        algHM.put("Brute Force", TSPAlgorithmFactory.BRUTEFORCE);
        algHM.put("Nearest Neighbour", TSPAlgorithmFactory.NEAREST_NEIGHBOUR);
        algHM.put("Simulated Annealing", TSPAlgorithmFactory.SIMULATEDANNEALING);
        algHM.put("Genetic Algorithm", TSPAlgorithmFactory.GENETICALGORITHM);
        algHM.put("Two-opt", TSPAlgorithmFactory.TWO_OPT);
        algHM.put("Three-opt", TSPAlgorithmFactory.THREE_OPT);

        GraphicsContext gc = backgroundCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, backgroundCanvas.getWidth(), backgroundCanvas.getHeight());
        drawGridLines();

        setNewProblem();

        mainAnchorPane.widthProperty().addListener(e -> {
            hasResized();
        });

        mainAnchorPane.heightProperty().addListener(e -> {
            hasResized();
        });
    }

    private void hasResized() {
        theMainSplitPane.setPrefSize(mainAnchorPane.getWidth(), mainAnchorPane.getHeight());

        theMainSplitPane.setDividerPosition(0, 300 / mainAnchorPane.getWidth());

        double canvasSize = Math.min(mainAnchorPane.getWidth() * ( 1.0 - theMainSplitPane.getDividerPositions()[0]), mainAnchorPane.getHeight() - 50.0);

        backgroundCanvas.setWidth(canvasSize);
        backgroundCanvas.setHeight(canvasSize);

        pathCanvas.setWidth(canvasSize);
        pathCanvas.setHeight(canvasSize);

        cityCanvas.setWidth(canvasSize);
        cityCanvas.setHeight(canvasSize);

        clearCities();
        drawCities();

        clearPath();
        if (currentPath!= null) {
            drawPath(currentPath);
        }
        drawGridLines();
    }


    @FXML
    private void close() {
        Platform.exit();
    }

    @FXML
    private void setNewProblem() {
        int numberOfCities = (int) numberOfCitiesSlider.getValue();


        City[] cities = new City[numberOfCities];
        for (int i = 0; i < numberOfCities; i++) {
            cities[i] = new City();
        }
        TSPProblem.getProblem().setListOfCities(cities);
        drawCities();

        clearPath();
    }


    private void drawCities() {
        Platform.runLater(() -> {

            double xScale = cityCanvas.getWidth() / City.MAX_Y;
            double yScale = cityCanvas.getHeight() / City.MAX_X;

            GraphicsContext gc = cityCanvas.getGraphicsContext2D();

            gc.clearRect(0, 0, cityCanvas.getWidth(), cityCanvas.getHeight());

            gc.setFill(Color.valueOf("#fed100"));
            gc.fillOval(xScale * (TSPProblem.getProblem().getListOfCities()[0].getxPos() - CITY_SIZE), xScale * (TSPProblem.getProblem().getListOfCities()[0].getyPos() - CITY_SIZE), 2 * xScale *CITY_SIZE, 2 * xScale * CITY_SIZE);

            gc.setFill(Color.valueOf("#004165"));
            for (int i = 1; i < TSPProblem.getProblem().getNumberOfCities(); i++) {
                gc.fillOval(xScale * (TSPProblem.getProblem().getListOfCities()[i].getxPos() - CITY_SIZE), xScale * (TSPProblem.getProblem().getListOfCities()[i].getyPos() - CITY_SIZE), 2 * xScale *CITY_SIZE, 2 * xScale * CITY_SIZE);    // these have to be offset by (-5,-5) sinve fillOval puts the circle below/right of the point.
            }
        });
    }

    private void clearCities() {
        Platform.runLater(() -> {
            GraphicsContext gc = cityCanvas.getGraphicsContext2D();

            gc.clearRect(0, 0, cityCanvas.getWidth(), cityCanvas.getHeight());
        });
    }

    synchronized private void drawPath(Path p) {

        Platform.runLater( () -> {

            double xScale = cityCanvas.getWidth() / City.MAX_Y;
            double yScale = cityCanvas.getHeight() / City.MAX_X;

            try {
                lengthLabel.setText(String.format("%,.1fm", p.length()));

                GraphicsContext gc = pathCanvas.getGraphicsContext2D();
                gc.clearRect(0, 0, pathCanvas.getWidth(), pathCanvas.getHeight());

                gc.setStroke(Color.valueOf("#c4161c"));
                gc.setLineWidth(xScale * PATH_LINE_WIDTH);

                for (int i = 1; i < p.size(); i++) {

                    gc.strokeLine(
                            xScale * TSPProblem.getProblem().getListOfCities()[p.get(i - 1)].getxPos(),
                            yScale * TSPProblem.getProblem().getListOfCities()[p.get(i - 1)].getyPos(),
                            xScale * TSPProblem.getProblem().getListOfCities()[p.get(i)].getxPos(),
                            yScale * TSPProblem.getProblem().getListOfCities()[p.get(i)].getyPos()
                    );
                }

                if (p instanceof Tour) {
                    gc.strokeLine(
                            xScale * TSPProblem.getProblem().getListOfCities()[p.get(0)].getxPos(),
                            yScale * TSPProblem.getProblem().getListOfCities()[p.get(0)].getyPos(),
                            xScale * TSPProblem.getProblem().getListOfCities()[p.get(p.size() - 1)].getxPos(),
                            yScale * TSPProblem.getProblem().getListOfCities()[p.get(p.size() - 1)].getyPos()
                    );
                }
            } catch (Exception e) {
                warningBox("An error occured while drawing the path.");
            }
        });

    }
    @FXML
    private void clearPath() {
        Platform.runLater( () -> {

            GraphicsContext gc = pathCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, pathCanvas.getWidth(), pathCanvas.getHeight());

        });
    }

    synchronized public void setCurrentPath(Path p) {

        try {
            currentPath = p.clone();
            drawPath(currentPath);
        } catch (CloneNotSupportedException ex) {}
    }

    synchronized public Path getCurrentPath() {
        return currentPath;
    }

    @FXML
    private void startAlgorithm() {
        try {
            TSPAlgorithmFactory.getInstance().runAlgorithm(
                    algHM.get(algorithmChoiceBox.getSelectionModel().getSelectedItem())
            );

            runningProgressBar.progressProperty().unbind();
            runningProgressBar.progressProperty().bind(TSPAlgorithmFactory.getInstance().getProgressProperty());

            runningProgressIndicator.progressProperty().unbind();
            runningProgressIndicator.progressProperty().bind(TSPAlgorithmFactory.getInstance().getProgressProperty());


        } catch (RejectedExecutionException e) {
            warningBox("Already running. Stop the current algorithm before starting another.");
        }

    }

    @FXML
    private void stopAlgorithm() {
        if (TSPAlgorithmFactory.getInstance().isRunning()) {
            TSPAlgorithmFactory.getInstance().stopAlgorithm();
        }
    }

    @FXML
    private void createTSPProblem() {

        double xScale = cityCanvas.getWidth() / City.MAX_Y;
        double yScale = cityCanvas.getHeight() / City.MAX_X;

        saveCreatedProblemBtn.setVisible(true);
        snapToGridCB.setVisible(true);

        clearCities();
        clearPath();


        snapToGridCB.setSelected(snapToGrid);

        snapToGridCB.setOnAction((e) -> {
            snapToGrid = snapToGridCB.isSelected();
        });

        List<City> newCityList = new ArrayList<>();

        cityCanvas.setOnMouseClicked((e) -> {
            double x = e.getX();
            double y = e.getY();

            if (snapToGrid) {
                double gridBoxWidth = cityCanvas.getWidth() / NUMBER_OF_VERTICAL_GRID_LINES;
                double gridBoxHeight = cityCanvas.getHeight() / NUMBER_OF_HORIZONTAL_GRID_LINES;

                x = gridBoxWidth * Math.round(x / gridBoxWidth);
                y = gridBoxHeight * Math.round(y / gridBoxHeight);
            }
            newCityList.add(new City(x / xScale, y / yScale));
            GraphicsContext gc = cityCanvas.getGraphicsContext2D();
            gc.fillOval(x - 5, y - 5, 10, 10);
        });

        saveCreatedProblemBtn.setOnMouseClicked((e) -> {
            TSPProblem.getProblem().setListOfCities(newCityList.toArray(new City[newCityList.size()]));
            clearCities();
            drawCities();
            saveCreatedProblemBtn.setVisible(false);
            snapToGridCB.setVisible(false);

            numberOfCitiesSlider.setValue(TSPProblem.getProblem().getNumberOfCities());
        });
    }


    @FXML
    private void openFile() {
        ArrayList<City> openingCities = new ArrayList<>(); // Use an ArrayList to store the data since we don't know how many there are

        // The following are used to calculate the bounding box for the data
        int maxX = 0; int minX = Integer.MAX_VALUE;
        int maxY = 0; int minY = Integer.MAX_VALUE;

        FileChooser fc = new FileChooser();
        fc.setTitle("Open file");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("TSP Files", "*.tsp"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*.*"));

        File inputFile = fc.showOpenDialog(null);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {

            String line;

            // Run through to find the start of the nodes
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("NAME") | line.startsWith("COMMENT")) {
                    System.out.println(":: " + line);
                }

                if (line.startsWith("NODE_COORD_SECTION")) {
                    break;
                }
            }

            // Read node coordinates and parse them into ArrayList openingCities
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // removes leading whitespace;

                if (line.matches("\\d+\\s+\\-?[\\d,\\.,\\+,\\-,e]+\\s+\\-?[\\d,\\.,\\+,\\-,e]+")) {
                    // REGEX: of the form nnn -nnn.ddd +/-nnn.ddd
                    String[] splitLine = line.split("\\s+");
                    int x = (int) Double.parseDouble(splitLine[1]);
                    int y = (int) Double.parseDouble(splitLine[2]);

                    maxY = y > maxY ? y : maxY;
                    maxX = x > maxX ? x : maxX;

                    minX = x < minX ? x : minX;
                    minY = y < minY ? y : minY;


                    openingCities.add(new City(x, y));
                }
            }

        } catch (IOException e) {
            warningBox("Error in file opening dialog:\n " + e.getMessage());
        }

        try {

            City [] listOfCities = new City[openingCities.size()];
            int numberOfCities = openingCities.size();

            double maxInp = (double) Math.max(maxX - minX, maxY - minY);
            double minOut = (double) Math.min(City.MAX_X, City.MAX_Y);

            for (int i = 0; i < openingCities.size(); i++) { // scale the values to our system and transform the centre to our centre
                listOfCities[i] = new City((int) ( ((double) openingCities.get(i).getxPos() - (double) (minX + maxX) / 2) * minOut / maxInp + (double) City.MAX_X / 2),
                        City.MAX_Y - (int) (((double) openingCities.get(i).getyPos() - (double) (minY + maxY) / 2 ) * minOut / maxInp + (double) City.MAX_Y / 2));
            }

            numberOfCitiesSlider.setValue(Math.min(numberOfCities, numberOfCitiesSlider.getMax()));

            TSPProblem.getProblem().setListOfCities(listOfCities);
            clearPath();
            drawCities();

        } catch (Exception e) {
            warningBox("An error occured while opening file: " + inputFile.getName());
        }
    }

    @FXML
    private void saveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TSP files (*.tsp)", "*.tsp"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt"));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write("NAME: " + file.getName() + "\n");
                writer.write("COMMENT :\n");
                writer.write("TYPE : TSP");
                writer.write("DIMENSION : ");
                writer.write(String.valueOf(TSPProblem.getProblem().getNumberOfCities()));
                writer.write("\n");
                writer.write("EDGE_WEIGHT_TYPE : ATT\n");
                writer.write("NODE_COORD_SECTION\n");

                for(int i = 0; i < TSPProblem.getProblem().getNumberOfCities(); i++) {
                    writer.write(String.valueOf(i+1));
                    writer.write(" ");
                    writer.write(String.valueOf(TSPProblem.getProblem().getListOfCities()[i].getxPos()));
                    writer.write(" ");
                    writer.write(String.valueOf(City.MAX_Y - TSPProblem.getProblem().getListOfCities()[i].getyPos()));
                    writer.write("\n");
                }

                writer.close();
            } catch (IOException ex) {
                warningBox("An error occured while saving the file:\n" + ex.getMessage());
            }
        }
    }


    @FXML
    private void openSettingsMenu() throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXMLSettings.fxml"));
            Scene scene = new Scene(root);
            settingsStage.setScene(scene);

            settingsStage.show();
        } catch (Exception e) {
            warningBox("An error occured. Please restart.");
        }
    }

    @FXML
    public void closeSettingsMenu() {
        settingsStage.close();
    }

    @FXML
    private void toggleCities() {

        if (showCitiesMenuItem.getText().equals("Hide Cities")) {
            showCitiesMenuItem.setText("Show Cities");
            cityCanvas.setVisible(false);
        } else {
            showCitiesMenuItem.setText("Hide Cities");
            cityCanvas.setVisible(true);
        }
    }

    private void drawGridLines() {

        GraphicsContext gc = backgroundCanvas.getGraphicsContext2D();

        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, backgroundCanvas.getWidth(), backgroundCanvas.getHeight());

        gc.setStroke(Color.grayRgb(180));

        for (int i = 0; i <= NUMBER_OF_HORIZONTAL_GRID_LINES; i++) {
            gc.beginPath();
            gc.moveTo(0, backgroundCanvas.getHeight() * (double) i / (double) NUMBER_OF_HORIZONTAL_GRID_LINES);
            gc.lineTo(backgroundCanvas.getWidth(), backgroundCanvas.getHeight() * (double) i / (double) NUMBER_OF_HORIZONTAL_GRID_LINES);
            gc.closePath();
            gc.stroke();
        }

        for (int i = 0; i <= NUMBER_OF_VERTICAL_GRID_LINES; i++) {
            gc.beginPath();
            gc.moveTo(backgroundCanvas.getWidth() * (double) i / (double) NUMBER_OF_VERTICAL_GRID_LINES, 0);
            gc.lineTo(backgroundCanvas.getWidth() * (double) i / (double) NUMBER_OF_VERTICAL_GRID_LINES, backgroundCanvas.getHeight());
            gc.closePath();
            gc.stroke();
        }
    }

    /**
     * Gives access to the current controller instance
     * @return the current controller instance
     */
    public static FXMLGUIController GUIController() {
        return App.getController();
    }

    // Generic pop-up box for use elsewhere
    private void warningBox(String warningText) {
        Alert alertBox;
        alertBox = new Alert(Alert.AlertType.INFORMATION, warningText);
        alertBox.showAndWait();
    }
}

