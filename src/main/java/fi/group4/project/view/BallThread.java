package fi.group4.project.view;

import javafx.application.Platform;

public class BallThread extends Thread{
    private Display area = null;
    private volatile boolean fRunning = true;
    volatile double dx = 1, dy = 1;
    public BallThread(Display pa){
        area = pa;
    }

    public void setTarget(int x, int y){
        double sqrt = Math.sqrt(x * x + y * y);
        this.dx = sqrt *x;
        this.dy = sqrt *y;

    }

    @Override
    public void run() {
        while (fRunning) {
            Platform.runLater(() -> {
                double targetX = area.getTargetX();
                double targetY = area.getTargetY();
                double ballX = area.getX();
                double ballY = area.getY();

                if(targetX >= 0 && targetY >= 0) {
                    double diffX = targetX - ballX;
                    double diffY = targetY - ballY;
                    double length = Math.sqrt(diffX*diffX + diffY*diffY);

                    if(length > 1) {
                        double speed = 3;
                        double dx = speed * diffX / length;
                        double dy = speed * diffY / length;
                        area.moveBall(dx, dy);
                    }
                }
            });

            try {
                sleep(10);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void terminate(){
        fRunning = false;
    }
}