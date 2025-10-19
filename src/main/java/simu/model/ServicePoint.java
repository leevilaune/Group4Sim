package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.*;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Represents a service point in the simulation, managing a queue of customers and
 * simulating service times using a random generator.
 *
 * <p>This class tracks metrics such as total usage time, tasks serviced, average
 * service time, throughput, and queue statistics. Each service point can schedule
 * service completion events and handle customers in its queue.</p>
 *
 * <p>Features include:</p>
 * <ul>
 *   <li>Queue management for incoming customers</li>
 *   <li>Service time generation using a specified {@link ContinuousGenerator}</li>
 *   <li>Event scheduling via {@link EventList}</li>
 *   <li>Tracking of performance metrics like utilization, throughput, and waiting times</li>
 *   <li>Methods to reset state, retrieve statistics, and visualize the queue</li>
 * </ul>
 *
 * <p>This class implements {@link Comparable} to allow prioritization based on the
 * next customer's arrival time in the queue.</p>
 */

public class ServicePoint implements Comparable<ServicePoint>{
	private PriorityQueue<Customer> queue = new PriorityQueue<>();
	private ContinuousGenerator generator;
	private EventList eventList;
	private EventType eventTypeScheduled;
	//Queuestrategy strategy; // option: ordering of the customer
	private boolean reserved = false;

    private double totalUsageTime = 0;
    private double totalTaskServiced = 0;

    //wating time includes service time
    private double totalWaitingTimeInSp = 0;

    private int maxQue = 0;
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

        if(queue.size() > maxQue){
            maxQue = queue.size();
        }
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

    /**
     * Returns the utilization of this service point.
     * Utilization is calculated as total usage time divided by the current simulation time.
     *
     * @return the utilization of the service point
     */
    public double getServicePointUtilization(){
        //simulation time needs to be added as parameter (1000 is the default total simulation time)
        return totalUsageTime/Clock.getInstance().getClock();
    }

    /**
     * Returns the throughput of this service point.
     * Throughput is calculated as the number of serviced tasks divided by the current simulation time.
     *
     * @return the throughput of the service point
     */
    public double getServiceThroughput(){
        //simulation time needs to be added as parameter(1000 is the default total simulation time)
        return totalTaskServiced/Clock.getInstance().getClock();
    }

    /**
     * Returns the average service time for customers at this service point.
     *
     * @return the average service time
     */
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

    /**
     * Compares this service point to another based on the arrival time of the customer
     * at the head of their respective queues.
     *
     * @param o the other ServicePoint to compare with
     * @return a negative integer, zero, or a positive integer as this service point's
     *         head customer has a later, equal, or earlier arrival time than the other's
     */
    @Override
    public int compareTo(ServicePoint o) {
        return Double.compare(o.queue.peek().getArrivalTime(), this.queue.peek().getArrivalTime());
    }

	/**
	 * Returns the queue of customers for this service point.
	 *
	 * @return the customer queue
	 */
	public PriorityQueue<Customer> getQueue() {
		return queue;
	}

	/**
	 * Sets the queue of customers for this service point.
	 *
	 * @param queue the customer queue to set
	 */
	public void setQueue(PriorityQueue<Customer> queue) {
		this.queue = queue;
	}

	/**
	 * Returns the service time generator used by this service point.
	 *
	 * @return the continuous generator
	 */
	public ContinuousGenerator getGenerator() {
		return generator;
	}

	/**
	 * Sets the service time generator for this service point.
	 *
	 * @param generator the continuous generator to set
	 */
	public void setGenerator(ContinuousGenerator generator) {
		this.generator = generator;
	}

	/**
	 * Returns the event list used by this service point.
	 *
	 * @return the event list
	 */
	public EventList getEventList() {
		return eventList;
	}

	/**
	 * Sets the event list for this service point.
	 *
	 * @param eventList the event list to set
	 */
	public void setEventList(EventList eventList) {
		this.eventList = eventList;
	}

	/**
	 * Returns the event type scheduled for this service point.
	 *
	 * @return the scheduled event type
	 */
	public EventType getEventTypeScheduled() {
		return eventTypeScheduled;
	}

	/**
	 * Sets the event type scheduled for this service point.
	 *
	 * @param eventTypeScheduled the event type to set
	 */
	public void setEventTypeScheduled(EventType eventTypeScheduled) {
		this.eventTypeScheduled = eventTypeScheduled;
	}

	/**
	 * Sets the reserved state of this service point.
	 *
	 * @param reserved true if reserved, false otherwise
	 */
	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

    /**
     * Returns the total usage time accumulated by this service point.
     *
     * @return the total usage time
     */
    public double getTotalUsageTime() {
        return totalUsageTime;
    }

    /**
     * Returns the total number of tasks serviced by this service point.
     *
     * @return the total number of serviced tasks
     */
    public double getTotalTaskServiced() {
        return totalTaskServiced;
    }

    /**
     * Returns the total waiting time (including service time) accumulated in this service point.
     *
     * @return the total waiting time in the service point
     */
    public double getTotalWaitingTimeInSp(){
        return totalWaitingTimeInSp;
    }

    /**
     * Returns the maximum queue length observed at this service point.
     *
     * @return the maximum queue size
     */
    public int getMaxQue(){
        return maxQue;
    }

	/**
	 * Returns a list of customer IDs currently in the queue.
	 *
	 * @return list of customer IDs as strings
	 */
	public List<String> getCustomerIDs(){
		return queue.stream()
				.map(Customer::toString)
				.toList();
	}

    /**
     * Returns a string representation of this service point, including its queue,
     * event list, scheduled event type, and reservation status.
     *
     * @return a string representation of the service point
     */
    @Override
    public String toString() {
        return "ServicePoint{" +
                "queue=" + queue +
                ", eventList=" + eventList +
                ", eventTypeScheduled=" + eventTypeScheduled +
                ", reserved=" + reserved +
                '}';
    }

    /**
     * Resets the state of this service point, clearing the queue and all measurement parameters.
     */
    public void reset() {
        queue.clear();
        reserved = false;
        totalUsageTime = 0;
        totalTaskServiced = 0;
        totalWaitingTimeInSp = 0;
        maxQue = 0;
    }


}
