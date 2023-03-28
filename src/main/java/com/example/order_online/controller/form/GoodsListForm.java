package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author 16537
 * @Classname GoodsListForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/14 12:32
 */
@Data
public class GoodsListForm {
    @NotNull
    @Min(value = 1,message = "当前页不小于1")
    private Integer currentPage;
    @Pattern(regexp = "^[\\u4E00-\\u9FFF\\w]{1,15}$",message = "搜索字符小于15")
    private String search;
}
