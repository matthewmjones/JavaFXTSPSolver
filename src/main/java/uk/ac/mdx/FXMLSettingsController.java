package uk.ac.mdx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;

/**
 * FXML Controller class
 *
 * @author Matthew M. Jones
 */
public class FXMLSettingsController implements Initializable {

    @FXML
    private CheckBox slowMotionCheckBox;

    @FXML
    private Slider initialTemperatureSlider;

    @FXML
    private Slider finalTemperatureSlider;

    @FXML
    private Slider coolingConstantSlider;

    @FXML
    private Slider populationSizeSlider;

    @FXML
    private Slider numberOfParentsSlider;

    @FXML
    private Slider numberOfGenerationsSlider;

    @FXML
    private Slider birthRateSlider;

    @FXML
    private Slider mutationRateSlider;

    @FXML
    private CheckBox elitismCheckBox;

    @FXML
    private ChoiceBox<String> crossoverChoiceBox;

    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        crossoverChoiceBox.getItems().addAll("Order Based Crossover", "Order Crossover");
        crossoverChoiceBox.setValue("Order Based Crossover");

        slowMotionCheckBox.setSelected(ParameterRepository.Store().isSlowMotion());

        initialTemperatureSlider.setValue(ParameterRepository.Store().getInitialTemperature());
        finalTemperatureSlider.setValue(ParameterRepository.Store().getFinalTemperature());
        coolingConstantSlider.setValue(ParameterRepository.Store().getCoolingConstant());

        populationSizeSlider.setValue((double) ParameterRepository.Store().getPopulationSize());
        numberOfParentsSlider.setValue((double) ParameterRepository.Store().getNumberOfParents());
        numberOfGenerationsSlider.setValue((double) ParameterRepository.Store().getNumberOfGenerations());
        birthRateSlider.setValue(ParameterRepository.Store().getBirthRate());
        mutationRateSlider.setValue(ParameterRepository.Store().getMutationRate());
        elitismCheckBox.setSelected(ParameterRepository.Store().isElitism());
    }

    @FXML
    private void saveChanges() {
        ParameterRepository.Store().setSlowMotion(slowMotionCheckBox.isSelected());

        ParameterRepository.Store().setInitialTemperature(initialTemperatureSlider.getValue());
        ParameterRepository.Store().setFinalTemperature(finalTemperatureSlider.getValue());
        ParameterRepository.Store().setCoolingConstant(coolingConstantSlider.getValue());

        ParameterRepository.Store().setPopulationSize((int) populationSizeSlider.getValue());
        ParameterRepository.Store().setNumberOfParents((int) numberOfParentsSlider.getValue());
        ParameterRepository.Store().setNumberOfGenerations((int) numberOfGenerationsSlider.getValue());
        ParameterRepository.Store().setBirthRate(birthRateSlider.getValue());
        ParameterRepository.Store().setMutationRate(mutationRateSlider.getValue());
        ParameterRepository.Store().setElitism(elitismCheckBox.isSelected());

        String selectedItem = crossoverChoiceBox.getSelectionModel().getSelectedItem();


        //FXMLGUIController.GUIController().closeSettingsMenu();

    }

}
