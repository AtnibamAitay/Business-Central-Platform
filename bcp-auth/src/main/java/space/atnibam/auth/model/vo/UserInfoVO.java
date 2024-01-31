package space.atnibam.auth.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@ApiModel("用户信息展示类")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户信息ID")
    private Integer userId;

    @ApiModelProperty("用户名-默认值(应该是随机生成)")
    private String userName;

    @ApiModelProperty("用户角色-0:平台管理员;1:机构管理员;2:老师;3:学生;(默认为0)")
    private Integer userRole;

    @ApiModelProperty("头像-默认（固定默认头像）")
    private String userAvatar;

    @ApiModelProperty("用户简介")
    private String userIntroduction;

    @ApiModelProperty("省份")
    private String userLocationProvince;

    @ApiModelProperty("城市")
    private String userLocationCity;

    @ApiModelProperty("区域")
    private String userLocationRegion;

    @ApiModelProperty("经度")
    private Double longitude;

    @ApiModelProperty("纬度")
    private Double latitude;

    @ApiModelProperty("最后一次上线时间")
    private Date loginLastTime;

    @ApiModelProperty("最后一次下线时间")
    private Date offLineLastTime;

    @ApiModelProperty("最后一次登录ip")
    private String loginLastTimeIp;

    @ApiModelProperty("账号注册时间-非空")
    private Date userRegistTime;

    @ApiModelProperty("更新日期")
    private Date updateTime;

    @ApiModelProperty("用户表外键")
    private Integer credentialsId;
}
