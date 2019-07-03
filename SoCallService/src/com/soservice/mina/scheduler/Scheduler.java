package com.soservice.mina.scheduler;


import java.util.Map;


public interface Scheduler<K> {
	/**
	 * 初始化调度器
	 * 
	 * @param map
	 */
	public void init(Map<String, K> map);

	/**
	 * 根据轮询算法获取下一个对象
	 * 
	 * @return
	 */
	public K getNextObject();

	/**
	 * 根据索引下标获取对象
	 * 
	 * @param index
	 * @return
	 */
	public K getObject(int index);
}
