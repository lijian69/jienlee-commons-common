package com.teach.core.entity;

import lombok.Data;

@Data
public class TeacherEntity {

    /**
     * 是否通过认证
     */
    private int passTeacher;
    /**
     * 当前认证状态
     */
    private int teacherStatus;

}
