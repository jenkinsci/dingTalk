package io.jenkins.plugins;

import io.jenkins.plugins.enums.BuildStatusType;
import io.jenkins.plugins.enums.NoticeOccasionType;
import io.jenkins.plugins.model.BuildMessage;
import io.jenkins.plugins.service.impl.DingTalkServiceImpl;
import hudson.Extension;
import hudson.model.Cause.UserIdCause;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import hudson.tasks.Publisher;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nonnull;
import jenkins.model.Jenkins;

/**
 * @author liuwei
 * @date 2020/1/19 17:06
 * @desc 构建监听器
 */
@Extension
public class DingTalkListener extends RunListener<FreeStyleBuild> {

  private final String rootPath = Jenkins.get().getRootUrl();

  private DingTalkServiceImpl service = new DingTalkServiceImpl();

  private DingTalkGlobalConfig globalConfig = DingTalkGlobalConfig.getInstance();


  public DingTalkListener() {
    super(FreeStyleBuild.class);
  }

  public void send(FreeStyleBuild build, TaskListener listener, BuildStatusType statusType) {
    UserIdCause user = build.getCause(UserIdCause.class);
    if (user == null) {
      user = new UserIdCause();
    }
    FreeStyleProject project = build.getProject();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    for (Publisher publisher : project.getPublishersList()) {
      if (publisher instanceof DingTalkNotifier) {
        DingTalkNotifier notifier = (DingTalkNotifier) publisher;
        String jobUrl = rootPath + build.getUrl();
        BuildMessage message = BuildMessage.builder()
            .projectName(project.getFullDisplayName())
            .projectUrl(project.getAbsoluteUrl())
            .jobName(build.getDisplayName())
            .jobUrl(jobUrl)
            .statusType(statusType)
            .duration(build.getDurationString())
            .executorName(user.getUserName())
            .executorPhone(user.getShortDescription())
            .datetime(formatter.format(build.getTimestamp().getTime()))
            .detailUrl(jobUrl + "/console")
            .build();
        Collection<String> result = service.send(notifier, message);
        if (listener != null && !result.isEmpty()) {
          listener.getLogger()
              .println("======================== DingTalk ========================");
          result.forEach(listener::error);
        }
      }
    }
  }


  @Override
  public void onStarted(FreeStyleBuild build, TaskListener listener) {
    if (
        globalConfig.getNoticeOccasions().contains(
            NoticeOccasionType.START.name()
        )
    ) {
      this.send(build, listener, BuildStatusType.START);
    }
  }


  @Override
  public void onCompleted(FreeStyleBuild build, @Nonnull TaskListener listener) {
    BuildStatusType statusType = null;
    Set<String> noticeOccasions = globalConfig.getNoticeOccasions();
    Result result = build.getResult();
    if (Result.SUCCESS.equals(result)) {

      if (noticeOccasions.contains(NoticeOccasionType.SUCCESS.name())) {
        statusType = BuildStatusType.SUCCESS;
      }

    } else if (Result.FAILURE.equals(result)) {

      if (noticeOccasions.contains(NoticeOccasionType.FAILURE.name())) {
        statusType = BuildStatusType.FAILURE;
      }

    } else if (Result.ABORTED.equals(result)) {

      if (noticeOccasions.contains(NoticeOccasionType.ABORTED.name())) {
        statusType = BuildStatusType.ABORTED;
      }

    } else if (Result.UNSTABLE.equals(result)) {

      if (noticeOccasions.contains(NoticeOccasionType.UNSTABLE.name())) {
        statusType = BuildStatusType.UNSTABLE;
      }

    } else if (Result.NOT_BUILT.equals(result)) {

      if (noticeOccasions.contains(NoticeOccasionType.NOT_BUILT.name())) {
        statusType = BuildStatusType.NOT_BUILT;
      }

    } else {
      statusType = BuildStatusType.UNKNOWN;
    }

    if (statusType != null) {
      this.send(build, listener, statusType);
    }

  }

}