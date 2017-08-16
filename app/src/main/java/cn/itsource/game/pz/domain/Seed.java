package cn.itsource.game.pz.domain;

import static cn.itsource.game.pz.Configs.flowerScore;
import static cn.itsource.game.pz.Configs.flowers;
import static cn.itsource.game.pz.Configs.nutScore;
import static cn.itsource.game.pz.Configs.nuts;
import static cn.itsource.game.pz.Configs.peaScore;
import static cn.itsource.game.pz.Configs.peas;
import static cn.itsource.game.pz.Configs.score;
import static cn.itsource.game.pz.Configs.seedFlower;
import static cn.itsource.game.pz.Configs.seedNut;
import static cn.itsource.game.pz.Configs.seedPea;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import cn.itsource.game.pz.GramView;

//面板上的对象、等待安放到跑道的对象
public class Seed extends BaseModel implements TouchAble {
	// 面板上的向日葵
	public static final int SEED_FLOWER = 0x0001;
	// 面板上的豌豆
	public static final int SEED_PEA = 0x0002;
	//面板上的坚果
	public static final int SEED_NUT=0x0005;
	//等待安放的坚果
	public static final  int EMPLACE_NUT=0x0006;
	// 等待安放到跑道的向日葵
	public static final int EMPLACE_FLOWER = 0x0003;
	// 等待安放到跑道的豌豆
	public static final int EMPLACE_PEA = 0x0004;

	public Seed(int locationX, int locationY, int state) {
		super(locationX, locationY, state);
	}

	@Override
	public void drawSelf(Canvas canvas) {
		// 实例化矩形位置的时候：4个参数都是坐标值,后两个参数必须加上起点坐标
		if (state == SEED_FLOWER) {// 面板上面的向日葵
			this.rect = new Rect(locationX, locationY, locationX
					+ seedFlower.getWidth(), locationY + seedFlower.getHeight());
			canvas.drawBitmap(seedFlower, locationX, locationY, null);
		} else if (state == SEED_PEA) {// 面板上面的豌豆
			this.rect = new Rect(locationX, locationY, locationX
					+ seedPea.getWidth(), locationY + seedPea.getHeight());
			canvas.drawBitmap(seedPea, locationX, locationY, null);
		}
		else if(state==SEED_NUT){//坚果
			this.rect=new Rect(locationX,locationY,locationX+
					seedNut.getHeight(),locationY+seedNut.getHeight());
			canvas.drawBitmap(seedNut,locationX,locationY,null);
		}
		else if (state == EMPLACE_FLOWER) {// 等待安放到跑道的向日葵
			this.rect = new Rect(locationX, locationY, locationX
					+ flowers[0].getWidth(), locationY + flowers[0].getHeight());
			canvas.drawBitmap(flowers[0], locationX, locationY, null);
		} else if (state == EMPLACE_PEA) {// 等待安放到跑道的豌豆
			this.rect = new Rect(locationX, locationY, locationX
					+ peas[0].getWidth(), locationY + peas[0].getHeight());
			canvas.drawBitmap(peas[0], locationX, locationY, null);
		}
		else if(state==EMPLACE_NUT){//等待安放的坚果
			this.rect = new Rect(locationX, locationY, locationX
					+ nuts[0].getWidth(), locationY + nuts[0].getHeight());
			canvas.drawBitmap(nuts[0], locationX, locationY, null);
		}
		else {
			throw new RuntimeException("不能找到对应状态的对象:" + state);
		}
		// 绘制矩形的位置
		// Paint paint = new Paint();
		// paint.setColor(Color.RED);
		// canvas.drawRect(rect, paint);
	}

	@Override
	public boolean onTouch(int x, int y, int action) {
		System.out.println("0000000000000000" + rect);
		if (rect != null && rect.contains(x, y)) {
			if (this.state == SEED_FLOWER) {
				if (score >= flowerScore) {
					// 表示选中了面板上面的向日葵或者豌豆，添加等待安放到跑道的向日葵或者豌豆
					addEmplace(this);
				}
				return true;// 触屏到了当前对象
			} else if (this.state == SEED_PEA) {
				if (score >= peaScore) {
					// 表示选中了面板上面的向日葵或者豌豆，添加等待安放到跑道的向日葵或者豌豆
					addEmplace(this);
				}
				return true;// 触屏到了当前对象
			} else if(this.state==SEED_NUT){
				if(score>=nutScore){
					addEmplace(this);
				}
				return true;
			}
			else if (this.state == EMPLACE_FLOWER
					|| this.state == EMPLACE_PEA
					||this.state==EMPLACE_NUT) {
				if (action == MotionEvent.ACTION_DOWN) {
					System.out.println("按下");
				}
				if (action == MotionEvent.ACTION_UP) {
					System.out.println("按下之后的松开");
					// 把等待安放状态的对象清除，换成跑道上面的对象
					addPlant(this);
				}
				if (action == MotionEvent.ACTION_MOVE) {
					System.out.println("按下之后的移动");
					// 一直朝右下角的方向移动
					// this.locationX = x;
					// this.locationY = y;
					// 修改这个值
					this.locationX = x - flowers[0].getWidth() / 2;
					this.locationY = y - flowers[0].getHeight() / 2;
					this.rect.offsetTo(locationX, locationY);
				}
				return true;// 触屏到了当前对象
			}
		}
		return false;
	}

	// 选中了面板上面的向日葵或者豌豆之后，添加等待安放到跑道的向日葵或者豌豆
	private void addEmplace(Seed seed) {
		GramView.getInstance().addEmplace(seed);
	}

	// 把等待安放状态的对象清除，换成跑道上面的对象
	private void addPlant(Seed seed) {
		GramView.getInstance().addPlant(seed);
	}
}
