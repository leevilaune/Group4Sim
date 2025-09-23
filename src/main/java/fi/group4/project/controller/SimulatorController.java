package fi.group4.project.controller;

import fi.group4.project.view.SimulatorView;
import simu.framework.Engine;
import simu.model.MyEngine;

public class SimulatorController {

    private Engine engine;
    private SimulatorView view;

    public SimulatorController(SimulatorView view){
        this.engine = new MyEngine();
        this.view = view;
    }
}
