package com.yizhan.dataobject;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Data
public class JavaTuoPuQuartz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**  拓扑名称.  */
    @Column(name = "tuoPuName", nullable = false, length = 200)
    private String tuoPuName;

    /**  根节点的ids.  */
    @Column(name = "rootids", nullable = true, length = 200)
    private String rootids;

    /**  前端传过来的信息，不需要保存到数据库(实体映射).  */
    @Transient //不需要映射到数据库
    private List<JavaQuartz> javaQuartzList;

    @Transient //不需要映射到数据库
    private String javaTuoPuQuartzJsonString;






}
