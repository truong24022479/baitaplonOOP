package org.example.demo;

import javafx.scene.media.AudioClip;

public class SoundManager {
    private static AudioClip explosion;
    private static AudioClip playerDeath;
    private static AudioClip getBuffs;
    private static AudioClip newLevel;
    private static AudioClip victory;
    private static AudioClip gameOver;

    static {
        try {
            explosion = new AudioClip(SoundManager.class.getResource("/org/example/demo/sounds/explosion.wav").toExternalForm());
            System.out.println("Explosion Sound Loaded: " + SoundManager.class.getResource("/sounds/explosion.wav"));
            playerDeath = new AudioClip(SoundManager.class.getResource("/org/example/demo/sounds/playerDeath.wav").toExternalForm());
            getBuffs = new AudioClip(SoundManager.class.getResource("/org/example/demo/sounds/getBuffs.wav").toExternalForm());
            newLevel = new AudioClip(SoundManager.class.getResource("/org/example/demo/sounds/newLevel.wav").toExternalForm());
            victory = new AudioClip(SoundManager.class.getResource("/org/example/demo/sounds/victory.wav").toExternalForm());
            gameOver = new AudioClip(SoundManager.class.getResource("/org/example/demo/sounds/gameOver.wav").toExternalForm());
        } catch (Exception e) {
            System.err.println("Lỗi khi load âm thanh: " + e.getMessage());
        }
    }

    public static void playExplosion() {
        if (explosion != null) {
            explosion.play();
        }
    }

    public static void playPlayerDeath() {
        if (playerDeath != null) {
            playerDeath.play();
        }
    }

    public static void playGetBuffs() {
        if (getBuffs != null) {
            getBuffs.play();
        }
    }

    public static void playNewLevel() {
        if (newLevel != null) {
            newLevel.play();
        }
    }

    public static void playVictory() {
        if (victory != null) {
            victory.play();
        }
    }

    public static void playGameOver() {
        if (gameOver != null) {
            gameOver.play();
        }
    }
}
