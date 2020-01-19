package io.jenkins.plugins.enums;

import io.jenkins.plugins.Messages;
import lombok.Getter;
import lombok.ToString;

/**
 * @author liuwei
 * @date 2020/1/19 12:02
 * @desc 安全策略
 */

@ToString
public enum SecurityPolicyType {
  /**
   * ip 地址/段
   */
  IP(Messages.SecurityPolicyType_ip()),

  /**
   * 关键字
   */
  KEY(Messages.SecurityPolicyType_key()),

  /**
   * 加密
   */
  SECRET(Messages.SecurityPolicyType_secret());

  @Getter
  private String desc;

  SecurityPolicyType(String desc) {
    this.desc = desc;
  }
}