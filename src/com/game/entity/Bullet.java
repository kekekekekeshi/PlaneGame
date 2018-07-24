package com.game.entity;

import com.game.PlaneGame;

/**
 * 子弹类
 */
public class Bullet extends Flyer {

    //飞行速度
    private int speed = 3;
    /** 初始化数据 */
    public Bullet(int x,int y){
        this.x = x;
        this.y = y;
        this.image = PlaneGame.hero_bullet;
    }

    /** 移动 */
    @Override
    public void step(){
        y-=speed;
    }

    /** 越界处理 */
    @Override
    public boolean outOfBounds() {
        return y<-height;
    }
}
