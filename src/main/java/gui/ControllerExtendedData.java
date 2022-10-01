package gui;

import analysis.ExtendedDataAnalysis;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import model.Data;
import model.PlatformData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerExtendedData implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExtendedData.class);

    @FXML
    DatePicker DateFrom;
    @FXML
    DatePicker DateUntil;
    @FXML
    PieChart PieChartTypeDistribution;
    @FXML
    PieChart PieChartPlatformDistribution;

    /**
     * Listens to a change event to refresh the data if necessary
     *
     * @param location .
     * @param resources .
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //handles refresh after a new import (only when extended mode is active)
        Data.extendedDataChangeEvent.addListener(change -> {
            Data.extendedDataChangeEvent.setValue(false);
            if (Data.getExtendedMode()) {
                DateFrom.setValue(Data.getFirstPlaybackDate());
                DateUntil.setValue(Data.getLastPlaybackDate());
                handleRefresh();
            }
            //Don't know why this works or is needed but the event listener stops working without this line
            Data.extendedDataChangeEvent.getValue();
        });
    }

    /**
     * Refreshes the charts with new data if correct dates are selected
     */
    @FXML
    private void handleRefresh() {
        LOGGER.debug("handling refresh of extended data");
        DateFrom.getStyleClass().remove("date-picker-error");
        DateUntil.getStyleClass().remove("date-picker-error");
        PieChartTypeDistribution.getData().clear();
        PieChartPlatformDistribution.getData().clear();
        if (DateFrom.getValue() == null || DateUntil.getValue() == null) {
            DateFrom.getStyleClass().add("date-picker-error");
            DateUntil.getStyleClass().add("date-picker-error");
        } else {
            if (DateFrom.getValue().isBefore(DateUntil.getValue())) {   // !Data.library.isEmpty() &&
                //0 - Songs      1 - Podcasts
                long[] typeTime = ExtendedDataAnalysis.getPodcastDistribution(DateFrom.getValue(), DateUntil.getValue());
                PieChartTypeDistribution.getData().add(new PieChart.Data("Songs\t", typeTime[0]));
                PieChartTypeDistribution.getData().add(new PieChart.Data("Podcasts", typeTime[1]));
                for (PlatformData platformData : ExtendedDataAnalysis.getPlatformDistribution(DateFrom.getValue(), DateUntil.getValue())) {
                    PieChartPlatformDistribution.getData().add(new PieChart.Data(platformData.getDescription(), platformData.getTime()));
                }
                createHoverNodes(PieChartTypeDistribution);
                createHoverNodes(PieChartPlatformDistribution);
            } else {
                DateFrom.getStyleClass().add("date-picker-error");
                DateUntil.getStyleClass().add("date-picker-error");
            }
        }
    }

    /**
     * Creates floating fields that are displayed when the mouse is moved over data of the chart
     */
    @FXML
    private void createHoverNodes(PieChart chart) {
        long totalTime = 0;
        for (PieChart.Data data : chart.getData()) {
            totalTime += data.getPieValue();
        }
        for (PieChart.Data data : chart.getData()) {
            Tooltip.install(data.getNode(), new Tooltip(
                    data.getName() + "\t" + Math.round(data.getPieValue() / totalTime * 100) + "%\n"
                            + (int) (data.getPieValue() / 60) + " Minutes listened"));
            //Adding class on hover
            data.getNode().setOnMouseEntered(event -> data.getNode().getStyleClass().add("onHover"));
            //Removing class on exit
            data.getNode().setOnMouseExited(event -> data.getNode().getStyleClass().remove("onHover"));
        }
    }

}
