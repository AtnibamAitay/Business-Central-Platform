package space.atnibam.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import space.atnibam.api.pms.model.dto.SpuBaseInfoDTO;
import space.atnibam.api.pms.model.dto.SpuDTO;
import space.atnibam.api.ums.RemoteUserInfoService;
import space.atnibam.pms.mapper.SpuMapper;
import space.atnibam.pms.model.entity.Spu;
import space.atnibam.pms.service.SpuCoverService;
import space.atnibam.pms.service.SpuDetailService;
import space.atnibam.pms.service.SpuService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName: SpuServiceImpl
 * @Description: 商品服务实现类
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-08 21:44
 **/
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu>
        implements SpuService {
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private SpuMapper spuMapper;
    @Resource
    private SpuCoverService spuCoverService;
    @Resource
    private SpuDetailService spuDetailService;
    @Resource
    private RemoteUserInfoService remoteUserInfoService;

    /**
     * 根据商品ID查询商品信息
     *
     * @param spuId 商品ID
     * @return 商品实体对象，如果不存在则返回空Optional
     */
    @Override
    public Optional<SpuDTO> getSpuById(Integer spuId) {
        Spu spu = spuMapper.selectById(spuId);
        // TODO:需要处理spu为空的情况

        // 手动映射属性到 DTO 对象
        SpuDTO spuDTO = new SpuDTO();
        BeanUtils.copyProperties(spu, spuDTO);

        // 设置封面集合
        spuDTO.setCover(spuCoverService.getSpuCoverListBySpuId(spuId));
        // 设置spu详情集合
        spuDTO.setDetail(spuDetailService.getSpuDetailListBySpuId(spuId));

        // 转换商户信息
        Object userInfo = remoteUserInfoService.getDetailedUserInfo(spu.getMerchantId()).getData();
        Map<String, Object> merchantDataMap = (Map<String, Object>) userInfo;
        SpuDTO.MerchantDTO merchantDTO = objectMapper.convertValue(merchantDataMap, SpuDTO.MerchantDTO.class);

        // 设置商户信息到 DTO
        spuDTO.setMerchant(merchantDTO);

        return Optional.ofNullable(spuDTO);
    }

    /**
     * 根据一个或多个商品ID获取商品基本的信息列表
     *
     * @param spuIdList 商品ID列表
     * @return 商品基本的信息列表
     */
    @Override
    public List<SpuBaseInfoDTO> getSpuBaseInfoList(List<Integer> spuIdList) {
        List<Spu> spuList = spuMapper.selectSpuBaseInfoListBySpuIds(spuIdList);
        List<SpuBaseInfoDTO> spuBaseInfoDTOList = new ArrayList<>();
        for (Spu Spu : spuList) {
            SpuBaseInfoDTO spuBaseInfoDTO = new SpuBaseInfoDTO();
            BeanUtils.copyProperties(Spu, spuBaseInfoDTO);
            // 转换商户信息
            Object userInfo = remoteUserInfoService.getDetailedUserInfo(Spu.getMerchantId()).getData();
            Map<String, Object> merchantDataMap = (Map<String, Object>) userInfo;
            SpuBaseInfoDTO.MerchantBaseInfoDTO merchantBaseInfoDTO = objectMapper.convertValue(merchantDataMap, SpuBaseInfoDTO.MerchantBaseInfoDTO.class);
            spuBaseInfoDTO.setMerchant(merchantBaseInfoDTO);

            spuBaseInfoDTOList.add(spuBaseInfoDTO);
        }
        return spuBaseInfoDTOList;
    }
}
