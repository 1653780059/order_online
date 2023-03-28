package com.example.order_online.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author 16537
 * @Classname TreeDto
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/26 12:57
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeDto {
    private Integer id;
    private String label;
    private List<TreeDto> children;
}
