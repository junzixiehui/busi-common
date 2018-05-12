# 业务通用框架

--基于jdk8 spring-boot


#### 代码规范 
>    严格代码规范，可读性，可维护性，可扩展

```
- 原则1：每个类和方法写注释（姓名+时间+描述）
- 原则2：业务日志打印业务必须用LogBuilder，具体参考com.renrenche.aftersale.order.order.service.RefundOrderService.applyRefundOrder
- 原则3：异常日志打印业务必须用ApplicationLogger。举例：ApplicationLogger.error("updateApplyQuality error",e);
- 原则4：调用第三方接口服务必须用com.renrenche.aftersale.order.common.util.http.HttpClientUtils,具体参考com.renrenche.aftersale.order.common.service.OctoService
- 原则5：接口接收入参必须是继承ParamReq的入参类；
- 原则6：接口返回必须是Resp；
- 原则7：接口Url地址必须写在Apis类统一管理，并命名规范；
- 原则8：接口入参定义全部不能为null,默认字符代替；
- 原则9：打印日志简短而优美，日志是给自己看的；
- 原则10：常量一定要抽象到常量类(ApplicationConstant,OrderConstant)；
- 原则11：钉钉播报统一调用com.renrenche.aftersale.order.notify.Notify 分为指标播报和报警播报；


```












---
Just because you can, doesn't mean you should