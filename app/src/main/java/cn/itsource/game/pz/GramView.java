package cn.itsource.game.pz;

import static cn.itsource.game.pz.Configs.bk;
import static cn.itsource.game.pz.Configs.cellHeight;
import static cn.itsource.game.pz.Configs.cellWidth;
import static cn.itsource.game.pz.Configs.d1;
import static cn.itsource.game.pz.Configs.dead;
import static cn.itsource.game.pz.Configs.flowerScore;
import static cn.itsource.game.pz.Configs.flowers;
import static cn.itsource.game.pz.Configs.nutScore;
import static cn.itsource.game.pz.Configs.over;
import static cn.itsource.game.pz.Configs.peaScore;
import static cn.itsource.game.pz.Configs.plantPoints;
import static cn.itsource.game.pz.Configs.playerOVER;
import static cn.itsource.game.pz.Configs.score;
import static cn.itsource.game.pz.Configs.screenHeight;
import static cn.itsource.game.pz.Configs.screenWidth;
import static cn.itsource.game.pz.Configs.seedFlower;
import static cn.itsource.game.pz.Configs.seedNut;
import static cn.itsource.game.pz.Configs.seedPea;
import static cn.itsource.game.pz.Configs.seedbank;
import static cn.itsource.game.pz.Configs.seedbankX;
import static cn.itsource.game.pz.Configs.soundPool;
import static java.lang.System.exit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import cn.itsource.game.pz.domain.Plant;
import cn.itsource.game.pz.domain.Seed;
import cn.itsource.game.pz.domain.Sun;
import cn.itsource.game.pz.domain.Zombie;
import cn.itsource.game.pz.domain.ZombieManager;

public class GramView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable {
	// 为了获取当前类的实例对象
	private static GramView gramView;
	private SurfaceHolder surfaceHolder;
	private Thread thread;
	private Paint paint;
	// 定义一个标记位，游戏是否正在运行
	private boolean runing = false;
	// 存储面板上面的对象
	private List<Seed> seeds;
	// 存储等待安放到跑道的对象
	private List<Seed> emplaces;
	// 存储跑道里面放置的向日葵，豌豆
	private Map<Integer, Plant> plantMap;
	// 存储跑道里面的阳光对象
	private List<Sun> suns;
	// 存储跑道里面的子弹对象
	private List<Plant> bullets;
	// 存储跑道里面的僵尸对象
	private List<Zombie> zombies;

	public GramView(Context context) {
		super(context);
		// 获取父类的SurfaceHolder的引用
		surfaceHolder = getHolder();
		// 当前类必须实现SurfaceHolder.Callback接口，而且监听事件
		surfaceHolder.addCallback(this);
		System.out.println("GramView被创建了");
		gramView = this;
	}

	// 静态方法：为了获取当前类的实例对象
	public static GramView getInstance() {
		return gramView;
	}

	// surface被创建的时候：设置初始参数，一般开始一个子线程
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("surface被创建了");
		init();
		runing = true;
		// 开始一个子线程
		thread = new Thread(this);
		thread.start();
	}

	private void init() {
		// 初始化画布
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(20F);

		seeds = new ArrayList<Seed>();
		Seed flower = new Seed(seedbankX + seedFlower.getWidth(), 0,
				Seed.SEED_FLOWER);
		Seed pea = new Seed(seedbankX + seedPea.getWidth() * 2, 0,
				Seed.SEED_PEA);
		Seed nut=new Seed(seedbankX+seedNut.getHeight()*3-65,0,Seed.SEED_NUT);
		seeds.add(flower);
		seeds.add(pea);
		seeds.add(nut);

		emplaces = new ArrayList<Seed>();
		plantMap = new HashMap<Integer, Plant>();
		suns = new ArrayList<Sun>();
		bullets = new ArrayList<Plant>();
		zombies = new ArrayList<Zombie>();
	}

	// surface被改变的时候：屏幕发送了变化:视频(全屏，1/2屏)
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	// surface被销毁的时候：改变初始参数，停止线程
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		runing = false;
		// 判断线程不为空并且处于运行中
		if (thread != null && thread.isAlive()) {
			// 销毁绘图线程
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 绘图到surface，等待提交至主(UI)线程
	int y = 100;

	@Override
	public void run() {
		while (runing) {
			// 把Canvas放在外面提高性能，不必每次都定义这个变量
			Canvas canvas = null;
			// 将成员变量转为局部变量，也是提高性能(成员变量的引用必须局部变量的引用耗时)
			SurfaceHolder holder = this.surfaceHolder;
			// System.out.println("子线程处于一直运行中");
			// 因为是子线程操作：多线程编程，最好把surfaceHolder同步一下
			synchronized (surfaceHolder) {
				try {
					// 获取一个锁的画布
					canvas = holder.lockCanvas();

					// 在这里进行绘图
					render(canvas);
					// 修改数据的方法：清除生命周期已经结束的对象
					updateDate();

				} finally {
					if (canvas != null) {
						// 保证此代码必须执行：把锁的画布提交至sucrface，在通过sucrface提交至主线程渲染到屏幕
						holder.unlockCanvasAndPost(canvas);
					}
				}
				// 人的眼睛在1秒钟有24帧图片变化，连续的动画
				try {
					Thread.sleep(1000 / 12);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	// 绘制内容
	private void render(Canvas canvas) {
		if (canvas == null)
			return;
		if(over==true){
			canvas.drawBitmap(dead, 0, 0, null);
			playerOVER.start();
		}
		else {
			// 先把屏幕清空一下:重新绘制一下背景
			canvas.drawBitmap(bk, 0, 0, null);
			// 画面板
			canvas.drawBitmap(seedbank, seedbankX, 0, null);


			// 画分值
			paint.setTextSize(45);
			canvas.drawText(score + "", seedbankX + seedFlower.getWidth() / 3,
					(int) (seedbank.getHeight() * 0.95), paint);

			// 画面板上面的对象
			for (Seed seed : seeds) {
				seed.drawSelf(canvas);
			}

			// 画跑道的对象
			for (Iterator iterator = plantMap.keySet().iterator(); iterator
					.hasNext(); ) {
				Integer key = (Integer) iterator.next();
				Plant plant = plantMap.get(key);
				if (plant.getLifeValue() > 0) {
					plant.drawSelf(canvas);
				} else {
					iterator.remove();
				}

			}

			// 画子弹的对象
			Iterator<Plant> bulletIterator = bullets.iterator();
			while (bulletIterator.hasNext()) {
				Plant bullet = (Plant) bulletIterator.next();
				if (bullet.getLifeValue() > 0) {
					bullet.drawSelf(canvas);
				} else {
					bulletIterator.remove();
				}
			}

			// 画阳光
			// 第一种
			Iterator<Sun> sunIterator = suns.iterator();
			while (sunIterator.hasNext()) {
				Sun sun = (Sun) sunIterator.next();
				if (sun.getLifeValue() > 0) {
					sun.drawSelf(canvas);
				} else {
					sunIterator.remove();
				}
			}

			// 画僵尸
			Iterator<Zombie> zombieIterator = zombies.iterator();
			while (zombieIterator.hasNext()) {
				Zombie zombie = (Zombie) zombieIterator.next();
				if (zombie.getLifeValue() > 0) {
					zombie.drawSelf(canvas);
				} else {
					zombieIterator.remove();
				}
			}

			// 画等待安放到跑道的对象
			for (Seed seed : emplaces) {
				seed.drawSelf(canvas);
			}
		}
	}

	// 修改数据的方法：清除生命周期已经结束的对象
	private void updateDate() {
		// 随机产生僵尸
		ZombieManager.addZombie();

		// for (Sun sunDead : sunDeads) {
		// suns.remove(sunDead);
		// }
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(over==true){
			exit(0);
		}
		int x = (int) event.getX();
		int y = (int) event.getY();
		// System.out.println("x:" + x + ",y:" + y);
		// 先判断等待安放状态的对象是否可以操作
		for (Seed seed : emplaces) {
			if (seed.onTouch(x, y, event.getAction())) {
				System.out.println("等待安放状态的对象");
				return true;
			}
		}

		for (Sun sun : suns) {
			if (sun.onTouch(x, y, event.getAction())) {
				System.out.println("收集阳光,移动阳光");
				return true;
			}
		}

		for (Seed seed : seeds) {
			if (seed.onTouch(x, y, event.getAction())) {
				System.out.println("选中了面板上面对象");
				return true;
			}
		}

		return super.onTouchEvent(event);
	}

	// 选中了面板上面的向日葵或者豌豆之后，添加等待安放到跑道的向日葵或者豌豆
	public void addEmplace(Seed seed) {
		// 面板上面的分值够不够？

		// 只允许安放状态的列表只能有一个对象
		if (emplaces.size() > 0) {
			return;
		}
		if (seed.getState() == Seed.SEED_FLOWER) {// 面板上面的向日葵
			Seed emplaceFlower = new Seed(seed.getLocationX(),
					seed.getLocationY(), Seed.EMPLACE_FLOWER);
			emplaces.add(emplaceFlower);
		} else if (seed.getState() == Seed.SEED_PEA) {// 面板上面的豌豆
			Seed emplacePea = new Seed(seed.getLocationX(),
					seed.getLocationY(), Seed.EMPLACE_PEA);
			emplaces.add(emplacePea);
		}else if (seed.getState() == Seed.SEED_NUT) {// 面板上面的豌豆
			Seed emplaceNut = new Seed(seed.getLocationX(),
					seed.getLocationY(), Seed.EMPLACE_NUT);
			emplaces.add(emplaceNut);
		}
		else {
			throw new RuntimeException("不能找到对应状态的对象:" + seed.getState());
		}

	}

	// 把等待安放状态的对象清除，换成跑道上面的对象
	// 1.寻找合适的位置:
	// 安放图片如果与单元格的重叠的宽高：
	// 使用安放图片x坐标-单元格的x坐标<(单元格宽度的一半)
	// 使用安放图片y坐标-单元格的y坐标<(单元格高度的一半)
	// 已经寻找到了合适的位置
	public void addPlant(Seed seed) {
		int flag = 0;
		// 必须对跑道里面的Map遍历
		for (Map.Entry<Integer, Point> entry : plantPoints.entrySet()) {
			Integer key = entry.getKey();
			Point point = entry.getValue();
			int x = point.x;
			int y = point.y;
			if (Math.abs(seed.getLocationX() - x) < cellWidth / 2
					&& Math.abs(seed.getLocationY() - y) < cellHeight / 2) {
				System.out.println("找到了合适的位置,判断原来是否已经存在");
				// 2.判断原来的单元格是否已经存在跑道对象
				System.out.println("---------------" + plantMap.get(key));
				if (plantMap.get(key) != null) {
					flag = 1;
					break;
				}
				// 放置到跑道坐标就应该是单元格的起始坐标
				if (seed.getState() == Seed.EMPLACE_FLOWER) {
					Plant plant = new Plant(x, y, Plant.PLANT_FLOWER);
					plantMap.put(key, plant);
					score -= flowerScore;
				} else if (seed.getState() == Seed.EMPLACE_PEA) {
					Plant plant = new Plant(x, y, Plant.PLANT_PEA);
					plantMap.put(key, plant);
					score -= peaScore;
				} else if (seed.getState() == Seed.EMPLACE_NUT) {
					Plant plant = new Plant(x, y, Plant.PLANT_NUT);
					plantMap.put(key, plant);
					plant.setLifeValue(900);
					score -= nutScore;
				}
				flag = 2;
				// 已经找到合适的位置之后，是不需要循环的
				break;
			}
		}
		// 只要放置不成功就直接清除掉等待安放的对象
		if (flag == 2) {// 找到合适的位置
			emplaces.clear();
		} else if (flag == 1) {// 在跑道的局域没有找到合适的位置：改进的代码：最好是按照9宫格的中心找其他的是否可以安放
			emplaces.clear();
		} else {// 没有找到合适位置
			emplaces.clear();
		}

	}

	// 跑道里面的向日葵在一定时间间隔里面自动创建阳光
	public void addSun(Plant plant) {
		Sun sun = new Sun(plant.getLocationX(), plant.getLocationY());
		suns.add(sun);
	}

	// 跑道里面的豌豆在一定时间间隔里面自动创建子弹
	public void addBullet(Plant plant) {
		Plant bullet = new Plant(plant.getLocationX() + cellWidth * 3 / 4,
				plant.getLocationY() + cellHeight * 1 / 4, Plant.PLANT_BULLET);
		bullets.add(bullet);
	}

	public void addZombie(Zombie zombie) {
		zombies.add(zombie);
	}

	// 由僵尸自动发出碰撞检测
	// 僵尸碰到了豌豆、向日葵:豌豆、向日葵消失
	// 僵尸碰到了子弹，子弹消失，僵尸生命值-5
	// 碰撞检测的方式：矩形检测
	public void checkLife(Zombie zombie) {
		// 检测僵尸碰到了豌豆
		zombie.setSpeed(3);
		for (Map.Entry<Integer, Plant> entry : plantMap.entrySet()) {
			Plant plant = entry.getValue();
			// 在判断的时候必须按照同一个跑道来进行判断碰撞
			// System.out.println("plant Y:" + (plant.getLocationY() -
			// cellHeight / 4));
			// System.out.println("zombie Y:" + zombie.getLocationY());
			// 因为在创建僵尸的时候调整了locationY - cellHeight / 4:ZombieManager.java p22
			if (plant != null
					&& (plant.getLocationY() - cellHeight / 4) == zombie
							.getLocationY()
					&& Math.abs(zombie.getLocationX() - plant.getLocationX()) < flowers[0]
							.getWidth() / 2) {

				//plant.setLifeValue(0);
				plant.setLifeValue(plant.getLifeValue()-5);
				if(plant.getLifeValue()>0){
                   zombie.setSpeed(0);
				}
				if(plant.getLifeValue()<=0){
					soundPool.play(d1,1,1,0,0,1);
					zombie.setSpeed(3);
				}
				System.out.println("向日葵和豌豆与僵尸发送了碰撞");
			}

		}

		// 检测僵尸碰到了子弹
		for (Plant bullet : bullets) {
			// System.out.println("plant Y:" + (bullet.getLocationY() -
			// cellHeight / 2));
			// System.out.println("zombie Y:" + zombie.getLocationY());
			// 在判断的时候必须按照同一个跑道来进行判断碰撞
			if (bullet != null
					&& (bullet.getLocationY() - cellHeight / 2) == zombie
							.getLocationY()
					&& Math.abs(zombie.getLocationX() - bullet.getLocationX()) < Configs.bullet
							.getWidth() / 2) {
				bullet.setLifeValue(0);
				zombie.setLifeValue(zombie.getLifeValue() - 5);
				System.out.println("子弹与僵尸发送了碰撞");
			}
		}

	}

}
