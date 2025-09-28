package fi.group4.project.controller;

import fi.group4.project.view.SimulatorView;
import simu.framework.Engine;
import simu.model.MyEngine;

public class SimulatorController implements IControllerVtoM, IControllerMtoV{

    private MyEngine engine;
    private SimulatorView view;

    public SimulatorController(SimulatorView view){
        this.engine = new MyEngine();
        this.engine.setSimulationTime(1000);
        this.engine.start();
        this.engine.setDelay(1000);
        this.view = view;
    }

    @Override
    public void showEndTime(double time) {

    }

    @Override
    public void visualiseCustomer() {

    }

    @Override
    public void startSimulation() {

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
}
