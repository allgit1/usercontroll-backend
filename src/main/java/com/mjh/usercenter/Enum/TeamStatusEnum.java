package com.mjh.usercenter.Enum;

/**
 * encoding='utf-8'
 *队伍状态枚举
 * @author mjh
 * date:2023-05-04 14:42
 **/
public enum TeamStatusEnum {
    PUBLIC(0,"公开"),
    PRIVATE(1,"私有"),
    SECRET(2,"加密");

    private int value;
    private String text;

    public static TeamStatusEnum getTeamStatusEnum(Integer value){
        if (value == null){
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum teamStatusEnum : values) {
            if (value==teamStatusEnum.getValue()){
                return teamStatusEnum;
            }
        }
        return null;

    }

    TeamStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
