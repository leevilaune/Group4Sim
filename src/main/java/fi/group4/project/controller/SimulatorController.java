package fi.group4.project.controller;

import fi.group4.project.view.BallThread;
import fi.group4.project.view.SimulatorView;
import javafx.application.Platform;
import simu.framework.Clock;
import simu.framework.Engine;
import simu.framework.Trace;
import simu.model.MyEngine;
import simu.model.ServicePoint;
import simu.model.ServicePointController;

import java.util.Arrays;
import java.util.List;

public class SimulatorController implements IControllerVtoM, IControllerMtoV{

    private MyEngine engine;
    private SimulatorView view;

    private Thread simulationThread;

    public SimulatorController(SimulatorView view){
        this.engine = new MyEngine(this);
        this.view = view;
    }

    @Override
    public void showEndTime(double time) {

    }

    @Override
    public void visualiseCustomer() {
    }

    @Override
    public void startSimulation(int param1, int param2, int param3, int param4, int param5, long seed) {
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
        this.engine.generateServicePoints(param1,param2,param3,param4,param5,seed);

        for (ServicePointController spc : engine.getServicePoints()) {
            spc.reset();
        }
        Clock.getInstance().setClock(0);

        this.engine.setSimulationTime(1000);
        this.engine.setDelay(1000);

        simulationThread = new Thread(engine);
        simulationThread.start();
    }

    public void setSpeed(double delay){
        this.engine.setDelay((long) delay);
    }

    @Override
    public void increaseSpeed() {
        this.engine.setDelay((long) (this.engine.getDelay()*1.1f));
    }

    @Override
    public void decreaseSpeed() {
        this.engine.setDelay((long) (this.engine.getDelay()*0.9f));
    }

    public void updateCounters(ServicePointController[] servicePointControllers){
        Arrays.stream(servicePointControllers).forEach(System.out::println);
        Platform.runLater(() -> view.updateCounters(servicePointControllers));
    }

    public void terminate() {

        engine.runningNo();

        try {
            simulationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setParameters(int param1, int param2, int param3, int param4, int param5, long seed){
        this.engine.generateServicePoints(param1,param2,param3,param4,param5,seed);
    }
    public ServicePointController[] getServicePointControllers(){
        return this.engine.getServicePoints();
    }

    public double getDelay(){
        return this.engine.getDelay();
    }

    public int getArrivals(){
        return engine.getArrivals();
    }
    public int getExpresentation() {
        return engine.getExpresentation();
    }

    public int getInpresentation() {
        return engine.getInpresentation();
    }
}
