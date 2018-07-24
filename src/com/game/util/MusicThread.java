package com.game.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.*;
import javazoom.jl.decoder.*;
import javazoom.jl.player.*;

public class MusicThread extends Thread {

    private Player player;
    private String musicName;

    //加载音乐
    public MusicThread(String musicName) {
        this.musicName = musicName;
    }

    //播放音乐
    public void play() {
        try {
            new Player(getClass().getResourceAsStream(musicName)).play();
        } catch (JavaLayerException ex) {
            Logger.getLogger(MusicThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //循环播放音乐
    public void loop() {
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                new Player(new BufferedInputStream(new FileInputStream(musicName))).play();
            } catch (JavaLayerException ex) {
                Logger.getLogger(MusicThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
