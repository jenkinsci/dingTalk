package io.jenkins.plugins;

import hudson.Extension;
import hudson.model.Cause.UserIdCause;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.User;
import hudson.model.listeners.RunListener;
import io.jenkins.plugins.enums.BuildStatusEnum;
import io.jenkins.plugins.enums.MsgTypeEnum;
import io.jenkins.plugins.enums.NoticeOccasionEnum;
import io.jenkins.plugins.model.BuildJobModel;
import io.jenkins.plugins.model.ButtonModel;
import io.jenkins.plugins.model.MessageModel;
import io.jenkins.plugins.service.impl.DingTalkServiceImpl;
import io.jenkins.plugins.tools.Utils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import jenkins.model.Jenkins;
import org.apache.commons.lang.StringUtils;

/**
 * @author liuwei
 * @date 2019/12/28 15:31
 * @desc freeStyle project、matrix project 触发
 */
@Extension
public class DingTalkRunListener extends RunListener<Run<?, ?>> {

  private DingTalkServiceImpl service = new DingTalkServiceImpl();

  private DingTalkGlobalConfig globalConfig = DingTalkGlobalConfig.getInstance();

  private final String rootPath = Jenkins.get().getRootUrl();

  public void send(Run<?, ?> run, TaskListener listener, BuildStatusEnum statusType) {
    Job<?, ?> job = run.getParent();
    UserIdCause userIdCause = run.getCause(UserIdCause.class);
    User user =
        userIdCause == null ||
            StringUtils.isEmpty(
                userIdCause.getUserId()
            ) ?
            User.getUnknown() :
            User.getById(
                userIdCause.getUserId(),
                true
            );

    // 项目信息
    String projectName = job.getFullDisplayName();
    String projectUrl = job.getAbsoluteUrl();

    // 构建信息
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String jobName = run.getDisplayName();
    String jobUrl = rootPath + run.getUrl();
    String duration = run.getDurationString();
    String executorName = user.getDisplayName();
    String executorMobile = user.getProperty(DingTalkUserProperty.class).getMobile();
    String datetime = formatter.format(run.getTimestamp().getTime());
    List<ButtonModel> btns = Utils.createDefaultBtns(jobUrl);

    List<String> result = new ArrayList<>();
    DingTalkJobProperty property = job.getProperty(DingTalkJobProperty.class);
    property.getCheckedNotifierConfigs().forEach(notifierConfig -> {
      String robotId = notifierConfig.getRobotId();
      Set<String> atMobiles = notifierConfig.getAtMobiles();
      String text = BuildJobModel.builder()
          .projectName(projectName)
          .projectUrl(projectUrl)
          .jobName(jobName)
          .jobUrl(jobUrl)
          .statusType(statusType)
          .duration(duration)
          .datetime(datetime)
          .executorName(executorName)
          .executorMobile(executorMobile)
          .build()
          .toMarkdown();
      System.out.println(text);
      MessageModel message = MessageModel.builder()
          .type(MsgTypeEnum.ACTION_CARD)
          .title("Jenkins 构建通知")
          .atMobiles(atMobiles)
          .text(text)
          .btns(btns)
          .build();
      String msg = service.send(robotId, message);
      if (msg != null) {
        result.add(msg);
      }
    });
    if (listener != null && !result.isEmpty()) {
      result.forEach(msg -> Utils.log(listener, msg));
    }
  }

  @Override
  public void onStarted(Run<?, ?> build, TaskListener listener) {
    if (
        globalConfig.getNoticeOccasions().contains(
            NoticeOccasionEnum.START.name()
        )
    ) {
      this.send(build, listener, BuildStatusEnum.START);
    }
  }

  @Override
  public void onCompleted(Run<?, ?> build, @Nonnull TaskListener listener) {
    BuildStatusEnum statusType = null;
    Set<String> noticeOccasions = globalConfig.getNoticeOccasions();
    Result result = build.getResult();
    if (Result.SUCCESS.equals(result)) {

      if (noticeOccasions.contains(NoticeOccasionEnum.SUCCESS.name())) {
        statusType = BuildStatusEnum.SUCCESS;
      }

    } else if (Result.FAILURE.equals(result)) {

      if (noticeOccasions.contains(NoticeOccasionEnum.FAILURE.name())) {
        statusType = BuildStatusEnum.FAILURE;
      }

    } else if (Result.ABORTED.equals(result)) {

      if (noticeOccasions.contains(NoticeOccasionEnum.ABORTED.name())) {
        statusType = BuildStatusEnum.ABORTED;
      }

    } else if (Result.UNSTABLE.equals(result)) {

      if (noticeOccasions.contains(NoticeOccasionEnum.UNSTABLE.name())) {
        statusType = BuildStatusEnum.UNSTABLE;
      }

    } else if (Result.NOT_BUILT.equals(result)) {

      if (noticeOccasions.contains(NoticeOccasionEnum.NOT_BUILT.name())) {
        statusType = BuildStatusEnum.NOT_BUILT;
      }

    } else {
      statusType = BuildStatusEnum.UNKNOWN;
    }

    if (statusType != null) {
      this.send(build, listener, statusType);
    }

  }

}
