package space.atnibam.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.atnibam.common.core.common.IntArrayValuable;

import java.util.Arrays;

/**
 * @ClassName: CommonStatusEnum
 * @Description: 通用状态枚举类，表示开启和关闭两种状态
 * @Author: AtnibamAitay
 * @CreateTime: 2023-09-11 15:16
 **/
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements IntArrayValuable {
    /**
     * 关闭状态
     */
    DISABLE(0, "关闭"),
    /**
     * 开启状态
     */
    ENABLE(1, "开启");
    /**
     * 状态数组
     */
    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(CommonStatusEnum::getStatus).toArray();
    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

    /**
     * 获取状态数组
     *
     * @return 状态数组
     */
    @Override
    public int[] array() {
        return ARRAYS;
    }
}