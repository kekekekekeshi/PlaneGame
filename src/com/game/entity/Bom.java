package com.game.entity;

import com.game.PlaneGame;

public class Bom extends Flyer {

    public Bom(){
        this.image = PlaneGame.destroy;
    }

    public void setValue(Flyer flyer){
        this.x = flyer.x;
        this.y = flyer.y;
        this.width = flyer.width;
        this.height = flyer.height;
    }

    /**
     * 检查是否出界
     *
     * @return true 出界与否
     */
    @Override
    public boolean outOfBounds() {
        return false;
    }

    /**
     * 飞行物移动一步
     */
    @Override
    public void step() {

    }
}
