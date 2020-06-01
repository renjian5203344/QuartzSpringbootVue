package com.yizhan.dataobject;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class QuartzEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**  任务名称.  */
    @Column(name = "jobName", nullable = false, length = 20)
    private String jobName;

    /**  任务分组.  */
    @Column(name = "jobGroup", nullable = false, length = 20)
    private String jobGroup;

    /**  任务描述.  */
    @Column(name = "description", nullable = true, length = 200)
    private String description;

    /**  执行类.  */
    @Column(name = "jobClassName", nullable = false, length = 50)
    private String jobClassName;

    /**  执行时间.  */
    @Column(name = "cronExpression", nullable = false, length = 100)
    private String cronExpression;

    /**  触发器的名称.  */
    @Column(name = "triggerName", nullable = false, length = 50)
    private String triggerName;

    /**  触发器的分组.  */
    @Column(name = "triggerGroup", nullable = false, length = 50)
    private String triggerGroup;

    /**  任务名称,用于修改.  */
    @Column(name = "oldJobName", nullable = true, length = 50)
    private String oldJobName;

    /**  任务分组,用于修改.  */
    @Column(name = "oldJobGroup", nullable = true, length = 50)
    private String oldJobGroup;
}
