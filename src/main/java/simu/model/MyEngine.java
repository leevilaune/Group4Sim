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
	private SimulatorController controller;
	private long seed;
	private ContinuousGenerator generator;

    private int arrivals = 0;
    private int expresentation = 0;
    private int inpresentation = 0;

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
			/*

			servicePoints[0] = new ServicePointController(1,new Normal(10, 6), eventList, EventType.DEP1);
			servicePoints[1] = new ServicePointController(1,new Normal(10, 10), eventList, EventType.DEP2);
			servicePoints[2] = new ServicePointController(1,new Normal(5, 3), eventList, EventType.DEP3);
            servicePoints[3] = new ServicePointController(1,new Normal(5, 3), eventList, EventType.DEP4);
            servicePoints[4] = new ServicePointController(1,new Normal(5, 3), eventList, EventType.DEP5);

			*/
			//arrivalProcess = new ArrivalProcess(new Negexp(15, 5), eventList, EventType.ARR1);

	}

	public void generateServicePoints(int planners,
									  int implementators,
									  int testers,
									  int reviewers,
									  int presenters,
									  long seed)
	{
		long realSeed = (seed == 0L) ? 1L : seed;
		System.out.println("Using seed: " + realSeed);
		this.seed = realSeed;
		System.out.println(servicePoints[0]);
		this.generator = new Uniform(0,1,realSeed);

		this.servicePoints[0] = new ServicePointController(planners,new Normal(10, 6,realSeed), eventList, EventType.PLANNING);
		this.servicePoints[1] = new ServicePointController(implementators,new Normal(10, 10,realSeed), eventList, EventType.IMPLEMENTATION);
		this.servicePoints[2] = new ServicePointController(testers,new Normal(5, 3,realSeed), eventList, EventType.TESTING);
		this.servicePoints[3] = new ServicePointController(reviewers,new Normal(5, 3,realSeed), eventList, EventType.REVIEW);
		this.servicePoints[4] = new ServicePointController(presenters,new Normal(5, 3,realSeed), eventList, EventType.PRESENTATION);
		arrivalProcess = new ArrivalProcess(new Negexp(15, realSeed), eventList, EventType.ARR1);

		System.out.println(servicePoints[0]);
		this.setSimulationTime(1000);
		this.setDelay(500);
		//this.start();
		//System.exit(0);
	}

	@Override
	protected void initialize() {	// First arrival in the system
		arrivalProcess.generateNextEvent();
	}

	@Override
	protected void runEvent(Event t) {  // B phase events
		Customer a;
		double r = this.generator.sample();
		Trace.out(Trace.Level.INFO,String.valueOf(r),"samples2.txt");
		switch ((EventType)t.getType()) {
		case ARR1:
            a = new Customer((int) (1+r));
			servicePoints[0].addQueue(a);
            arrivals++;

			arrivalProcess.generateNextEvent();
			break;

		case PLANNING:
			a = servicePoints[0].removeQueue();

            servicePoints[1].addQueue(a);
			break;

		case IMPLEMENTATION:
			a = servicePoints[1].removeQueue();

            if(r < 0.6) {
                //continues normally
                servicePoints[2].addQueue(a);
            }else{
                //rollback
                servicePoints[0].addQueue(a);
            }
			break;

		case TESTING:
			a = servicePoints[2].removeQueue();

            servicePoints[3].addQueue(a);

			break;

        case REVIEW:
            a = servicePoints[3].removeQueue();

            if(r < 0.6) {
                //continues normally
                servicePoints[4].addQueue(a);
            }else{
                //rollback
                servicePoints[1].addQueue(a);
            }
            break;

        case PRESENTATION:
            a = servicePoints[4].removeQueue();
            if(r < 0.5){
                inpresentation++;
                //not sure yet how we use internal and external presentation in simulation
                //internal presentation
            }else{
                //external presentation
                expresentation++;
            }

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

    public int getArrivals() {
        return arrivals;
    }

    public int getExpresentation() {
        return expresentation;
    }

    public int getInpresentation() {
        return inpresentation;
    }
}
