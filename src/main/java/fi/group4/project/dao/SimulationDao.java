package fi.group4.project.dao;

import java.sql.*;
import fi.group4.project.datasource.MariaDbConnection;
import fi.group4.project.entity.Simulation;

import java.util.*;

public class SimulationDao {

    public static List<Simulation> getAllSimulations() {
        Connection conn = MariaDbConnection.getConnection();
        String sql = "SELECT id, created_at, parameter_1, parameter_2, parameter_3, parameter_4, parameter_5, parameter_6, distribution FROM simulation";
        List<Simulation> simulations = new ArrayList<Simulation>();

        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt(1);
                String date = rs.getString(2);
                int param1 = rs.getInt(3);
                int param2 = rs.getInt(4);
                int param3 = rs.getInt(5);
                int param4 = rs.getInt(6);
                int param5 = rs.getInt(7);
                long param6 = rs.getLong(8);
                String  distribution = rs.getString(9);
                Simulation sim = new Simulation(id, date, param1, param2, param3, param4, param5, param6, distribution);
                simulations.add(sim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return simulations;
    }


    public static Simulation getSimulation(int id) {
        Connection conn = MariaDbConnection.getConnection();
        String sql = "SELECT parameter_1, parameter_2, parameter_3, parameter_4, parameter_5, parameter_6, distribution FROM simulation WHERE id=?";

        int param1 = 0;
        int param2 = 0;
        int param3 = 0;
        int param4 = 0;
        int param5 = 0;
        long param6 = 0;
        String distribution = null;

        int count = 0;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                count++;
                param1 = rs.getInt(1);
                param2 = rs.getInt(2);
                param3 = rs.getInt(3);
                param4 = rs.getInt(4);
                param5 = rs.getInt(5);
                param6 = rs.getLong(6);
                distribution = rs.getString(7);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (count==1) {
            return new Simulation(param1, param2, param3, param4, param5, param6, distribution);
        }
        else {
            return null;
        }
    }

    public static boolean isSimulationFound(Simulation sim){
        Connection conn = MariaDbConnection.getConnection();
        String sql =    "SELECT * " +
                        "FROM simulation " +
                        "WHERE parameter_1=? AND parameter_2=? AND parameter_3=? " +
                        "AND parameter_4=? AND parameter_5=? AND parameter_6=? AND distribution=?";

        //String sql_exists = "SELECT EXISTS (SELECT 1 FROM simulation WHERE parameter_1=? AND parameter_2=? AND parameter_3=? AND parameter_4=? AND parameter_5=? AND parameter_6=? AND distribution=?)";

        int count = 0;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, sim.getParameter1());
            ps.setInt(2, sim.getParameter2());
            ps.setInt(3, sim.getParameter3());
            ps.setInt(4, sim.getParameter4());
            ps.setInt(5, sim.getParameter5());
            ps.setLong(6, sim.getSeed());
            ps.setString(7, sim.getDistribution());

            ResultSet rs = ps.executeQuery();

            //System.out.println("rs: " + rs);

            while (rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count == 1;
    }

    public static void persist(Simulation sim) {
        Connection conn = MariaDbConnection.getConnection();
        String sql = "INSERT INTO simulation (parameter_1, parameter_2, parameter_3, parameter_4, parameter_5, parameter_6, distribution) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, sim.getParameter1());
            ps.setInt(2, sim.getParameter2());
            ps.setInt(3, sim.getParameter3());
            ps.setInt(4, sim.getParameter4());
            ps.setInt(5, sim.getParameter5());
            ps.setLong(6, sim.getSeed());
            ps.setString(7, sim.getDistribution());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}