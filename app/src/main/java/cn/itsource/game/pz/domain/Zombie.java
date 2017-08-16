package cn.itsource.game.pz.domain;

import static android.R.id.content;
import static cn.itsource.game.pz.Configs.attack;
import static cn.itsource.game.pz.Configs.cellHeight;

import static cn.itsource.game.pz.Configs.zombieMove;
import static cn.itsource.game.pz.Configs.zombies;
import  static cn.itsource.game.pz.Configs.over;
import android.graphics.Canvas;
import android.graphics.Rect;
import cn.itsource.game.pz.GramView;

//僵尸
public class Zombie extends BaseModel {
	private int indexZombie = 0;
	private int speed=zombieMove;
	public Zombie(int locationX, int locationY) {
		super(locationX, locationY, 0);
		this.rect = new Rect(locationX, locationY, locationX
				+ zombies[0].getWidth(), locationY + zombies[0].getHeight());
	}

	@Override
	public void drawSelf(Canvas canvas) {
		// 僵尸跑到屏幕的左边消失
		if (locationX < 0) {
			this.lifeValue = 0;
			over=true;
		} else {
			// 绘制矩形的位置
			// Paint paint = new Paint();
			// paint.setColor(Color.RED);
			// canvas.drawRect(rect, paint);
			locationX -= speed;
			if(speed!=0){
				canvas.drawBitmap(zombies[indexZombie++], locationX, locationY-(int)cellHeight/4,
						null);
			}
			if(speed==0){
				canvas.drawBitmap(attack[indexZombie++], locationX-attack[0].getWidth()/2, locationY-(int)cellHeight/2,null);
			}

			if (indexZombie > 5) {
				indexZombie = 0;
			}
			// 由僵尸自动发出碰撞检测
			GramView.getInstance().checkLife(this);
		}
	}
	public void setSpeed(int speed1){
		speed=speed1;
	}

}
