package org.malacca.dingtalk;

import lombok.Data;

@Data
public class DingtalkCard {
    private String title;
    private String markdown;
    private String single_title;
    private String single_url;
}
