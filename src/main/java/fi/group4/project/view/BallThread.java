package fi.group4.project.view;

import javafx.application.Platform;
import javafx.scene.paint.Color;

public class BallThread extends Thread {
    private double x, y;
    private Color color;
    private double targetX = -1, targetY = -1;
    private double speed;
    private final Display area;
    private volatile boolean running = true;

    public BallThread(Display area, double x, double y, double speed, Color color) {
        this.area = area;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.color = color;
    }

    @Override
    public void run() {
        while (running) {
            Platform.runLater(() -> {
                if (targetX >= 0 && targetY >= 0) {
                    double dx = targetX - x;
                    double dy = targetY - y;
                    double len = Math.sqrt(dx*dx + dy*dy);

                    if (len > 1) {
                        x += speed * dx / len;
                        y += speed * dy / len;
                        area.drawBalls(); // Display draws all threads
                    }
                }
            });
            try { sleep(10); } catch (InterruptedException ignored) {}
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getTargetX() {
        return targetX;
    }

    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    public void setTarget(double targetX,double targetY){
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public double getTargetY() {
        return targetY;
    }

    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Display getArea() {
        return area;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}