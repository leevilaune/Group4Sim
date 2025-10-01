package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import eduni.distributions.Uniform;
import fi.group4.project.controller.SimulatorController;
import simu.framework.*;
import eduni.distributions.Negexp;

import java.util.Arrays;
import java.util.Random;

/**
 * Main simulator engine.
 *
 * TODO: This is the place where you implement your own simulator
 *
 * Demo simulation case:
 * Simulate three service points, customer goes through all three service points to get serviced
 * 		--> SP1 --> SP2 --> SP3 -->
 */
public class MyEngine extends Engine{
	private ArrivalProcess arrivalProcess;
	private ServicePointController[] servicePoints;
	public SimulatorController controller;


	/**
	 * Service Points and random number generator with different distributions are created here.
	 * We use exponent distribution for customer arrival times and normal distribution for the
	 * service times.
	 */
	public MyEngine(SimulatorController controller) {
		super(controller);
		servicePoints = new ServicePointController[5];
		this.controller = controller;

			/* more realistic simulation case with variable customer arrival times and service times */
			servicePoints[0] = new ServicePointController(1,new Normal(10, 6), eventList, EventType.DEP1);
			servicePoints[1] = new ServicePointController(1,new Normal(10, 10), eventList, EventType.DEP2);
			servicePoints[2] = new ServicePointController(1,new Normal(5, 3), eventList, EventType.DEP3);
            servicePoints[3] = new ServicePointController(1,new Normal(5, 3), eventList, EventType.DEP4);
            servicePoints[4] = new ServicePointController(1,new Normal(5, 3), eventList, EventType.DEP5);

			arrivalProcess = new ArrivalProcess(new Negexp(15, 5), eventList, EventType.ARR1);

	}

	@Override
	protected void initialize() {	// First arrival in the system
		arrivalProcess.generateNextEvent();
	}

	@Override
	protected void runEvent(Event t) {  // B phase events
		Customer a;

		switch ((EventType)t.getType()) {
		case ARR1:
			servicePoints[0].addQueue(new Customer((int) (Math.random()*3)));
			arrivalProcess.generateNextEvent();
			break;

		case DEP1:
			a = servicePoints[0].removeQueue();
			servicePoints[1].addQueue(a);
			break;

		case DEP2:
			a = servicePoints[1].removeQueue();
			servicePoints[2].addQueue(a);
			break;

		case DEP3:
			a = servicePoints[2].removeQueue();
            servicePoints[4].addQueue(a);

			break;

        case DEP4:
            a = servicePoints[3].removeQueue();
            servicePoints[4].addQueue(a);
            break;

        case DEP5:
            a = servicePoints[4].removeQueue();

            a.setRemovalTime(Clock.getInstance().getClock());
            a.reportResults();
            break;
		}

	}

	@Override
	protected void tryCEvents() {
		Trace.out(Trace.Level.INFO, "Checking service points at time " + Clock.getInstance().getClock());
		Arrays.stream(servicePoints).forEach(ServicePointController::printQueues);
		for (ServicePointController p: servicePoints){
			Trace.out(Trace.Level.INFO, "Controller reserved? " + p.isReserved() + ", onQueue? " + p.isOnQueue());

			if (!p.isReserved() && p.isOnQueue()){
				p.beginService();

			}
		}
	}

	public ArrivalProcess getArrivalProcess() {
		return arrivalProcess;
	}

	public void setArrivalProcess(ArrivalProcess arrivalProcess) {
		this.arrivalProcess = arrivalProcess;
	}

	public ServicePointController[] getServicePoints() {
		return servicePoints;
	}

	public void setServicePoints(ServicePointController[] servicePoints) {
		this.servicePoints = servicePoints;
	}

	@Override
	public void updateCounters(){
		this.controller.updateCounters(this.servicePoints);

	}


	@Override
	protected void results() {
		System.out.println("Simulation ended at " + Clock.getInstance().getClock());
		System.out.println("Results ... are currently missing");
	}
}
