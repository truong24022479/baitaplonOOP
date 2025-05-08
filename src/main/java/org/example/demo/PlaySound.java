package org.example.demo;

import javafx.scene.media.AudioClip;
import java.net.URL;

public class PlaySound {
    private static AudioClip themeClip; // Clip cho nhạc nền
    private static AudioClip effectClip; // Clip cho hiệu ứng

    public static void playExplosionSound(double volume) {
        URL soundURL = PlaySound.class.getResource("/org/example/demo/sounds/explosion.wav");
        if (soundURL == null) {
            System.out.println("Không tìm thấy file âm thanh explosion.wav!");
            return;
        }

        // Dừng hiệu ứng cũ (nếu có)
        stopEffectClip();

        effectClip = new AudioClip(soundURL.toExternalForm());
        effectClip.setVolume(volume); // Âm lượng từ 0.0 đến 1.0
        effectClip.play();
        System.out.println("Explosion sound đang phát...");
    }

    public static void playPlayerDeathSound(double volume) {
        URL soundURL = PlaySound.class.getResource("/org/example/demo/sounds/playerDeath.wav");
        if (soundURL == null) {
            System.out.println("Không tìm thấy file âm thanh playerDeath.wav!");
            return;
        }

        stopEffectClip();

        effectClip = new AudioClip(soundURL.toExternalForm());
        effectClip.setVolume(volume);
        effectClip.play();
        System.out.println("Player death sound đang phát...");
    }

    public static void playThemeSound(double volume) {
        URL soundURL = PlaySound.class.getResource("/org/example/demo/sounds/theme_converted.wav");
        if (soundURL == null) {
            System.out.println("Không tìm thấy file âm thanh theme_converted.wav!");
            return;
        }

        // Dừng theme cũ (nếu có)
        stopThemeClip();

        themeClip = new AudioClip(soundURL.toExternalForm());
        themeClip.setVolume(volume);
        themeClip.setCycleCount(AudioClip.INDEFINITE); // Lặp lại vô hạn
        themeClip.play();
        System.out.println("Theme sound đang phát...");
    }

    // Dừng hiệu ứng âm thanh
    private static void stopEffectClip() {
        if (effectClip != null) {
            effectClip.stop();
            effectClip = null;
        }
    }

    // Dừng nhạc nền
    private static void stopThemeClip() {
        if (themeClip != null) {
            themeClip.stop();
            themeClip = null;
        }
    }

    // Dừng tất cả âm thanh
    public static void stopAllSounds() {
        stopEffectClip();
        stopThemeClip();
    }

    public static void main(String[] args) {
        playThemeSound(0.5); // Phát nhạc nền với âm lượng 50%
        try {
            Thread.sleep(2000); // Chờ 2 giây để nghe theme
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        playExplosionSound(1.0); // Phát tiếng nổ với âm lượng 100%
        try {
            Thread.sleep(1000); // Chờ 1 giây
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        playPlayerDeathSound(1); // Phát âm thanh player death với âm lượng 70%
    }
}