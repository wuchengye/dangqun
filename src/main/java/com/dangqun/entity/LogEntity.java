package com.dangqun.entity;

import lombok.Data;

/**
 * @author wcy
 */

@Data
public class LogEntity {
    private Integer logId;
    private String logUser;
    private String logMethod;
    private String logParams;
    private String logReturn;
    private String logIp;
    private String logTime;
}
