package cn.itsource.game.pz;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.SoundPool;

//定义系统里面所有用到的常量、变量对象
public class Configs {

	// 屏幕的宽高
	public static int screenWidth;
	public static int screenHeight;
	// 通过背景图片计算横纵比例
	public static float scaleWidth;
	public static float scaleHeight;
	// 跑道里面的单元格的宽高
	public static int cellWidth;
	public static int cellHeight;

	// 存储背景图片
	public static Bitmap bk;
	public static Bitmap dead;
	// 面板图片
	public static Bitmap seedbank;
	// 面板上的向日葵图片
	public static Bitmap seedFlower;
	// 面板上的豌豆图片
	public static Bitmap seedPea;
	//M面板上的坚果
	public static Bitmap seedNut;
	// 向日葵和豌豆数组对象
	public static Bitmap[] flowers = new Bitmap[8];
	public static Bitmap[] peas = new Bitmap[8];
	public static Bitmap[] nuts=new Bitmap[3];
	public static Bitmap[] zombies = new Bitmap[7];
	//背景音乐
	public static MediaPlayer playerBGM;
	//结束背景音乐
	public static MediaPlayer playerOVER;
	//植物毁灭音效
	public static SoundPool soundPool;
	//音效id
	public static int d1;
	public static int d2;
	public static int d3;
    //僵尸攻击
	public  static Bitmap[] attack=new Bitmap[6];
	// 阳光
	public static Bitmap sun;
	// 子弹
	public static Bitmap bullet;

	// 存储面板的坐标位置
	public static int seedbankX;

	// 定义装单元格的集合
	// key:不重复索引
	// value：Point
	public static Map<Integer, Point> plantPoints = new HashMap<Integer, Point>();
	// 定义多少个跑道
	public static int[] raceWayYpoints = new int[5];

	// 跑道里面的向日葵出现4秒钟之后产生阳光
	public static int createSunTime = 4000;
	// 跑道里面的豌豆出现4秒钟之后产生子弹
	public static int createBulletTime = 1500;
	// 跑道里面的僵尸产生的时间
	public static int createZombieTime = 3000;
	// 子弹移动的速度
	public static int bulletMove = 50;
	// 僵尸移动的速度
	public static int zombieMove = 3;
	// 在一定的时间范围内，没有做收集自动死亡:sunDeadTime<createSunTime
	public static int sunDeadTime = 2000;
	// 阳光收集的时候的速度值
	public static float sunMove = 5F;
	// 游戏的初始分值
	public static int score = 400;
	// 阳光收集一次增加分值
	public static int sunScore = 25;
	// 正确使用一次向日葵
	public static int flowerScore = 50;
	// 正确使用一次豌豆
	public static int peaScore = 100;
	// 正确使用一次坚果
	public static int nutScore = 125;
	//游戏结束
	public static boolean over=false;

}
