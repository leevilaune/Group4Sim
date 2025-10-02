package fi.group4.project.controller;

import fi.group4.project.view.BallThread;
import fi.group4.project.view.SimulatorView;
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
    public void startSimulation() {
        this.engine = new MyEngine(this);
        this.engine.setSimulationTime(1000);
        Trace.out(Trace.Level.INFO,"SimulatorController.startSimulation(): "+this.engine.getServicePoints()[0]);
        this.engine.start();
        this.engine.setDelay(1000);
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
        this.view.updateCounters(servicePointControllers);
    }

    public void terminate(){
        Clock.getInstance().setClock(1000);
    }

    public void setParameters(int param1, int param2, int param3, int param4, int param5){
        this.engine.generateServicePoints(param1,param2,param3,param4,param5);
    }
    public ServicePointController[] getServicePointControllers(){
        return this.engine.getServicePoints();
    }

    public double getDelay(){
        return this.engine.getDelay();
    }
}
