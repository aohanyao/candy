# mvp-core-library
mvp-core-library


Write less, Error less



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