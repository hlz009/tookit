package com.hu.tookit.lesson.lesson6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hu.tookit.algorithm.datastructure.BinaryHeap;

/**
 * 简单模拟银行事件处理
 * 生产-消费者模式  一个生产线程（类似顾客到来排队，请求处理业务）
 * 一个消费线程（类似银行处理完，顾客离开）
 * 目前BinaryHeap不是带锁的优先队列 
 * @author xiaozhi009
 *
 */
public class BankSimulation {
	private BinaryHeap<Integer> myQueue = new BinaryHeap<Integer>(10);

	private class Customer implements Runnable {
		@Override
		public void run() {
			// 加入到银行等待的业务
			while (true) {
				if (myQueue.size() == 10) {
					System.out.println("排队到极限，请稍后再来");
					continue;
				}
				System.out.println("进入银行开始排队，等候处理");
				myQueue.insert(1);
				System.out.println(myQueue);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class Banker implements Runnable {
		@Override
		public void run() {
			// 银行处理完业务
			while (true) {
				if (myQueue.size() == 0) {
					System.out.println("银行暂时没有业务处理");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				System.out.println("银行处理完毕，准备离开");
				myQueue.removeMin();
				System.out.println(myQueue);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		ExecutorService service = Executors.newFixedThreadPool(2);
		BankSimulation bankSimulation = new BankSimulation();
		service.execute(bankSimulation.new Customer());
		service.execute(bankSimulation.new Banker());
		service.shutdown();
	}
}
