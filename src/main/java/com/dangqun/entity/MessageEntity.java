package com.dangqun.entity;

import lombok.Data;

/**
 * @author wcy
 */
@Data
public class MessageEntity {
    private Integer mesId;
    private String mesName;
    private String mesContent;
    private String mesSendUser;
    private String mesRecUser;
    private Integer mesType;
    private String mesBeginTime;
    private String mesEndTime;
    private Integer mesStatus;
    private String mesConfirmUser;

    @Override
    public String toString() {
        return "{" +
                "mesId:" + mesId +
                ", mesName:'" + mesName + '\'' +
                ", mesContent:'" + mesContent + '\'' +
                ", mesSendUser:'" + mesSendUser + '\'' +
                ", mesRecUser:'" + mesRecUser + '\'' +
                ", mesType:" + mesType +
                ", mesBeginTime:'" + mesBeginTime + '\'' +
                ", mesEndTime:'" + mesEndTime + '\'' +
                ", mesStatus:" + mesStatus +
                ", mesConfirmUser:'" + mesConfirmUser + '\'' +
                '}';
    }
}
