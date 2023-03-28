package com.example.order_online.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 16537
 * @Classname PageData
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/23 15:25
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class PageData {
    private Long total;
    private Object pageData;

}
