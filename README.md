概述 Overview
==================

jetbrick-template 是一个新一代 Java 模板引擎，具有高性能和高扩展性。 适合于动态 HTML 页面输出或者代码生成，可替代 JSP 页面或者 Velocity 等模板。 指令和 Velocity 相似，表达式和 Java 保持一致，易学易用。

* 支持类似于 Velocity 的多种指令
* 支持静态编译
* 支持编译缓存
* 支持热加载
* 支持类型推导
* 支持泛型
* 支持可变参数方法调用
* 支持方法重载
* 支持类似于 Groovy 的方法扩展
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
#define(List<UserInfo> userlist)
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


卓越性能 Performance
========================

jetbrick-template 将模板编译成 Java ByteCode 运行，并采用强类型推导，无需反射和减少类型转换。渲染速度等价于 Java 硬编码。比 Velocity 等模板快一个数量级。 比 JSP 也快，因为 JSP 只有 Scriptlet 是编译的，Tag 和 EL 都是解释执行的。 而 jetbrick-template 是全编译的。

![performance](http://subchen.github.io/assets/images/perfermance.png)

在 Stream 模式中(Webapp 采用 OutputStream 将文本输出到浏览器)，由于 Java 硬编码输出字符串需要进行一次编码的转换。 而 jetbrick-template 却在第一次运行期间就缓存了编码转换结果，使得 jetbrick-template 的性能甚至优于 Java 硬编码。

测试平台 platform: Window 7 x64, Intel i5, 16GB RAM, JDK 1.6.0_41 x64


作者 Author
===================

* Author: Guoqiang Chen, Shanghai, China
* Email: subchen&#64;gmail.com
* WebSite: http://subchen.github.io/
* Blog: http://my.oschina.net/sub/blog


开源许可 License
===================

jetbrick-template  
http://subchen.github.io/jetbrick-template/

Copyright 2010-2014 Guoqiang Chen. All rights reserved.  
Email: subchen@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
