package fi.group4.project.view;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class Display extends Canvas {
    private double x = 40;
    private double y = 60;
    private double targetX = -1;
    private double targetY = -1;
    private boolean xCollision = false;
    private boolean yCollision = false;
    private final GraphicsContext gc;
    private double speed;
    private Color color;
    private final List<BallThread> balls = new ArrayList<>();

    public Display(int w, int h, Color color) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        this.color = color;
        this.setOnMouseClicked(e -> {
            for (BallThread b : balls) {
                b.setTarget(e.getX(), e.getY());
            }
        });
    }

    private void clrDisplay() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void addBall(double x, double y, double speed, Color color) {
        BallThread bt = new BallThread(this,x, y, speed,color);
        this.balls.add(bt);
        bt.start();

        drawBalls();
    }

    public void drawBalls() {
        clrDisplay();
        for (BallThread b : this.balls) {
            gc.setFill(b.getColor());
            gc.fillOval(b.getX(), b.getY(), 30, 30);
        }
    }
    public void drawBall() {
        clrDisplay();
        gc.setFill(this.color);
        gc.fillOval(x, y, 30, 30);
    }

    private boolean xCollision(double x) {
        return x < 0 || x > this.getWidth() - 30;
    }

    public boolean xCollision() {
        return xCollision;
    }

    private boolean yCollision(double y) {
        return y < 0 || y > this.getHeight() - 30;
    }

    public boolean yCollision() {
        return yCollision;
    }

    public void moveBall(double dx, double dy) {
        if (!xCollision((int) (x+dx))) {
            x += dx;
            xCollision = false;
        } else
            xCollision = true;

        if (!yCollision((int) (y+dy))) {
            y += dy;
            yCollision = false;
        } else
            yCollision = true;

        drawBall();
    }

    public void newPosition(double x, double y) {
        if (!xCollision(x) && !yCollision(y)) {
            this.x = x;
            this.y = y;
            drawBall();
        }
    }
    public void setTarget(double x, double y) {
        this.targetX = x;
        this.targetY = y;
    }

    public double getTargetX() {
        return targetX;
    }

    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}