package cn.itsource.game.pz.domain;

import static cn.itsource.game.pz.Configs.bullet;
import static cn.itsource.game.pz.Configs.bulletMove;
import static cn.itsource.game.pz.Configs.cellHeight;
import static cn.itsource.game.pz.Configs.createBulletTime;
import static cn.itsource.game.pz.Configs.createSunTime;
import static cn.itsource.game.pz.Configs.flowers;
import static cn.itsource.game.pz.Configs.nuts;
import static cn.itsource.game.pz.Configs.peas;
import static cn.itsource.game.pz.Configs.screenWidth;
import android.graphics.Canvas;
import cn.itsource.game.pz.GramView;

//跑道的对象:向日葵，豌豆，子弹
public class Plant extends BaseModel {
	// 跑道的对象的向日葵
	public static final int PLANT_FLOWER = 0x0001;
	// 跑道的对象的豌豆
	public static final int PLANT_PEA = 0x0002;
	// 跑道的对象的子弹
	public static final int PLANT_BULLET = 0x0003;
	// 跑道的对象的坚果
	public static final int PLANT_NUT = 0x0004;
	// 定义向日葵、豌豆的索引
	private int indexFlower;
	private int indexPea;
	private int indexNut;

	// 存储向日葵被创建的时间
	private long flowerCreateTime;
	// 存储豌豆被创建的时间
	private long peaCreateTime;

	public Plant(int locationX, int locationY, int state) {
		super(locationX, locationY, state);
		if (state == PLANT_FLOWER) {// 向日葵被创建的时候
			flowerCreateTime = System.currentTimeMillis();
		} else if (state == PLANT_PEA) {// 豌豆被创建的时候
			peaCreateTime = System.currentTimeMillis();
		}
	}

	@Override
	public void drawSelf(Canvas canvas) {
		if (state == PLANT_FLOWER) {
			canvas.drawBitmap(flowers[indexFlower++], locationX, locationY-(int)cellHeight/4,
					null);
			// 判断向日葵被创建的间隔时间
			long now = System.currentTimeMillis();
			if (now - flowerCreateTime > createSunTime) {
				System.out.println("创建阳光");
				addSun(this);
				flowerCreateTime = now;
			}

		} else if (state == PLANT_PEA) {
			canvas.drawBitmap(peas[indexPea++], locationX, locationY-(int)cellHeight/4, null);
			// 判断豌豆被创建的间隔时间
			long now = System.currentTimeMillis();
			if (now - peaCreateTime > createBulletTime) {
				System.out.println("创建子弹");
				addBullet(this);
				peaCreateTime = now;
			}
		}else if (state == PLANT_NUT) {
			if(this.getLifeValue()>=600)
			canvas.drawBitmap(nuts[0], locationX, locationY-(int)cellHeight/4, null);
			else if(this.getLifeValue()>=300)
				canvas.drawBitmap(nuts[1],locationX,locationY-(int)cellHeight/4,null);
			else
				canvas.drawBitmap(nuts[2],locationX,locationY-(int)cellHeight/4,null);
			// 判断豌豆被创建的间隔时间


		}
		else if (state == PLANT_BULLET) {
			// 说明子弹在右边已经消失
			if (this.locationX > screenWidth) {
				this.lifeValue = 0;
			} else {
				locationX += bulletMove;
				canvas.drawBitmap(bullet, locationX, locationY-(int)cellHeight/4, null);
			}
		} else {
			throw new RuntimeException("跑道里面没有此对象的状态");
		}
		if (indexFlower >= flowers.length) {
			this.indexFlower = 0;
		}
		if (indexPea >= peas.length) {
			this.indexPea = 0;
		}
		//if (indexNut >= nuts.length) {
		//	this.indexNut = 0;
		//}

	}

	// 跑道里面的豌豆在一定时间间隔里面自动创建子弹
	private void addBullet(Plant plant) {
		GramView.getInstance().addBullet(plant);
	}

	// 跑道里面的向日葵在一定时间间隔里面自动创建阳光
	private void addSun(Plant plant) {
		GramView.getInstance().addSun(plant);
	}

}
