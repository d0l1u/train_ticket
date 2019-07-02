/**
 * 
 */
package com.l9e.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.l9e.vo.WorkerVo;

/**
 *  @author meizs
 *	基于内存的机器人队列
 */
public class WorkerQueue {
	
	private static Logger  logger=Logger.getLogger(WorkerQueue.class);
	
    public static final int  QUEUE_MAX_SIZE = 10000;
	
    private static volatile  WorkerQueue  instance = null;
	
	public static WorkerQueue getInstance(){
		
		if (instance ==null) {
			synchronized (WorkerQueue.class) {
				if (instance ==null) {
					instance=new WorkerQueue();
					logger.info("*******初始化成功*******");
				}
			}
			
		}
		return instance;
	}
	
	private BlockingQueue<WorkerVo> blockingQueue=new LinkedBlockingQueue<WorkerVo>(QUEUE_MAX_SIZE);
	
	
	/**
	 *入队列
	 */
	public boolean push(WorkerVo workerVo) {
           return this.blockingQueue.offer(workerVo);
    }

	/**
	 * 出队列
	 */
	public WorkerVo poll() {
		WorkerVo result = null;
        try {
            result = this.blockingQueue.take();
        } catch (InterruptedException e) {
            logger.info("出队列失败", e);
        }
        return result;
    }
	
	public int size() {
        return this.blockingQueue.size();
    }
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		
		WorkerQueue.getInstance().push(null);
		
		
		WorkerVo workerVo=WorkerQueue.getInstance().poll();
		
		
		
		

	}

}
