package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.EventList;
import simu.framework.Trace;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Controls a group of ServicePoint instances, managing their queues and service processes.
 * Provides methods to interact with and retrieve statistics from the managed service points.
 */
public class ServicePointController {

    private ServicePoint[] servicePoints;

    /**
     * Constructs a ServicePointController with a specified number of ServicePoints.
     *
     * @param amount the number of ServicePoints to create
     * @param generator the generator used for service times
     * @param eventList the event list for scheduling events
     * @param type the type of event handled by the service points
     */
    public ServicePointController(int amount, ContinuousGenerator generator, EventList eventList, EventType type) {
        servicePoints = new ServicePoint[amount];
        for (int i = 0; i < amount; i++) {
            servicePoints[i] = new ServicePoint(generator, eventList, type);
        }
    }

    /**
     * Adds a customer to the queue of the first available (non-reserved) ServicePoint.
     * If all are reserved, adds to the first ServicePoint's queue.
     *
     * @param customer the customer to add to a queue
     */
    public void addQueue(Customer customer) {
        for (ServicePoint sp : servicePoints) {
            if (!sp.isReserved()) {
                sp.addQueue(customer);
                return;
            }
        }
        servicePoints[0].addQueue(customer);
    }

    /**
     * Removes and returns the next customer from the queue of the first ServicePoint that has a queue.
     *
     * @return the removed Customer, or null if no ServicePoint has a queued customer
     */
    public Customer removeQueue() {
        for (ServicePoint sp : servicePoints) {
            if (sp.isOnQueue()) {
                return sp.removeQueue();
            }
        }
        return null;
    }

    /**
     * Checks if all ServicePoints are reserved.
     *
     * @return true if all ServicePoints are reserved, false otherwise
     */
    public boolean isReserved() {
        return Arrays.stream(servicePoints).allMatch(ServicePoint::isReserved);
    }

    /**
     * Checks if any ServicePoint has customers in its queue.
     *
     * @return true if any ServicePoint has customers in queue, false otherwise
     */
    public boolean isOnQueue() {
        return Arrays.stream(servicePoints).anyMatch(ServicePoint::isOnQueue);
    }

    /**
     * Begins service at the first ServicePoint that is not reserved and has customers in its queue.
     */
    public void beginService() {
        for (ServicePoint sp : servicePoints) {
            if (!sp.isReserved() && sp.isOnQueue()) {
                sp.beginService();
                return;
            }
        }
    }

    /**
     * Prints information about the queues in the controller and logs it.
     */
    public void printQueues(){
        Trace.out(Trace.Level.INFO,"Events in "+this.servicePoints[0].getEventTypeScheduled());
        Trace.out(Trace.Level.INFO,""+this,"spcontroller.log");
/*
        for(ServicePoint sp : this.servicePoints){
            Trace.out(Trace.Level.INFO,""+this);
        }

 */
    }
    /**
     * Gets the total number of customers in all queues managed by this controller.
     *
     * @return the total number of queued customers
     */
    public int getQuelength(){
        ArrayList<String> tasks = new ArrayList<>();
        for (ServicePoint sp : this.servicePoints){
            tasks.addAll(sp.getCustomerIDs());
        }
        return tasks.size();
    }

    /**
     * Gets the number of ServicePoints that are currently reserved (busy).
     *
     * @return the number of reserved ServicePoints
     */
    public int reservedAmount(){
        return (int) Arrays.stream(this.servicePoints).filter(ServicePoint::isReserved).count();
    }

    /**
     * Gets the EventType of the ServicePoints managed by this controller.
     *
     * @return the EventType scheduled for these ServicePoints
     */
    public EventType getType(){
        return this.servicePoints[0].getEventTypeScheduled();
    }

    /**
     * Gets the list of all customer IDs currently queued in all ServicePoints.
     *
     * @return an ArrayList of customer IDs in all queues
     */
    public ArrayList<String> getTotalQueue(){
        ArrayList<String> tasks = new ArrayList<>();
        for (ServicePoint sp : this.servicePoints){
            tasks.addAll(sp.getCustomerIDs());
        }
        return tasks;
    }


    /**
     * Returns a string representation of this ServicePointController.
     *
     * @return a string describing the controller, its type, and statistics
     */
    @Override
    public String toString() {
        return "SPController"+this.servicePoints[0].getEventTypeScheduled()+"{" +
                "reserved=" + this.reservedAmount() +
                ", points" + Arrays.toString(this.servicePoints) +
                ", pointsAmount" + this.servicePoints.length +

                '}';
    }

    /**
     * Calculates the average response time (queue + service) for all ServicePoints.
     *
     * @return the average response time for all serviced tasks
     */
    public double getResponseTimeInSp(){
        double totalTasksThroughSp = 0;
        //variable below will hod the que time + service time of all sp of one type
        double totalWaitTimeInSp = 0;

        for (ServicePoint sp : this.servicePoints){
            totalTasksThroughSp += sp.getTotalTaskServiced();
            totalWaitTimeInSp += sp.getTotalWaitingTimeInSp();
        }
        return totalWaitTimeInSp/totalTasksThroughSp;
    }

    /**
     * Calculates the average queue length at the ServicePoints over the simulation time.
     *
     * @return the average queue length
     */
    public double getAverageQueLenghtAtSp(){

        double totalWaitTimeInSp = 0;
        for (ServicePoint sp : this.servicePoints){
            totalWaitTimeInSp += sp.getTotalWaitingTimeInSp();
        }
        return totalWaitTimeInSp/ Clock.getInstance().getClock();
    }

    /**
     * Gets the array of ServicePoints managed by this controller.
     *
     * @return the array of ServicePoints
     */
    public ServicePoint[] getServicePoints(){
        return this.servicePoints;
    }

    /**
     * Resets all ServicePoints managed by this controller.
     */
    public void reset() {
        for (ServicePoint sp : servicePoints) {
            sp.reset();
        }
    }

    /**
     * Gets the maximum queue length reached at the first ServicePoint.
     *
     * @return the maximum queue length at the first ServicePoint
     */
    public int getMaxQue(){
        return servicePoints[0].getMaxQue();
    }

}