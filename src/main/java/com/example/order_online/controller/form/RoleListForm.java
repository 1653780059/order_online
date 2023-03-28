package com.example.order_online.controller.form;

import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author 16537
 * @Classname RoleListForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/25 17:49
 */
@Data
public class RoleListForm {
    @NotNull(message = "当前页不为空")
    @Min(value = 1,message = "当前页不小于1")
    private Integer currentPage;
    @Range(min = 0,max = 1,message = "参数错误")
    private Integer systemctl;
    private String roleName;
}
