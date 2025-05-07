package org.example.demo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class PlaySound {
    public static void playExplosionSound(float volume) throws Exception {
        // Tải file âm thanh từ tài nguyên (classpath)
        URL soundURL = PlaySound.class.getResource("/org/example/demo/sounds/explosion.wav");
        if (soundURL == null) {
            System.out.println("Không tìm thấy file âm thanh!");
            return;
        }

        // Tạo AudioInputStream từ URL
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);

        // Mở và phát âm thanh
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);

        // Điều chỉnh âm lượng
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);

        clip.start();

        // Giữ chương trình chạy để âm thanh phát xong (tùy chọn)
        Thread.sleep(clip.getMicrosecondLength() / 1000);
    }

    public static void playPlayerDeathSound(float volume) throws Exception {
        // Tải file âm thanh từ tài nguyên (classpath)
        URL soundURL = PlaySound.class.getResource("/org/example/demo/sounds/playerDeath.wav");
        if (soundURL == null) {
            System.out.println("Không tìm thấy file âm thanh!");
            return;
        }

        // Tạo AudioInputStream từ URL
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);

        // Mở và phát âm thanh
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);

        // Điều chỉnh âm lượng
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);

        clip.start();

        // Giữ chương trình chạy để âm thanh phát xong (tùy chọn)
        Thread.sleep(clip.getMicrosecondLength() / 1000);
    }

    public static void main(String[] args) throws Exception {
        // Gọi hàm để phát âm thanh với âm lượng (0.0 đến 1.0, ngoài khoảng là lỗi)
        playExplosionSound(0.5f); // Âm lượng 50%
        //playPlayerDeathSound(0.7f); // Âm lượng 70%
    }
}