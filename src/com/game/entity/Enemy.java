package com.game.entity;

import com.game.PlaneGame;

import java.util.Random;

public class Enemy extends Flyer{

    private int speed = 3;  //移动步骤

    /** 初始化数据 */
    public Enemy(){
        this.image = PlaneGame.enemy;
        width = image.getWidth();
        height = image.getHeight();
        y = -height;
        Random rand = new Random();
        x = rand.nextInt(PlaneGame.WIDTH - width);
    }

    /** //越界处理 */
    @Override
    public  boolean outOfBounds() {
        return y>PlaneGame.HEIGHT;
    }

    /** 移动 */
    @Override
    public void step() {
        y += speed;
    }

    public int getScore() {
        return 5;
    }
}
