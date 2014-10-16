概述 Overview
==================

jetbrick-template 是一个新一代 Java 模板引擎，具有高性能和高扩展性。 适合于动态 HTML 页面输出或者代码生成，可替代 JSP 页面或者 Velocity 等模板。 指令和 Velocity 相似，表达式和 Java 保持一致，易学易用。

* 支持类似于 Velocity 的多种指令
* 支持热加载
* 支持类型推导
* 支持可变参数方法调用
* 支持方法重载
* 支持方法扩展
* 支持函数扩展
* 支持自定义标签 #tag
* 支持宏定义 #macro
* 支持布局 Layout

文档 Documents
=================

http://subchen.github.io/jetbrick-template/

简单易用的指令
=================

jetbrick-template 指令集和老牌的模板引擎 Velocity 非常相似，易学易用。

```html
#define(List userlist)
<table>
  <tr>
    <td>序号</td>
    <td>姓名</td>
    <td>邮箱</td>
  </tr>
  #for (UserInfo user : userlist)
  <tr>
    <td>${for.index}</td>
    <td>${user.name}</td>
    <td>${user.email}</td>
  </tr>
  #end
</table>
```

基本开发 API 
=================

1. 创建自定义配置的 `JetEngine` 对象。推荐使用单例模式创建。
2. 根据模板路径，获取一个模板对象 `JetTemplate`。
3. 创建一个 `Map<String, Object>` 对象，并加入你的 data objects。
5. 准备一个待输出的对象，`OutputStream` 或者 `Writer`。
6. 根据你的 data objects 来渲染模板，并获得输出结果。

具体的 Java 代码，看上去是这样的：

```java
// 创建一个默认的 JetEngine
JetEngine engine = JetEngine.create(); 

// 获取一个模板对象
JetTemplate template = engine.getTemplate("/sample.jetx");

// 创建 context 对象
Map<String, Object> context = new HashMap<String, Object>();
context.put("user", user);
context.put("books", books);

// 渲染模板
StringWriter writer = new StringWriter();
template.render(context, writer);

// 打印结果
System.out.println(writer.toString());
```

开源许可 License
===================

```
Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.

  Author: Guoqiang Chen
   Email: subchen@gmail.com
  WebURL: https://github.com/subchen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
