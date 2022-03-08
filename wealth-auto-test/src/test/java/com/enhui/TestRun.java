package com.enhui;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.enhui.util.ExcelUtil;
import com.enhui.util.RequestUtil;
import com.enhui.util.RegexUtil;
import com.enhui.util.LocalCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

@Slf4j
//@Listeners(value = ExtentTestNgFormatter.class)//使用老师重新开发的监听类
public class TestRun {
    private String excelPath;

    @Parameters({"fileNamePara"})//为了动态配置ExcelUtil类实例化时的excel路径参数
    @BeforeTest
    public void temp(String testCase){
        this.excelPath = testCase;

        log.info("TestRun testCase=" + testCase);
    }

    /**
     * @DataProvider
     * 作用：对@Test注解的方法提供数据，且此注解的方法返回值是Object[][]
     *
     * 变量：
     * name  表示数据驱动的名字，若不指定则让方法名作为@DataProvider的名字；反之就是name定义的名字
     * parallel  表示数据驱动是否并行跑，默认值是false，并行指的是两个线程以上；当前场景不适合
     */
    @DataProvider(name = "testcase")
    public Object[][] dp() throws IOException {
        ExcelUtil excelUtil = new ExcelUtil(excelPath);// /Users/bytedance/Documents/工作簿1.xlsx
        Object[][] data = excelUtil.getCellValue(0);
        log.info("TestRun Object[][] data =" + JSON.toJSON(data).toString());
        return data;
        //return excelUtil.getCellValue(0);//为了加log，修改为上述代码
    }

    /**
     * @Test
     *作用：标记到类上称为测试类，标记到方法上称为测试方法
     *
     * 变量：
     * dataProvider 对@Test方法指定数据驱动名字，此变量跟需@DataProvider的name变量数据走；若@DataProvider给提供n行数据，则@Test注解的方法需执行n次
     * dataProviderClass  1.此变量引出数据驱动类，它有多个static定义的数据驱动方法组成；
     *                    2.此变量需和dataProvider变量结合使用；
     * invocationCount  执行测试方法执行的次数
     * invocationTimeOut invocationTimeOut在指定的时间内没完成，就报超时错误，时间由invocationTimeOut变量定义；必须和invocationCount结合使用，反之无效
     * threadPoolSize  用指定的线程数共同完成invocationCount的次数，必须和invocationCount结合使用，反之无效
     * timeout 指定测试方法花费最多的时间，此变量可针对接口并发时功能和健壮性进行测试
     *
     * groups  让@Test方法加入组
     * dependsOnGroups（平时很少使用）  让@Test方法依赖groups组，即强绑定此组，让组内的方法先运行，自身最后运行，不足：是基于方法的首字母顺序运行的
     * dependsOnMethods（平时很少使用） 依赖某个@Test方法，即强绑定其他方法，让其他方法现运行，自身后运行；不足：是基于方法的首字母顺序运行的
     *
     * enabled  指定@Test方法是否运行，ture是运行，反之不运行
     * priority  @Test方法指定priority值越小即优先运行
     * description  对@Test方法进行描述
     *
     * suitName  了解
     * retryAnalyzer 对失败用例重试
     *
     *
     * @Listeners  对@Test类监听
     * @Parameters  对@Test方法进行参数化
     * @Factory  在测试类中标记某方法为Factory工厂注解方法，且此方法返回值为Object[]
     *
     * @Ignore  作用在测试类上，测试方法不执行
     *
     * @BeforeMethod 每个@Test方法运行前先运行此注解方法(注意：是每个@Test方法哦)
     * @AfterMethod 每个@Test方法运行后再运行此注解方法
     * @BeforeClass 当前测试类中第1个@Test方法执行前先执行此注解方法
     * @AfterClass  当前测试类中所有@Test方法执行后再执行此注解方法
     * @BeforeTest 当前测试类中有test标记的方法运行前先执行此注解方法
     * @AfterTest 当前测试类中有test标记的方法运行后再执行此注解方法
     * @BeforeSuite 在当前suite里所有@Test方法运行前先执行此注解方法
     * @AfterSuite 在当前suite里所有@Test方法运行后再执行此注解方法
     *
     *
     * 基于xml运行是基于suite运行的
     */
    @Test(dataProvider = "testcase",description = "接口测试")//这里把入参改为数字，数据类型改为Interger也一直报错//retryAnalyzer = RetryAnalyzer.class
    public void req(String id,String isExec,String testCase,String reqType,String reqHost,String reqInterface,
                    String reqData,String reqExpect,String isDep,String depKey) throws Exception {

        String url = reqHost + reqInterface;
        System.out.println(url);
        String actResult = null;

        /*
        step2:
            发送请求前程序判断Req_data是否有依赖别的接口，值searchword=api/loginCheck:$.msg
            有依赖，程序需要从map.get(/api/loginCheck:$.msg)=管理员登录成功，然后把字符串替换为searchword=管理员登录成功，替换成功后继续运行
            无依赖，程序正常运行

        String reqDataRegex = "[\\w/]+(\\$[\\.\\w]+)";
        Pattern reqDatap = Pattern.compile(reqDataRegex);
        Matcher reqDatam = reqDatap.matcher(reqData);
        while (reqDatam.find()){
            //条件为真进入find代码块-有依赖需要后续处理
            String key = reqDatam.group();  //    /api/loginCheck:$.msg
            String value = MapUtil.get(key);     //管理员登录成功

            //原始字符串- searchword=/api/loginCheck:$.msg
            //替换字符串- searchword=管理员登录成功
            reqData = reqData.replace(key,value);
        }
         */
        //将上面step2封装到RegexUtil
        //请求数据依赖处理
        reqData = RegexUtil.handlerReqDataOfDep(reqData);
        System.out.println(reqData);
        //MapUtil都合测试报告有关
        LocalCacheUtil.put("reqData",reqData);

        //System.out.println(id + " " + testCase + ThreadUtil.currentThreadInfo());//线程

        /*
        if ("POST".equals(reqType)) {
            //发送post
            actResult = RequestUtil.sendPost(url,reqData);
        }else {
            actResult = RequestUtil.sendGet(url,reqData);
        }
         */
        //优化上面if else代码
        //发送请求//发请求的log已经在TestListener加了？有疑问问老师
        actResult = "POST".equals(reqType)?RequestUtil.sendPost(url,reqData):
                RequestUtil.sendGet(url,reqData);
        //为了顺序在测试报告打印实际结果
        LocalCacheUtil.put(reqInterface,actResult);

        //接口输出与输入依赖
        /*
        处理第10列dep_key值 /api/loginCheck:$.msg;/api/loginCheckk:$stateCode ==>服务器返回值{"msg":"管理员登录成功"，"stateCode":"1"}
        思路
        step1:
        1.用正则表达式提取出表达式$.msg,$.stateCode
        2.JSONPath.read(actResult,$.msg)=管理员登录成功
          JSONPath.read(actResult,$.stateCode)=1
        3.map存储数据,key=/api/loginCheck:$.msg             value=管理员登录成功
                     key=/api/loginCheck:$stateCode        value=1

        if ("YES".equals(isDep)) {
            String regex = "[\\w/]+(\\$[\\.\\w]+)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(DepKey);
            while(m.find()){
                String depKey = m.group();
                String depValue = JSONPath.read(actResult,m.group(1)).toString();
                MapUtil.put(depKey,depValue);
            }
        }
         */
        //将上面step1封装到RegexUtil
        //对depKey判断处理
        RegexUtil.handlerDepKey(isDep,depKey,actResult);

        //System.out.println("result=" + actResult);
        //actResult={"msg":"管理员登录成功"，"stateCode":"1"}

        //思路：解析json里的msg和stateCode
        //实际值
        String msg = JSONPath.read(actResult,"$.msg").toString();
        String stateCode = JSONPath.read(actResult,"$.stateCode").toString();
        System.out.println("msg=" + msg + " ;stateCode=" + stateCode);

        //预期值
        //reqExpect=来自于excel的预期值，规则：JSONPath表达式=期望的值；JSONPath表达式=期望的值
        //excel预期值：$.msg=管理员登录成功；$.stateCode=1

        //实际值和预期值共同点：共用1个jsonpath表达式
        /*
        好处：
        解析预期值：excel预期值：$.msg=管理员登录成功；$.stateCode=1，结果为：
        预期值 String A1 = $.msg   String B1 = 管理员登录成功 ==> 实际值 C1 = JSONPath.read(actResult,A1).toString() ==>B1和C1对比
        预期值 String A2 = $.state   String B2 = 1 ==> 实际值 C1 = JSONPath.read(actResult,A2).toString() ==>B2和C2对比
         */

        /*
        设计预期值
        来源：接口文档（很多公司没有接口文档，需要手动抓包）（多和开发交流）git拉开发代码看返回值有哪些
        比对规则：尽量多或全覆盖实际值
         */
        //预期值
        /*
        String regex = "(\\$\\.\\w+)=([\\w\\u4e00-\\u9fa5]+)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(reqExpect);
        while(m.find()){
            String jsonPath = m.group(1);//$.msg          $.stateCode
            String expValue = m.group(2);//管理员登录成功   1

            String actValue = JSONPath.read(actResult,jsonPath).toString();
            System.out.println("预期值=" + expValue +" "+ "实际值=" + actValue);
            //断言失败后续程序不在执行
            Assert.assertEquals(actValue,expValue);
        }
        */
        //预期值和实际值对比
        RegexUtil.compareResult(reqExpect,actResult);
    }



}


