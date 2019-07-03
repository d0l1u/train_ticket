package com.l9e.transaction.job;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ChangeService;
import com.l9e.transaction.vo.ChangeInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 改签异步回调job
 *
 * @author caona
 */
@Component("changeNoticeJob")
public class ChangeNoticeJob {
    private static final Logger logger = Logger.getLogger(ChangeNoticeJob.class);

    @Resource
    private ChangeService changeService;

    /**
     * 改签结果通知
     */
    public void changeNotice() {
        /* 获取高铁管家回调通知列表 */

        List<ChangeInfo> notifyList = changeService.getNoticeChangeInfo(TrainConsts.GT_MERCHANT_ID);

        if (notifyList != null && notifyList.size() > 0) {
            logger.info("高铁管家改签回调准备开始");
            changeService.changeNotice(notifyList);
        } else {
            logger.info("高铁管家改签回调信息为空");
        }
    }

}
