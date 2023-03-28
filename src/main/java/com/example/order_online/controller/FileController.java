package com.example.order_online.controller;

import cn.hutool.core.util.RandomUtil;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import com.example.order_online.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 16537
 * @Classname FileController
 * @Description
 * @Version 1.0.0
 * @Date 2023/1/18 15:48
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    @Resource
    private FileStorageService fileStorageService;
    private static final AtomicInteger SUFFIX_COUNT = new AtomicInteger();
    private static final List<String> ALLOW_FILE_TYPE=new ArrayList<>();
    static {
        ALLOW_FILE_TYPE.add(".jpg");
        ALLOW_FILE_TYPE.add(".png");
        ALLOW_FILE_TYPE.add(".gif");
        ALLOW_FILE_TYPE.add(".jpeg");
    }
    @PostMapping("/upload")
    public Result fileUpload(MultipartFile file){

        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));//.jpg
        assertFile(ext);
        String path = doUpload(file,ext);
        log.info(path);
        return Result.success("上传成功").put("img",path);
    }

    private void assertFile(String ext) {
        List<String> collect = ALLOW_FILE_TYPE.stream().filter(ext::equals).collect(Collectors.toList());
        if(collect.isEmpty()){
           throw new RuntimeException("非法文件类型");
        }
    }

    private String doUpload(MultipartFile file, String ext) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String prefix = sdf.format(new Date());
        String suffix = getSuf();
        String randomString= RandomUtil.randomString(10);

        return fileStorageService.of(file)
                .setPath(prefix)
                .setSaveFilename(randomString+suffix+ext)
                .upload().getUrl();

    }

    private String getSuf() {
        int suffixCount = SUFFIX_COUNT.get();

        synchronized (SUFFIX_COUNT){
            if(suffixCount>100){
                SUFFIX_COUNT.set(0);
                suffixCount=0;
            }
        }
        return "_"+suffixCount;

    }
}
