package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import eduni.distributions.Uniform;
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
public class MyEngine extends Engine {
	private ArrivalProcess arrivalProcess;
	private ServicePointController[] servicePoints;




	/**
	 * Service Points and random number generator with different distributions are created here.
	 * We use exponent distribution for customer arrival times and normal distribution for the
	 * service times.
	 */
	public MyEngine() {
		servicePoints = new ServicePointController[4];

			/* more realistic simulation case with variable customer arrival times and service times */
			servicePoints[0] = new ServicePointController(1,new Normal(10, 6), eventList, EventType.DEP1);
			servicePoints[1] = new ServicePointController(1,new Normal(10, 10), eventList, EventType.DEP2);
			servicePoints[2] = new ServicePointController(1,new Normal(5, 3), eventList, EventType.DEP3);
            servicePoints[3] = new ServicePointController(1,new Normal(5, 3), eventList, EventType.DEP4);

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
			servicePoints[0].addQueue(new Customer());
			arrivalProcess.generateNextEvent();
			break;

		case DEP1:
			a = servicePoints[0].removeQueue();
			servicePoints[1].addQueue(a);
			break;

		case DEP2:
			a = servicePoints[1].removeQueue();

            if(Math.random() < 0.9){
                // chance for servicePoints[0].addQueue(a)
                servicePoints[0].addQueue(a);

                //animation logic here?
            }else{
                servicePoints[2].addQueue(a);
                //animation logic here?
            }
			break;

        case DEP3:
            a = servicePoints[2].removeQueue();
            servicePoints[3].addQueue(a);

		case DEP4:
			a = servicePoints[3].removeQueue();

            if(Math.random() < 0.9){
                //placeholder math.randoms
                // chance for rollback servicePoints[1].addQueue(a)
                servicePoints[1].addQueue(a);

            }else{
                a.setRemovalTime(Clock.getInstance().getClock());
                a.reportResults();
            }

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

	@Override
	protected void results() {
		System.out.println("Simulation ended at " + Clock.getInstance().getClock());
		System.out.println("Results ... are currently missing");
	}
}
