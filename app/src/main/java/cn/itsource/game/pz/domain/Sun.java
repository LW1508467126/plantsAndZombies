package cn.itsource.game.pz.domain;

import static cn.itsource.game.pz.Configs.score;
import static cn.itsource.game.pz.Configs.seedbankX;
import static cn.itsource.game.pz.Configs.sun;
import static cn.itsource.game.pz.Configs.sunDeadTime;
import static cn.itsource.game.pz.Configs.sunMove;
import static cn.itsource.game.pz.Configs.sunScore;
import android.graphics.Canvas;
import android.graphics.Rect;

//阳光:在一定的时间范围内，没有做收集自动死亡
public class Sun extends BaseModel implements TouchAble {
	private long sunCreateTime;
	// 阳光收集的时候移动的速度
	private float moveX;
	private float moveY;
	// 阳光的状态:移动的状态，显示状态(默认)
	private static final int SUN_SHOW = 0x0001;
	private static final int SUN_MOVE = 0x0002;

	public Sun(int locationX, int locationY) {
		super(locationX, locationY, SUN_SHOW);
		this.sunCreateTime = System.currentTimeMillis();
	}

	@Override
	public void drawSelf(Canvas canvas) {
		this.rect = new Rect(locationX, locationY, locationX + sun.getWidth(),
				locationY + sun.getHeight());
		if (this.state == SUN_SHOW) {
			long now = System.currentTimeMillis();
			if (now - sunCreateTime > sunDeadTime) {
				this.lifeValue = 0;
			} else {
				canvas.drawBitmap(sun, locationX, locationY, null);
			}
		} else {// 处于收集的过程中
			if (locationY < 0) {// 收集完成,跑到手机的顶部
				this.lifeValue = 0;
				System.out.println("阳光成功被收集，加入分值");
				score += sunScore;
			} else {// 处于收集的移动中
				this.locationX -= moveX;
				this.locationY -= moveY;
				canvas.drawBitmap(sun, locationX, locationY, null);
			}
		}

	}

	@Override
	public boolean onTouch(int x, int y, int action) {
		if (rect != null && rect.contains(x, y)) {
			// 计算当前阳光的位置到面板收集阳光的距离
			int sunX = this.locationX - seedbankX;
			int sunY = this.locationY - 0;
			moveX = sunX / sunMove;
			moveY = sunY / sunMove;
			this.state = SUN_MOVE;
		}
		return false;
	}

}
