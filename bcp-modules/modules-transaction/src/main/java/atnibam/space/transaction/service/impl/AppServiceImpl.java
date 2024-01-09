package atnibam.space.transaction.service.impl;

import atnibam.space.common.core.enums.CommonStatusEnum;
import atnibam.space.common.core.enums.ResultCode;

import atnibam.space.common.core.exception.AppException;

import atnibam.space.transaction.mapper.AppMapper;
import atnibam.space.transaction.model.entity.App;
import atnibam.space.transaction.service.AppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
* @author Atnibam Aitay
* @description 针对表【app】的数据库操作Service实现
* @createDate 2023-09-11 15:07:19
*/
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>
    implements AppService {

    @Resource
    private AppMapper appMapper;

    /**
     * 校验应用是否有效
     *
     * @param id 应用ID
     * @return 如果应用有效，则返回true；否则，抛出业务异常
     */
    @Override
    public App validPayApp(Integer id) {
        App app = appMapper.selectById(id);

        // 校验是否存在
        if (app == null) {
            throw new AppException(ResultCode.APP_NOT_FOUND);
        }

        // 校验是否禁用
        if (CommonStatusEnum.DISABLE.getStatus().equals(app.getStatus())) {
            throw new AppException(ResultCode.APP_IS_DISABLE);
        }

        return app;
    }

}