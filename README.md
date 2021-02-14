# RichTextX - 富文本编辑和显示
## Description
使用 RichTextX 只需少量的代码就可以轻松的在Android应用中实现富文本的编辑与Html文本的显示。
* 基于Android原生EditText。
* 支持文本编辑过程中图像的插入删除以及点击事件；更改文本字号；加粗；缩进等富文本编辑；支持Html文件解析并显示。
* RichTextX中图像的处理和加载由开发者自定义实现，这意味着图文混排的编辑和显示将更加灵活。
## Download
使用Gradle:

```gradle
dependencies{
   implementation 'com.shine56.richtextx:richtextx:1.0.5-beta'
}
```
或者Maven：

```gradle
<dependency>
	<groupId>com.shine56.richtextx</groupId>
	<artifactId>richtextx</artifactId>
	<version>1.0.5-beta</version>
	<type>pom</type>
</dependency>
```

## How to use RichTextX
#### EditText example
像添加EditText一样在布局文件中文件中添加RichEditText

```xml
<com.shine56.richtextx.view.RichEditText
    android:id="@+id/rich_edit_text"
    android:textSize="16sp"
    android:lineHeight="20dp"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```
在kotlin文件中

```kotlin
val richEditText = findViewById<RichEditText>(R.id.rich_edit_text)
/**
 * 插入图片
 */
val image = richEditText.imageBuilder
    .setImageUrl("imageUrl")
    .setDrawableGet {
        //获取drawable逻辑（这部分逻辑将执行在IO线程）
    }.create()
writeEdit.insertPhoto(image)

//我们还可以为image设置点击事件和删除事件
val image = writeEdit.imageBuilder
    .setImageUrl(it)
    .setDrawableGet {
       //获取drawable逻辑
    }
    .setOnImageCLickListener { view, imgUrl ->
        //点击事件响应逻辑
    }
    .setOnImageDeleteListener { view, imgUrl ->
        //删除事件响应逻辑
    }
    .create()
writeEdit.insertPhoto(image)

/**
 * 加粗
 */
richEditText.setBold(true)

/**
 * 缩进
 */
richEditText.indent()

/**
 * 设置字号
 */
richEditText.setFontSize(28)
```
#### TextView example
像添加TextView一样在布局文件中文件中添加RichTextView
```xml
<com.shine56.richtextx.view.RichTextView
    android:id="@+id/rich_text_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```
在kotlin文件中
```kotlin
/**
 * 显示htnl文本
 */
//将editText的文本转成html字符串
val htmlText = Html.toHtml(richEditText.text)

//将html字符串显示在RichTextView
val image = writeEdit.imageBuilder
    .setImageUrl(it)
    .setDrawableGet {
       //获取drawable逻辑
    }.create()
val richTextView = findViewById<RichTextView>(R.id.rich_text_view)
richTextView.setTextFromHtml(htmlText, image)
```
#### 补充说明
[blog](https://blog.csdn.net/weixin_42619856/article/details/110527709)
## License
[Apache License 2.0](https://github.com/shine56/RichTextX/blob/master/LICENSE)

## Screenshot
<img src="https://s1.ax1x.com/2020/10/10/06NMqO.md.jpg" width = "250" height = "550" alt="图片名称" align=center />    <img src="https://s1.ax1x.com/2020/10/10/06NlZD.md.jpg" width = "250" height = "550" alt="图片名称" align=center />
