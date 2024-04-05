package com.ice.job.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/4/5 20:48
 */
@Data
public class MajorVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 专业名称
     */
    private String majorName;

    private static final long serialVersionUID = -3724822413135742135L;

}
