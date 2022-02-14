package gui;

import analysis.ListeningTimeAnalysis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import model.Data;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class ControllerListeningTime implements Initializable {

    @FXML
    DatePicker DateFrom;
    @FXML
    DatePicker DateUntil;
    @FXML
    StackedBarChart<String, Number> Chart;
    @FXML
    CategoryAxis XAxis;
    @FXML
    NumberAxis YAxis;
    @FXML
    Label TotalListeningTime;
    @FXML
    ToggleButton ToggleDaily;
    @FXML
    ToggleButton ToggleMonthly;
    @FXML
    ToggleButton ToggleYearly;

    static XYChart.Series<String, Number> seriesNight = new XYChart.Series<>();
    static XYChart.Series<String, Number> seriesMorning = new XYChart.Series<>();
    static XYChart.Series<String, Number> seriesAfternoon = new XYChart.Series<>();
    static XYChart.Series<String, Number> seriesEvening = new XYChart.Series<>();
    static XYChart.Series<String, Number> seriesMonths = new XYChart.Series<>();
    static XYChart.Series<String, Number> seriesYears = new XYChart.Series<>();

    static ObservableList<String> categoriesDaily = FXCollections.observableArrayList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Average");
    static ObservableList<String> categoriesMonthly = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", "Average");
    static ObservableList<String> categoriesYearly = FXCollections.observableArrayList();

    /**
     * Sets the series names, adds listeners and initializes the chart
     *
     * @param location .
     * @param resources .
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        seriesNight.setName("Night (0 - 6)\t");
        seriesMorning.setName("Morning (6 - 12)\t");
        seriesAfternoon.setName("Afternoon (12 - 18)\t\t");
        seriesEvening.setName("Evening (18 - 24)");
        seriesMonths.setName("Months");
        seriesYears.setName("Years");

        categoriesYearly.addAll(ListeningTimeAnalysis.getYears(DateFrom.getValue(), DateUntil.getValue()));

        //Sets the DatePicker values to the first and last playback imported after a new import
        Data.listeningTimeChangeEvent.addListener(change -> {
            Data.listeningTimeChangeEvent.setValue(false);
            DateFrom.setValue(Data.getFirstPlaybackDate());
            DateUntil.setValue(Data.getLastPlaybackDate());
            seriesNight.getData().clear();
            seriesMorning.getData().clear();
            seriesAfternoon.getData().clear();
            seriesEvening.getData().clear();
            seriesMonths.getData().clear();
            seriesYears.getData().clear();
            TotalListeningTime.setText("0");
            Chart.getData().clear();
            handleRefresh();
            //Dont know why this works or is needed but the event listener stops working without this line
            Data.listeningTimeChangeEvent.getValue();
        });

        ToggleDaily.setSelected(true);
        Chart.setAnimated(false);
        handleToggleDaily();

        ListeningTimeAnalysis.totalListeningTime.addListener(change -> TotalListeningTime.setText(String.valueOf(ListeningTimeAnalysis.totalListeningTime.getValue())));

        Tooltip.install(TotalListeningTime, new Tooltip("in Minutes"));
    }

    /**
     * Handles refresh button presses by calling refreshChart() and reloading the chart if the library is not empty
     */
    @FXML
    private void handleRefresh() {
        System.out.println(">Listening Time Refresh<");
        DateFrom.getStyleClass().remove("date-picker-error");
        DateUntil.getStyleClass().remove("date-picker-error");
        if (DateFrom.getValue() == null || DateUntil.getValue() == null) {
            DateFrom.getStyleClass().add("date-picker-error");
            DateUntil.getStyleClass().add("date-picker-error");
        } else {
            if (DateFrom.getValue().isBefore(DateUntil.getValue())) {   // !Data.library.isEmpty() &&
                refreshChart(DateFrom.getValue(), DateUntil.getValue());
                if (ToggleDaily.isSelected()) handleToggleDaily();
                else if (ToggleMonthly.isSelected()) handleToggleMonthly();
                else if (ToggleYearly.isSelected()) handleToggleYearly();
            } else {
                DateFrom.getStyleClass().add("date-picker-error");
                DateUntil.getStyleClass().add("date-picker-error");
            }
        }
    }

    /**
     * Handles the daily toggle switch by deselecting monthly and yearly and changing the chart data to the daily ones
     */
    @FXML
    private void handleToggleDaily() {
        ToggleDaily.setDisable(true);
        ToggleMonthly.setDisable(false);
        ToggleMonthly.setSelected(false);
        ToggleYearly.setDisable(false);
        ToggleYearly.setSelected(false);
        XAxis.getCategories().clear();
        Chart.getData().clear();
        XAxis.getCategories().addAll(categoriesDaily);
        Chart.getData().addAll(FXCollections.observableArrayList(seriesNight, seriesMorning, seriesAfternoon, seriesEvening));
        createHoverNodes();
    }

    /**
     * Handles the monthly toggle switch by deselecting daily and yearly and changing the chart data to the monthly one
     * (uses complete months and ignores exact dates)
     */
    @FXML
    private void handleToggleMonthly() {
        ToggleMonthly.setDisable(true);
        ToggleDaily.setDisable(false);
        ToggleDaily.setSelected(false);
        ToggleYearly.setDisable(false);
        ToggleYearly.setSelected(false);
        XAxis.getCategories().clear();
        Chart.getData().clear();
        XAxis.getCategories().addAll(categoriesMonthly);
        Chart.getData().addAll(FXCollections.observableArrayList(seriesMonths));
        createHoverNodes();
    }

    /**
     * Handles the yearly toggle switch by deselecting daily and monthly and changing the chart data to the yearly one
     * (uses complete years and ignores exact dates)
     */
    @FXML
    private void handleToggleYearly() {
        ToggleYearly.setDisable(true);
        ToggleDaily.setDisable(false);
        ToggleDaily.setSelected(false);
        ToggleMonthly.setDisable(false);
        ToggleMonthly.setSelected(false);
        categoriesYearly.clear();
        XAxis.getCategories().clear();
        Chart.getData().clear();
        categoriesYearly.addAll(ListeningTimeAnalysis.getYears(DateFrom.getValue(), DateUntil.getValue()));
        XAxis.getCategories().addAll(categoriesYearly);
        Chart.getData().addAll(FXCollections.observableArrayList(seriesYears));
        createHoverNodes();
    }

    /**
     * Creates floating fields that are displayed when the mouse is moved over data of the chart
     */
    @FXML
    private void createHoverNodes() {
        for (XYChart.Series<String, Number> series : Chart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Tooltip.install(data.getNode(), new Tooltip(
                        data.getXValue() + " " + (ToggleDaily.isSelected() ? series.getName() : "") + "\n" +
                                data.getYValue() + " Minutes listened"));
                //Adding class on hover
                data.getNode().setOnMouseEntered(event -> data.getNode().getStyleClass().add("onHover"));
                //Removing class on exit
                data.getNode().setOnMouseExited(event -> data.getNode().getStyleClass().remove("onHover"));
            }
        }
    }

    /**
     * Refreshes the chart by calling ListeningTimeAnalysis.refresh() and updating all series
     *
     * @param from  start date of the chosen period
     * @param until end date of the chosen period
     */
    public static void refreshChart(LocalDate from, LocalDate until) {
        ListeningTimeAnalysis.refresh(from, until);
        seriesNight.getData().clear();
        seriesNight.getData().addAll(
                (new XYChart.Data<>("Monday", ListeningTimeAnalysis.night.get(1))),
                (new XYChart.Data<>("Tuesday", ListeningTimeAnalysis.night.get(2))),
                (new XYChart.Data<>("Wednesday", ListeningTimeAnalysis.night.get(3))),
                (new XYChart.Data<>("Thursday", ListeningTimeAnalysis.night.get(4))),
                (new XYChart.Data<>("Friday", ListeningTimeAnalysis.night.get(5))),
                (new XYChart.Data<>("Saturday", ListeningTimeAnalysis.night.get(6))),
                (new XYChart.Data<>("Sunday", ListeningTimeAnalysis.night.get(7))),
                (new XYChart.Data<>("Average", ListeningTimeAnalysis.night.get(0)))
        );
        seriesMorning.getData().clear();
        seriesMorning.getData().addAll(
                (new XYChart.Data<>("Monday", ListeningTimeAnalysis.morning.get(1))),
                (new XYChart.Data<>("Tuesday", ListeningTimeAnalysis.morning.get(2))),
                (new XYChart.Data<>("Wednesday", ListeningTimeAnalysis.morning.get(3))),
                (new XYChart.Data<>("Thursday", ListeningTimeAnalysis.morning.get(4))),
                (new XYChart.Data<>("Friday", ListeningTimeAnalysis.morning.get(5))),
                (new XYChart.Data<>("Saturday", ListeningTimeAnalysis.morning.get(6))),
                (new XYChart.Data<>("Sunday", ListeningTimeAnalysis.morning.get(7))),
                (new XYChart.Data<>("Average", ListeningTimeAnalysis.morning.get(0)))
        );
        seriesAfternoon.getData().clear();
        seriesAfternoon.getData().addAll(
                (new XYChart.Data<>("Monday", ListeningTimeAnalysis.afternoon.get(1))),
                (new XYChart.Data<>("Tuesday", ListeningTimeAnalysis.afternoon.get(2))),
                (new XYChart.Data<>("Wednesday", ListeningTimeAnalysis.afternoon.get(3))),
                (new XYChart.Data<>("Thursday", ListeningTimeAnalysis.afternoon.get(4))),
                (new XYChart.Data<>("Friday", ListeningTimeAnalysis.afternoon.get(5))),
                (new XYChart.Data<>("Saturday", ListeningTimeAnalysis.afternoon.get(6))),
                (new XYChart.Data<>("Sunday", ListeningTimeAnalysis.afternoon.get(7))),
                (new XYChart.Data<>("Average", ListeningTimeAnalysis.afternoon.get(0)))
        );
        seriesEvening.getData().clear();
        seriesEvening.getData().addAll(
                (new XYChart.Data<>("Monday", ListeningTimeAnalysis.evening.get(1))),
                (new XYChart.Data<>("Tuesday", ListeningTimeAnalysis.evening.get(2))),
                (new XYChart.Data<>("Wednesday", ListeningTimeAnalysis.evening.get(3))),
                (new XYChart.Data<>("Thursday", ListeningTimeAnalysis.evening.get(4))),
                (new XYChart.Data<>("Friday", ListeningTimeAnalysis.evening.get(5))),
                (new XYChart.Data<>("Saturday", ListeningTimeAnalysis.evening.get(6))),
                (new XYChart.Data<>("Sunday", ListeningTimeAnalysis.evening.get(7))),
                (new XYChart.Data<>("Average", ListeningTimeAnalysis.evening.get(0)))
        );
        seriesMonths.getData().clear();
        seriesMonths.getData().addAll(
                (new XYChart.Data<>("January", ListeningTimeAnalysis.months.get(1))),
                (new XYChart.Data<>("February", ListeningTimeAnalysis.months.get(2))),
                (new XYChart.Data<>("March", ListeningTimeAnalysis.months.get(3))),
                (new XYChart.Data<>("April", ListeningTimeAnalysis.months.get(4))),
                (new XYChart.Data<>("May", ListeningTimeAnalysis.months.get(5))),
                (new XYChart.Data<>("June", ListeningTimeAnalysis.months.get(6))),
                (new XYChart.Data<>("July", ListeningTimeAnalysis.months.get(7))),
                (new XYChart.Data<>("August", ListeningTimeAnalysis.months.get(8))),
                (new XYChart.Data<>("September", ListeningTimeAnalysis.months.get(9))),
                (new XYChart.Data<>("October", ListeningTimeAnalysis.months.get(10))),
                (new XYChart.Data<>("November", ListeningTimeAnalysis.months.get(11))),
                (new XYChart.Data<>("December", ListeningTimeAnalysis.months.get(12))),
                (new XYChart.Data<>("Average", ListeningTimeAnalysis.months.get(0)))
        );
        seriesYears.getData().clear();
        ArrayList<String> years = ListeningTimeAnalysis.getYears(from, until);
        for (int i = 0; i < ListeningTimeAnalysis.years.size(); i++) {
            seriesYears.getData().add(new XYChart.Data<>(years.get(i), ListeningTimeAnalysis.years.get(i)));
        }
    }

}
