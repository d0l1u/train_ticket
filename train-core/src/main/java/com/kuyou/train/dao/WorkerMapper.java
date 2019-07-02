package com.kuyou.train.dao;

import com.kuyou.train.entity.po.WorkerPo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * WorkerMapper
 *
 * @author taokai3
 * @date 2018/11/21
 */
public interface WorkerMapper {

    /**
     * 查询可用机器人
     *
     * @param type
     * @param free
     * @return
     */
    List<WorkerPo> selectAllAvailable(@Param("type") String type, @Param("status") String free);

    /**
     * 乐观锁
     *
     * @param workerId
     * @param status
     * @param preStatus
     * @return
     */
    int updateStatusPre(@Param("workerId") Integer workerId, @Param("status") String status,
            @Param("preStatus") String preStatus);

    /**
     * 更新状态
     *
     * @param workerId
     * @param status
     * @return
     */
    int updateStatus(@Param("workerId") Integer workerId, @Param("status") String status);

    /**
     * 卡状态
     *
     * @param status
     * @param time
     * @return
     */
    List<WorkerPo> selectStuck(@Param("status") String status, @Param("time") Date time);
}
