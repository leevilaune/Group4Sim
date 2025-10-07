package fi.group4.project.entity;

public class Simulation {
    private int id, parameter1, parameter2, parameter3, parameter4, parameter5;
    private long seed;
    private String date, distribution;

    public Simulation(int parameter1, int parameter2, int parameter3, int parameter4, int parameter5, long seed, String distribution) {
        this.id = 0;
        this.date = null;
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
        this.parameter3 = parameter3;
        this.parameter4 = parameter4;
        this.parameter5 = parameter5;
        this.seed = seed;
        this.distribution = distribution;
    }

    public Simulation(int id, String date,int parameter1, int parameter2, int parameter3, int parameter4, int parameter5, long seed, String distribution) {
        this.id = id;
        this.date = date;
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
        this.parameter3 = parameter3;
        this.parameter4 = parameter4;
        this.parameter5 = parameter5;
        this.seed = seed;
        this.distribution = distribution;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getParameter1() {
        return parameter1;
    }
    public void setParameter1(int parameter1) {
        this.parameter1 = parameter1;
    }

    public int getParameter2() {
        return parameter2;
    }

    public void setParameter2(int parameter2) {
        this.parameter2 = parameter2;
    }

    public int getParameter3() {
        return parameter3;
    }

    public void setParameter3(int parameter3) {
        this.parameter3 = parameter3;
    }

    public int getParameter4() {
        return parameter4;
    }

    public void setParameter4(int parameter4) {
        this.parameter4 = parameter4;
    }

    public int getParameter5() {
        return parameter5;
    }

    public void setParameter5(int parameter5) {
        this.parameter5 = parameter5;
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

    public String toString(){
        return ("Id: " + this.getId() + ", Date: " + this.getDate() +
                ", \nParameters: " + this.getParameter1() + " " + this.getParameter2() + " " + this.getParameter3() + " " +
                this.getParameter4() + " " + this.getParameter5() + " " + this.getSeed() + ", Dist: " + this.getDistribution());
    }
}
