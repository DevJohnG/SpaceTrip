package com.example.spacetrip.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;


public class Obstacle {
    private Bitmap image;
    private int x, y;
    private int speed;

    public Obstacle(Bitmap image, int startX, int startY, int speed) {
        this.image = image;
        this.x = startX;
        this.y = startY;
        this.speed = speed;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update(int width, int height) {
        y += speed; // Mueve el meteorito hacia abajo

        // cuando el meteorito sale de la pantalla se vuelve a crear por la parte de arriba
        if (y > height) {
            y = -image.getHeight();
            x = new Random().nextInt(Math.max(1, width - image.getWidth()));
        }
    }

    public boolean isCollision(int shipX, int shipY, int shipWidth, int shipHeight) {
        // Verifica la colisión con la nave
        Rect shipRect = new Rect(shipX, shipY, shipX + shipWidth, shipY + shipHeight);
        Rect obstacleRect = new Rect(x, y, x + image.getWidth(), y + image.getHeight());
        return Rect.intersects(shipRect, obstacleRect);
    }
}