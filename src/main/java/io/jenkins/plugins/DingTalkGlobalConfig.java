package io.jenkins.plugins;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import io.jenkins.plugins.DingTalkRobotConfig.DingTalkRobotConfigDescriptor;
import io.jenkins.plugins.enums.NoticeOccasionEnum;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import jenkins.model.Jenkins;
import lombok.Getter;
import lombok.ToString;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * 全局配置
 *
 * @author liuwei
 */
@SuppressWarnings("unused")
@Getter
@ToString
@Extension
@Symbol("dingtalk")
public class DingTalkGlobalConfig extends Descriptor<DingTalkGlobalConfig>
    implements Describable<DingTalkGlobalConfig> {

  private static volatile DingTalkGlobalConfig instance;

  /**
   * 网络代理
   */
  private DingTalkProxyConfig proxyConfig;

  /**
   * 是否打印详细日志
   */
  private boolean verbose;

  /**
   * 通知时机
   */
  private Set<String> noticeOccasions = Arrays.stream(NoticeOccasionEnum.values()).map(Enum::name)
      .collect(Collectors.toSet());

  /**
   * 机器人配置列表
   */
  private ArrayList<DingTalkRobotConfig> robotConfigs = new ArrayList<>();

  /**
   * 获取网络代理
   *
   * @return proxy
   */
  public Proxy getProxy() {
    if (proxyConfig == null) {
      return null;
    }
    return proxyConfig.getProxy();
  }

  @DataBoundSetter
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  @DataBoundSetter
  public void setNoticeOccasions(Set<String> noticeOccasions) {
    this.noticeOccasions = noticeOccasions;
  }

  @DataBoundSetter
  public void setProxyConfig(DingTalkProxyConfig proxyConfig) {
    this.proxyConfig = proxyConfig;
  }

  @DataBoundSetter
  public void setRobotConfigs(ArrayList<DingTalkRobotConfig> robotConfigs) {
    this.robotConfigs = robotConfigs;
  }

  @DataBoundConstructor
  public DingTalkGlobalConfig(DingTalkProxyConfig proxyConfig, boolean verbose,
      Set<String> noticeOccasions, ArrayList<DingTalkRobotConfig> robotConfigs) {
    this.proxyConfig = proxyConfig;
    this.verbose = verbose;
    this.noticeOccasions = noticeOccasions;
    this.robotConfigs = robotConfigs;
  }

  public DingTalkGlobalConfig() {
    super(self());
    this.load();
  }

  @Override
  public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
    Object robotConfigObj = json.get("robotConfigs");

    if (robotConfigObj == null) {
      json.put("robotConfigs", new JSONArray());
    } else {
      JSONArray robotConfigs = JSONArray.fromObject(robotConfigObj);
      robotConfigs.removeIf(item -> {
        JSONObject jsonObject = JSONObject.fromObject(item);
        String webhook = jsonObject.getString("webhook");
        return StringUtils.isEmpty(webhook);
      });
    }
    System.out.println(json.toString());
    req.bindJSON(this, json);
    this.save();
    return super.configure(req, json);
  }

  //  public synchronized void doConfigure(StaplerRequest req, StaplerResponse rsp)
//      throws IOException, ServletException {
//    Jenkins.get().checkPermission(Jenkins.ADMINISTER);
//
//    JSONObject json = req.getSubmittedForm();
//    Object robotConfigObj = json.get("robotConfigs");
//
//    if (robotConfigObj == null) {
//      json.put("robotConfigs", new JSONArray());
//    } else {
//      JSONArray robotConfigs = JSONArray.fromObject(robotConfigObj);
//      robotConfigs.removeIf(
//          item -> {
//            JSONObject jsonObject = JSONObject.fromObject(item);
//            String webhook = jsonObject.getString("webhook");
//            return StringUtils.isEmpty(webhook);
//          });
//    }
//    System.out.println(json.toString());
//    req.bindJSON(this, json);
//    Jenkins.get().save();
//    FormApply.success(req.getContextPath() + "/manage").generateResponse(req, rsp, null);
//  }

  /**
   * 通知时机列表
   *
   * @return 通知时机
   */
  public NoticeOccasionEnum[] getAllNoticeOccasions() {
    return NoticeOccasionEnum.values();
  }

  @Override
  public Descriptor<DingTalkGlobalConfig> getDescriptor() {
    return this;
  }


  /**
   * `网络代理` 配置页面
   *
   * @return 网络代理配置页面
   */
  public DingTalkProxyConfig getDingTalkProxyConfigDescriptor() {
    return Jenkins.get().getDescriptorByType(DingTalkProxyConfig.class);
  }

  /**
   * `机器人` 配置页面
   *
   * @return 机器人配置页面
   */
  public DingTalkRobotConfigDescriptor getDingTalkRobotConfigDescriptor() {
    return Jenkins.get().getDescriptorByType(DingTalkRobotConfigDescriptor.class);
  }


  /**
   * 获取全局配置信息
   *
   * @return 全局配置信息
   */
  public static DingTalkGlobalConfig getInstance() {
    if (instance == null) {
      synchronized (DingTalkGlobalConfig.class) {
        if (instance == null) {
          instance = null;
        }
      }
    }
    return instance;
  }

}
