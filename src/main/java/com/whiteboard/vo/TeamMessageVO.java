package com.whiteboard.vo;

import java.util.Date;

public class TeamMessageVO {
    private Integer messageId;

    private Integer sendUid;

    private Integer teamId;

    private String sendTime;

    private String description;

    private Integer type;

    private Integer priority;

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getSendUid() {
        return sendUid;
    }

    public void setSendUid(Integer sendUid) {
        this.sendUid = sendUid;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

}