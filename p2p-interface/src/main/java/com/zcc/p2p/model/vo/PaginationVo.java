package com.zcc.p2p.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @description:分页VO
 * @author:zcc
 * @data:2019/2/23 0023
 */
public class PaginationVo<T> implements Serializable {

    private List<T> dataList;

    private Long totalRows;

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public Long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Long totalRows) {
        this.totalRows = totalRows;
    }
}
