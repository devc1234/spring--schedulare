Task Execution and Scheduling
27.1 Introduction
The Spring Framework provides abstractions for asynchronous execution and scheduling of tasks with the TaskExecutor and TaskScheduler interfaces, respectively. Spring also features implementations of those interfaces that support thread pools or delegation to CommonJ within an application server environment. Ultimately the use of these implementations behind the common interfaces abstracts away the differences between Java SE 5, Java SE 6 and Java EE environments.

Spring also features integration classes for supporting scheduling with the Timer, part of the JDK since 1.3, and the Quartz Scheduler (http://quartz-scheduler.org). Both of those schedulers are set up using a FactoryBean with optional references to Timer or Trigger instances, respectively. Furthermore, a convenience class for both the Quartz Scheduler and the Timer is available that allows you to invoke a method of an existing target object (analogous to the normal MethodInvokingFactoryBean operation).

27.2 The Spring TaskExecutor abstraction
Spring 2.0 introduces a new abstraction for dealing with executors. Executors are the Java 5 name for the concept of thread pools. The "executor" naming is due to the fact that there is no guarantee that the underlying implementation is actually a pool; an executor may be single-threaded or even synchronous. Spring's abstraction hides implementation details between Java SE 1.4, Java SE 5 and Java EE environments.

Spring's TaskExecutor interface is identical to the java.util.concurrent.Executor interface. In fact, its primary reason for existence is to abstract away the need for Java 5 when using thread pools. The interface has a single method execute(Runnable task) that accepts a task for execution based on the semantics and configuration of the thread pool.

The TaskExecutor was originally created to give other Spring components an abstraction for thread pooling where needed. Components such as the ApplicationEventMulticaster, JMS's AbstractMessageListenerContainer, and Quartz integration all use the TaskExecutor abstraction to pool threads. However, if your beans need thread pooling behavior, it is possible to use this abstraction for your own needs.

27.2.1 TaskExecutor types
There are a number of pre-built implementations of TaskExecutor included with the Spring distribution. In all likelihood, you shouldn't ever need to implement your own.

SimpleAsyncTaskExecutor

This implementation does not reuse any threads, rather it starts up a new thread for each invocation. However, it does support a concurrency limit which will block any invocations that are over the limit until a slot has been freed up. If you're looking for true pooling, keep scrolling further down the page.

SyncTaskExecutor

This implementation doesn't execute invocations asynchronously. Instead, each invocation takes place in the calling thread. It is primarily used in situations where multithreading isn't necessary such as simple test cases.

ConcurrentTaskExecutor

This implementation is a wrapper for a Java 5 java.util.concurrent.Executor. There is an alternative, ThreadPoolTaskExecutor, that exposes the Executor configuration parameters as bean properties. It is rare to need to use the ConcurrentTaskExecutor but if the ThreadPoolTaskExecutor isn't robust enough for your needs, the ConcurrentTaskExecutor is an alternative.

SimpleThreadPoolTaskExecutor

This implementation is actually a subclass of Quartz's SimpleThreadPool which listens to Spring's lifecycle callbacks. This is typically used when you have a thread pool that may need to be shared by both Quartz and non-Quartz components.
ThreadPoolTaskExecutor
It is not possible to use any backport or alternate versions of the java.util.concurrent package with this implementation. Both Doug Lea's and Dawid Kurzyniec's implementations use different package structures which will prevent them from working correctly.
This implementation can only be used in a Java 5 environment but is also the most commonly used one in that environment. It exposes bean properties for configuring a java.util.concurrent.ThreadPoolExecutor and wraps it in a TaskExecutor. If you need something advanced such as a ScheduledThreadPoolExecutor, it is recommended that you use a ConcurrentTaskExecutor instead.
TimerTaskExecutor
This implementation uses a single TimerTask as its backing implementation. It's different from the SyncTaskExecutor in that the method invocations are executed in a separate thread, although they are synchronous in that thread.
CommonJ is a set of specifications jointly developed between BEA and IBM. These specifications are not Java EE standards, but are standard across BEA's and IBM's Application Server implementations.
This implementation uses the CommonJ WorkManager as its backing implementation and is the central convenience class for setting up a CommonJ WorkManager reference in a Spring context. Similar to the SimpleThreadPoolTaskExecutor, this class implements the WorkManager interface and therefore can be used directly as a WorkManager as well
