package com.soservice.mina.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 轮询调度器
 * 
 * @author zhangyou
 * @param <V>
 * 
 */
public class PollingScheduler<K> implements Scheduler<K> {
	/**
	 * 上一次的位置
	 */
	private int currentIndex = -1;
	private int size = -1;
	/**
	 * 对象集合
	 */
	private List<K> objList = null;

	public PollingScheduler() {
	}

	public PollingScheduler(Map<String, K> map) {
		init(map);
	}

	/**
	 * 初始化轮询调度器
	 * 
	 * @param map
	 */
	public synchronized void init(Map<String, K> map) {
		objList = new ArrayList<K>();
		for (Entry<String, K> keySet : map.entrySet()) {
			objList.add(keySet.getValue());
		}
		//currentIndex = -1;
		size = objList.size();
	}

	/**
	 * 根据轮询算法获取下一个对象
	 * 
	 * @return
	 */
	public synchronized K getNextObject() {
		// 如果集合有值
		if (size > 0) {
			return getObject(++currentIndex);
		} else {
			return null;
		}

	}

	/**
	 * 根据索引下标获取对象
	 * 
	 * @param index
	 * @return
	 */
	public K getObject(int index) {
		currentIndex = (index >= size - 1) ? 0 : index;
		return objList.get(index);

	}

}
