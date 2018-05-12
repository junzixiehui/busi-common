package com.junzixiehui.application.notify.dingding;



import com.google.common.collect.Maps;
import com.junzixiehui.application.core.json.FastJson;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * @author: qulibin
 * @description:
 * @date: 15:25 2017/12/22
 * @modify：
 */
@Builder
@Getter
public class DingTalkMarkdownMessage {
    private String title;
    private String text;

    @Override
    public String toString() {
        Map<String, Object> requestMap = Maps.newLinkedHashMap();
        requestMap.put("msgtype", "markdown");

        Map<String, String> markdown = Maps.newLinkedHashMap();
        markdown.put("title", this.title);

        //title不显示，必须用#加在text里面
        String text = "##### " + this.title + "\n" + this.text;
        markdown.put("text", text);
        requestMap.put("markdown", markdown);

        return FastJson.object2JsonStr(requestMap);
    }
}
