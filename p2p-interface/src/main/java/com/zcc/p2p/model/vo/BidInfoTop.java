package com.zcc.p2p.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:投资排行榜
 * @author:zcc
 * @data:2019/3/1 0001
 */

@Data
public class BidInfoTop implements Serializable {

    private String phone;
    private Double score;

}
