package space.atnibam.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "user_info")
public class UserInfo implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID-主键
     */
    @TableId(type = IdType.AUTO)
    private Integer userId;
    /**
     * 用户名-默认值(应该是随机生成)
     */
    private String userName;
    /**
     * 头像-默认（固定默认头像）
     */
    private String userAvatar;
    /**
     * 用户角色-0:平台管理员;1:机构管理员;2:老师;3:学生;(默认为0)
     */
    private Integer userRole;
    /**
     * 用户简介
     */
    private String userIntroduction;
    /**
     * 省份
     */
    private String userLocationProvince;
    /**
     * 城市
     */
    private String userLocationCity;
    /**
     * 区域
     */
    private String userLocationRegion;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 最后一次上线时间
     */
    private Date loginLastTime;
    /**
     * 最后一次下线时间
     */
    private Date offLineLastTime;
    /**
     * 最后一次登录ip
     */
    private String loginLastTimeIp;
    /**
     * 账号注册时间-非空
     */
    private Date userRegistTime;
    /**
     * 用户状态码-0:未注销;1:已注销;2:暂时被冻结;(默认为0)
     */
    private Integer userStatus;
    /**
     * 更新日期
     */
    private Date updateTime;
    /**
     * 用户注销标记：0-未注销，1-确认注销，2-取消注销
     */
    private Integer logoutStatus;
    /**
     * 1 STEAM课堂 2 北极星宠 3万象课堂
     */
    private String appCode;
    /**
     * 凭证ID
     */
    private Integer credentialsId;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserInfo other = (UserInfo) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
                && (this.getUserRole() == null ? other.getUserRole() == null : this.getUserRole().equals(other.getUserRole()))
                && (this.getUserAvatar() == null ? other.getUserAvatar() == null : this.getUserAvatar().equals(other.getUserAvatar()))
                && (this.getUserIntroduction() == null ? other.getUserIntroduction() == null : this.getUserIntroduction().equals(other.getUserIntroduction()))
                && (this.getUserLocationProvince() == null ? other.getUserLocationProvince() == null : this.getUserLocationProvince().equals(other.getUserLocationProvince()))
                && (this.getUserLocationCity() == null ? other.getUserLocationCity() == null : this.getUserLocationCity().equals(other.getUserLocationCity()))
                && (this.getUserLocationRegion() == null ? other.getUserLocationRegion() == null : this.getUserLocationRegion().equals(other.getUserLocationRegion()))
                && (this.getLongitude() == null ? other.getLongitude() == null : this.getLongitude().equals(other.getLongitude()))
                && (this.getLatitude() == null ? other.getLatitude() == null : this.getLatitude().equals(other.getLatitude()))
                && (this.getLoginLastTime() == null ? other.getLoginLastTime() == null : this.getLoginLastTime().equals(other.getLoginLastTime()))
                && (this.getOffLineLastTime() == null ? other.getOffLineLastTime() == null : this.getOffLineLastTime().equals(other.getOffLineLastTime()))
                && (this.getLoginLastTimeIp() == null ? other.getLoginLastTimeIp() == null : this.getLoginLastTimeIp().equals(other.getLoginLastTimeIp()))
                && (this.getUserRegistTime() == null ? other.getUserRegistTime() == null : this.getUserRegistTime().equals(other.getUserRegistTime()))
                && (this.getUserStatus() == null ? other.getUserStatus() == null : this.getUserStatus().equals(other.getUserStatus()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getLogoutStatus() == null ? other.getLogoutStatus() == null : this.getLogoutStatus().equals(other.getLogoutStatus()))
                && (this.getAppCode() == null ? other.getAppCode() == null : this.getAppCode().equals(other.getAppCode()))
                && (this.getCredentialsId() == null ? other.getCredentialsId() == null : this.getCredentialsId().equals(other.getCredentialsId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserRole() == null) ? 0 : getUserRole().hashCode());
        result = prime * result + ((getUserAvatar() == null) ? 0 : getUserAvatar().hashCode());
        result = prime * result + ((getUserIntroduction() == null) ? 0 : getUserIntroduction().hashCode());
        result = prime * result + ((getUserLocationProvince() == null) ? 0 : getUserLocationProvince().hashCode());
        result = prime * result + ((getUserLocationCity() == null) ? 0 : getUserLocationCity().hashCode());
        result = prime * result + ((getUserLocationRegion() == null) ? 0 : getUserLocationRegion().hashCode());
        result = prime * result + ((getLongitude() == null) ? 0 : getLongitude().hashCode());
        result = prime * result + ((getLatitude() == null) ? 0 : getLatitude().hashCode());
        result = prime * result + ((getLoginLastTime() == null) ? 0 : getLoginLastTime().hashCode());
        result = prime * result + ((getOffLineLastTime() == null) ? 0 : getOffLineLastTime().hashCode());
        result = prime * result + ((getLoginLastTimeIp() == null) ? 0 : getLoginLastTimeIp().hashCode());
        result = prime * result + ((getUserRegistTime() == null) ? 0 : getUserRegistTime().hashCode());
        result = prime * result + ((getUserStatus() == null) ? 0 : getUserStatus().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getLogoutStatus() == null) ? 0 : getLogoutStatus().hashCode());
        result = prime * result + ((getAppCode() == null) ? 0 : getAppCode().hashCode());
        result = prime * result + ((getCredentialsId() == null) ? 0 : getCredentialsId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", userRole=").append(userRole);
        sb.append(", userAvatar=").append(userAvatar);
        sb.append(", userIntroduction=").append(userIntroduction);
        sb.append(", userLocationProvince=").append(userLocationProvince);
        sb.append(", userLocationCity=").append(userLocationCity);
        sb.append(", userLocationRegion=").append(userLocationRegion);
        sb.append(", longitude=").append(longitude);
        sb.append(", latitude=").append(latitude);
        sb.append(", loginLastTime=").append(loginLastTime);
        sb.append(", offLineLastTime=").append(offLineLastTime);
        sb.append(", loginLastTimeIp=").append(loginLastTimeIp);
        sb.append(", userRegistTime=").append(userRegistTime);
        sb.append(", userStatus=").append(userStatus);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", logoutStatus=").append(logoutStatus);
        sb.append(", appCode=").append(appCode);
        sb.append(", credentialsId=").append(credentialsId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}