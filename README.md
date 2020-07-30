# RichTextX
## Description
RichTextX是一个帮助Android应用轻松实现富文本的编辑和显示的组件
* 基于kotlin、AndroidX
* 支持文本编辑过程中图像和动画GIF的插入、删除以及点击事件；更改字号；加粗；缩进等富文本编辑；支持Html文件解析。
* RichTextX中图像的处理和加载由开发者自定义实现，这意味着图文混排的编辑和显示将更加灵活。
## Download
你可以直接将Github项目下载放到你的项目当中
或者使用Gradle:

```gradle
implementation 'com.shine56.richtextx:richtextx:1.0.0'
```
或者Maven：

```gradle
<dependency>
	<groupId>com.shine56.richtextx</groupId>
	<artifactId>richtextx</artifactId>
	<version>1.0.0</version>
	<type>pom</type>
</dependency>
```

## How to use RichTextX
### 文本编辑示范
像添加EditText一样在布局文件中文件中添加RichEditText

```xml
<com.shine56.richtextx.RichEditText
    android:id="@+id/rich_edit_text"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```
在kotlin文件中

```kotlin
val richEditText = findViewById<RichEditText>(R.id.rich_edit_text)

//插入图片
richEditText.insertPhoto("R.drawable.example"){
    getDrawable(R.drawable.example)!!
}.apply()

//加粗
richEditText.isBold = true

//缩进
richEditText.indent()

//设置字号
richEditText.setFontSize(28)

//为图片设置点击事件
richEditText.insertPhoto("R.drawable.example"){
    getDrawable(R.drawable.example)!!
}.setOnCLickListener { view, imgUrl ->
    //点击事件逻辑
}.setOnDeleteListener { view, imgUrl ->
    //点击右上角删除事件逻辑
}
```
### 文本显示示范
像添加EditText一样在布局文件中文件中添加RichTextView
```xml
<com.shine56.richtextx.RichTextView
    android:id="@+id/rich_text_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```
在kotlin文件中
```kotlin
//将editText的文本转成html字符串
val htmlText = Html.toHtml(richEditText.text)

//将html字符串显示在RichTextView
val richTextView = findViewById<RichTextView>(R.id.rich_text_view)
val imageGetter = MyImageGetter(this)

richTextView.setTextFromHtml(htmlText, imageGetter)
```
## License
[Apache License 2.0](https://github.com/shine56/RichTextX/blob/master/LICENSE)
