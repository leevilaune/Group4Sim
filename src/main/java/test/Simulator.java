package test;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import fi.group4.project.controller.SimulatorController;
import simu.framework.Engine;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.MyEngine;

/**
 * Command-line type User Interface
 *
 * With setTraceLevel() you can control the number of diagnostic messages printed to the console.

public class Simulator {
	public static void main(String[] args) throws InterruptedException {
		Trace.setTraceLevel(Level.INFO);

		MyEngine m = new MyEngine(null);
		m.generateServicePoints(1,1,1,1,1,1);
		m.start();
		m.join();
	}
}
 */