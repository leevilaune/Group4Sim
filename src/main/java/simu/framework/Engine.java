package simu.framework;

import simu.model.ServicePoint;

public abstract class Engine extends Thread {
	private double simulationTime = 0;
	private long delay = 0;
	private Clock clock;
	protected EventList eventList;
	protected ServicePoint[] servicePoints;

	public Engine() {
		clock = Clock.getInstance();
		eventList = new EventList();
	}

	public void setSimulationTime(double time) {
		simulationTime = time;
	}

	public void setDelay(long time) {
		this.delay = time;
	}

	public long getDelay() {
		return delay;
	}

	@Override
	public void run() {
		initialize(); // creating, e.g., the first event

		while (simulate()){
			delay();
			clock.setClock(currentTime());
			runBEvents();
			tryCEvents();
		}

		results();
	}

	private void runBEvents() {
		while (eventList.getNextEventTime() == clock.getClock()){
			runEvent(eventList.remove());
		}
	}

	protected void tryCEvents() {
		for (ServicePoint p: servicePoints){
			if (!p.isReserved() && p.isOnQueue()){
				p.beginService();
			}
		}
	}

	private double currentTime(){
		return eventList.getNextEventTime();
	}

	private boolean simulate() {
		Trace.out(Trace.Level.INFO, "Time is: " + clock.getClock());
		return clock.getClock() < simulationTime;
	}

	private void delay() {
		Trace.out(Trace.Level.INFO, "Delay " + delay);
		try {
			sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected abstract void initialize(); 	// Defined in simu.model-package's class who is inheriting the Engine class
	protected abstract void runEvent(Event t);	// Defined in simu.model-package's class who is inheriting the Engine class
	protected abstract void results(); 			// Defined in simu.model-package's class who is inheriting the Engine class
}