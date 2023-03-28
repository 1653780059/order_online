package com.example.order_online.constants;

/**
 * @author 16537
 * @Classname ElaticSearchConstant
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/23 9:24
 */
public class ElasticSearchConstant {
    public static final String ES_SHOP_INDEX_TEMPLATE="{\n" +
            "  \"mappings\": {\n" +
            "  \n" +
            "    \"properties\": {\n" +
            "      \"img\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      ,\"index\": false},\n" +
            "      \"id\":{\n" +
            "        \"type\": \"integer\",\n" +
            "        \"index\": true\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"index\":true\n" +
            "      },\n" +
            "      \"address\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"index\": true,\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      },\n" +
            "      \"type\":{\n" +
            "        \"type\": \"integer\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"own\":{\n" +
            "        \"type\":\"integer\"\n" +
            "      ,\"index\": false\n" +
            "        \n" +
            "      },\n" +
            "      \"score\":{\n" +
            "        \"type\": \"double\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"auto\":{\n" +
            "        \"type\": \"completion\"\n" +
            "        \n" +
            "      },\n" +
            "      \"desc\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "         \"copy_to\": \"all\"\n" +
            "      }\n" +
            "      ,\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_max_word\"\n" +
            "      },\n" +
            "      \"payment\":{\n" +
            "        \"type\": \"double\",\n" +
            "        \"index\": false\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public static final String ES_GOODS_INDEX_TEMPLATE="{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\":{\n" +
            "        \"type\": \"integer\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"index\":true\n" +
            "      },\n" +
            "      \"price\":{\n" +
            "        \"type\": \"double\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"type\":{\n" +
            "        \"type\": \"integer\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "     \n" +
            "      \"shop\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": true\n" +
            "      },\n" +
            "      \"orderCount\":{\n" +
            "        \"type\": \"integer\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"auto\":{\n" +
            "        \"type\": \"completion\"\n" +
            "        \n" +
            "      },\n" +
            "      \"desc\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "         \"copy_to\": \"all\"\n" +
            "      }\n" +
            "      ,\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_max_word\"\n" +
            "      },\n" +
            "      \"img\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      ,\"index\": false}\n" +
            "     \n" +
            "    }\n" +
            "  }\n" +
            "}";
    public static final String GLOBAL_SEARCH_FIELD="all";
    public static final String SHOP_INDEX_NAME = "shop";
    public static final String GOODS_INDEX_NAME = "goods";
    public static final String DEFAULT_SHOP_SORT = "payment";
    public static final String DEFAULT_HIGHLIGHT_DESC = "desc";
    public static final String DEFAULT_HIGHLIGHT_NAME = "name";
    public static final String AUTO_COMPLETION = "auto";
    public static final String DEFAULT_GOODS_SORT = "orderCount";
}
