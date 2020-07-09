package com.yizhan.dataobject;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Data
public class JavaQuartz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**  任务名称.  */
    @NotEmpty(message = "任务名称必填项")
    @Column(name = "jobName", nullable = false, length = 20)
    private String jobName;

    /**  任务分组.  */
    @NotEmpty(message = "任务分组必填项")
    @Column(name = "jobGroup", nullable = false, length = 20)
    private String jobGroup;

    /**  任务描述.  */
    @Column(name = "description", nullable = true, length = 200)
    private String description;

    /**  执行路径.  */
    @NotEmpty(message = "执行路径必填项")
    @Column(name = "jarPath", nullable = false, length = 50)
    private String jarPath;

    /**  执行参数.  */
    @NotEmpty(message = "执行参数必填项")
    @Column(name = "parameter", nullable = false, length = 50)
    private String parameter;

    /**  jvm参数设置.  */
    @NotEmpty(message = "JVM参数必填项")
    @Column(name = "vmParam", nullable = false, length = 50)
    private String vmParam;

    /**  执行时间.  */
    @NotEmpty(message = "执行时间必填项")
    @Column(name = "cronExpression", nullable = false, length = 100)
    private String cronExpression;

    /**  任务名称,用于修改.  */
    @Column(name = "oldJobName", nullable = true, length = 50)
    private String oldJobName;

    /**  任务分组,用于修改.  */
    @Column(name = "oldJobGroup", nullable = true, length = 50)
    private String oldJobGroup;

    /**  任务状态,1、0未启动、1启动、2暂停、3恢复、4已完成 .  */
    @Column(name = "jobStatus", nullable = false, length = 20)
    private Integer jobStatus;


    /**  父任务id,如果没有父节点就是-1 .  */
    @Column(name = "parentTaskId", nullable = false, length = 20)
    private   String  parentTaskId ;

    /**  子任务id,如果没有子任务就是为空.  */
    @Column(name = "childTaskId", nullable = false, length = 20)
    private   String  childTaskId ;

    /**  任务信息.  */
    @Transient//不需要映射成数据库
    private String taskInfo;

    @Transient//不需要映射成数据库
    /**  子节点.  */
    private List<JavaQuartz> childList;



}
