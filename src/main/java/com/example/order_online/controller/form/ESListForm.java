package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author 16537
 * @Classname ShopListForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/23 13:52
 */
@Data
public class ESListForm {
    private Object search;
    @Min(value = 1,message = "页数不小于1")
    @NotNull
    private Integer currentPage;
    @Max(value = 50,message = "每页不大于50条记录")
    @NotNull
    private Integer pageSize;
    //排序字段
    private String sort;
    //查询字段，不指定默认为all字段
    private String field;
    private Double max;
    private Double min;
    @NotBlank(message = "type不能为空")
    @Pattern(regexp = "matchAll|match|range|term",message = "查询类型错误")
    private String type;
}
