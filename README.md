# RichTextX - 富文本编辑和显示
## Description
使用 RichTextX 只需少量的代码就可以轻松的在Android应用中实现富文本的编辑与Html文本的显示。
* 基于kotlin、AndroidX
* 支持文本编辑过程中图像和动画GIF的插入，删除以及点击事件；更改局部文本字号；加粗；缩进等富文本编辑；支持Html文件解析并显示。
* RichTextX中图像的处理和加载由开发者自定义实现，这意味着图文混排的编辑和显示将更加灵活。
## Download
使用Gradle:

```gradle
dependencies{
   implementation 'com.shine56.richtextx:richtextx:1.0.0'
}
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
#### EditText example
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
    //获取drawable逻辑
}.apply()

//加粗
richEditText.isBold = true

//缩进
richEditText.indent()

//设置字号
richEditText.setFontSize(28)

//插入图片同时为其设置点击事件
richEditText.insertPhoto("R.drawable.example"){
    //获取getDrawable逻辑
}.setOnCLickListener { view, imgUrl ->
    //点击事件逻辑
}.setOnDeleteListener { view, imgUrl ->
    //点击右上角删除事件逻辑
}.apply()


```
#### TextView example
像添加TextView一样在布局文件中文件中添加RichTextView
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
//setTextFromHtml()第一个参数是html文本，第二个参数是Html.ImageGetter实例。
//开发者需要写一个类继承Html.ImageGetter，实例化其中getDrawable行为。
richTextView.setTextFromHtml(htmlText, imageGetter).apply()

//为图片设置点击事件
richTextView.setTextFromHtml(htmlText, imageGetter)
   .setOnCLickListener { view, imgUrl ->
       //点击事件逻辑
   }.apply()
```
## License
[Apache License 2.0](https://github.com/shine56/RichTextX/blob/master/LICENSE)