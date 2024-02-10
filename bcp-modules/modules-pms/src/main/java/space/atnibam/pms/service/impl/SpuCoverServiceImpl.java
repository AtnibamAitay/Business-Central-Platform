package space.atnibam.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.atnibam.pms.mapper.SpuCoverMapper;
import space.atnibam.pms.model.entity.SpuCover;
import space.atnibam.pms.service.SpuCoverService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【spu_cover(SPU封面表)】的数据库操作Service实现
 * @createDate 2024-02-08 21:44:45
 */
@Service
public class SpuCoverServiceImpl extends ServiceImpl<SpuCoverMapper, SpuCover>
        implements SpuCoverService {
    @Resource
    private SpuCoverMapper spuCoverMapper;

    /**
     * 根据商品id获取spu封面信息
     *
     * @param spuId 商品id
     * @return Optional<SpuCover> spu封面
     */
    @Override
    public List<String> getSpuCoverListBySpuId(Integer spuId) {
        return spuCoverMapper.selectCoverUrlsBySpuId(spuId);
    }
}