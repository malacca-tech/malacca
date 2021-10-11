package org.malacca.dingtalk;

import lombok.Data;

import java.util.Date;

@Data
public class DingtalkToken {
    private String token;
    private Date date;
}
