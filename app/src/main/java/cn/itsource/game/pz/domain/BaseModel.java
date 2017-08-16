package cn.itsource.game.pz.domain;

import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class BaseModel {
	// 当前对象的坐标的位置:绘制图片只需要知道左上角的位置
	protected int locationX;
	protected int locationY;
	// 只要生命值小于1就表示此对象已经死亡
	protected int lifeValue = 10;
	// 矩形框对象,通过触屏传入的坐标判断是否选中了当前对象rect.contains(x, y)
	protected Rect rect;
	// 状态
	protected int state;

	public BaseModel(int locationX, int locationY, int state) {
		this.locationX = locationX;
		this.locationY = locationY;
		this.state = state;
	}

	public abstract void drawSelf(Canvas canvas);

	// 状态:区分是向日葵还是豌豆或者其他
	public int getLocationX() {
		return locationX;
	}

	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}

	public int getLocationY() {
		return locationY;
	}

	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}

	public int getLifeValue() {
		return lifeValue;
	}

	public void setLifeValue(int lifeValue) {
		this.lifeValue = lifeValue;
	}

	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
