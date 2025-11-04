package com.DevChickens.Arkanoid.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.FloatControl;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp SoundManager quản lý việc tải và phát các file âm thanh.
 */
public class SoundManager {

    /**
     * Một Map để lưu trữ tất cả các Clip âm thanh đã được tải.
     * Key (String): Tên âm thanh định danh.
     * Value (Clip): Đối tượng âm thanh đã được nạp.
     */
    private Map<String, Clip> soundClips;

    /**
     * Hàm khởi tạo.
     * Khởi tạo HashMap để lưu trữ các âm thanh.
     */
    public SoundManager() {
        soundClips = new HashMap<>();
    }

    /**
     * Tải một file âm thanh từ thư mục resources vào bộ nhớ.
     * @param name Tên định danh đặt cho âm thanh.
     * @param resourcePath Đường dẫn tương đối trong thư mục resources
     */
    public void loadSound(String name, String resourcePath) {
        try {
            // Lấy URL của tài nguyên từ classpath
            URL soundUrl = getClass().getClassLoader().getResource(resourcePath);

            // Kiểm tra xem file có tồn tại không
            if (soundUrl == null) {
                // In lỗi nếu không tìm thấy file
                throw new RuntimeException("Không thể tìm thấy file âm thanh tại classpath: " + resourcePath);
            }

            // Mở luồng âm thanh
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundUrl);
            // Lấy một đối tượng Clip
            Clip clip = AudioSystem.getClip();
            // Mở clip với luồng âm thanh (tải dữ liệu vào bộ nhớ)
            clip.open(audioIn);
            // Đóng luồng sau khi đã tải xong
            audioIn.close();
            // Lưu clip vào Map để sử dụng sau
            soundClips.put(name, clip);
            System.out.println("Tải âm thanh thành công: " + resourcePath);

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Lỗi: Định dạng file âm thanh không được hỗ trợ: " + resourcePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Lỗi: Không thể đọc file âm thanh: " + resourcePath);
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("Lỗi: Tài nguyên âm thanh không khả dụng: " + resourcePath);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi tải âm thanh: " + resourcePath);
            e.printStackTrace();
        }
    }

    /**
     * Hàm cài đặt âm lượng cho một Clip.
     * Chuyển đổi âm lượng tuyến tính (0.0 - 1.0) sang Decibel (dB).
     * @param clip Clip cần chỉnh âm.
     * @param linearVolume Âm lượng tuyến tính (từ 0.0f đến 1.0f).
     */
    private void setClipVolume(Clip clip, float linearVolume) {
        if (clip == null) return;

        // Đảm bảo giá trị nằm trong khoảng [0.0, 1.0]
        float volume = Math.max(0.0f, Math.min(1.0f, linearVolume));
        try {
            // Lấy bộ điều khiển âm lượng
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // Xử lý trường hợp tắt tiếng
            if (volume == 0.0f) {
                gainControl.setValue(gainControl.getMinimum()); // Tắt tiếng hoàn toàn
            } else {
                // Công thức chuyển đổi tuyến tính sang Decibel
                float dB = (float) (Math.log10(volume) * 20.0);

                // Giới hạn giá trị dB trong khoảng min/max mà clip cho phép
                dB = Math.max(gainControl.getMinimum(), Math.min(dB, gainControl.getMaximum()));
                gainControl.setValue(dB);
            }
        } catch (IllegalArgumentException e) {
            // Xảy ra nếu MASTER_GAIN không được hỗ trợ
            System.err.println("Không thể chỉnh âm lượng cho: " + clip);
            // e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi set âm lượng: " + e.getMessage());
        }
    }

    /**
     * Phát một âm thanh đã được tải.
     * Âm thanh sẽ được phát một lần từ đầu.
     * @param name Tên định danh của âm thanh.
     */
    public void playSound(String name, float linearVolume) {
        Clip clip = soundClips.get(name);

        if (clip != null) {
            setClipVolume(clip, linearVolume);
            // Dừng clip nếu nó đang chạy
            if (clip.isRunning()) {
                clip.stop();
            }
            // Tua về đầu (frame 0)
            clip.setFramePosition(0);
            // Bắt đầu phát
            clip.start();
        } else {
            System.err.println("Cảnh báo: Không thể phát âm thanh. Không tìm thấy tên: " + name);
        }
    }

    /**
     * Phát lặp lại một âm thanh: nhạc nền.
     * @param name Tên định danh của âm thanh.
     */
    public void loopSound(String name, float linearVolume) {
        Clip clip = soundClips.get(name);

        if (clip != null) {
            setClipVolume(clip, linearVolume);
            // Dừng nếu đang chạy
            if (clip.isRunning()) {
                clip.stop();
            }
            // Tua về đầu
            clip.setFramePosition(0);
            // Bắt đầu lặp vô tận
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.err.println("Cảnh báo: Không thể lặp âm thanh. Không tìm thấy tên: " + name);
        }
    }

    /**
     * Cập nhật âm lượng của một clip đang phát.
     * @param name Tên định danh của âm thanh.
     * @param linearVolume Âm lượng mới.
     */
    public void updateVolume(String name, float linearVolume) {
        Clip clip = soundClips.get(name);
        if (clip != null && clip.isRunning()) {
            setClipVolume(clip, linearVolume);
        }
    }

    /**
     * Dừng một âm thanh đang phát .
     * @param name Tên định danh của âm thanh.
     */
    public void stopSound(String name) {
        Clip clip = soundClips.get(name);

        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0); // Tua về đầu cho lần phát sau
        }
    }
}