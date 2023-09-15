# Monitor-Network

中文文档[[README_zh.md](README_zh.md)]

Based on ServiceLoader, monitor okhttp network requests, realize the functions of intercepting,
parsing, dispatching, storing, reporting and so on.Adopt componentized development mode, each
business component can be split and used individually, while providing export external call
components, external calls do not need to care about the internal implementation, the integration
between the components is completely decoupled.

## Components Introduction

> Components-stone

- Provide basic dependency management[build.gradle.kts](stone/build.gradle.kts)(Modifiable
  dependency version)
- Provide DataStore/Gson/RetrofitProvider external call management class (replaceable)
- Provide NetworkInterceptor global interceptor(Okhttp Interceptor)(if replace，handle global
  interface annotations)
- Provide Application lifecycle management，base on @AutoService annotation generation and
  decoupling(
  if replace，handle global interface annotations)

> Components-http-export

This component is an externally integrated component that can be directly relied upon for

- [HttpDataResult.kt](http-export%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fhttp%2Fexport%2FHttpDataResult.kt)
  Provide external access to the interceptor parsing entity model
- [HttpInterceptorListener.kt](http-export%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fhttp%2Fexport%2FHttpInterceptorListener.kt)
  Provide external forwarding entity mod data interface, can be used for the upper layer of business
  to obtain the network request data and customize the processing, refer to the app component
  module[[HttpInterceptor.kt](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fnetwork%2Finterceptor%2FHttpInterceptor.kt)]

> Components-http

This component is http-export internal business implementation components, dealing with network
request whitelisting, parsing, forwarding and other business operations, can not be directly relied
upon, it is recommended that the upper business business integration

- [HttpInterceptor.kt](http%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fhttp%2FHttpInterceptor.kt)
  Intercept okhttp requests and assemble entity
  models[HttpResult.kt](http%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fhttp%2FHttpResult.kt)
- [HttpResultParser.kt](http%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fhttp%2FHttpResultParser.kt)
  Parsing the original request header, body, formBody and assembling the [HttpDataResult] model
- [DispatchProvider.kt](http%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fhttp%2FDispatchProvider.kt)
  Data model for forwarding completed http processing via asynchronous concatenation with
  ServiceLoader

> Components-room-export

This component is an externally integrated component that can be directly relied upon for

- [HttpDataEntity.kt](room-export%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Froom%2Fexport%2FHttpDataEntity.kt)
  Database table mapping associated entity model for external combination of data and storage of
  data sources, reference app
  component[HttpInterceptor.kt](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fnetwork%2Finterceptor%2FHttpInterceptor.kt)
- [HttpDataManage.kt](room-export%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Froom%2Fexport%2FHttpDataManage.kt)
  Database external operation interface, providing additions, deletions, changes and customized
  query sql function
- [QueryFilter.kt](room-export%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Froom%2Fexport%2FQueryFilter.kt)
  Customized query filters, reference app
  components[MainActivity.kt](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Fnetwork%2FMainActivity.kt)

> Components-room

This component is the internal business implementation component of room-export, which provides room
database, table, and dao implementations.

- [HttpDataBaseLifecycle.kt](room%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Froom%2FHttpDataBaseLifecycle.kt)
  Initialize room-related components during the lifecycle, get instances of dao operations
- [HttpDataDaoProvider.kt](room%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Froom%2FHttpDataDaoProvider.kt)
  Entity Model and Table Table Mapping Relationship Association
- [HttpDataManage.kt](room%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Froom%2FHttpDataManage.kt)
  Database additions, deletions, and changes are implemented in the room-export
  component [HttpDataManage.kt].

> Components-report-export

This component is an externally integrated component that can be directly relied upon for

- [WorkReport.kt](report-export%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Freport%2Fexport%2FWorkReport.kt)
  Open to the public business query filtering entity, business reporting platform distinction,
  associated WorkManager-InputData parameters

> Components-report

This component is the internal business implementation component of report-export, which provides
the WorkManager backend reporting function.

- [dingtalk](report%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Freport%2Fdingtalk)
  DingTalk workspace and authentication services and data reporting (for reference only), the
  specific implementation can be replaced with the actual reporting business platforms
- [markdown](report%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Freport%2Fmarkdown)
  Formatted reporting messages (for reference only), according to actual business requirements
- [work](report%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Freport%2Fwork)
  Handle data reporting and redundant data handling (for reference only), depending on actual
  business requirements

> Components-app

Upper layer business integration component shell, including all export and internal implementation
components, assembly of additional public parameters (user information, device information,
application information)

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

Specific call methods in [MainActivity.kt] and [HttpInterceptor.kt], the application BaseApplication
should be replaced with the actual class used by the project.

## Instructions for use

In the project for the purpose of demonstrating the effect of DingTalk reporting, so you need to
apply for enterprise application AppKey/SecretKey/AgentId/ for authentication to, through the above
key information to obtain UserIds to complete the message push notification, the actual application
is recommended to be reported to the log center or the relevant log platform as a link to the whole
chain monitoring.
In practice, it is recommended to report to the log center or related log platforms as a link in the
whole chain monitoring.
> DingTalk Platform > https://open.dingtalk.com/

> gradle.properties
>
The following should be configured, and the association affects the location of the authentication
method : [DingRepository.kt](report%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Freport%2Fdingtalk%2FDingRepository.kt)

```
# Ding key config
dingAppKey=
dingSecretKey=
dingAgentId=
dingUsersId=
```

Example of actual calling code
> [DingProvider.kt](report%2Fsrc%2Fmain%2Fjava%2Fcom%2Fmanna%2Fmonitor%2Freport%2Fdingtalk%2FDingProvider.kt)

```
suspend fun reportWorkSpace(title: String, content: String, success: (Boolean) -> Unit) {
  ...
  ...
  ...
}
```

> Customizable DataServerProvider.kt to handle actual data reporting to the platform.

```
suspend fun reportDataServer(title: String, content: String, success: (Boolean) -> Unit) {
   //do something
}

suspend fun reportDataOther(title: String, content: String, success: (Boolean) -> Unit) {
   //do something
}
```

Demo screenshot [screenshot](screenshot)

![example.png](screenshot%2Fexample.png)

![insert-room.png](screenshot%2Finsert-room.png)

![report.png](screenshot%2Freport.png)

## ServiceLoader

The metadata generated based on the @AutoService annotation can be viewed in the respective
component build directory, for example:

- report/build/intermediates/java_res/debug/out/META_INF/services/*
- report/build/intermediates/runtime_library_classes_dir/debug/META_INF/services/*
- report/build/tmp/kapt3/classes/debug/META_INF.services

The usage can be seen in each business component containing the @AutoService class, for example:
> auto-service official document address > https://github.com/google/auto/tree/main/service

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

## Third Libraries

1. Google JetPacks > https://developer.android.com/jetpack
2. Kotlin Coroutines > https://github.com/Kotlin/kotlinx.coroutines
3. Retrofit & Okhttp > https://github.com/square/retrofit & https://github.com/square/okhttp
4. Gson > https://github.com/google/gson
5. auto-service > https://github.com/google/auto/tree/main/service