package com.enhui.activiti.first;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 胡恩会
 * @Date 2021/8/4 20:55
 **/
@SpringBootTest
public class ActivitiQuickTest {


    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    private final String bpmnNameAndKey = "first";

    /**
     * 项目启动即自动创建表结构
     *
     * @Author: 胡恩会
     * @Date: 2021/8/4 21:02
     **/
    @Test
    public void createTable() {

    }

    /**
     * 第一步：--永远是画制流程图---部署流程图
     */
    @Test
    public void deploy() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/first.bpmn")
                .addClasspathResource("processes/first.png")
                .key(bpmnNameAndKey)
                .name(bpmnNameAndKey + "name")
                .category("HR")
                .deploy();
        System.out.println("流程部署ID\t" + deploy.getId());
        System.out.println("流程keyID\t" + deploy.getKey());
        System.out.println("流程名称ID\t" + deploy.getName());
        System.out.println("流程分类ID\t" + deploy.getCategory());
    }

    /**
     * 第二步：启动一个流程实例，相当于----有人请假一次
     */
    @Test
    public void start() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnNameAndKey);
        System.out.println("流程实例ID\t" + processInstance.getId());
        System.out.println("流程定义ID\t" + processInstance.getProcessDefinitionId());
        System.out.println("流程定义key\t" + processInstance.getProcessDefinitionKey());
    }

    /**
     * 第三步:查找个人待办任务列表
     */
    @Test
    public void findMyTask() {
        String assignee = "李四";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();
        if (!CollectionUtils.isEmpty(list)) {
            for (Task task : list) {
                System.out.println("任务ID\t" + task.getId());
                System.out.println("任务名称\t" + task.getName());
                System.out.println("任务执行人\t" + task.getAssignee());
            }
        }
    }

    /**
     * 第四步：执行任务
     */
    @Test
    public void complte() {
        String taskId = "5002";
        taskService.complete(taskId);
        System.out.println("任务执行完成");
    }

    /**
     * 第五步：查看历史流程实例
     */
    @Test
    public void findhistProcessInstance() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(bpmnNameAndKey)
                .list();
        if (!CollectionUtils.isEmpty(list)) {
            for (HistoricProcessInstance historicProcessInstance : list) {
                System.out.println("业务系统key\t" + historicProcessInstance.getBusinessKey());
                System.out.println("部署对象ID\t" + historicProcessInstance.getDeploymentId());
                System.out.println("执行时长\t" + historicProcessInstance.getDurationInMillis());
                System.out.println("流程定义ID\t" + historicProcessInstance.getProcessDefinitionId());
                System.out.println("流程定义的key\t" + historicProcessInstance.getProcessDefinitionKey());
                System.out.println("流程定义名称\t" + historicProcessInstance.getProcessDefinitionName());
            }
        }
    }

    /**
     * 第六步：查看历史任务
     */
    @Test
    public void findHisTask() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .list();
        if (!CollectionUtils.isEmpty(list)) {
            for (HistoricTaskInstance historicTaskInstance : list) {
                System.out.println("任务执行人\t" + historicTaskInstance.getAssignee());
                System.out.println("任务名称\t" + historicTaskInstance.getName());
                System.out.println("任务ID\t" + historicTaskInstance.getId());
                System.out.println("流程实例ID\t" + historicTaskInstance.getProcessInstanceId());
                System.out.println("*****************");
            }
        }
    }


}
