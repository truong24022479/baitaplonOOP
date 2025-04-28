//package org.example.demo;
//
//import javafx.scene.media.AudioClip;
//
//public class SoundManager {
//    private static AudioClip explosion;
//    private static AudioClip playerDeath;
//
//    static {
//        try {
//            explosion = new AudioClip(SoundManager.class.getResource("org/example/demo/sounds/explosion.wav").toExternalForm());
//            System.out.println("Explosion Sound Loaded: " + SoundManager.class.getResource("/sounds/explosion.wav"));
//            playerDeath = new AudioClip(SoundManager.class.getResource("org/example/demo/sounds/playerDeath.wav").toExternalForm());
//        } catch (Exception e) {
//            System.err.println("Lỗi khi load âm thanh: " + e.getMessage());
//        }
//    }
//
//    public static void playExplosion() {
//        if (explosion != null) {
//            explosion.play();
//        }
//    }
//
//    public static void playPlayerDeath() {
//        if (playerDeath != null) {
//            playerDeath.play();
//        }
//    }
//}
