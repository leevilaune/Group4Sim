package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.*;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Service Point implements the functionalities, calculations and reporting.
 *
 * TODO: This must be modified to actual implementation. Things to be added:
 *     - functionalities of the service point
 *     - measurement variables added
 *     - getters to obtain measurement values
 *
 * Service point has a queue where customers are waiting to be serviced.
 * Service point simulated the servicing time using the given random number generator which
 * generated the given event (customer serviced) for that time.
 *
 * Service point collects measurement parameters.
 */
public class ServicePoint implements Comparable<ServicePoint>{
	private PriorityQueue<Customer> queue = new PriorityQueue<>(); // Data Structure used
	private ContinuousGenerator generator;
	private EventList eventList;
	private EventType eventTypeScheduled;
	//Queuestrategy strategy; // option: ordering of the customer
	private boolean reserved = false;

    private double totalUsageTime = 0;
    private double totalTaskServiced = 0;

    //wating time includes service time
    private double totalWaitingTimeInSp = 0;
	/**
	 * Create the service point with a waiting queue.
	 *
	 * @param generator Random number generator for service time simulation
	 * @param eventList Simulator event list, needed for the insertion of service ready event
	 * @param type Event type for the service end event
	 */
	public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType type){
		this.eventList = eventList;
		this.generator = generator;
		this.eventTypeScheduled = type;
	}

	/**
	 * Add a customer to the service point queue.
	 *
	 * @param a Customer to be queued
	 */
	public void addQueue(Customer a) {	// The first customer of the queue is always in service
        queue.add(a);
        a.setResponseTimeVariable();
	}

	/**
	 * Remove customer from the waiting queue.
	 * Here we calculate also the appropriate measurement values.
	 *
	 * @return Customer retrieved from the waiting queue
	 */
	public Customer removeQueue() {		// Remove serviced customer
		reserved = false;
        Customer ab = queue.poll();
        totalWaitingTimeInSp += Clock.getInstance().getClock() - ab.getResponseTimeVariable();
		return ab;
	}

	/**
	 * Begins a new service, customer is on the queue during the service
	 *
	 * Inserts a new event to the event list when the service should be ready.
	 */
	public void beginService() {		// Begins a new service, customer is on the queue during the service
		Trace.out(Trace.Level.INFO, "Starting a new service for the customer #" + queue.peek().getId());
		
		reserved = true;

		double serviceTime = generator.sample();
		System.out.println("Service time ="+ serviceTime);
        // The average time spent in servicePoint is gotten from here with a simple getter or having some kind
        // of counter during the run of the program and collects the serviceTimes
        totalUsageTime += serviceTime;
        totalTaskServiced += 1;
        // a way to add rollbacks is to add a probability here that chooses the eventType out of the possibilities



		eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock()+serviceTime));
	}

    public double getServicePointUtilization(){
        //simulation time needs to be added as parameter (1000 is the default total simulation time)
        return totalUsageTime/Clock.getInstance().getClock();
    }
    public double getServiceThroughput(){
        //simulation time needs to be added as parameter(1000 is the default total simulation time)
        return totalTaskServiced/Clock.getInstance().getClock();
    }
    public double getAverageServiceTime(){
        return totalUsageTime/totalTaskServiced;
    }

	/**
	 * Check whether the service point is busy
	 *
	 * @return logical value indicating service state
	 */
	public boolean isReserved(){
		return reserved;
	}

	/**
	 * Check whether there is customers on the waiting queue
	 *
	 * @return logival value indicating queue status
	 */
	public boolean isOnQueue(){
		return queue.size() != 0;
	}

	@Override
	public int compareTo(ServicePoint o) {
        return Double.compare(o.queue.peek().getArrivalTime(), this.queue.peek().getArrivalTime());
	}

	public PriorityQueue<Customer> getQueue() {
		return queue;
	}

	public void setQueue(PriorityQueue<Customer> queue) {
		this.queue = queue;
	}

	public ContinuousGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(ContinuousGenerator generator) {
		this.generator = generator;
	}

	public EventList getEventList() {
		return eventList;
	}

	public void setEventList(EventList eventList) {
		this.eventList = eventList;
	}

	public EventType getEventTypeScheduled() {
		return eventTypeScheduled;
	}

	public void setEventTypeScheduled(EventType eventTypeScheduled) {
		this.eventTypeScheduled = eventTypeScheduled;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

    public double getTotalUsageTime() {
        return totalUsageTime;
    }

    public double getTotalTaskServiced() {
        return totalTaskServiced;
    }

    //wating time includes service time
    public double getTotalWaitingTimeInSp(){
        return totalWaitingTimeInSp;
    }

	public List<String> getCustomerIDs(){
		return queue.stream()
				.map(Customer::toString)
				.toList();
	}

	@Override
	public String toString() {
		return "ServicePoint{" +
				"queue=" + queue +
				", eventList=" + eventList +
				", eventTypeScheduled=" + eventTypeScheduled +
				", reserved=" + reserved +
				'}';
	}
}
