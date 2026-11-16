package edu.hitsz.audio;

import edu.hitsz.MusicThread;
import java.io.File;

/**
 * 音频管理器，统一管理背景音乐和音效的播放、循环与停止
 */
public class AudioManager {
    private static MusicThread bgmThread;
    private static MusicThread bossBgmThread;

    private static String resolvePath(String path) {
        File f = new File(path);
        if (f.exists()) {
            return path;
        }
        String alt = System.getProperty("user.dir") + File.separator + path;
        return alt;
    }

    public static void playBgm(String path, boolean loop) {
        stopBgm();
        String p = resolvePath(path);
        bgmThread = new MusicThread(p, loop);
        bgmThread.start();
    }

    public static void stopBgm() {
        if (bgmThread != null) {
            bgmThread.stopMusic();
            bgmThread = null;
        }
    }

    public static void playBossBgm(String path, boolean loop) {
        stopBossBgm();
        String p = resolvePath(path);
        bossBgmThread = new MusicThread(p, loop);
        bossBgmThread.start();
    }

    public static void stopBossBgm() {
        if (bossBgmThread != null) {
            bossBgmThread.stopMusic();
            bossBgmThread = null;
        }
    }

    public static void playEffect(String path) {
        String p = resolvePath(path);
        new MusicThread(p, false).start();
    }
}