package com.hu.tookit.algorithm;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 关于队列的一些小应用
 * @author xiaozhi009
 *
 */
public class AlQueue {
	public static void main(String[] args) {
		handleHomework();
	}

	/**
	 * 模拟单线程处理作业，多余的放入排队队列中
	 * 只能调用一次start方法，否则会报错 java.lang.IllegalThreadStateException
	 * 可以多次调用run方法。
	 */
	public static void handleHomework() {
		Queue<Integer> queue = new ArrayBlockingQueue<>(16);
		AlQueue alQueue = new AlQueue();
		AlQueue.HomeworkThread homeworkThread = alQueue.new HomeworkThread();
		Thread work = new Thread(homeworkThread, "作业线程");
		work.start();
		for (int i = 0; i < 20; i++) {
			if (queue.size() == 16) {
				System.out.println("人挤爆了，暂时无法安排");
				continue;
			}
			if (work.isAlive()) {
				queue.add(1);
				continue;
			}
			homeworkThread.run();
		}
		while(!queue.isEmpty()) {
			work.start();
			if (!work.isAlive()) {
				homeworkThread.run();
				queue.remove();
			}
		}
	}

	class HomeworkThread implements Runnable {

		private volatile int modIndex = 0;

		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("执行一次任务" + (++modIndex));
		}
		
	}
}
