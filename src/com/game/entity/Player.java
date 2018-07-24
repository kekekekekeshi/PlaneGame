package com.game.entity;

import com.game.PlaneGame;
import com.game.util.KeyBufferUtil;

import java.awt.image.BufferedImage;

public class Player extends Flyer {

    private BufferedImage[] images = {};  //英雄机图片
    private int index = 0;                //英雄机图片切换索引

    private int doubleFire;   //双倍火力
    private int life;   //命
    private int speed;

    /** 初始化数据 */
    public Player(){
        life = 3;   //初始3条命
        doubleFire = 0;   //初始火力为0
        speed = 5;
        images = new BufferedImage[]{PlaneGame.hero_0, PlaneGame.hero_1}; //英雄机图片数组
        image = PlaneGame.hero_0;   //初始为hero0图片
        width = image.getWidth();
        height = image.getHeight();
        x = (PlaneGame.WIDTH-this.width)/2;
        y = PlaneGame.HEIGHT-this.height;
    }

    /** 获取双倍火力 */
    public int isDoubleFire() {
        return doubleFire;
    }

    /** 设置双倍火力 */
    public void setDoubleFire(int doubleFire) {
        this.doubleFire = doubleFire;
    }

    /** 增加火力 */
    public void addDoubleFire(){
        doubleFire = 40;
    }

    /** 增命 */
    public void addLife(){  //增命
        life++;
    }

    /** 减命 */
    public void subtractLife(){   //减命
        life--;
    }

    /** 获取命 */
    public int getLife(){
        return life;
    }

    /** 当前物体移动了一下，相对距离，x,y鼠标位置  */
    public void moveTo(){
        if (KeyBufferUtil.contains(65)||KeyBufferUtil.contains(37)){//判断是否存在A  向左
            this.x -= this.speed;  //把X坐标减去速度
            if(this.x < 0||this.x == 0) this.x = 0; //不允许英雄机出界
        }
        if (KeyBufferUtil.contains(68)||KeyBufferUtil.contains(39)){  //判断是否存在D  向右
            this.x += this.speed;  //把X坐标加上速度
            if(this.x > PlaneGame.WIDTH-width||this.x == PlaneGame.WIDTH) this.x = PlaneGame.WIDTH-width;//不允许英雄机出界
        }
        if (KeyBufferUtil.contains(83)||KeyBufferUtil.contains(40)){  //判断是否存在S  向下
            this.y += this.speed;  //把Y坐标加上速度
            if(this.y > PlaneGame.HEIGHT-height||this.y == PlaneGame.HEIGHT-height) this.y = PlaneGame.HEIGHT-height;//不允许英雄机出界
        }
        if (KeyBufferUtil.contains(87)||KeyBufferUtil.contains(38)){  //判断是否存在W  向上
            this.y -= this.speed;  //把Y坐标减去速度
            if(this.y < 0||this.y == 0) this.y = 0;//不允许英雄机出界
        }
    }

    /** 越界处理 */
    @Override
    public boolean outOfBounds() {
        return false;
    }

    /** 发射子弹 */
    public Bullet[] shoot(){
        int xStep = width/4;      //4半
        int yStep = 20;  //步
        if(doubleFire>0){  //双倍火力
            Bullet[] bullets = new Bullet[2];
            bullets[0] = new Bullet(x+xStep,y-yStep);  //y-yStep(子弹距飞机的位置)
            bullets[1] = new Bullet(x+3*xStep,y-yStep);
            return bullets;
        }else{      //单倍火力
            Bullet[] bullets = new Bullet[1];
            bullets[0] = new Bullet(x+2*xStep,y-yStep);
            return bullets;
        }
    }

    /** 移动 */
    @Override
    public void step() {
        if(images.length>0){
            image = images[index++/10%images.length];  //切换图片hero0，hero1
        }
    }

    /** 碰撞算法 */
    public boolean hit(Flyer other){

        int x1 = other.x - this.width/2;                 //x坐标最小距离
        int x2 = other.x + this.width/2 + other.width;   //x坐标最大距离
        int y1 = other.y - this.height/2;                //y坐标最小距离
        int y2 = other.y + this.height/2 + other.height; //y坐标最大距离

        int herox = this.x + this.width/2;               //英雄机x坐标中心点距离
        int heroy = this.y + this.height/2;              //英雄机y坐标中心点距离

        return herox>x1 && herox<x2 && heroy>y1 && heroy<y2;   //区间范围内为撞上了
    }
}
