package com.example.spacetrip.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Planet {
    private Bitmap image;
    private int x, y;
    private int speedX, speedY;

    public Planet(Bitmap image, int x, int y, int speedX, int speedY) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.speedX = 1;
        this.speedY = 1;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update(int canvasWidth, int canvasHeight) {
        x += speedX;
        y += speedY;

        // volver que aparezca el planeta si asi lo quisieramos
        if (x > canvasWidth || y > canvasHeight) {
            x = -image.getWidth();
            y = new Random().nextInt(Math.max(1, canvasHeight - image.getHeight()));
        }
    }
}

