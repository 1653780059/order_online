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
            "    \"properties\": {\n" +
            "      \"id\":{\n" +
            "        \"type\": \"integer\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\",\n" +
            "        \"index\":true\n" +
            "      },\n" +
            "      \"address\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"index\": true,\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
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
            "      \"vip\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"vipDiscount\":{\n" +
            "        \"type\": \"double\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"score\":{\n" +
            "        \"type\": \"double\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"auto\":{\n" +
            "        \"type\": \"completion\"\n" +
            "        \n" +
            "      },\n" +
            "      \"goodsName\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "         \"copy_to\": \"all\"\n" +
            "      }\n" +
            "      ,\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public static final String ES_GOODS_INDEX_TEMPLATE="";

    public static final String SHOP_INDEX_NAME="shop";
}
