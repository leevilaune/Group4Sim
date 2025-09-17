package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.EventList;

import java.util.Arrays;

public class ServicePointController {

    private ServicePoint[] servicePoints;
    private ContinuousGenerator generator;
    private EventList eventList;
    private EventType type;

    public ServicePointController(int amount, ContinuousGenerator generator, EventList eventList, EventType type){
        this.servicePoints = new ServicePoint[amount];
        this.generateServicePoints(amount,generator,eventList,type);
    }
    private void generateServicePoints(int amount,ContinuousGenerator generator, EventList eventList, EventType type){
        for(int i = 0; i<amount;i++){
            this.servicePoints[i] = new ServicePoint(generator,eventList,type);
        }
    }

    public void addQueue(Customer a){
        Arrays.stream(this.servicePoints)
                .filter(sp -> !sp.isReserved())
                .findFirst()
                .ifPresent(sp -> sp.addQueue(a));
    }
    public boolean isReserved(){
        final boolean[] anyIsFree = new boolean[1];
        Arrays.stream(this.servicePoints)
                .forEach(sp -> {
                    if(!sp.isReserved()) anyIsFree[0] = true;
                });
        return anyIsFree[0];
    }
    public Customer removeQueue(){
        Customer[] customer = new Customer[1];
        Arrays.stream(this.servicePoints)
                .filter(ServicePoint::isOnQueue)
                .sorted()
                .findFirst()
                .ifPresent(sp -> {customer[0]=sp.removeQueue();
                });
        return customer[0];
    }

    public boolean isOnQueue() {
        boolean[] isOnQueue = new boolean[1];
        Arrays.stream(this.servicePoints)
                .filter(ServicePoint::isOnQueue)
                .findFirst()
                .ifPresent(sp -> {isOnQueue[0]=true;});
        return isOnQueue[0];
    }

    public void beginService(){
        Arrays.stream(this.servicePoints)
                .filter(ServicePoint::isOnQueue)
                .sorted()
                .findFirst()
                .ifPresent(ServicePoint::beginService);
    }
}

