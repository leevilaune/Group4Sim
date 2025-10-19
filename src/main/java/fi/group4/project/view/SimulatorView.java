package fi.group4.project.view;

import fi.group4.project.controller.SimulatorController;
import fi.group4.project.entity.SimulationRun;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import simu.framework.Clock;
import simu.model.EventType;
import simu.model.ServicePoint;
import simu.model.ServicePointController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * The main JavaFX view class for the simulation project.
 * Handles UI creation, scene transitions, and user interaction.
 * Communicates with {@link fi.group4.project.controller.SimulatorController} to start and manage simulations.
 */
public class SimulatorView extends Application {

    private SimulatorController controller;
    private Display d;

    private HashMap<EventType, TextField> queueCounters;
    private HashMap<EventType, TextField> reservedCounters;
    private Label timeLabel;

    private Label arriva = new Label("Arrivals: 0");
    private Label external = new Label("External Presentation: 0");
    private Label internal = new Label("Internal Presentation: 0");
    private Label total = new Label("Total Presentation: 0");

    private boolean dbConnected;
    private ListView<SimulationRun> listView;

    private HashMap<EventType, TextField> queueLength;

    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 500;

    /**
     * Initializes a new {@code SimulatorView} instance and its controller.
     */
    public SimulatorView(){
        this.controller = new SimulatorController(this);
        this.dbConnected = false;
    }

    @Override
    /**
     * Entry point for the JavaFX application.
     * Sets up and displays the initial parameter selection scene.
     *
     * @param stage the primary stage for this application
     */
    public void start(Stage stage) {
        Pane pane = new FlowPane();
        /*
        this.d = new Display(600,200, Color.BLANCHEDALMOND);
        d.setX(500);
        d.setY(300);
        d.drawBall();

        d.addBall(30,50,4,Color.BLANCHEDALMOND);
        d.addBall(500,300,2,Color.AZURE);


         */
        //pane.getChildren().add(d);
        stage.setScene(createParameterScene(stage));
        stage.show();
        stage.setOnCloseRequest(event -> {
            //terminate();
            System.exit(0);
        });
    }

    /**
     * Creates the scene displaying counters, queue information, and control options.
     *
     * @param stage the stage used for scene switching
     * @return a {@link Scene} displaying live simulation metrics
     */
    private Scene createCounterGrid(Stage stage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 20;");

        grid.add(new Label("Event Type"), 0, 0);
        grid.add(new Label("Queue"), 1, 0);
        grid.add(new Label("Reserved"), 2, 0);

        grid.add(new Label("Queue Length"), 3, 0);


        this.queueCounters = new HashMap<>();
        this.reservedCounters = new HashMap<>();
        this.queueLength = new HashMap<>();
        this.timeLabel = new Label("Time:");
        EventType[] labels = {EventType.ARR1, EventType.PLANNING, EventType.IMPLEMENTATION, EventType.TESTING,EventType.REVIEW,EventType.PRESENTATION};
        int i = 1;
        for (EventType s : labels) {
            Label label = new Label(s.name());
            TextField queueField = new TextField();
            TextField reservedField = new TextField();
            queueField.setEditable(false);
            reservedField.setEditable(false);
            this.queueCounters.putIfAbsent(s, queueField);
            this.reservedCounters.putIfAbsent(s, reservedField);


            TextField queLength = new TextField();
            queLength.setEditable(false);
            this.queueLength.putIfAbsent(s, queLength);


            grid.add(label, 0, i);
            grid.add(queueField, 1, i);
            grid.add(reservedField, 2, i);

            grid.add(queLength, 3, i);
            i++;
        }

        Label sliderLabel = new Label("Speed:");
        Slider slider = new Slider(0, 1000, this.controller.getDelay());
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(25);
        slider.setMinorTickCount(4);
        slider.setBlockIncrement(1);

        Button showStats = new Button("Stats");
        showStats.setOnAction(e -> {
            Platform.runLater(() -> stage.setScene(createStatisticsScene(stage)));
        });

        int sliderRow = i;
        grid.add(sliderLabel, 0, sliderRow);
        grid.add(slider, 1, sliderRow, 2, 1);
        grid.add(showStats,1,8);
        grid.add(timeLabel,2,8);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.controller.setSpeed(slider.getValue());
        });

        grid.add(getRerunButton(stage),3,8);



        grid.add(arriva, 0, 9);
        grid.add(external, 1, 9);
        grid.add(internal, 2, 9);
        grid.add(total, 3, 9);


        return new Scene(grid,this.SCREEN_WIDTH,this.SCREEN_HEIGHT);
    }

    /**
     * Builds the parameter input scene allowing users to configure simulation parameters
     * and select a previous run to rerun.
     *
     * @param stage the JavaFX stage
     * @return the parameter configuration {@link Scene}
     */
    private Scene createParameterScene(Stage stage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 20;");

        Label title = new Label("Set Parameters (People Amounts)");
        grid.add(title, 0, 0, 2, 1);

        TextField[] fields = new TextField[6];
        for (int i = 0; i < 6; i++) {
            Label label = new Label("Parameter " + (i + 1) + ":");
            TextField textField = new TextField();
            textField.setPromptText("Enter number");
            textField.setText("1");

            fields[i] = textField;

            grid.add(label, 0, i + 1);
            grid.add(textField, 1, i + 1);
        }

        ComboBox<String> distribution = new ComboBox<>();
        distribution.getItems().addAll(List.of("Normal", "LogNormal", "Uniform"));
        distribution.getSelectionModel().select("Normal");

        Button confirmBtn = getConfirmBtn(stage, fields, distribution);
        grid.add(distribution, 2, 6);

        this.listView = new ListView<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        List<SimulationRun> runs = this.controller.loadRuns();

        if(runs == null || runs.isEmpty()){
            runs = new ArrayList<>();
        }else{
            this.dbConnected = true;
        }

        this.listView.getItems().addAll(runs);
        this.listView.setPlaceholder(new Label("Database can't be connected or is empty"));
        this.listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(SimulationRun run, boolean empty) {
                super.updateItem(run, empty);
                if (empty || run == null) {
                    setText(null);
                } else {
                    setText(formatter.format(Instant.ofEpochSecond(run.getTs())));
                }
            }
        });

        grid.add(confirmBtn, 0, 7);
        grid.add(listView, 0, 8, 3, 1);

        this.listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                SimulationRun sr = newVal;
                fields[0].setText(String.valueOf(sr.getParam1()));
                fields[1].setText(String.valueOf(sr.getParam2()));
                fields[2].setText(String.valueOf(sr.getParam3()));
                fields[3].setText(String.valueOf(sr.getParam4()));
                fields[4].setText(String.valueOf(sr.getParam5()));
                fields[5].setText(String.valueOf(sr.getSeed()));
                distribution.getSelectionModel().select(sr.getDistribution());
            }
        });

        return new Scene(grid, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    /**
     * Creates the confirm button that saves parameters, starts the simulation, and transitions
     * to the counter grid scene.
     *
     * @param stage the JavaFX stage
     * @param fields the text fields containing parameter inputs
     * @param dist the distribution selection combo box
     * @return the configured {@link Button}
     */
    private Button getConfirmBtn(Stage stage, TextField[] fields, ComboBox<String> dist) {
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setOnAction(e -> {
            int[] params = new int[6];
            for (int i = 0; i < 6; i++) {
                try {
                    params[i] = Integer.parseInt(fields[i].getText());
                    System.out.println(params[i]);
                } catch (NumberFormatException ex) {
                    params[i] = 0;
                }
            }
            System.out.println(params[5]);
            SimulationRun savedRun = controller.saveRun(params[0], params[1], params[2], params[3], params[4], params[5],Instant.now().getEpochSecond(), dist.getValue());

            if (savedRun != null) {
                listView.getItems().add(savedRun);
            }
            //this.controller.setParameters(params[0],params[1],params[2],params[3],params[4],params[5]);
            this.controller.startSimulation(params[0],params[1],params[2],params[3],params[4],params[5],dist.getValue());
            Platform.runLater(() -> stage.setScene(createCounterGrid(stage)));
        });
        return confirmBtn;
    }

    /**
     * Creates a button that stops the current simulation and returns to the parameter setup view.
     *
     * @param stage the JavaFX stage
     * @return the rerun {@link Button}
     */
    private Button getRerunButton(Stage stage){

        Button b = new Button("Rerun");
        b.setOnAction(e -> {

            this.controller.terminate();
            stage.setScene(this.createParameterScene(stage));
        });
        return b;
    }

    /**
     * Builds the scene displaying simulation statistics including service point performance metrics.
     *
     * @param stage the JavaFX stage
     * @return a {@link Scene} showing detailed statistics
     */
    public Scene createStatisticsScene(Stage stage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(20);

        grid.add(new Label("Service Point"), 0, 0);
        grid.add(new Label("Response Time"), 1, 0);
        grid.add(new Label("Avg Queue Length"), 2, 0);
        grid.add(new Label("Max Queue Length"), 3, 0);



        int row = 1;
        for (ServicePointController spc : this.controller.getServicePointControllers()) {
            Label spLabel = new Label(spc.getType().toString());
            Label respTime = new Label(String.format("%.2f", spc.getResponseTimeInSp()));
            Label queueLen = new Label(String.format("%.2f", spc.getAverageQueLenghtAtSp()));
            Label maxQue = new Label(String.format("%d", spc.getMaxQue()));

            grid.add(spLabel, 0, row);
            grid.add(respTime, 1, row);
            grid.add(queueLen, 2, row);
            grid.add(maxQue, 3, row);

            row++;

        }

        row++;

        grid.add(new Label("Worker"), 0, row);
        grid.add(new Label("Worker Utilization"), 1, row);
        grid.add(new Label("Service Throughput"), 2, row);
        grid.add(new Label("Avg ServiceTime"), 3, row);

        row++;

        for (ServicePointController spc : this.controller.getServicePointControllers()) {
            int counter = 1;
            for(ServicePoint sp: spc.getServicePoints()){
                Label spWorkerLabel = new Label(spc.getType().toString() + " " + counter);
                Label spWorkerUtulizationLabel = new Label(String.format("%.2f", sp.getServicePointUtilization()));
                Label spServiceThroughput = new Label(String.format("%.2f", sp.getServiceThroughput()));
                Label spServiceTime = new Label(String.format("%.2f", sp.getAverageServiceTime()));

                grid.add(spWorkerLabel, 0, row);
                grid.add(spWorkerUtulizationLabel, 1, row);
                grid.add(spServiceThroughput, 2, row);
                grid.add(spServiceTime, 3, row);

                counter ++;
                row++;
            }

        }



        Button goBack = new Button("Back");
        goBack.setOnAction(e->{
            Platform.runLater(() -> stage.setScene(createCounterGrid(stage)));
        });

        grid.add(goBack,1,row);
        grid.add(getRerunButton(stage),2,row);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        return new Scene(scrollPane,this.SCREEN_WIDTH,this.SCREEN_HEIGHT);
    }

    /**
     * Updates all counter fields in the UI with the latest simulation data.
     *
     * @param servicePointControllers the array of controllers representing service points in the simulation
     */
    public void updateCounters(ServicePointController[] servicePointControllers){
        Arrays.stream(servicePointControllers).forEach(servicePointController -> {
            this.queueCounters.get(servicePointController.getType())
                    .setText(String.valueOf(servicePointController.getTotalQueue()));
        });
        Arrays.stream(servicePointControllers).forEach(servicePointController -> {
            this.reservedCounters.get(servicePointController.getType())
                    .setText(String.valueOf(servicePointController.reservedAmount()));
        });
        this.timeLabel.setText("Time: " +Clock.getInstance().getClock());

        this.arriva.setText("Arrivals: " + controller.getArrivals());
        this.external.setText("External Presentations: " + controller.getExpresentation());
        this.internal.setText("Internal Presentations: " + controller.getInpresentation());
        this.total.setText("Total Presentations: " + (controller.getExpresentation() + controller.getInpresentation()));

        Arrays.stream(servicePointControllers).forEach(servicePointController -> {
            this.queueLength.get(servicePointController.getType())
                    .setText(String.valueOf(servicePointController.getQuelength()));
        });


    }

    /**
     * Stops the animation and terminates any running visual elements.
     */
    public void terminate(){
        this.d.getBalls().forEach(b -> b.setRunning(false));
    }
}
