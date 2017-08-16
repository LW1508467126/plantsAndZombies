package cn.itsource.game.pz.domain;

//需要触屏就实现此接口
public interface TouchAble {
	boolean onTouch(int x, int y, int action);
}
