package com.example.spacetrip.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.spacetrip.R;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    private Paint paint;
    private Bitmap ship;

    private int shipX;
    private int shipY;

    private int shipWidth, shipHeight;
    private int height = 0;
    private boolean gameOver = false;
    private OnHeightChangedListener onHeightChangedListener;
    private GameControlListener gameControlListener;
    private List<com.example.spacetrip.activity.Obstacle> obstacles;
    private int obstacleSpeed = 3;  // con esto controlo la velocidad
    private MediaPlayer mediaPlayer;
    private MediaPlayer collisionSound;
    private MediaPlayer navigationSound;

    private Planet planet;
    private boolean planetAppeared = false;

    private String username; // Añadido para almacenar el nombre de usuario

    private boolean MessageShown = false; // Variable para controlar el mensaje
    private boolean SMessageShown = false; // Variable para controlar el mensaje
    private boolean LMessageShown = false; // Variable para controlar el mensaje
    private boolean MMessageShown = false; // Variable para controlar el mensaje
    private boolean VMessageShown = false; // Variable para controlar el mensaje
    private boolean MaMessageShown = false; // Variable para controlar el mensaje
    private boolean JuMessageShown = false; // Variable para controlar el mensaje
    private boolean SatMessageShown = false; // Variable para controlar el mensaje
    private boolean UrMessageShown = false; // Variable para controlar el mensaje
    private boolean NeMessageShown = false; // Variable para controlar el mensaje
    private boolean SolMessageShown = false; // Variable para controlar el mensaje

    private SharedPreferences preferences; // Añadido para SharedPreferences
    private static final String PREFS_NAME = "GamePrefs"; // Nombre para SharedPreferences
    private static final String HEIGHT_KEY = "height";

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);

        Bitmap originalShip = BitmapFactory.decodeResource(getResources(), R.drawable.ship);
        int desiredWidth = 100;
        int desiredHeight = (int) (originalShip.getHeight() * ((float) desiredWidth / originalShip.getWidth()));
        ship = Bitmap.createScaledBitmap(originalShip, desiredWidth, desiredHeight, false);

        shipWidth = ship.getWidth();
        shipHeight = ship.getHeight();

        gameThread = new GameThread(getHolder(), this);

        collisionSound = MediaPlayer.create(context, R.raw.choque);
        navigationSound = MediaPlayer.create(context, R.raw.navegacion);

        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        obstacles = new ArrayList<>();
        addObstacle();

        if (navigationSound != null) {
            navigationSound.setLooping(true);
            navigationSound.start();
        }

        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        shipX = (width - ship.getWidth()) / 2;
        shipY = height - ship.getHeight() - 20;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (navigationSound != null) {
            navigationSound.stop();
            navigationSound.release();
            navigationSound = null;
        }

        if (collisionSound != null) {
            collisionSound.release();
            collisionSound = null;
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.WHITE);

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int starX = random.nextInt(canvas.getWidth());
            int starY = random.nextInt(canvas.getHeight());
            canvas.drawCircle(starX, starY, 2, paint);
        }

        if (!gameOver) {
            canvas.drawBitmap(ship, shipX, shipY, null);


            for (Obstacle obstacle : obstacles) {
                obstacle.draw(canvas);
                obstacle.update(canvas.getWidth(), canvas.getHeight());
                if (obstacle.isCollision(shipX, shipY, ship.getWidth(), ship.getHeight())) {
                    gameOver = true;

                    if (collisionSound != null) {
                        collisionSound.start();
                    }

                    if (navigationSound != null) {
                        navigationSound.stop();
                    }


                    // Actualiza puntos totales al finalizar el juego
                //    updatePuntosTotales(username, height);
                    if (gameControlListener != null) {
                        gameControlListener.onGameOver();
                    }

                    break;
                }
            }

            if (height % 1000 == 0) {
                addObstacle();
            }

            if (height == 100 && !planetAppeared) {
                Bitmap planetImage = BitmapFactory.decodeResource(getResources(), R.drawable.tierra);
                int desiredPlanetWidth = 600;
                int desiredPlanetHeight = (int) (planetImage.getHeight() * ((float) desiredPlanetWidth / planetImage.getWidth()));
                Bitmap resizedPlanetImage = Bitmap.createScaledBitmap(planetImage, desiredPlanetWidth, desiredPlanetHeight, false);
                planet = new Planet(resizedPlanetImage, -resizedPlanetImage.getWidth(), new Random().nextInt(canvas.getHeight()), 1, 1);
                planetAppeared = true;

                // Mostrar el mensaje de Mercurio
                MessageShown = true;
            }

            // Mostrar el mensaje de Mercurio
            if (MessageShown) {
                paint.setTextSize(25);
                canvas.drawText("¡Estás saliendo de la orbita terrestre!", canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
            }

            // Ocultar el mensaje y mostrar el planeta después de alcanzar la altura de 3000
            if (height >= 1000 && MessageShown) {
                MessageShown = false;
                planetAppeared = false;
            }


            if (planetAppeared) {
                planet.draw(canvas);
                planet.update(canvas.getWidth(), canvas.getHeight());
            }
            if (height == 1500 && !planetAppeared) {
                Bitmap planetImage = BitmapFactory.decodeResource(getResources(), R.drawable.satelite);
                int desiredPlanetWidth = 300;
                int desiredPlanetHeight = (int) (planetImage.getHeight() * ((float) desiredPlanetWidth / planetImage.getWidth()));
                Bitmap resizedPlanetImage = Bitmap.createScaledBitmap(planetImage, desiredPlanetWidth, desiredPlanetHeight, false);
                planet = new Planet(resizedPlanetImage, -resizedPlanetImage.getWidth(), new Random().nextInt(canvas.getHeight()), 1, 1);
                planetAppeared = true;

                // Mostrar el mensaje de Mercurio
                SMessageShown = true;
            }

            // Mostrar el mensaje de Mercurio
            if (SMessageShown) {
                paint.setTextSize(25);
                canvas.drawText("¡Estás orbitando la estacion espacial internacional!", canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
            }

            // Ocultar el mensaje y mostrar el planeta después de alcanzar la altura de 3000
            if (height >= 2000 && SMessageShown) {
                SMessageShown = false;
                planetAppeared = false;
            }


            if (planetAppeared) {
                planet.draw(canvas);
                planet.update(canvas.getWidth(), canvas.getHeight());
            }
            if (height == 2300 && !planetAppeared) {
                Bitmap planetImage = BitmapFactory.decodeResource(getResources(), R.drawable.luna);
                int desiredPlanetWidth = 300;
                int desiredPlanetHeight = (int) (planetImage.getHeight() * ((float) desiredPlanetWidth / planetImage.getWidth()));
                Bitmap resizedPlanetImage = Bitmap.createScaledBitmap(planetImage, desiredPlanetWidth, desiredPlanetHeight, false);
                planet = new Planet(resizedPlanetImage, -resizedPlanetImage.getWidth(), new Random().nextInt(canvas.getHeight()), 1, 1);
                planetAppeared = true;

                // Mostrar el mensaje de Mercurio
                LMessageShown = true;
            }

            // Mostrar el mensaje de Mercurio
            if (LMessageShown) {
                paint.setTextSize(25);
                canvas.drawText("¡Estás orbitando La Luna!", canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
            }

            // Ocultar el mensaje y mostrar el planeta después de alcanzar la altura de 3000
            if (height >= 2800 && LMessageShown) {
                LMessageShown = false;
                planetAppeared = false;
            }


            if (planetAppeared) {
                planet.draw(canvas);
                planet.update(canvas.getWidth(), canvas.getHeight());
            }
            if (height == 3000 && !planetAppeared) {
                Bitmap planetImage = BitmapFactory.decodeResource(getResources(), R.drawable.mercurio);
                int desiredPlanetWidth = 300;
                int desiredPlanetHeight = (int) (planetImage.getHeight() * ((float) desiredPlanetWidth / planetImage.getWidth()));
                Bitmap resizedPlanetImage = Bitmap.createScaledBitmap(planetImage, desiredPlanetWidth, desiredPlanetHeight, false);
                planet = new Planet(resizedPlanetImage, -resizedPlanetImage.getWidth(), new Random().nextInt(canvas.getHeight()), 1, 1);
                planetAppeared = true;

                // Mostrar el mensaje de Mercurio
                MMessageShown = true;
            }

            // Mostrar el mensaje de Mercurio
            if (MMessageShown) {
                paint.setTextSize(25);
                canvas.drawText("¡Estás orbitando Mercurio!", canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
            }

            // Ocultar el mensaje y mostrar el planeta después de alcanzar la altura de 3000
            if (height >= 3600 && MMessageShown) {
                MMessageShown = false;
                planetAppeared = false;
            }

            if (planetAppeared) {
                planet.draw(canvas);
                planet.update(canvas.getWidth(), canvas.getHeight());
            }
            if (height == 4000 && !planetAppeared) {
                Bitmap planetImage = BitmapFactory.decodeResource(getResources(), R.drawable.venus);
                int desiredPlanetWidth = 300;
                int desiredPlanetHeight = (int) (planetImage.getHeight() * ((float) desiredPlanetWidth / planetImage.getWidth()));
                Bitmap resizedPlanetImage = Bitmap.createScaledBitmap(planetImage, desiredPlanetWidth, desiredPlanetHeight, false);
                planet = new Planet(resizedPlanetImage, -resizedPlanetImage.getWidth(), new Random().nextInt(canvas.getHeight()), 1, 1);
                planetAppeared = true;

                // Mostrar el mensaje de Mercurio
                VMessageShown = true;
            }

            // Mostrar el mensaje de Mercurio
            if (VMessageShown) {
                paint.setTextSize(25);
                canvas.drawText("¡Estás orbitando Venus!", canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
            }

            // Ocultar el mensaje y mostrar el planeta después de alcanzar la altura de 3000
            if (height >= 4500 && VMessageShown) {
                VMessageShown = false;
                planetAppeared = false;
            }


            if (planetAppeared) {
                planet.draw(canvas);
                planet.update(canvas.getWidth(), canvas.getHeight());
            }
            if (height == 4800 && !planetAppeared) {
                Bitmap planetImage = BitmapFactory.decodeResource(getResources(), R.drawable.marte);
                int desiredPlanetWidth = 300;
                int desiredPlanetHeight = (int) (planetImage.getHeight() * ((float) desiredPlanetWidth / planetImage.getWidth()));
                Bitmap resizedPlanetImage = Bitmap.createScaledBitmap(planetImage, desiredPlanetWidth, desiredPlanetHeight, false);
                planet = new Planet(resizedPlanetImage, -resizedPlanetImage.getWidth(), new Random().nextInt(canvas.getHeight()), 1, 1);
                planetAppeared = true;

                // Mostrar el mensaje de Mercurio
                MaMessageShown = true;
            }

            // Mostrar el mensaje de Mercurio
            if (MaMessageShown) {
                paint.setTextSize(25);
                canvas.drawText("¡Estás Orbitando Marte!", canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
            }

            // Ocultar el mensaje y mostrar el planeta después de alcanzar la altura de 3000
            if (height >= 5400 && MaMessageShown) {
                MaMessageShown = false;
                planetAppeared = false;
            }


            if (planetAppeared) {
                planet.draw(canvas);
                planet.update(canvas.getWidth(), canvas.getHeight());
            }
            if (height == 5700 && !planetAppeared) {
                Bitmap planetImage = BitmapFactory.decodeResource(getResources(), R.drawable.jupiter);
                int desiredPlanetWidth = 300;
                int desiredPlanetHeight = (int) (planetImage.getHeight() * ((float) desiredPlanetWidth / planetImage.getWidth()));
                Bitmap resizedPlanetImage = Bitmap.createScaledBitmap(planetImage, desiredPlanetWidth, desiredPlanetHeight, false);
                planet = new Planet(resizedPlanetImage, -resizedPlanetImage.getWidth(), new Random().nextInt(canvas.getHeight()), 1, 1);
                planetAppeared = true;

                // Mostrar el mensaje de Mercurio
                JuMessageShown = true;
            }

            // Mostrar el mensaje de Mercurio
            if (JuMessageShown) {
                paint.setTextSize(25);
                canvas.drawText("¡Estás Orbitando Jupiter!", canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
            }

            // Ocultar el mensaje y mostrar el planeta después de alcanzar la altura de 3000
            if (height >= 6200 && JuMessageShown) {
                JuMessageShown = false;
                planetAppeared = false;
            }


            if (planetAppeared) {
                planet.draw(canvas);
                planet.update(canvas.getWidth(), canvas.getHeight());
            }
            if (height == 6500 && !planetAppeared) {
                Bitmap planetImage = BitmapFactory.decodeResource(getResources(), R.drawable.saturno);
                int desiredPlanetWidth = 300;
                int desiredPlanetHeight = (int) (planetImage.getHeight() * ((float) desiredPlanetWidth / planetImage.getWidth()));
                Bitmap resizedPlanetImage = Bitmap.createScaledBitmap(planetImage, desiredPlanetWidth, desiredPlanetHeight, false);
                planet = new Planet(resizedPlanetImage, -resizedPlanetImage.getWidth(), new Random().nextInt(canvas.getHeight()), 1, 1);
                planetAppeared = true;

                // Mostrar el mensaje de Mercurio
                SatMessageShown = true;
            }

            // Mostrar el mensaje de Mercurio
            if (SatMessageShown) {
                paint.setTextSize(25);
                canvas.drawText("¡Estás Orbitando Saturno!", canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
            }

            // Ocultar el mensaje y mostrar el planeta después de alcanzar la altura de 3000
            if (height >= 7000 && SatMessageShown) {
                SatMessageShown = false;
                planetAppeared = false;
            }


            if (planetAppeared) {
                planet.draw(canvas);
                planet.update(canvas.getWidth(), canvas.getHeight());
            }
            if (height == 7500 && !planetAppeared) {
                Bitmap planetImage = BitmapFactory.decodeResource(getResources(), R.drawable.urano);
                int desiredPlanetWidth = 300;
                int desiredPlanetHeight = (int) (planetImage.getHeight() * ((float) desiredPlanetWidth / planetImage.getWidth()));
                Bitmap resizedPlanetImage = Bitmap.createScaledBitmap(planetImage, desiredPlanetWidth, desiredPlanetHeight, false);
                planet = new Planet(resizedPlanetImage, -resizedPlanetImage.getWidth(), new Random().nextInt(canvas.getHeight()), 1, 1);
                planetAppeared = true;

                // Mostrar el mensaje de Mercurio
                UrMessageShown = true;
            }

            // Mostrar el mensaje de Mercurio
            if (UrMessageShown) {
                paint.setTextSize(25);
                canvas.drawText("¡Estás Orbitando Urano!", canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
            }

            // Ocultar el mensaje y mostrar el planeta después de alcanzar la altura de 3000
            if (height >= 8000 && UrMessageShown) {
                UrMessageShown = false;
                planetAppeared = false;
            }
            if (planetAppeared) {
                planet.draw(canvas);
                planet.update(canvas.getWidth(), canvas.getHeight());
            }
            if (height == 8500 && !planetAppeared) {
                Bitmap planetImage = BitmapFactory.decodeResource(getResources(), R.drawable.neptuno);
                int desiredPlanetWidth = 300;
                int desiredPlanetHeight = (int) (planetImage.getHeight() * ((float) desiredPlanetWidth / planetImage.getWidth()));
                Bitmap resizedPlanetImage = Bitmap.createScaledBitmap(planetImage, desiredPlanetWidth, desiredPlanetHeight, false);
                planet = new Planet(resizedPlanetImage, -resizedPlanetImage.getWidth(), new Random().nextInt(canvas.getHeight()), 1, 1);
                planetAppeared = true;

                // Mostrar el mensaje de Mercurio
                NeMessageShown = true;
            }

            // Mostrar el mensaje de Mercurio
            if (NeMessageShown) {
                paint.setTextSize(25);
                canvas.drawText("¡Estás Orbitando Neptuno!", canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
            }

            // Ocultar el mensaje y mostrar el planeta después de alcanzar la altura de 3000
            if (height >= 9000 && NeMessageShown) {
                NeMessageShown = false;
                planetAppeared = false;
            }

            if (planetAppeared) {
                planet.draw(canvas);
                planet.update(canvas.getWidth(), canvas.getHeight());
            }
            if (height == 9500 && !planetAppeared) {
                Bitmap planetImage = BitmapFactory.decodeResource(getResources(), R.drawable.sol);
                int desiredPlanetWidth = 300;
                int desiredPlanetHeight = (int) (planetImage.getHeight() * ((float) desiredPlanetWidth / planetImage.getWidth()));
                Bitmap resizedPlanetImage = Bitmap.createScaledBitmap(planetImage, desiredPlanetWidth, desiredPlanetHeight, false);
                planet = new Planet(resizedPlanetImage, -resizedPlanetImage.getWidth(), new Random().nextInt(canvas.getHeight()), 1, 1);
                planetAppeared = true;

                // Mostrar el mensaje de Mercurio
                SolMessageShown = true;
            }

            // Mostrar el mensaje de Mercurio
            if (SolMessageShown) {
                paint.setTextSize(25);
                canvas.drawText("¡Estas Orbitando el Sol", canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
            }

            // Ocultar el mensaje y mostrar el planeta después de alcanzar la altura de 3000
            if (height >= 10000 && SolMessageShown) {
                SolMessageShown = false;
                planetAppeared = false;
            }

            if (planetAppeared) {
                planet.draw(canvas);
                planet.update(canvas.getWidth(), canvas.getHeight());
            }

            // Actualiza el valor de height y lo guarda en SharedPreferences
            height++;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(HEIGHT_KEY, height);
            editor.apply();

            if (onHeightChangedListener != null) {
                onHeightChangedListener.onHeightChanged(height);
            }
        } else {
            paint.setTextSize(100);
            canvas.drawText("Game Over", canvas.getWidth() / 4, canvas.getHeight() / 2, paint);

        }
    }

    private void addObstacle() {
        Bitmap obstacleImage = BitmapFactory.decodeResource(getResources(), R.drawable.meteorito);
        int desiredObstacleWidth = 80;
        int desiredObstacleHeight = (int) (obstacleImage.getHeight() * ((float) desiredObstacleWidth / obstacleImage.getWidth()));
        Bitmap resizedObstacleImage = Bitmap.createScaledBitmap(obstacleImage, desiredObstacleWidth, desiredObstacleHeight, false);
        int startX = new Random().nextInt(Math.max(1, getWidth() - resizedObstacleImage.getWidth()));
        int startY = -resizedObstacleImage.getHeight();
        obstacles.add(new Obstacle(resizedObstacleImage, startX, startY, obstacleSpeed));
    }

    public void setOnHeightChangedListener(OnHeightChangedListener listener) {
        onHeightChangedListener = listener;
    }

    public interface OnHeightChangedListener {
        void onHeightChanged(int height);
    }

    public void setGameControlListener(GameControlListener listener) {
        gameControlListener = listener;
    }

    public interface GameControlListener {
        void onGameOver();
    }

    public void updateShipPosition(int x, int y) {
        shipX = x;
        shipY = y;
        postInvalidate();
    }

    public void resetGame() {
        height = 0;
        gameOver = false;
        shipX = (getWidth() - ship.getWidth()) / 2;
        shipY = getHeight() - ship.getHeight() - 20;

        obstacles.clear();
        addObstacle();

        planetAppeared = false;
        MessageShown = false;
        releaseMediaPlayers();

        // Reinicializar los MediaPlayers
        navigationSound = MediaPlayer.create(getContext(), R.raw.navegacion);
        if (navigationSound != null) {
            navigationSound.setLooping(true);
            navigationSound.start();
        }

        collisionSound = MediaPlayer.create(getContext(), R.raw.choque);

        postInvalidate();

        if (!gameThread.isAlive()) {
            gameThread = new GameThread(getHolder(), this);
            gameThread.setRunning(true);
            gameThread.start();
        }
    }

    public int getShipWidth() {
        return shipWidth;
    }

    public int getShipHeight() {
        return shipHeight;
    }

    private class GameThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private GameView gameView;
        private boolean running;

        public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
            this.surfaceHolder = surfaceHolder;
            this.gameView = gameView;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            while (running) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        if (canvas != null) {
                            gameView.draw(canvas);
                        }
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    // Método para establecer el nombre de usuario
    public void setUsername(String username) {
        this.username = username;
    }
    public void releaseMediaPlayers() {
        if (navigationSound != null) {
            navigationSound.stop();
            navigationSound.release();
            navigationSound = null;
        }

        if (collisionSound != null) {
            collisionSound.release();
            collisionSound = null;
        }
    }


}
