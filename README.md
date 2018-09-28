

# Write less, Error less

# Version
[![](https://jitpack.io/v/aohanyao/candy.svg)](https://jitpack.io/#aohanyao/candy)


# MVP  Template Sample



[AndroidStudioCodeTemplate](https://github.com/aohanyao/AndroidStudioCodeTemplate)


# 项目初始化
## 1.设置主题颜色
在程序初始化的时候，调用[ThemHelper](library_core/src/main/java/com/td/framework/global/helper/ThemHelper.kt)进行主题颜色的初始化，可自行增加标识，只需要初始化一次。

```kotlin
ThemHelper.setPrimaryColor(color)
ThemHelper.setPrimaryDarkColor(color)

```
## 2.修改toolbar参数
请参照BaseToolbarStyle样式进行重写。

# 常用方法
## 1.事件总线
1. 重写方法

```kotlin
/**
 * 是否使用事件总线
 * @return
 */
protected fun useEventBus(): Boolean {
    return true
}
```

2. 发送事件

```kotlin
postEvent(event)
```

## 2.Activity的启动
请调用

     launcherActivity<Activity>()
     
类似的方法，进行了封装，全部以launcher开头

# 目录结构说明
## library_utils
工具类包
## library_ui
自定义View相关
- com.td.framework.ui
	- adapter
		- FullyGridLayoutManager  子view铺满
		- FullyLinearLayoutManager
	- flow
		- FlowTagLayout流式布局
	- scroll
		- StickScrollView悬停title的scrollView
	- swipebacklayout
		- SwipeBackLayou滑动返回
## library_core
- com.td.framework
	- base
		- listener 实现的一些常用的接口
		- view 封装的基类
			- TDBaseActivity
				- 是RxAppCompatActivity的子类
					- 提供RxJava的生命周期绑定
				- 事件总线(EventBus)
					- useEventBus
					- postEvent
				- Toolbar-的初始化和适配
					- 纯色背景
					- 自定义drawable背景
				- showToast
				- AppManager
					- Activity的加入和移除
				- 友盟
					- 设置友盟的相关状态
				- Them 设置
					- 根据ThemHelper的值来切换不用的主题颜色
			- TDBaseFragment
				- RxFragment的子类
					- 提供生命周期的绑定
				- 事件总线(EventBus)
					- useEventBus
					- postEvent
				- showToast
			- TDBaseLoadingActivity(TDBaseLoadingFragment) 做了状态布局的封装
				- inflateView(resId: Int),子类必须使用这个方法解析根布局
				- initLoadView() - 初始化相关状态布局
				- showLoading()- 显示loading动画，用于加载数据的时候
				- showRetry - 显示重试动画，用于网络请求错误重试
				- setErrorText - 设置重试的文字提示
				- setEmptyText 设置空布局状态下的文字提示
				- showNoPermissions  显示无权查看动画
				- showContent 显示内容，请求网络成功并填充数据完成后调用
				- showEmpty 显示空布局，一般没有数据的时候调用
				- onRetry 抽象方法，子类必须实现，出现错误时点击重试回调此方法
		- biz 网络层
			- cookie cookie管理相关
				- CookiesManager cookie管理类
				- PersistentCookieStore 持久化cookie的工具类
				- SerializableOkHttpCookies cookie序列化工具类
			- ApiSubscriber RxJava订阅类
				- 使用与RxJava的最后一步，订阅(Subscriber)结果
				- ApiSubscriber接收类泛型参数<T>
				- 构造方法接收BaseView，BaseView中包含onFail、complete等方法
				- ApiSubscriber中处理错误转换、结果转换和回调
				- 对异常进行初步的处理和分类，再通过BaseView回调到相应的Activity或Fragment
				- 处理授权失败和异常日志
			- BaseApi - API基类，在Application中通过registerConfigProvider方法配置相关信息
				- 配置baseUrl
				- getRetrofit
					- 重载两个方法，一个有baseUrl，一个没有
						- 传入baseUrl则使用自定义的baseUrl
						- 不传入baseUrl则使用前面配置好的baseUrl
					- ConverterFactory 使用的是自定义的CustGsonConverterFactory
				- HttpClient相关的配置
					- Interceptor
					- HttpLoggingInterceptor
			- NetError 错误异常分类
			- NetProvider 接口，用于BaseApi的registerConfigProvider
	- ex 异常
		- BaseException 基础异常
		- DbException  数据库异常
		- HttpException  网络相关异常，暂时没有使用
	- expand  kotlin的扩展函数
		- AnkoContext   从Anko中拷贝的代码，提供UI线程回调
		- ImageViewExpand
			- 提供各种直接加载网络图片方法
			- 提供加载本地文件方法
			- 提供加载资源方法
		- Intents
			- 拓展launcherActivity方法，通过Activity启动Activity直接调用launcherActivity方法
			- launcherActivity需要一个方法形参，该形参为Activity的之类，是跳转的目标Activity
			- launcherActivity传递不定长度的键值对-
				- "key1" to "value1","key2" to "value2"
			- makeCall  拨打电话，已做权限适配
		- Internals intent相关的封装
			- fillIntentArguments  键值对转换为intents
		- RouterExpand 路由相关的拓展
			- 拓展navigationActivityFromPair方法，通过路由地址方式启动请调用此方法
			- 和launcherActivity一样，接收键值对相关的数据传递方式
		- WidgetExpand 小组件的拓展函数
			- TextView.reset  清空TextView
			- TextView.trackingInput 回调文字更改的个数
			- View.switchLinkViews 切换显示状态（多个文本框绑定一个按钮）
			- TextView.contentText 直接获取文本内容
			- 剩余自行查看代码，全部有注释和例子
	- global
		- app
			- App 基类app，继承MultiDexApplication
				- 初始化AppManager
				- 初始化友盟
				- 初始化腾讯浏览服务
				- 初始化路由
			- AppManager 堆栈管理器
				- addOnStartActivity 将启动的Activity加入
				- removeOnStartActivity 移除Activity
				- isExitActivity 判断是否已经存在该Activity
				- exitApplication 清除所有Activity，退出应用
			- Constant 基类常量
				- IDK、IDK2、IDK3 数据传输过程中统一使用的key
			- CrashHandler 全局异常捕获，暂时无用
		- helper
			- AnimatorHelper 滑动动画帮助类
			- CallPhoneHelper 号码拨打
			- GlideRoundTransform Glide圆角
			- ImageHelper 图片加载类
				- kotlin中不建议直接使用，而是使用拓展函数
			- ThemHelper 主题帮助类
				- 用户不同Activity切换不同的颜色主题
		- router 路由相关
			- RouterBasePath 基本的路由地址，如登陆页
			- RouterHelper 路由跳转类
				- 不建议直接使用，而是使用拓展函数
	- moudle 存放单一的功能模块
		- db 数据库模块
		- loding 弹窗
		- share 第三方分享
		- suspension  通讯录
	- mvp mvp的封装，重点
		- base 封装了mvp的基类Activity和Fragment
			- MvpBaseActivity(MvpBaseFragment)
				- 接收一个类泛型 P
				- P 必须是BasePresenter的子类
				- 封装了P的subscribe和unSubscribe绑定
				- 初始化P，定义newP抽象方法，由子类进行实现和返回P的实例
				- handlerFail(NetError) 处理失败信息
				- handlerComplete 处理请求完成
				- 提供dialog和相关封装的调用方法，并且绑定了dialog和RxJava的订阅
					- dismissDialog
					- showLoadingDialog(msg: String, cancelable: Boolean)
						- cancelable为true，窗口可关闭，关闭后取消RxJava订阅