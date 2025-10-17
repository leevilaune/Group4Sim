package fi.group4.project.entity;

import jakarta.persistence.*;

/**
 * Represents a simulation run with specific parameters, a random seed, timestamp, and distribution type.
 */
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

    /**
     * Constructs a SimulationRun with the specified parameters.
     *
     * @param param1 the number of planners involved in the simulation
     * @param param2 the number of implementers involved in the simulation
     * @param param3 the number of reviewers involved in the simulation
     * @param param4 the number of testers involved in the simulation
     * @param param5 the number of deployers involved in the simulation
     * @param seed the random seed used for simulation reproducibility
     * @param ts the timestamp when the simulation run was created
     * @param distribution the type of distribution used in the simulation (e.g., normal, uniform)
     */
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

    /**
     * Default constructor for SimulationRun.
     */
    public SimulationRun(){

    }

    /**
     * Returns the unique identifier of this simulation run.
     *
     * @return the simulation run ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this simulation run.
     *
     * @param id the simulation run ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the number of planners involved in the simulation.
     *
     * @return the number of planners
     */
    public int getParam1() {
        return param1;
    }

    /**
     * Sets the number of planners involved in the simulation.
     *
     * @param param1 the number of planners to set
     */
    public void setParam1(int param1) {
        this.param1 = param1;
    }

    /**
     * Returns the number of implementers involved in the simulation.
     *
     * @return the number of implementers
     */
    public int getParam2() {
        return param2;
    }

    /**
     * Sets the number of implementers involved in the simulation.
     *
     * @param param2 the number of implementers to set
     */
    public void setParam2(int param2) {
        this.param2 = param2;
    }

    /**
     * Returns the number of reviewers involved in the simulation.
     *
     * @return the number of reviewers
     */
    public int getParam3() {
        return param3;
    }

    /**
     * Sets the number of reviewers involved in the simulation.
     *
     * @param param3 the number of reviewers to set
     */
    public void setParam3(int param3) {
        this.param3 = param3;
    }

    /**
     * Returns the number of testers involved in the simulation.
     *
     * @return the number of testers
     */
    public int getParam4() {
        return param4;
    }

    /**
     * Sets the number of testers involved in the simulation.
     *
     * @param param4 the number of testers to set
     */
    public void setParam4(int param4) {
        this.param4 = param4;
    }

    /**
     * Returns the number of deployers involved in the simulation.
     *
     * @return the number of deployers
     */
    public int getParam5() {
        return param5;
    }

    /**
     * Sets the number of deployers involved in the simulation.
     *
     * @param param5 the number of deployers to set
     */
    public void setParam5(int param5) {
        this.param5 = param5;
    }

    /**
     * Returns the random seed used for simulation reproducibility.
     *
     * @return the random seed
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Sets the random seed used for simulation reproducibility.
     *
     * @param seed the random seed to set
     */
    public void setSeed(long seed) {
        this.seed = seed;
    }

    /**
     * Returns the distribution type used in the simulation.
     *
     * @return the distribution type (e.g., normal, uniform)
     */
    public String getDistribution() {
        return distribution;
    }

    /**
     * Sets the distribution type used in the simulation.
     *
     * @param distribution the distribution type to set
     */
    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    /**
     * Returns the timestamp when the simulation run was created.
     *
     * @return the creation timestamp
     */
    public long getTs() {
        return ts;
    }

    /**
     * Sets the timestamp when the simulation run was created.
     *
     * @param ts the creation timestamp to set
     */
    public void setTs(long ts) {
        this.ts = ts;
    }
}
