package com.kuyou.train.kyfw.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kuyou.train.common.Constant;
import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.util.ParameterUtil;
import com.kuyou.train.entity.dto.LoginUserDto;
import com.kuyou.train.entity.dto.PassengerDelDto;
import com.kuyou.train.entity.dto.PassengerDto;
import com.kuyou.train.entity.dto.UserDto;
import com.kuyou.train.entity.kyfw.PassengerCommonData;
import com.kuyou.train.entity.kyfw.PassengerData;
import com.kuyou.train.entity.kyfw.PassengerParentData;
import com.kuyou.train.entity.kyfw.UserInfoData;
import com.kuyou.train.kyfw.api.LoginApi;
import com.kuyou.train.kyfw.api.PassengerApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PassengerApiRobot
 *
 * @author taokai3
 * @date 2018/12/10
 */
@Slf4j
@Service
public class PassengerApiRobot {

    @Resource
    private PassengerApi passengerApi;

    @Resource
    private LoginApi loginApi;


    /**
     * 查询常用联系人
     * @param name
     * @return
     * @throws IOException
     */
    public List<PassengerData> queryPassenger(String name) throws IOException {
        Map<String, Object> parameters = new LinkedHashMap<>(3);
        parameters.put("pageIndex", "1");
        parameters.put("pageSize", "200");
        if (StringUtils.isNotBlank(name)) {
            parameters.put("passengerDTO.passenger_name", name);
        }
        PassengerParentData data = passengerApi.queryPassenger(parameters).execute().body().getData();
        log.info("查询常用联系人 data:{}", data);
        List<PassengerData> datas = data.getDatas();
        if(CollectionUtils.isEmpty(datas)){
            return Lists.newArrayList();
        }
        return datas;
    }

    /**
     * 查询全部常用联系人，返回一个map
     *
     * @return
     * @throws IOException
     */
    public Map<String, List<PassengerData>> queryAllPassenger4Map() throws IOException {
        return queryPassenger("").stream().collect(Collectors.groupingBy(PassengerData::getPassengerIdNo));
    }

    /**
     * 删除常用联系人
     *
     * @param deleteDtos
     */
    public void deletePassenger(List<PassengerDelDto> deleteDtos) throws IOException {
        StringBuffer nameBuffer = new StringBuffer();
        StringBuffer idCardTypeBuffer = new StringBuffer();
        StringBuffer idCardNoBuffer = new StringBuffer();
        StringBuffer userSelfBuffer = new StringBuffer();
        deleteDtos.forEach(dto -> {
            nameBuffer.append(dto.getName()).append("#");
            idCardTypeBuffer.append(dto.getCardType());
            idCardNoBuffer.append(dto.getCardNo()).append("#");
            userSelfBuffer.append(dto.getUserSelf()).append("#");
        });

        LinkedHashMap<String, String> map = Maps.newLinkedHashMap();
        map.put("passenger_name", nameBuffer.toString());
        map.put("passenger_id_type_code", idCardTypeBuffer.toString());
        map.put("passenger_id_no", idCardNoBuffer.toString());
        map.put("isUserSelf", userSelfBuffer.toString());

        PassengerCommonData data = passengerApi.deletePassenger(map).execute().body().getData();
        log.info("删除常用联系人 data:{}", data);
        if (!data.getFlag()) {
            throw new TrainException(data.getMessage());
        }
    }

    /**
     * 删除常用联系人
     *
     * @param passengerDtoList
     */
    public void deletePassenger4Try(List<PassengerDto> passengerDtoList) throws IOException {
        try {
            deletePassenger(passengerDtoList.stream()
                    .map(dto -> PassengerDelDto.builder().name(dto.getName()).cardType(dto.getCardType())
                            .cardNo(dto.getCardNo()).build()).collect(Collectors.toList()));
        } catch (Exception e) {
            log.info("deletePassenger4Try 删除常用联系人异常", e);
        }
    }



    /**
     * 添加常用旅客
     *
     * @param add
     */
    public void addPassenger(PassengerDto add) throws IOException {
        PassengerCommonData data = passengerApi.addPassenger(ParameterUtil.convertToMap(add)).execute().body()
                .getData();
        log.info("添加常用联系人 name:{}, data:{}", add.getName(), data);
        if (!data.getFlag()) {
            String message = data.getMessage();
            //该联系人已存在，请使用不同的姓名和证件!
            if (StringUtils.isNotBlank(message) && message.contains("该联系人已存在")) {
                log.info("出现message:该联系人已存在，请使用不同的姓名和证件!,默认乘客:{}添加成功", add.getName());
            } else {
                throw new TrainException(message);
            }
        }
    }

    /**
     * 修改常用旅客
     *
     * @param edit
     */
    public void editPassenger(PassengerDto edit) throws IOException {
        PassengerCommonData data = passengerApi.editPassenger(ParameterUtil.convertToMap(edit)).execute().body()
                .getData();
        log.info("修改常用联系人 name:{}, data:{}", edit.getName(), data);
        if (!data.getFlag()) {
            throw new TrainException(data.getMessage());
        }
    }

    /**
     * 修改账号所属人
     *
     * @param edit
     */
    public void editUser(PassengerDto edit) throws IOException {
        UserInfoData userInfoData = loginApi.initQueryUserInfoApi().execute().body().getData();
        UserDto userDto = userInfoData.getUserDTO();
        LoginUserDto loginUserDto = userDto.getLoginUserDTO();
        LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
        map.put("userDTO.loginUserDTO.user_name", loginUserDto.getUser_name());
        map.put("userDTO.loginUserDTO.name", loginUserDto.getName());
        map.put("userDTO.loginUserDTO.id_no", loginUserDto.getId_no());
        map.put("GAT_valid_date_end", loginUserDto.getGat_valid_date_end());
        map.put("userDTO.country_code", userDto.getCountry_code());
        map.put("userDTO.password", "taokai1017");
        map.put("userDTO.mobile_no", userDto.getMobile_no());
        map.put("userDTO.phone_no", userDto.getPhone_no());
        map.put("userDTO.email", userDto.getEmail());
        map.put("userDTO.address", userDto.getEmail());
        map.put("userDTO.postalcode", userDto.getPostalcode());
        map.put("userDTO.studentInfoDTO.school_name", empty(edit.getStuToStationName()));
        map.put("userDTO.studentInfoDTO.school_code", empty(edit.getSchoolCode()));
        map.put("userDTO.studentInfoDTO.department", "");
        map.put("userDTO.studentInfoDTO.school_class", "");
        map.put("userDTO.studentInfoDTO.student_no", empty(edit.getStudentNo()));
        map.put("userDTO.studentInfoDTO.preference_card_no", "");
        map.put("userDTO.studentInfoDTO.preference_from_province_code", "");
        map.put("userDTO.studentInfoDTO.preference_to_province_code", "");
        map.put("userDTO.studentInfoDTO.preference_from_station_code", empty(edit.getStuFromStationCode()));
        map.put("userDTO.studentInfoDTO.preference_from_station_name", empty(edit.getStuFromStationName()));
        map.put("userDTO.studentInfoDTO.preference_to_station_code", empty(edit.getStuToStationCode()));
        map.put("userDTO.studentInfoDTO.preference_to_station_name", empty(edit.getStuToStationName()));
        map.put("userDTO.is_active", userDto.getIs_active());
        map.put("userDTO.check_code", "");
        map.put("userDTO.revSm_code", userDto.getRevSm_code());
        map.put("userDTO.flag", "");
        map.put("userDTO.last_login_time", userDto.getLast_login_time());
        map.put("userDTO.first_letter", "");
        map.put("userDTO.user_id", userDto.getUser_id());
        map.put("userDTO.phone_flag", userDto.getPhone_flag());
        map.put("userDTO.encourage_flag", userDto.getEncourage_flag());
        map.put("userDTO.needModifyPassword", "");
        map.put("userDTO.needModifyEmail", userDto.getNeedModifyEmail());
        map.put("userDTO.flag_member", userDto.getFlag_member());
        map.put("userDTO.loginUserDTO.channel", loginUserDto.getLogin_channel());
        map.put("userDTO.loginUserDTO.agent_contact", loginUserDto.getAgent_contact());
        map.put("userDTO.loginUserDTO.user_id", loginUserDto.getLogin_id());
        map.put("userDTO.loginUserDTO.refundLogin",
                StringUtils.isBlank(loginUserDto.getRefundLogin()) ? "" : loginUserDto.getRefundLogin());
        map.put("userDTO.studentInfoDTO.student_name", "");
        map.put("userDTO.studentInfoDTO.city_name", "");
        map.put("userDTO.studentInfoDTO.city_code", "");
        map.put("isInputPassword", "A");
        map.put("userDTO.loginUserDTO.user_type", loginUserDto.getUser_type());
        map.put("userDTO.studentInfoDTO.province_code", empty(edit.getProvinceCode()));
        map.put("userDTO.studentInfoDTO.school_system", empty(edit.getSchoolName()));
        map.put("userDTO.studentInfoDTO.enter_year", empty(edit.getEnterYear()));

        PassengerCommonData data = passengerApi.editUser(map).execute().body().getData();
        log.info("修改账号所属人 name:{}, data:{}", edit.getName(), data);
        if (data.getExistError().equals(Constant.Y)) {
            throw new TrainException(data.getMessageInfo());
        }
    }

    private String empty(String str) {
        return StringUtils.isBlank(str) ? "" : str;
    }


}
