package com.mjh.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * encoding='utf-8'
 *
 * @author mjh
 * date:2023-05-02 14:58
 **/
@Data
public class PageRequest implements Serializable {
    private static final long serialVersionUID = -4162304142710323660L;
    /**
     * 当前为第几页
     */
    protected int PageNum=1;
    /**
     * 每页的大小
     */
    protected int PageSize=10;
}
