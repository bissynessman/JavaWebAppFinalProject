package tvz.jwafp.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwafp")
public class AppProperties {
    private String applicationUrl;

    private String emailAgent;
    private String emailPassword;
    private String emailCron;
    private String mailHost;
    private int mailPort;

    private String cronApiKey;

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public String getEmailAgent() {
        return emailAgent;
    }

    public void setEmailAgent(String emailAgent) {
        this.emailAgent = emailAgent;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getEmailCron() {
        return emailCron;
    }

    public void setEmailCron(String emailCron) {
        this.emailCron = emailCron;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public int getMailPort() {
        return mailPort;
    }

    public void setMailPort(int mailPort) {
        this.mailPort = mailPort;
    }

    public String getCronApiKey() {
        return cronApiKey;
    }

    public void setCronApiKey(String cronApiKey) {
        this.cronApiKey = cronApiKey;
    }
}
