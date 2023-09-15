# Monitor-Network

基于 ServiceLoader，监控 Okhttp
网络请求，实现拦截、解析、转发、存储、上报等功能。采用组件化开发方式，各业务组件均可单独拆分使用，同时提供export对外调用组件，外部调用无需关心内部实现，组件之间集成完全解耦

## 集成组件介绍

> Components-stone

- 提供基础依赖管理[build.gradle.kts](stone/build.gradle.kts)(可修改依赖版本)
- 提供DataStore/Gson/RetrofitProvider对外调用管理类(可替换)
- 提供NetworkInterceptor全局拦截器(Okhttp Interceptor)(如需替换，需全局处理接口注解)
- 提供Application生命周期分发管理，基于@AutoService注解生成并解耦(
  如需替换，需全局处理接口注解)

> Components-http-export

该组件为对外集成组件，可直接依赖

- [HttpDataResult.kt](http-export%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fhttp%2Fexport%2FHttpDataResult.kt)
  提供对外获取Interceptor拦截器解析实体model
- [HttpInterceptorListener.kt](http-export%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fhttp%2Fexport%2FHttpInterceptorListener.kt)
  提供对外转发实体model数据接口，可供上层业务获取网络请求数据并自定义处理，参考app组件模块[[HttpInterceptor.kt](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fnetwork%2Finterceptor%2FHttpInterceptor.kt)]

> Components-http

该组件是http-export内部业务实现组件，处理网络请求白名单、解析、转发等业务操作,不可直接依赖，建议上层business业务集成

- [HttpInterceptor.kt](http%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fhttp%2FHttpInterceptor.kt)
  拦截原始okhttp网络请求，并组装实体模型[HttpResult.kt](http%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fhttp%2FHttpResult.kt)
- [HttpResultParser.kt](http%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fhttp%2FHttpResultParser.kt)
  解析原始请求header、body、formBody并组合[HttpDataResult]模型
- [DispatchProvider.kt](http%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fhttp%2FDispatchProvider.kt)
  通过异步协程与ServiceLoader转发http处理完成的数据模型

> Components-room-export

该组件为对外集成组件，可直接依赖

- [HttpDataEntity.kt](room-export%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Froom%2Fexport%2FHttpDataEntity.kt)
  数据库表映射关联实体模型，供外部组合数据并存储数据源，参考app组件[HttpInterceptor.kt](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fnetwork%2Finterceptor%2FHttpInterceptor.kt)
- [HttpDataManage.kt](room-export%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Froom%2Fexport%2FHttpDataManage.kt)
  数据库对外操作接口，提供增删改查及自定义查询sql功能
- [QueryFilter.kt](room-export%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Froom%2Fexport%2FQueryFilter.kt)
  自定义查询过滤条件，参考app组件[MainActivity.kt](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fnetwork%2FMainActivity.kt)

> Components-room

该组件是room-export内部业务实现组件，提供room database、table、dao实现

- [HttpDataBaseLifecycle.kt](room%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Froom%2FHttpDataBaseLifecycle.kt)
  生命周期内初始化room相关组件，获取dao操作实例
- [HttpDataDaoProvider.kt](room%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Froom%2FHttpDataDaoProvider.kt)
  实体模型与Table表映射关系关联
- [HttpDataManage.kt](room%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Froom%2FHttpDataManage.kt)
  数据库增删改查具体实现，关联room-export组件中[HttpDataManage.kt]

> Components-report-export

该组件为对外集成组件，可直接依赖

- [WorkReport.kt](report-export%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Freport%2Fexport%2FWorkReport.kt)
  对外开放业务查询过滤实体、业务上报平台区分，关联WorkManager-InputData参数

> Components-report

该组件是report-export内部业务实现组件，提供WorkManager后台上报功能

- [dingtalk](report%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Freport%2Fdingtalk)
  钉钉工作空间及鉴权服务与数据上报（仅作参考），具体实现可换为实际上报业务平台
- [markdown](report%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Freport%2Fmarkdown)
  格式化上报报文（仅作参考），具体按实际业务要求处理
- [work](report%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Freport%2Fwork)
  处理数据上报及冗余数据处理（仅作参考），具体按实际业务要求处理

> Components-app

上层业务集成组件壳，包含所有export及内部实现组件,组装额外公共参数(用户信息、设备信息、应用信息)

```
kapt("com.google.auto.service:auto-service:1.1.1")
api("com.google.auto.service:auto-service-annotations:1.1.1")
implementation(project(":stone"))
implementation(project(":http"))
implementation(project(":http-export"))
implementation(project(":room"))
implementation(project(":room-export"))
implementation(project(":report"))
implementation(project(":report-export"))
```

具体调用方法在[MainActivity.kt]与[HttpInterceptor.kt]，应用内BaseApplication应替换为项目实际使用类

## 使用说明

项目中为了演示效果采用钉钉上报，因此需要申请企业应用AppKey/SecretKey/AgentId/进行鉴权以，通过以上key信息获取UserIds完成消息推送通知，
实际应用中推荐上报到日志中台或相关日志平台，作为全链路监控的一环
> 钉钉开放平台 > https://open.dingtalk.com/

> gradle.properties
> 需配置以下内容,关联影响鉴权方法位置 : [DingRepository.kt](report%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Freport%2Fdingtalk%2FDingRepository.kt)

```
# Ding key config
dingAppKey=
dingSecretKey=
dingAgentId=
dingUsersId=
```

实际调用代码示例
> [DingProvider.kt](report%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Freport%2Fdingtalk%2FDingProvider.kt)

```
suspend fun reportWorkSpace(title: String, content: String, success: (Boolean) -> Unit) {
  ...
  ...
  ...
}
```

> 可自定义DataServerProvider.kt ，处理实际数据上报平台

```
suspend fun reportDataServer(title: String, content: String, success: (Boolean) -> Unit) {
   //do something
}

suspend fun reportDataOther(title: String, content: String, success: (Boolean) -> Unit) {
   //do something
}
```

演示效果截图 [screenshot](screenshot)

![example.png](screenshot%2Fexample.png)

![insert-room.png](screenshot%2Finsert-room.png)

![report.png](screenshot%2Freport.png)

## ServiceLoader

基于@AutoService注解生成的metadata可在各自组件build目录查看，例如:

- report/build/intermediates/java_res/debug/out/META_INF/services/*
- report/build/intermediates/runtime_library_classes_dir/debug/META_INF/services/*
- report/build/tmp/kapt3/classes/debug/META_INF.services

使用方式可参考各业务组件中含有@AutoService class,例如：
> auto-service官方文档地址 > https://github.com/google/auto/tree/main/service

> Declaration Interface

```
/**
 * Define lifecycle methods what you want to use
 */
interface ApplicationLifecycle {

    /** Lifecycle onAttachBaseContext */
    fun onAttachBaseContext(context: Context)

    /** Lifecycle onCreate */
    fun onCreate(application: Application)

    /** marking priority,0 is highest priority,next is 1,2,3...100 ,you can custom it */
    fun priority(): Int
}
```

> Annotation Class

```
/**
 * Add google [@AutoService] annotation,to implementation it
 */
@AutoService(ApplicationLifecycle::class)
class ApplicationLifecycleProxy : ApplicationLifecycle {

    override fun onAttachBaseContext(context: Context) {
        //do somethings
    }

    override fun onCreate(application: Application) {
        //do somethings
        DataStoreProvider.initDataStore(application)
    }

    override fun priority(): Int {
        //in base application context,do somethings highest priority,so you should return 0
        return 0
    }
}
```

> ServiceLoader.load

```
/**
 * With google auto-service,it will collect all @AutoService annotation class
 */
class ServiceLoaderProxy {

    private val loader: ServiceLoader<ApplicationLifecycle> by lazy {
        ServiceLoader.load(ApplicationLifecycle::class.java)
    }

    val lifecycleQueue by lazy { loader.sortedWith(compareBy { it.priority() }) }
}
```

## 引用三方库

1. Google JetPacks > https://developer.android.com/jetpack
2. Kotlin Coroutines > https://github.com/Kotlin/kotlinx.coroutines
3. Retrofit & Okhttp > https://github.com/square/retrofit & https://github.com/square/okhttp
4. Gson > https://github.com/google/gson
5. auto-service > https://github.com/google/auto/tree/main/service