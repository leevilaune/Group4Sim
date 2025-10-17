package fi.group4.project.controller;

import fi.group4.project.dao.SimulationRunDao;
import fi.group4.project.entity.SimulationRun;
import fi.group4.project.view.BallThread;
import fi.group4.project.view.SimulatorView;
import javafx.application.Platform;
import simu.framework.Clock;
import simu.framework.Engine;
import simu.framework.Trace;
import simu.model.Customer;
import simu.model.MyEngine;
import simu.model.ServicePoint;
import simu.model.ServicePointController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

public class SimulatorController implements IControllerVtoM, IControllerMtoV{

    private MyEngine engine;
    private SimulatorView view;

    private Thread simulationThread;

    private SimulationRunDao dao;

    /**
     * Creates a new {@code SimulatorController} that connects the view and engine layers.
     *
     * @param view the {@link SimulatorView} instance controlling UI updates
     */
    public SimulatorController(SimulatorView view){
        this.engine = new MyEngine(this);
        this.view = view;
    }

    /**
     * Displays the simulation end time in the view.
     *
     * @param time the time at which the simulation ended
     */
    @Override
    public void showEndTime(double time) {

    }

    /**
     * Triggers visualization for a customer event in the UI.
     * Currently unimplemented.
     */
    @Override
    public void visualiseCustomer() {
    }

    /**
     * Returns a list of previously saved simulation names.
     *
     * @return list of simulation history entries
     */
    public List<String> getHistory(){
        List<String> history = new ArrayList<>(List.of("Simulation A", "Simulation B", "Simulation C"));
        return history;
    }

    /**
     * Initializes and starts a new simulation run with the given parameters.
     * Configures service points, resets global state, and starts the simulation thread.
     *
     * @param param1 number of planning service points
     * @param param2 number of implementation service points
     * @param param3 number of testing service points
     * @param param4 number of reviewing service points
     * @param param5 number of presentation service points
     * @param seed random seed for reproducibility
     * @param distribution distribution type used for event generation
     */
    @Override
    public void startSimulation(int param1, int param2, int param3, int param4, int param5, long seed,String distribution) {
        /*
        if (simulationThread != null && simulationThread.isAlive()) {
            engine.stop();
            try {
                simulationThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        */
        this.engine = new MyEngine(this);
        this.engine.generateServicePoints(param1,param2,param3,param4,param5,seed,distribution);

        for (ServicePointController spc : engine.getServicePoints()) {
            spc.reset();
        }
        Clock.getInstance().setClock(0);
        Customer.reset();
        /**
         * Defines the total runtime for the simulation.
         * The simulation will continue executing events until this time threshold is reached.
         * This duration can be adjusted to control simulation length.
         */
        this.engine.setSimulationTime(1000);
        this.engine.setDelay(1000);

        simulationThread = new Thread(engine);
        simulationThread.start();
    }

    /**
     * Adjusts the simulation speed by setting a custom delay value.
     *
     * @param delay the delay between simulation steps in milliseconds
     */
    public void setSpeed(double delay){
        this.engine.setDelay((long) delay);
    }

    /**
     * Increases the simulation speed by lengthening the delay.
     */
    @Override
    public void increaseSpeed() {
        this.engine.setDelay((long) (this.engine.getDelay()*1.1f));
    }

    /**
     * Decreases the simulation speed by shortening the delay.
     */
    @Override
    public void decreaseSpeed() {
        this.engine.setDelay((long) (this.engine.getDelay()*0.9f));
    }

    /**
     * Updates the counter displays in the view using data from the engine.
     *
     * @param servicePointControllers the array of service point controllers containing live data
     */
    public void updateCounters(ServicePointController[] servicePointControllers){
        Arrays.stream(servicePointControllers).forEach(System.out::println);
        Platform.runLater(() -> view.updateCounters(servicePointControllers));
    }

    /**
     * Stops the running simulation safely and joins the simulation thread.
     */
    public void terminate() {
        engine.runningNo();
        try {
            simulationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates engine parameters for a future simulation run.
     * Currently not used directly in this implementation.
     *
     * @param param1 planners
     * @param param2 implementers
     * @param param3 testers
     * @param param4 reviewers
     * @param param5 presenters
     * @param seed random seed for reproducibility
     */
    public void setParameters(int param1, int param2, int param3, int param4, int param5, long seed){
        //this.engine.generateServicePoints(param1,param2,param3,param4,param5,seed);
    }

    /**
     * Returns all active service point controllers in the simulation.
     *
     * @return array of {@link ServicePointController} objects
     */
    public ServicePointController[] getServicePointControllers(){
        return this.engine.getServicePoints();
    }

    /**
     * Returns the current simulation delay value.
     *
     * @return delay in milliseconds
     */
    public double getDelay(){
        return this.engine.getDelay();
    }

    /**
     * Returns the number of customers that have arrived during the simulation.
     *
     * @return total arrivals count
     */
    public int getArrivals(){
        return engine.getArrivals();
    }

    /**
     * Returns the number of external presentations completed in the simulation.
     *
     * @return external presentation count
     */
    public int getExpresentation() {
        return engine.getExpresentation();
    }

    /**
     * Returns the number of internal presentations completed in the simulation.
     *
     * @return internal presentation count
     */
    public int getInpresentation() {
        return engine.getInpresentation();
    }

    /**
     * Saves a completed simulation configuration and run data to the database.
     *
     * @param param1 planners
     * @param param2 implementers
     * @param param3 testers
     * @param param4 reviewers
     * @param param5 presenters
     * @param seed random seed
     * @param ts timestamp of the simulation
     * @param distribution distribution type used
     * @return the saved {@link SimulationRun} entity
     */
    public SimulationRun saveRun(int param1,int param2, int param3,int param4,int param5, long seed, long ts, String distribution){
        this.engine.saveRun(param1,param2,param3,param4,param5,seed,ts,distribution);
        return new SimulationRun(param1,param2,param3,param4,param5,seed,ts,distribution);
    }

    /**
     * Loads all previously saved simulation runs from the database.
     *
     * @return list of saved {@link SimulationRun} objects
     */
    public List<SimulationRun> loadRuns(){
        return this.engine.loadRuns();
    }
}
