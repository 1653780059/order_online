package com.example.order_online.utils;

import cn.hutool.core.lang.UUID;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.qcloud.cos.transfer.Upload;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author 16537
 * @Classname TencentObjectSave
 * @Description
 * @Version 1.0.0
 * @Date 2022/12/22 14:15
 */
@Component
@Data
@ConfigurationProperties(prefix = "tencent.cloud")
public class TencentObjectSave {
    private String appId;
    private String secretId;
    private String secretKey;
    private String region;
    private String bucket;
    private String[] arrow;
    private Long size;
    private COSClient getCosClient(){
        BasicCOSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(this.region);
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient(cred,clientConfig);
    }

   private TransferManager getTransferManager(){
       COSClient cosClient = getCosClient();
       ExecutorService executorService = Executors.newFixedThreadPool(4);
       TransferManager transferManager = new TransferManager(cosClient, executorService);
       TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
       transferManagerConfiguration.setMultipartUploadThreshold(5*1024*1024);
       transferManagerConfiguration.setMinimumUploadPartSize(1*1024*1024);
       transferManager.setConfiguration(transferManagerConfiguration);
       return transferManager;
   }
    public void shutdownTransferManager(TransferManager transferManager){
        transferManager.shutdownNow(true);
    }
    private String getFilePath(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String ext = filename.substring(filename.lastIndexOf("."));
        assertFile(file,ext);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String format = sdf.format(new Date());
        UUID uuid = UUID.randomUUID();
        return format+"/"+uuid+ext;

    }

    private void assertFile(MultipartFile file,String ext) {
        List<String> collect =
                Arrays.stream(arrow).filter(item -> item.equals(ext)).collect(Collectors.toList());
        if(collect.isEmpty()){
            throw new RuntimeException("不支持的文件格式");
        }
        if(file.getSize()>size){
            throw new RuntimeException("文件过大最多10M");
        }
    }

    public String putObject(MultipartFile file){
        String key = getFilePath(file);
        TransferManager transferManager = getTransferManager();
        try {
            InputStream inputStream = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, inputStream,objectMetadata);
            putObjectRequest.setStorageClass(StorageClass.Standard);
            Upload upload = transferManager.upload(putObjectRequest);
            upload.waitForUploadResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败");
        }
        return getAccessPath(key);
    }

    private String getAccessPath(String key) {
        return "https://"+bucket+".cos."+region+".myqcloud.com"+"/"+key;
    }


}
