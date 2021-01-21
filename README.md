**NETTY 练习**

Netty是一个利用Java高级网路能力，提供了一个易于使用的API的客户/服务端框架。

Netty与Tomcat最大的区别就在于通信协议，Tomcat是基于Http协议的，而Netty可以通过编程实现各种协议，
因为netty能够通过codec自己来编码/解码字节流，完成类似redis访问的功能。

Netty优点：

* 高并发
* 传输快
* 封装好