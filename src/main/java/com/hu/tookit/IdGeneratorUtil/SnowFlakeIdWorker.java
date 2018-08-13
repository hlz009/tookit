package com.hu.tookit.IdGeneratorUtil;

/**
 * SnowFlake id生成算法
 * @author xiaozhi009
 *
 */
public class SnowFlakeIdWorker {
	private final long workerId;//工作机器的编号id
	private final static long TWEPOCH = 1288834974657L;
	private long sequence = 0L;
	private final static long workerIdBits = 4L;
	public final static long maxWorkerId = -1L ^ -1L << workerIdBits;
	private final static long sequenceBits = 10L;
	private final static long workerIdShift = sequenceBits;
	private final static long timeLeftShift = sequenceBits + workerIdBits;
	public final static long sequenceMask = -1L ^ -1L << sequenceBits;
	private long lastTimeMillis = -1L;

	public SnowFlakeIdWorker(final long workerId) {
		super();
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format(
			"worker Id can't be greater than %d or less than 0",
			maxWorkerId));
		}
		this.workerId = workerId;
	}

	public synchronized long nextId() {
	    long currentTimeMillis = System.currentTimeMillis();
	    if (currentTimeMillis < this.lastTimeMillis) {
	        throw new RuntimeException(String.format("clock is moving backwards. Rejecting requests until %d.", lastTimeMillis));
	    }

	    if (currentTimeMillis == this.lastTimeMillis) {
	        sequence = (sequence + 1) & sequenceMask;
	        if (sequence == 0) {
	            while (currentTimeMillis <= lastTimeMillis) {
	                currentTimeMillis = System.currentTimeMillis();
	            }
	        }
	    } else {
	        sequence = 0;
	    }

	    lastTimeMillis = currentTimeMillis;
	    return ((currentTimeMillis - TWEPOCH) << timeLeftShift) |
//	        (dataCenterId << dataCenterLShift) |
	        (workerId << workerIdShift) |
	        sequence;
	}

	public static void main(String[] args) {
		SnowFlakeIdWorker sfIdWorker1 = new SnowFlakeIdWorker(0);
		for (int i = 0; i <= 1000; i++) {
			System.out.println(i + " --> " + sfIdWorker1.nextId() + "(0)");
		}
	}
}
