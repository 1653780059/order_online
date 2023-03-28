package com.example.order_online.utils;

import cn.hutool.core.util.StrUtil;
import com.example.order_online.constants.ElasticSearchConstant;
import com.example.order_online.controller.form.ESListForm;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;

import java.util.*;

/**
 * @author 16537
 * @Classname ElasticSearchUtils
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/23 13:38
 */
public class ElasticSearchUtils {
    public static List<Map<String,Object>> resolveResponseGetHits(SearchResponse response, String searchField){
        List<Map<String,Object>> resultList=new ArrayList<>();
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            Set<String> strings = highlightFields.keySet();
            strings.forEach(key->{
                HighlightField field= highlightFields.get(key);
                if(field!= null){
                    Text[] fragments = field.fragments();
                    String n_field = "";
                    for (Text fragment : fragments) {
                        n_field += fragment;
                    }
                    //高亮标题覆盖原标题
                    sourceAsMap.put(key,n_field);
                }
            });
            resultList.add(hit.getSourceAsMap());

        }

        return resultList;
    }

    public static void termPageQueryBuild(SearchRequest searchRequest, ESListForm form) {
        String field = form.getField();
        Object search = form.getSearch();
        if (StrUtil.isBlank(field)||Objects.isNull(search)){
            throw new RuntimeException("请指定精确查询字段");
        }
        searchRequest.source().query(QueryBuilders.termQuery(field, search));
        int from = (form.getCurrentPage() - 1) * form.getPageSize();
        searchRequest.source().from(from).size(form.getPageSize());
        highLightFieldSet(searchRequest,field);
    }

    public static void sortFieldSet(SearchRequest searchRequest, String sort, String index) {
        if(StrUtil.isBlank(sort)&&index.equals(ElasticSearchConstant.SHOP_INDEX_NAME)){
            sort=ElasticSearchConstant.DEFAULT_SHOP_SORT;
        }
        if(StrUtil.isBlank(sort)&&index.equals(ElasticSearchConstant.GOODS_INDEX_NAME)){
            sort=ElasticSearchConstant.DEFAULT_GOODS_SORT;
        }
        searchRequest.source().sort(sort, SortOrder.DESC);
    }

    public static long resolveResponseGetPageTotal(SearchResponse response) {
        return response.getHits().getTotalHits().value;
    }

    public static void matchAllPageQueryBuild(SearchRequest searchRequest, ESListForm form) {
        searchRequest.source().query(QueryBuilders.matchAllQuery());
        int from = (form.getCurrentPage() - 1) * form.getPageSize();
        searchRequest.source().from(from).size(form.getPageSize());
    }

    public static void rangePageQueryBuild(SearchRequest searchRequest, ESListForm form) {
        if (form.getMax()==null||form.getMin()==null||StrUtil.isBlank(form.getField())||form.getMax()<form.getMin()){
            throw new RuntimeException("参数错误");
        }
        highLightFieldSet(searchRequest,form.getField());
        searchRequest.source().query(QueryBuilders.rangeQuery(form.getField()).lte(form.getMax()).gte(form.getMin()));
    }

    public static void matchPageQueryBuild(SearchRequest searchRequest, ESListForm form) {
        String field = form.getField();
        Object search = form.getSearch();
        if (Objects.isNull(search)){
            throw new RuntimeException("参数错误");
        }
        if(StrUtil.isBlank(field)){
            field=ElasticSearchConstant.GLOBAL_SEARCH_FIELD;
        }
        searchRequest.source().query(QueryBuilders.matchQuery(field, search));
        highLightFieldSet(searchRequest,field);
    }

    public static void highLightFieldSet(SearchRequest searchRequest, String field) {
        if(StrUtil.isNotBlank(field)&&!field.equals(ElasticSearchConstant.DEFAULT_HIGHLIGHT_DESC)&&!field.equals(ElasticSearchConstant.DEFAULT_HIGHLIGHT_NAME)){
            searchRequest.source().highlighter(SearchSourceBuilder.highlight().requireFieldMatch(false).field(ElasticSearchConstant.DEFAULT_HIGHLIGHT_DESC).field(ElasticSearchConstant.DEFAULT_HIGHLIGHT_NAME).field(field));
        }else {
            searchRequest.source().highlighter(SearchSourceBuilder.highlight().requireFieldMatch(false).field(ElasticSearchConstant.DEFAULT_HIGHLIGHT_DESC).field(ElasticSearchConstant.DEFAULT_HIGHLIGHT_NAME));
        }
    }


    public static void autoCompletionQueryBuild(SearchRequest searchRequest, String search) {
        searchRequest.source()
                .suggest(new SuggestBuilder()
                        .addSuggestion(
                                ElasticSearchConstant.AUTO_COMPLETION,
                                SuggestBuilders.completionSuggestion(ElasticSearchConstant.AUTO_COMPLETION)
                                        .prefix(search)
                                        .skipDuplicates(true)
                                        .size(10)
                        ));
    }

    public static List<String> resolveResponseGetSuggest(SearchResponse response) {
        List<String> suggestionList = new ArrayList<>();
        Suggest suggest = response.getSuggest();
        Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> suggestion = suggest.getSuggestion(ElasticSearchConstant.AUTO_COMPLETION);
        for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> options : suggestion) {
            for (Suggest.Suggestion.Entry.Option option : options.getOptions()) {
                suggestionList.add(option.getText().toString());
            }
        }
        return suggestionList;
    }
}
