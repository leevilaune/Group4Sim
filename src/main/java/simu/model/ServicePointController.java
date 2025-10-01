package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.EventList;
import simu.framework.Trace;

import java.util.ArrayList;
import java.util.Arrays;

public class ServicePointController {

    private ServicePoint[] servicePoints;

    public ServicePointController(int amount, ContinuousGenerator generator, EventList eventList, EventType type) {
        servicePoints = new ServicePoint[amount];
        for (int i = 0; i < amount; i++) {
            servicePoints[i] = new ServicePoint(generator, eventList, type);
        }
    }

    public void addQueue(Customer customer) {
        for (ServicePoint sp : servicePoints) {
            if (!sp.isReserved()) {
                sp.addQueue(customer);
                return;
            }
        }
        servicePoints[0].addQueue(customer);
    }

    public Customer removeQueue() {
        for (ServicePoint sp : servicePoints) {
            if (sp.isOnQueue()) {
                return sp.removeQueue();
            }
        }
        return null;
    }

    public boolean isReserved() {
        return Arrays.stream(servicePoints).allMatch(ServicePoint::isReserved);
    }

    public boolean isOnQueue() {
        return Arrays.stream(servicePoints).anyMatch(ServicePoint::isOnQueue);
    }

    public void beginService() {
        for (ServicePoint sp : servicePoints) {
            if (!sp.isReserved() && sp.isOnQueue()) {
                sp.beginService();
                return;
            }
        }
    }

    public void printQueues(){
        Trace.out(Trace.Level.INFO,"Events in "+this.servicePoints[0].getEventTypeScheduled());
        Trace.out(Trace.Level.INFO,""+this,"spcontroller.log");
/*
        for(ServicePoint sp : this.servicePoints){
            Trace.out(Trace.Level.INFO,""+this);
        }

 */
    }

    public int reservedAmount(){
        return (int) Arrays.stream(this.servicePoints).filter(ServicePoint::isReserved).count();
    }

    public EventType getType(){
        return this.servicePoints[0].getEventTypeScheduled();
    }

    public ArrayList<String> getTotalQueue(){
        ArrayList<String> tasks = new ArrayList<>();
        for (ServicePoint sp : this.servicePoints){
            tasks.addAll(sp.getCustomerIDs());
        }
        return tasks;
    }


    @Override
    public String toString() {
        return "SPController"+this.servicePoints[0].getEventTypeScheduled()+"{" +
                "reserved=" + this.reservedAmount() +
                ", points" + Arrays.toString(this.servicePoints) +
                '}';
    }

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

    public double getAverageQueLenghtAtSp(){
        //needs total simulationtime as parameter (by default 1000)
        double totalWaitTimeInSp = 0;
        for (ServicePoint sp : this.servicePoints){
            totalWaitTimeInSp += sp.getTotalWaitingTimeInSp();
        }
        return totalWaitTimeInSp/1000;
    }

}