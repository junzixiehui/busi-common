package com.junzixiehui.application.trace.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author: qulibin
 * @description:
 * @date: 14:46 2017/12/29
 * @modify：
 */
@Setter
@Getter
@ToString
public class Span implements Serializable {

  private String traceId;
  private String id;
  private String parentId;

  private String serviceName;
  private String methodName;



  @Override
  public int hashCode() {
    int result = 17;
    result = result * 31 + id.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Span)) {
      return false;
    }

    Span that = (Span) obj;
    return this.traceId.equals(that.traceId) && this.id.equals(that.id);
  }
}
