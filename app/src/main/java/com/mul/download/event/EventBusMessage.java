package com.mul.download.event;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.event
 * @ClassName: EventBusMessage
 * @Author: zdd
 * @CreateDate: 2019/8/15 16:18
 * @Description: EventBus的消息传递类
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/8/15 16:18
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class EventBusMessage {
    private int status; // 状态码
    private String statusStr; // 状态码
    private Object message; // 消息实体

    public EventBusMessage(int status) {
        this.status = status;
    }

    public EventBusMessage(String statusStr) {
        this.statusStr = statusStr;
    }

    public EventBusMessage(String statusStr, Object message) {
        this.statusStr = statusStr;
        this.message = message;
    }

    public EventBusMessage(int status, Object message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
