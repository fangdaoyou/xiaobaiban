package com.whiteboard.pojo;

import java.util.Date;

public class Task {
    private Integer taskId;

    private Integer teamId;

    private Date createTime;

    private Date ddlTime;

    private Date finishTime;

    private String topic;

    private String description;

    private Integer sponsorId;

    private Integer state;

    private Integer priority;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDdlTime() {
        return ddlTime;
    }

    public void setDdlTime(Date ddlTime) {
        this.ddlTime = ddlTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic == null ? null : topic.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(Integer sponsorId) {
        this.sponsorId = sponsorId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", teamId=" + teamId +
                ", createTime=" + createTime +
                ", ddlTime=" + ddlTime +
                ", finishTime=" + finishTime +
                ", topic='" + topic + '\'' +
                ", description='" + description + '\'' +
                ", sponsorId=" + sponsorId +
                ", state=" + state +
                ", priority=" + priority +
                '}';
    }
}