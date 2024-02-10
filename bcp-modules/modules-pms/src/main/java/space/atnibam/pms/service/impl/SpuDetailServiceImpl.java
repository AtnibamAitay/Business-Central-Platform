package space.atnibam.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.atnibam.pms.mapper.SpuDetailMapper;
import space.atnibam.pms.model.entity.SpuDetail;
import space.atnibam.pms.service.SpuDetailService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【spu_detail(SPU详细表)】的数据库操作Service实现
 * @createDate 2024-02-08 21:44:45
 */
@Service
public class SpuDetailServiceImpl extends ServiceImpl<SpuDetailMapper, SpuDetail>
        implements SpuDetailService {
    @Resource
    private SpuDetailMapper spuDetailMapper;

    /**
     * 根据商品id获取spu介绍图列表
     *
     * @param spuId 商品id
     * @return spu介绍图列表
     */
    @Override
    public List<String> getSpuDetailListBySpuId(Integer spuId) {
        return spuDetailMapper.selectDetailUrlsBySpuId(spuId);
    }
}