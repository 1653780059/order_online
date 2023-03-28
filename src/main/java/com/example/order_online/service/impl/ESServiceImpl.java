package com.example.order_online.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.order_online.controller.form.ESListForm;
import com.example.order_online.pojo.dto.PageData;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.ESService;
import com.example.order_online.utils.ElasticSearchUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author 16537
* @description 针对表【shop】的数据库操作Service实现
* @createDate 2023-02-23 09:45:54
*/
@Service
@Slf4j
public class ESServiceImpl implements ESService {
    @Resource
    private RestHighLevelClient client;
    @Override
    public Result getList(ESListForm form, String index) {
        SearchRequest searchRequest = new SearchRequest(index);
        String type = form.getType();
        switch (type){
            case "matchAll":
                ElasticSearchUtils.matchAllPageQueryBuild(searchRequest,form);break;
            case "term":
                ElasticSearchUtils.termPageQueryBuild(searchRequest, form);break;
            case "range":
                ElasticSearchUtils.rangePageQueryBuild(searchRequest,form);break;
            case "match":
                ElasticSearchUtils.matchPageQueryBuild(searchRequest,form);break;
            default:
                throw new RuntimeException("未匹配的查询");
        }

        //是否指定排序字段，不指定则按默认相关性排序
        if(StrUtil.isNotBlank(form.getSort())){
            //设置排序字段
            ElasticSearchUtils.sortFieldSet(searchRequest,form.getSort(),index);
        }

        List<Map<String,Object>> resultList;
        List<String> highlight;
        long total;

        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            resultList = ElasticSearchUtils.resolveResponseGetHits(response,form.getField());
            total=ElasticSearchUtils.resolveResponseGetPageTotal(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return Result.success(new PageData(total,resultList));
    }

    @Override
    public Result suggest(String search, String index) {
        SearchRequest searchRequest = new SearchRequest(index);
        ElasticSearchUtils.autoCompletionQueryBuild(searchRequest,search);
        List<String> suggestList;
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            suggestList = ElasticSearchUtils.resolveResponseGetSuggest(response);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return Result.success(suggestList);
    }
}




