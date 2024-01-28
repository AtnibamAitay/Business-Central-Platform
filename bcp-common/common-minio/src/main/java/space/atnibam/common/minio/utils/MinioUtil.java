package space.atnibam.common.minio.utils;

import space.atnibam.common.core.domain.R;
import space.atnibam.common.core.exception.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import space.atnibam.common.minio.model.dto.UploadFileParamsDTO;
import space.atnibam.common.minio.service.FileInfoService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

import static space.atnibam.common.core.enums.ResultCode.MINIO_UPLOAD_ERROR;

/**
 * @ClassName: MinioUtil
 * @Description: Minio对象存储工具类
 * @Author: AtnibamAitay
 * @CreateTime: 2023-10-16 16:04
 **/
@Slf4j
@Component
public class MinioUtil {

    @Resource
    private FileInfoService fileInfoService;

    /**
     * 一次性上传文件接口
     * 适用于小文件
     *
     * @param files  文件
     * @param bucket 存储桶
     * @param userId 用户id
     * @param folder 文件存储的文件夹，可以为空
     * @return 返回文件上传结果信息
     */
    public String upload(MultipartFile files, String bucket, Integer userId, String folder) {

        log.info("文件上传开始，文件名：{}", files.getOriginalFilename());

        // 创建一个新的文件上传参数对象
        UploadFileParamsDTO uploadFileParamsDTO = UploadFileParamsDTO.builder()
                // 设置文件名
                .fileName(files.getOriginalFilename())
                // 设置文件大小
                .fileSize(files.getSize())
                // 设置桶
                .bucket(bucket)
                // 设置用户id
                .userId(userId)
                // 设置文件类型
                .contentType(FileServiceUtil.getContentType(Objects.requireNonNull(files.getOriginalFilename())))
                .build();

        try {
            // 调用服务层方法进行文件上传，并返回上传结果
            String url = fileInfoService.uploadFile(uploadFileParamsDTO, files.getBytes(), folder);

            log.info("文件上传成功，文件url：{}", url);

            return url;
        } catch (IOException e) {
            log.error("文件上传失败，错误信息：{}", e.getMessage());
            // 如果在上传过程中发生错误，那么抛出自定义异常
            throw new MinioException(MINIO_UPLOAD_ERROR);
        }
    }

    /**
     * 文件上传前检查文件
     *
     * @param md5 文件的MD5值
     * @return 返回R对象，包含布尔类型结果表示文件是否已存在
     */
    public R checkFile(String md5) {
        return R.ok(fileInfoService.checkFile(md5));
    }

    /**
     * 分块文件上传前检查分块
     *
     * @param md5   文件的MD5值
     * @param chunk 文件的块索引
     * @return 返回R对象，包含布尔类型结果表示该分块是否已存在
     */
    public R checkChunk(String md5, int chunk, String bucket) {
        return R.ok(fileInfoService.checkChunk(md5, chunk, bucket));
    }

    /**
     * 上传分块后文件的单块
     *
     * @param file  分块文件
     * @param md5   文件的MD5值
     * @param chunk 文件的块索引
     * @return 返回R对象，具体内容根据实际业务定义
     * @throws Exception 可能发生的异常
     */
    public R uploadChunk(MultipartFile file, String md5, int chunk, String bucket) throws Exception {
        return R.ok(fileInfoService.uploadChunk(md5, chunk, file.getBytes(), bucket));
    }

    /**
     * 合并分块文件
     *
     * @param md5        文件的MD5值
     * @param fileName   文件名
     * @param chunkTotal 分块总数
     * @return 返回R对象，具体内容根据实际业务定义
     */
    public R mergeChunks(String md5, String fileName, int chunkTotal, String bucket) {

        // 创建一个新的文件上传参数对象
        UploadFileParamsDTO uploadFileParamsDTO = UploadFileParamsDTO.builder()
                // 设置文件名
                .fileName(fileName)
                // 设置桶
                .bucket(bucket)
                .build();

        // 合并分块文件
        fileInfoService.mergeChunks(md5, chunkTotal, uploadFileParamsDTO);
        return R.ok();
    }
}
