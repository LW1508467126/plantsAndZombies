package cn.itsource.game.pz;

import static cn.itsource.game.pz.Configs.attack;
import static cn.itsource.game.pz.Configs.bk;
import static cn.itsource.game.pz.Configs.bullet;
import static cn.itsource.game.pz.Configs.cellHeight;
import static cn.itsource.game.pz.Configs.cellWidth;
import static cn.itsource.game.pz.Configs.d1;
import static cn.itsource.game.pz.Configs.dead;
import static cn.itsource.game.pz.Configs.flowers;
import static cn.itsource.game.pz.Configs.nuts;
import static cn.itsource.game.pz.Configs.peas;
import static cn.itsource.game.pz.Configs.plantPoints;
import static cn.itsource.game.pz.Configs.playerBGM;
import static cn.itsource.game.pz.Configs.playerOVER;
import static cn.itsource.game.pz.Configs.raceWayYpoints;
import static cn.itsource.game.pz.Configs.scaleHeight;
import static cn.itsource.game.pz.Configs.scaleWidth;
import static cn.itsource.game.pz.Configs.screenHeight;
import static cn.itsource.game.pz.Configs.screenWidth;
import static cn.itsource.game.pz.Configs.seedFlower;
import static cn.itsource.game.pz.Configs.seedNut;
import static cn.itsource.game.pz.Configs.seedPea;
import static cn.itsource.game.pz.Configs.seedbank;
import static cn.itsource.game.pz.Configs.seedbankX;
import static cn.itsource.game.pz.Configs.soundPool;
import static cn.itsource.game.pz.Configs.sun;
import static cn.itsource.game.pz.Configs.zombies;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import cn.itsource.game.pz.util.ConfigUtils;

public class MainActivity extends Activity {
	private GramView gramView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);

		// 处理一下初始化操作
		init();

		gramView = new GramView(this);
		// DoubleView gramView = new DoubleView(this);
		// 把游戏对象作为屏幕
		setContentView(gramView);
	}

	// 处理一下初始化操作
	private void init() {
		// 获取屏幕的宽高
		ConfigUtils.init(this);
		// 背景原图图片
		bk = BitmapFactory.decodeResource(getResources(), R.drawable.bk);
		dead=BitmapFactory.decodeResource(getResources(),R.drawable.dead);
		//加载背景音乐
		playerBGM= MediaPlayer.create(this,R.raw.bgm);
		playerOVER= MediaPlayer.create(this,R.raw.bgmover);
		playerBGM.start();
		soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
		d1=soundPool.load(this,R.raw.jianjiao,1);

		// 图片横纵向的比例:参照背景图片的横纵向的比例
		scaleWidth = (float) screenWidth / bk.getWidth();// 缩小
		scaleHeight = (float) screenHeight / bk.getHeight();

		// 通过矩阵对象进行横纵向比例缩放
		bk = ConfigUtils.resizeBitmap(bk, screenWidth, screenHeight);
		dead=ConfigUtils.resizeBitmap(dead, screenWidth, screenHeight);
		// 面板图片
		seedbank = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.seedbank));
		// 计算面板的x坐标的值：(屏幕的宽度-面板的宽度)/其实就是居中放置
		seedbankX = (screenWidth - seedbank.getWidth()) / 2;

		seedFlower = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.seed_flower),
				seedbank.getWidth() / 7, seedbank.getHeight());
		seedPea = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.seed_pea), seedbank.getWidth() / 7,
				seedbank.getHeight());
		seedNut=ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(),R.drawable.seed_05),seedbank.getWidth()/7,
				seedbank.getHeight());

		flowers[0] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_1_01));
		flowers[1] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_1_02));
		flowers[2] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_1_03));
		flowers[3] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_1_04));
		flowers[4] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_1_05));
		flowers[5] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_1_06));
		flowers[6] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_1_07));
		flowers[7] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_1_08));

		peas[0] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_2_01));
		peas[1] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_2_02));
		peas[2] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_2_03));
		peas[3] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_2_04));
		peas[4] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_2_05));
		peas[5] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_2_06));
		peas[6] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_2_07));
		peas[7] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.p_2_08));

		nuts[0]= ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.jianguo1));
		nuts[1]= ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.jiaoguo2));
		nuts[2]= ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.jiaoguo3));

		// 计算单元格宽高
		// 单元格的宽度：左侧1.75+9+右侧0.25
		cellWidth = screenWidth / (9 + 2);
		// 单元格的高度：顶部0.75+5+低部0.25
		cellHeight = screenHeight / (5 + 1);

		// 设置跑道的单元格
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 9; j++) {
				// key:不重复索引
				// 第一行：0-8
				// 第二行：10-18
				// 第三行：20-28
				// 第四行：30-38
				// 第五行：40-48
				// value：Point
				int x = 2 * cellWidth - cellWidth / 2 + j * cellWidth;// x的起始坐标是从1.5个单元格开始
				int y = (i + 1) * cellHeight;// y的起始坐标是从1个单元格开始
				Point point = new Point(x, y);
				plantPoints.put(i * 10 + j, point);
				// 存储跑道y坐标，并存储这些安放点到Configs.raceWayYpoints
				if (j == 0) {
					raceWayYpoints[i] = (i + 1) * cellHeight;
					System.out.println("(i + 1) * cellHeight:" + (i + 1)
							* cellHeight);
				}
			}
		}

		sun = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.sun));

		bullet = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.bullet));

		zombies[0] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.z_1_01));
		zombies[1] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.z_1_02));
		zombies[2] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.z_1_03));
		zombies[3] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.z_1_04));
		zombies[4] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.z_1_05));
		zombies[5] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.z_1_06));
		zombies[6] = ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.z_1_07));

		attack[0]= ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.at1));
		attack[1]= ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.at2));
		attack[2]= ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.at3));
		attack[3]= ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.at4));
		attack[4]= ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.at5));
		attack[5]= ConfigUtils.resizeBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.at6));


	}

	// 屏幕的触屏事件：传递给游戏对象的触屏事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gramView.onTouchEvent(event);
	}

}