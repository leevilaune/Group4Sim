package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.*;
import java.util.LinkedList;

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
	private LinkedList<Customer> queue = new LinkedList<>(); // Data Structure used
	private ContinuousGenerator generator;
	private EventList eventList;
	private EventType eventTypeScheduled;
	//Queuestrategy strategy; // option: ordering of the customer
	private boolean reserved = false;


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
	}

	/**
	 * Remove customer from the waiting queue.
	 * Here we calculate also the appropriate measurement values.
	 *
	 * @return Customer retrieved from the waiting queue
	 */
	public Customer removeQueue() {		// Remove serviced customer
		reserved = false;
		return queue.poll();
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
        // The average time spent in servicePoint is gotten from here with a simple getter or having some kind
        // of counter during the run of the program and collects the serviceTimes

        // all servicePoint statistics can be gotten from here by having some if else's based on eventType

        // simplest way to add rollbacks is to add a probability here that chooses the eventType out of the possibilities

		eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock()+serviceTime));
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

	public LinkedList<Customer> getQueue() {
		return queue;
	}

	public void setQueue(LinkedList<Customer> queue) {
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
