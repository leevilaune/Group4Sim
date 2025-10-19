package simu.model;

import eduni.distributions.*;
import fi.group4.project.controller.SimulatorController;
import fi.group4.project.dao.SimulationRunDao;
import fi.group4.project.entity.SimulationRun;
import simu.framework.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Core simulation engine for the software development workflow simulation.
 * <p>
 * This engine models the process of a project moving through different phases:
 * Planning → Implementation → Testing → Review → Presentation.
 * Each phase is represented by a {@link ServicePointController}, and customers
 * (representing tasks) flow through them according to stochastic event timing.
 * </p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Configurable service points for different development roles</li>
 *   <li>Randomized processing times using various probability distributions</li>
 *   <li>Rollback logic for failed reviews or implementations</li>
 *   <li>Support for saving and loading simulation configurations</li>
 * </ul>
 *
 * <p>
 * This class extends {@link simu.framework.Engine} and manages the event loop,
 * scheduling, and state transitions of the simulated process.
 * </p>
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


	private SimulationRunDao dao;
	/**
	 * Service Points and random number generator with different distributions are created here.
	 * We use exponent distribution for customer arrival times and normal distribution for the
	 * service times.
	 */
	public MyEngine(SimulatorController controller) {
		super(controller);
		servicePoints = new ServicePointController[5];
		this.dao = new SimulationRunDao();

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

	/**
	 * Generates service points for the simulation with specified numbers of staff for each role,
	 * sets the random seed and distribution for timing, and initializes arrival process.
	 *
	 * @param planners      Number of planners to simulate.
	 * @param implementators Number of implementers to simulate.
	 * @param testers       Number of testers to simulate.
	 * @param reviewers     Number of reviewers to simulate.
	 * @param presenters    Number of presenters to simulate.
	 * @param seed          Seed value for random number generation; if 0, defaults to 1.
	 * @param distribution  Name of the distribution to use ("LogNormal", "Normal", or others for Uniform).
	 */
	public void generateServicePoints(int planners,
									  int implementators,
									  int testers,
									  int reviewers,
									  int presenters,
									  long seed,
									  String distribution)
	{
		long realSeed = (seed == 0L) ? 1L : seed;
		System.out.println("Using seed: " + realSeed);
		this.seed = realSeed;
		System.out.println(servicePoints[0]);
		this.generator = getGenerator(distribution,realSeed,0,1);

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

	/**
	 * Returns a random number generator based on the specified distribution type.
	 *
	 * @param distribution Name of the distribution ("LogNormal", "Normal", or others for Uniform).
	 * @param seed         Seed for the random number generator.
	 * @param mean         Mean value for the distribution.
	 * @param variance     Variance value for the distribution.
	 * @return A {@link ContinuousGenerator} instance for sampling random values.
	 */
	private ContinuousGenerator getGenerator(String distribution, long seed, double mean, double variance){
        return switch (distribution) {
            case "LogNormal" -> new LogNormal(mean, variance, seed);
            case "Normal" -> new Normal(mean, variance, seed);
            default -> new Uniform(mean, variance, seed);
        };
	}

	/**
	 * Initializes the simulation by scheduling the first arrival event.
	 */
	@Override
	protected void initialize() {	// First arrival in the system
		arrivalProcess.generateNextEvent();
	}

	/**
	 * Handles the execution of simulation events as they occur.
	 * Processes arrivals and service completions, moving customers through the workflow,
	 * including rollback logic for failed implementation or review stages.
	 *
	 * @param t The event to process.
	 */
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

	/**
	 * Attempts to initiate service at any service points that are not currently reserved
	 * and have customers waiting in their queue.
	 * This method is called repeatedly to progress the simulation.
	 */
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

	/**
	 * Returns the arrival process responsible for generating customer arrivals.
	 *
	 * @return The current {@link ArrivalProcess} instance.
	 */
	public ArrivalProcess getArrivalProcess() {
		return arrivalProcess;
	}

	/**
	 * Sets the arrival process used to generate customer arrivals.
	 *
	 * @param arrivalProcess The {@link ArrivalProcess} instance to set.
	 */
	public void setArrivalProcess(ArrivalProcess arrivalProcess) {
		this.arrivalProcess = arrivalProcess;
	}

	/**
	 * Returns the array of service point controllers representing different workflow phases.
	 *
	 * @return An array of {@link ServicePointController} instances.
	 */
	public ServicePointController[] getServicePoints() {
		return servicePoints;
	}

	/**
	 * Sets the service point controllers representing different workflow phases.
	 *
	 * @param servicePoints An array of {@link ServicePointController} instances to set.
	 */
	public void setServicePoints(ServicePointController[] servicePoints) {
		this.servicePoints = servicePoints;
	}

	/**
	 * Updates counters in the associated controller to reflect the current state
	 * of service points and simulation progress.
	 */
	@Override
	public void updateCounters(){
		this.controller.updateCounters(this.servicePoints);

	}

    /**
     * Outputs the final results after the simulation completes.
     * Displays the simulation end time and placeholder text for further result analysis.
     * This method can be extended to include statistical summaries or performance metrics.
     */
	@Override
	protected void results() {
		System.out.println("Simulation ended at " + Clock.getInstance().getClock());
		System.out.println("Results ... are currently missing");
	}

    /**
     * Returns the total number of arrivals processed during the simulation.
     *
     * @return The count of arrivals.
     */
    public int getArrivals() {
        return arrivals;
    }

    /**
     * Returns the number of external presentations completed.
     *
     * @return The count of external presentations.
     */
    public int getExpresentation() {
        return expresentation;
    }

    /**
     * Returns the number of internal presentations completed.
     *
     * @return The count of internal presentations.
     */
    public int getInpresentation() {
        return inpresentation;
    }

	/**
	 * Persists a simulation run configuration and metadata to the database.
	 *
	 * @param param1       Number of planners used in the run.
	 * @param param2       Number of implementers used in the run.
	 * @param param3       Number of testers used in the run.
	 * @param param4       Number of reviewers used in the run.
	 * @param param5       Number of presenters used in the run.
	 * @param seed         Seed value used for random number generation.
	 * @param ts           Timestamp of the simulation run.
	 * @param distribution Distribution name used in the simulation.
	 */
	public void saveRun(int param1,int param2, int param3,int param4,int param5, long seed, long ts, String distribution){
		this.dao.persist(new SimulationRun(param1,param2,param3,param4,param5,seed,ts,distribution));
	}

	/**
	 * Loads all previously saved simulation runs from the database.
	 *
	 * @return A list of {@link SimulationRun} entities representing past runs.
	 */
	public List<SimulationRun> loadRuns(){
		return this.dao	.findAll();
	}
}
