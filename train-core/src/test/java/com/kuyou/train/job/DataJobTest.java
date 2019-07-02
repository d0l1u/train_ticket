package com.kuyou.train.job;

import com.kuyou.train.MvcBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * DataJobTest
 *
 * @author taokai3
 * @date 2018/11/1
 */
@Slf4j
public class DataJobTest extends MvcBaseTest {

    @Resource
    private UpdateStationJob updateStationJob;

    @Resource
    private UpdateSchoolJob updateSchoolJob;

    @Resource
    private UpdateCityJob updateCityJob;

    @Test
    public void updateStation() {
        updateStationJob.updateStation();
    }

    @Test
    public void updateSchool() {
        updateSchoolJob.updateSchool();
    }

    @Test
    public void updateCity() {
        updateCityJob.updateCity();
    }

}
