package com.flora.note.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author qinxiang
 * @Date 2022/11/28-下午2:22
 * 封装返回结果的类
 * 状态码 成功=1 失败=0
 * 提示信息
 * 返回的对象（字符串、javabean、集合、Map等）
 */
@Getter
@Setter
public class ResultInfo<T> {
    private Integer code;
    private String msg;
    private T result;
}
