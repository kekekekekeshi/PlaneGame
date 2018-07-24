package com.game.util;

import com.game.PlaneGame;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.applet.AudioClip;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JApplet;

public class PlayMusicUtil {

    /**
     * 播放wav
     * @param musicPath
     */
    public static void playWav(URL musicPath) {
        AudioClip christmas = JApplet.newAudioClip(musicPath);
        christmas.play();
    }

    /**
     * 循环播放背景音乐
     */
    public static void playBgm(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Player player = new Player(PlaneGame.class.getResourceAsStream("audio/bgm.mp3"));
                        player.play();
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 播放射击音乐
     */
    public static void playShoot(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Player player = new Player(PlaneGame.class.getResourceAsStream("audio/shoot.mp3"));
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 播放射击音乐
     */
    public static void playAward(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Player player = new Player(PlaneGame.class.getResourceAsStream("audio/award.mp3"));
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 播放射击音乐
     */
    public static void playBom(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Player player = new Player(PlaneGame.class.getResourceAsStream("audio/enemy1_down.mp3"));
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 播放射击音乐
     */
    public static void playClick(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Player player = new Player(PlaneGame.class.getResourceAsStream("audio/btn.mp3"));
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 播放射击音乐
     */
    public static void playOver(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Player player = new Player(PlaneGame.class.getResourceAsStream("audio/game_over.mp3"));
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 播放射击音乐
     */
    public static void playDie(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Player player = new Player(PlaneGame.class.getResourceAsStream("audio/out_porp.mp3"));
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
