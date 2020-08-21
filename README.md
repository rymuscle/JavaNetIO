# Java NetIO
一步步从 BIO -> BIO+多线程 -> 单线程+NIO(非阻塞IO) -> NIO(IO多路复用+单线程) -> NIO(IO多路复用+多线程)

## Java BIO
1. 两处阻塞点
2. **问题**：两处阻塞点，导致无法实现与多个客户端进行通信
3. [DEMO](https://github.com/rymuscle/JavaNetIO/tree/master/src/com/javaio/study/bio/chapter02)

## Java BIO + 多线程
1. 主线程保持第一处阻塞点，即 accept() 处的阻塞。而将第二处阻塞点，即read()处的阻塞交由客户端子线程各自去处理。从而可以实现与多客户端进行通信。
2. **问题**：高并发场景下, 为每个用户请求创建一个进程或线程的开销非常大; 多线程编程的复杂度也比较高; 即使采用线程池，也只能起到一定的缓解作用
3. [DEMO](https://github.com/rymuscle/JavaNetIO/tree/master/src/com/javaio/study/bio/chapter03)

## Java NIO (非阻塞IO)
1. 用户态(应用程序)轮询检测客户端socketfd集合。（Linux IO模型中的 NIO）
2. 问题：低效
3. [DEMO](https://github.com/rymuscle/JavaNetIO/tree/master/src/com/javaio/study/nio/chapter03)

## Java NIO (IO多路复用 + 单线程) 
1. 将客户端Socketfd集合的轮询交由系统内核去完成 （Selector底层根据操作系统的不同会选择使用 select 或者 epoll）
2. IO多路复用 本身整体对外(用户线程)是阻塞式的。但是IO多路复用内部其实是非阻塞的,这点可以在代码中体会到(如果在注册到Selector之前，不设置为非阻塞代码会报错)
3. [DEMO](https://github.com/rymuscle/JavaNetIO/tree/master/src/com/javaio/study/nio/iomultiplexing)
