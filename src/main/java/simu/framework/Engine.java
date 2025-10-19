package simu.framework;

import fi.group4.project.controller.IControllerMtoV;
import fi.group4.project.controller.SimulatorController;
import simu.model.ServicePoint;


/**
 * Abstract base class representing the core simulation engine.
 * Handles timing, event execution, and coordination between service points.
 * Subclasses must define initialization, event execution, and result reporting.
 */
public abstract class Engine extends Thread {
    private double simulationTime = 0;

    private long delay = 0;

    private Clock clock;

    protected EventList eventList;

    protected ServicePoint[] servicePoints;

    private SimulatorController controller;

    private boolean running = true;

    /**
     * Constructs a new simulation engine with a shared {@link Clock} and event list.
     *
     * @param controller the controller coordinating between the model and the view
     */
    public Engine(SimulatorController controller) {
        clock = Clock.getInstance();
        eventList = new EventList();
        this.controller = controller;
    }

    /**
     * Sets the maximum simulation time.
     *
     * @param time the duration of the simulation
     */
    public void setSimulationTime(double time) {
        simulationTime = time;
    }

    /**
     * Returns the total simulation time.
     *
     * @return the simulation duration
     */
    public double getSimulationTime(){
        return this.simulationTime;
    }

    /**
     * Sets the delay between each simulation step.
     *
     * @param time delay in milliseconds
     */
    public void setDelay(long time) {
        this.delay = time;
    }

    /**
     * Returns the delay between each simulation step.
     *
     * @return delay in milliseconds
     */
    public long getDelay() {
        return delay;
    }

    /**
     * Main simulation loop.
     * Initializes the simulation, processes events in sequence, and updates counters
     * until the defined simulation time elapses or the engine is stopped.
     */
    @Override
    public void run() {
        initialize(); // creating, e.g., the first event
        running = true;

        while (simulate() && running){
            delay();
            clock.setClock(currentTime());
            runBEvents();
            tryCEvents();
            this.updateCounters();
        }

        results();
    }

    /**
     * Executes all events scheduled for the current simulation time.
     */
    private void runBEvents() {
        while (eventList.getNextEventTime() == clock.getClock()){
            runEvent(eventList.remove());
        }
    }

    /**
     * Checks for idle service points with customers in their queue
     * and starts their service.
     */
    protected void tryCEvents() {
        for (ServicePoint p: servicePoints){
            if (!p.isReserved() && p.isOnQueue()){
                p.beginService();
            }
        }
    }

    /**
     * Returns the time of the next scheduled event.
     *
     * @return next event time
     */
    private double currentTime(){
        return eventList.getNextEventTime();
    }

    /**
     * Determines whether the simulation should continue running.
     *
     * @return true if the clock is below the simulation time limit
     */
    private boolean simulate() {
        Trace.out(Trace.Level.INFO, "Time is: " + clock.getClock());
        return clock.getClock() < simulationTime;
    }

    /**
     * Pauses the simulation thread for the configured delay duration.
     */
    private void delay() {
        Trace.out(Trace.Level.INFO, "Delay " + delay);
        try {
            sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates counter values or other metrics during simulation.
     * This method can be overridden by subclasses.
     */
    public void updateCounters(){
    }

    /**
     * Initializes the simulation state and schedules the first events.
     * Must be implemented by subclasses.
     */
    protected abstract void initialize();

    /**
     * Executes a given simulation event.
     * Must be implemented by subclasses.
     *
     * @param t the event to execute
     */
    protected abstract void runEvent(Event t);

    /**
     * Produces and records the final simulation results.
     * Must be implemented by subclasses.
     */
    protected abstract void results();

    /**
     * Resumes the simulation loop.
     */
    public void runningYes(){
        running = true;
    }

    /**
     * Pauses or stops the simulation loop.
     */
    public void runningNo(){
        running = false;
    }
}