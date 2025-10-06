package fi.group4.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "run")
public class SimulationRun {
     @Id
     @GeneratedValue(strategy= GenerationType.IDENTITY)
     private int id;
     private int param1;
     private int param2;
     private int param3;
     private int param4;
     private int param5;
     private long seed;
     private long ts;
     private String distribution;

    public SimulationRun(int param1, int param2, int param3, int param4, int param5, long seed, long ts,String distribution) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
        this.seed = seed;
        this.ts = ts;
        this.distribution = distribution;
    }

    public SimulationRun(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParam1() {
        return param1;
    }

    public void setParam1(int param1) {
        this.param1 = param1;
    }

    public int getParam2() {
        return param2;
    }

    public void setParam2(int param2) {
        this.param2 = param2;
    }

    public int getParam3() {
        return param3;
    }

    public void setParam3(int param3) {
        this.param3 = param3;
    }

    public int getParam4() {
        return param4;
    }

    public void setParam4(int param4) {
        this.param4 = param4;
    }

    public int getParam5() {
        return param5;
    }

    public void setParam5(int param5) {
        this.param5 = param5;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
