package com.game;

import com.game.entity.*;
import com.game.util.KeyBufferUtil;
import com.game.util.PlayMusicUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlaneGame extends JFrame{

    /*静态常量资源*/
    private final static String TITLE = "PlaneGame";
    public final static int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;//600;
    public final static int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;//800;

    /** 游戏的当前状态: START RUNNING PAUSE GAME_OVER */
    private int state;
    private static final int START = 0;
    private static final int RUNNING = 1;
    private static final int PAUSE = 2;
    private static final int GAME_OVER = 3;

    private static boolean isBom = false;
    private Bom bom = new Bom();

    private int score = 0; // 得分
    private int killer = 0; //杀敌数量
    private int aliveTimes = 0; //生存时间
    private Timer timer; // 定时器
    private int intervel = 1000 / 100; // 时间间隔(毫秒)

    private Flyer[] flyers = {}; // 敌机数组
    private Bullet[] bullets = {}; // 子弹数组
    private Player player = new Player(); // 英雄机

    /*游戏静态资源*/
    public static BufferedImage hero_0 = null;
    public static BufferedImage hero_1 = null;
    public static BufferedImage bee = null;
    public static BufferedImage hero_bullet = null;
    public static BufferedImage enemy = null;
    public static BufferedImage enemy_bullet = null;
    public static BufferedImage destroy = null;
    public static BufferedImage map = null;
    public static BufferedImage btn_start = null;
    public static BufferedImage btn_restart = null;
    public static BufferedImage btn_over = null;
    public static BufferedImage btn_continue = null;
    public static BufferedImage logo = null;

    /*静态代码块，初始化资源*/
    static {
        try {
            hero_0 = ImageIO.read(PlaneGame.class.getResource("img/hero1.png"));
            hero_1 = ImageIO.read(PlaneGame.class.getResource("img/hero2.png"));
            bee = ImageIO.read(PlaneGame.class.getResource("img/bee.png"));
            hero_bullet = ImageIO.read(PlaneGame.class.getResource("img/bullet1.png"));
            enemy = ImageIO.read(PlaneGame.class.getResource("img/enemy.png"));
            enemy_bullet = ImageIO.read(PlaneGame.class.getResource("img/bullet2.png"));
            destroy = ImageIO.read(PlaneGame.class.getResource("img/destroy.png"));
            map = ImageIO.read(PlaneGame.class.getResource("img/background.png"));
            btn_start = ImageIO.read(PlaneGame.class.getResource("img/game_start.png"));
            btn_restart = ImageIO.read(PlaneGame.class.getResource("img/game_Reagain.png"));
            btn_over = ImageIO.read(PlaneGame.class.getResource("img/game_over.png"));
            btn_continue = ImageIO.read(PlaneGame.class.getResource("img/game_continue.png"));
            logo = ImageIO.read(PlaneGame.class.getResource("img/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘制英雄(玩家)机
     * @param g
     */
    private void paintHero(Graphics g){
        g.drawImage(player.getImage(), player.getX(), player.getY(), null);
    }

    /**
     * 绘制子弹
     * @param g
     */
    private void paintBullet(Graphics g){
        for (int i = 0; i < bullets.length; i++) {
            Bullet b = bullets[i];
            g.drawImage(b.getImage(), b.getX() - b.getWidth() / 2, b.getY(),
                    null);
        }
    }

    /**
     * 绘制敌人
     * @param g
     */
    private void paintEnemy(Graphics g){
        for (int i = 0; i < flyers.length; i++) {
            Flyer f = flyers[i];
            g.drawImage(f.getImage(), f.getX(), f.getY(), null);
        }
    }

    private void paintBom(Graphics g){
        if(isBom)
            g.drawImage(bom.getImage(), bom.getX(), bom.getY(), bom.getWidth(), bom.getHeight(), null);
        isBom = !isBom;
    }

    /**
     * 绘制分数
     * @param g
     */
    private void paintScore(Graphics g){
        int x = 10; // x坐标
        int y = 50; // y坐标
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 22); // 字体
        g.setColor(new Color(0x00FF00));
        g.setFont(font); // 设置字体
        g.drawString("SCORE:" + score, x, y); // 画分数
        y=y+20; // y坐标增20
        g.drawString("LIFE:" + player.getLife(), x, y); // 画命
    }

    /**
     * 绘制游戏状态
     * @param g
     */
    private void paintState(Graphics g){

        if(state == START) {
            //绘制开始游戏btn
            g.drawImage(btn_start,
                    (WIDTH-btn_start.getWidth())/2,
                    HEIGHT/2-btn_start.getHeight()/2, null);
            //绘制logo
            g.drawImage(logo,
                    (WIDTH-logo.getWidth())/2,
                    (HEIGHT/2-logo.getHeight())/2,  null);
            //绘制游戏说明
            Font font = new Font(Font.SANS_SERIF, Font.BOLD, 35); // 字体
            g.setColor(new Color(0xFF0000));
            g.setFont(font); // 设置字体
            g.drawString("操作说明",(WIDTH-btn_start.getWidth())/2-btn_start.getWidth()/4, HEIGHT/2-btn_start.getHeight()/2+btn_start.getHeight()*4);
            font = new Font(Font.SANS_SERIF, Font.BOLD, 25); // 字体
            g.setFont(font); // 设置字体
            g.setColor(new Color(0x000000));
            g.drawString("退出：ESC",(WIDTH-btn_start.getWidth())/2-btn_start.getWidth()/4+50, HEIGHT/2-btn_start.getHeight()/2+btn_start.getHeight()*5);
            g.drawString("按钮/暂停：MouseClick",(WIDTH-btn_start.getWidth())/2-btn_start.getWidth()/4+50, HEIGHT/2-btn_start.getHeight()/2+btn_start.getHeight()*6);
            g.drawString("控制：（A、S、D、W）、（UP、DOWN、LEFT、RIGHT）",(WIDTH-btn_start.getWidth())/2-btn_start.getWidth()/4+50, HEIGHT/2-btn_start.getHeight()/2+btn_start.getHeight()*7);
        }else if(state == RUNNING){             //运行状态
            paintHero(g);
            paintBullet(g);
            paintBom(g);
            paintEnemy(g);
            paintScore(g);
        }else if(state == PAUSE){               //暂停状态
            //绘制开始游戏btn
            g.drawImage(btn_continue,
                    (WIDTH-btn_start.getWidth())/2,
                    HEIGHT/2-btn_start.getHeight()/2, null);
            //绘制logo
            g.drawImage(logo,
                    (WIDTH-logo.getWidth())/2,
                    (HEIGHT/2-logo.getHeight())/2,  null);
        }else {                                 //结束状态
            //绘制重新开始游戏btn
            g.drawImage(btn_restart,
                    (WIDTH-btn_start.getWidth())/2,
                    HEIGHT/2-btn_start.getHeight()/2, null);
            //绘制logo
            g.drawImage(logo,
                    (WIDTH-logo.getWidth())/2,
                    (HEIGHT/2-logo.getHeight())/2,  null);
            //当前分数
            Font font = new Font(Font.SANS_SERIF, Font.BOLD, 30); // 字体
            g.setColor(new Color(0xFF0000));
            g.setFont(font); // 设置字体
            g.drawString("本局分数：" + score, (WIDTH-btn_start.getWidth())/2+btn_start.getWidth()/4, HEIGHT/2-btn_start.getHeight()/2+btn_start.getHeight()*3); // 画分数
            g.drawString("本局杀敌：" + killer, (WIDTH-btn_start.getWidth())/2+btn_start.getWidth()/4, HEIGHT/2-btn_start.getHeight()/2+btn_start.getHeight()*5); // 画战绩
            g.drawString("存活(s)：" + aliveTimes/1000, (WIDTH-btn_start.getWidth())/2+btn_start.getWidth()/4, HEIGHT/2-btn_start.getHeight()/2+btn_start.getHeight()*7); // 画生存时间
        }
    }

    /**
     * 双缓冲绘图
     */
    Image offScreenImage = null;
    Graphics gImage = null;
    @Override
    public void paint(Graphics g) {
        // 在重绘函数中实现双缓冲机制
        offScreenImage = createImage(WIDTH, HEIGHT);
        // 获得截取图片的画布
        gImage = offScreenImage.getGraphics();
        super.paint(gImage);
        //绘制背景
        gImage.drawImage(map, 0,0, WIDTH, HEIGHT,null);
        paintState(gImage);

        g.drawImage(offScreenImage,0, 0, null);
    }

    public PlaneGame(){
        //初始化标题和窗体大小
        this.setTitle("PlaneGame");
        this.setSize(WIDTH, HEIGHT);
        this.setIconImage(logo);
        // 把窗口位置设置到屏幕的中心
        this.setLocationRelativeTo(null);
        //禁止缩放
        this.setResizable(false);
        //默认退出行为
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //播放背景音乐
        PlayMusicUtil.playBgm();
    }

    public static void main(String[] args) {
        /*
         * 在 AWT 的事件队列线程中创建窗口和组件, 确保线程安全,
         * 即 组件创建、绘制、事件响应 需要处于同一线程。
         */
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 创建窗口对象
                PlaneGame planeGame = new PlaneGame();
                // 显示窗口
                planeGame.setVisible(true);
                //事件监听
                planeGame.actionListener();
            }
        });
    }

    /**
     * 事件监听及处理
     */
    public void actionListener(){
        MouseAdapter mouseAdapter = new MouseAdapter() {
            /**
             * {@inheritDoc}
             *鼠标点击
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX();
                int y = e.getY();
                if(state == START) {
                    //开始游戏按钮
                    if(x>(WIDTH-btn_start.getWidth())/2
                            &&x<(WIDTH-btn_start.getWidth())/2+btn_start.getWidth()
                            &&y>HEIGHT/2-btn_start.getHeight()/2
                            &&y<HEIGHT/2+btn_start.getHeight()/2){
                        //开启游戏状态
                        state = RUNNING;
                        //播放点击音乐
                        PlayMusicUtil.playClick();
                    }
                }else  if(state == RUNNING) {
                    state = PAUSE;  //游戏过程中点击窗体进入暂停状态
                }else  if(state == GAME_OVER) {
                    //开始游戏按钮
                    if(x>(WIDTH-btn_start.getWidth())/2
                            &&x<(WIDTH-btn_start.getWidth())/2+btn_start.getWidth()
                            &&y>HEIGHT/2-btn_start.getHeight()/2
                            &&y<HEIGHT/2+btn_start.getHeight()/2){
                        //初始化游戏状态
                        //score = 0;
//                        flyers = null;
//                        bullets = null;
//                        flyEnteredIndex = 0;
//                        shootIndex = 0;
                        player = null;
                        player = new Player();
                        //开启游戏状态
                        state = RUNNING;
                        //播放点击音乐
                        PlayMusicUtil.playClick();
                    }
                }else {    //if（state==PAUSE）
                    //开始游戏按钮
                    if(x>(WIDTH-btn_start.getWidth())/2
                            &&x<(WIDTH-btn_start.getWidth())/2+btn_start.getWidth()
                            &&y>HEIGHT/2-btn_start.getHeight()/2
                            &&y<HEIGHT/2+btn_start.getHeight()/2){
                        //继续游戏状态
                        state = RUNNING;
                        //播放点击音乐
                        PlayMusicUtil.playClick();
                    }
                }
            }
        };
        this.addMouseListener(mouseAdapter);
        KeyAdapter keyAdapter = new KeyAdapter() {
            /**
             * Invoked when a key has been pressed.
             *键盘按下
             * @param e
             */
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                KeyBufferUtil.KeyDown(e.getKeyCode());
                //退出程序
                if(e.getKeyCode() == 27) System.exit(0);
            }

            /**
             * Invoked when a key has been released.
             *键盘抬起
             * @param e
             */
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                KeyBufferUtil.KeyUp(e.getKeyCode());
            }
        };
        this.addKeyListener(keyAdapter);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state == RUNNING) { // 运行状态
                    enterAction(); // 飞行物入场
                    stepAction(); // 走一步
                    shootAction(); // 英雄机射击
                    bangAction(); // 子弹打飞行物
                    outOfBoundsAction(); // 删除越界飞行物及子弹
                    checkGameOverAction(); // 检查游戏结束
                    //计时器
                    aliveTimes+=intervel;
                }
                repaint(); // 重绘，调用paint()方法
            }
        }, intervel, intervel);
    }

    int flyEnteredIndex = 0; // 飞行物入场计数

    /** 飞行物入场 */
    public void enterAction() {
        flyEnteredIndex++;
        if (flyEnteredIndex % 40 == 0) { // 400毫秒生成一个飞行物--10*40
            Flyer obj = nextOne(); // 随机生成一个飞行物
            flyers= Arrays.copyOf(flyers, flyers.length + 1);
            flyers[flyers.length - 1] = obj;
        }
    }

    /** 走一步 */
    public void stepAction() {
        for (int i = 0; i < flyers.length; i++) { // 飞行物走一步
            Flyer f = flyers[i];
            f.step();
        }

        for (int i = 0; i < bullets.length; i++) { // 子弹走一步
            Bullet b = bullets[i];
            b.step();
        }
        player.step(); // 英雄机走一步

        player.moveTo();
    }

    /** 飞行物走一步 */
    public void flyingStepAction() {
        for (int i = 0; i < flyers.length; i++) {
            Flyer f = flyers[i];
            f.step();
        }
    }

    int shootIndex = 0; // 射击计数

    /** 射击 */
    public void shootAction() {
        shootIndex++;
        if (shootIndex % 30 == 0) { // 300毫秒发一颗
            Bullet[] bs = player.shoot(); // 英雄打出子弹
            //播放射击音效
            PlayMusicUtil.playShoot();
            bullets = Arrays.copyOf(bullets, bullets.length + bs.length); // 扩容
            System.arraycopy(bs, 0, bullets, bullets.length - bs.length,
                    bs.length); // 追加数组
        }
    }

    /** 子弹与飞行物碰撞检测 */
    public void bangAction() {
        for (int i = 0; i < bullets.length; i++) { // 遍历所有子弹
            Bullet b = bullets[i];
            bang(b); // 子弹和飞行物之间的碰撞检查
        }
    }

    /** 删除越界飞行物及子弹 */
    public void outOfBoundsAction() {
        int index = 0; // 索引
        Flyer[] flyingLives = new Flyer[flyers.length]; // 活着的飞行物
        for (int i = 0; i < flyers.length; i++) {
            Flyer f = flyers[i];
            if (!f.outOfBounds()) {
                flyingLives[index++] = f; // 不越界的留着
            }
        }
        flyers = Arrays.copyOf(flyingLives, index); // 将不越界的飞行物都留着

        index = 0; // 索引重置为0
        Bullet[] bulletLives = new Bullet[bullets.length];
        for (int i = 0; i < bullets.length; i++) {
            Bullet b = bullets[i];
            if (!b.outOfBounds()) {
                bulletLives[index++] = b;
            }
        }
        bullets = Arrays.copyOf(bulletLives, index); // 将不越界的子弹留着
    }

    /** 检查游戏结束 */
    public void checkGameOverAction() {
        if (isGameOver()==true) {
            PlayMusicUtil.playOver();
            state = GAME_OVER; // 改变状态
        }
    }

    /** 检查游戏是否结束 */
    public boolean isGameOver() {

        for (int i = 0; i < flyers.length; i++) {
            int index = -1;
            Flyer obj = flyers[i];
            if (player.hit(obj)) { // 检查英雄机与飞行物是否碰撞
                PlayMusicUtil.playDie();
                player.subtractLife(); // 减命
                player.setDoubleFire(0); // 双倍火力解除
                index = i; // 记录碰上的飞行物索引
            }
            if (index != -1) {
                Flyer t = flyers[index];
                flyers[index] = flyers[flyers.length - 1];
                flyers[flyers.length - 1] = t; // 碰上的与最后一个飞行物交换

                flyers = Arrays.copyOf(flyers, flyers.length - 1); // 删除碰上的飞行物
            }
        }

        return player.getLife() <= 0;
    }

    /** 子弹和飞行物之间的碰撞检查 */
    public void bang(Bullet bullet) {
        int index = -1; // 击中的飞行物索引
        for (int i = 0; i < flyers.length; i++) {
            Flyer obj = flyers[i];
            if (obj.shootBy(bullet)) { // 判断是否击中
                index = i; // 记录被击中的飞行物的索引
                break;
            }
        }
        if (index != -1) { // 有击中的飞行物
            Flyer one = flyers[index]; // 记录被击中的飞行物

            Flyer temp = flyers[index]; // 被击中的飞行物与最后一个飞行物交换
            flyers[index] = flyers[flyers.length - 1];
            flyers[flyers.length - 1] = temp;

            flyers = Arrays.copyOf(flyers, flyers.length - 1); // 删除最后一个飞行物(即被击中的)
            //杀敌计数
            killer++;
            //爆炸效果
            isBom = true;
            bom.setValue(temp);

            // 检查one的类型(敌人加分，奖励获取)
            if (one instanceof Enemy) { // 检查类型，是敌人，则加分
                Enemy e = (Enemy) one; // 强制类型转换
                score += e.getScore(); // 加分
            } else { // 若为奖励，设置奖励
                PlayMusicUtil.playAward();
                Award a = (Award) one;
                int type = a.getType(); // 获取奖励类型
                switch (type) {
                    case Award.DOUBLE_FIRE:
                        player.addDoubleFire(); // 设置双倍火力被击中的飞行物与最后一个飞行物交换
                        //flyers[index] = flyers[flyers.length - 1];
                        break;
                    case Award.LIFE:
                        player.addLife(); // 设置加命
                        break;
                }
            }
        }
    }

    /**
     * 随机生成飞行物
     *
     * @return 飞行物对象
     */
    public static Flyer nextOne() {
        Random random = new Random();
        int type = random.nextInt(20); // [0,20)
        if (type < 4) {
            return new Bee();
        } else {
            return new Enemy();
        }
    }

}
