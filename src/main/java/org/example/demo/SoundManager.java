package org.example.demo;

import javafx.scene.media.AudioClip;

public class SoundManager {
    private static AudioClip footstep;
    private static AudioClip playerDeath;
    private static AudioClip getBuffs;
    private static AudioClip newLevel;
    private static AudioClip victory;
    private static AudioClip gameOver;

    static {
        try {
            footstep = new AudioClip(SoundManager.class.getResource("/org/example/demo/sounds/footstep.wav").toExternalForm());
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

    public static void playFootStep(float volume) {
        if (footstep != null) {
            footstep.setVolume(volume); // volume từ 0.0 (im re) đến 1.0 (max volume)
            footstep.play();
        }
    }

    public static void main(String[] args) {
        //playPlayerDeath();
        //playGetBuffs();
        //playNewLevel();
        //playVictory();
        //playGameOver();
    }
}
