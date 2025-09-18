package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.EventList;
import simu.framework.Trace;

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

    @Override
    public String toString() {
        return "SPController"+this.servicePoints[0].getEventTypeScheduled()+"{" +
                "reserved=" + this.reservedAmount() +
                ", points" + Arrays.toString(this.servicePoints) +
                '}';
    }
}