package cn.itsource.game.pz.domain;

import static cn.itsource.game.pz.Configs.cellHeight;
import static cn.itsource.game.pz.Configs.createZombieTime;
import static cn.itsource.game.pz.Configs.raceWayYpoints;
import static cn.itsource.game.pz.Configs.screenWidth;

import java.util.Random;

import cn.itsource.game.pz.GramView;

public class ZombieManager {
	private static Random random = new Random();
	private static long createTime;

	// 随机产生僵尸的代码
	public static void addZombie() {
		long now = System.currentTimeMillis();
		if (now - createTime > createZombieTime*2) {
			createTime = now;
			// 获取一个从0-4随机数
			int index = random.nextInt(5);
			int x = screenWidth;
			int y = raceWayYpoints[index] - cellHeight / 4;
			Zombie zombie = new Zombie(x, y);
			GramView.getInstance().addZombie(zombie);
		}
	}
}
