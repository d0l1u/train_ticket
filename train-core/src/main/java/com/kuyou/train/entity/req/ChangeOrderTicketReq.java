package com.kuyou.train.entity.req;

import com.kuyou.train.entity.po.ChangeCpPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * ChangeTicketDto
 *
 * @author taokai3
 * @date 2018/11/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeOrderTicketReq {

    private String cpId;
    private String name;
    private String oldSubSequence;
    private String cardType;
    private String cardNo;
    private BigDecimal price;
    private String ticketType;
    private String seatType;
    private String boxName;
    private String seatName;
    private String changeSeatType;


    public ChangeOrderTicketReq(ChangeCpPo changeCpPo) {
        this.cpId = changeCpPo.getNewCpId();
        this.name = changeCpPo.getUserName();
        this.oldSubSequence = changeCpPo.getOldSubSequence();
        this.cardType = changeCpPo.getIdsType();
        this.cardNo = changeCpPo.getUserIds();
        this.ticketType = changeCpPo.getTicketType();
        this.seatType = changeCpPo.getSeatType();
        this.boxName = changeCpPo.getTrainBox();
        this.seatName = changeCpPo.getSeatNo();
        this.changeSeatType = changeCpPo.getChangeSeatType();
        this.price = changeCpPo.getBuyMoney();
    }

    public void setName(String name) {
        name = name.trim().toUpperCase();
        //萨代提·克依木
        if (name.contains(".")) {
            name = name.replaceAll("\\.", "·");
        }
        if (name.contains("。")) {
            name = name.replaceAll("。", "·");
        }
        this.name = name.trim();
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo.replaceAll("\\s", "").toUpperCase();
    }

    public static void main(String[] args) {
        ChangeOrderTicketReq changeTicketDto = new ChangeOrderTicketReq();
        changeTicketDto.setName("tao。kai");
        System.err.println(changeTicketDto.getName());
    }
}
