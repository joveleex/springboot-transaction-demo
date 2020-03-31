# Spring事务管理

对与Spring事务管理的笔记

## 什么时候会回滚事务

Spring是通过AOP代理的方式来管理事务回滚的，因此只要当异常抛出方法外时就会回滚事务（即使方法外catch了异常）  
在方法内catch异常不会回滚事务


## 对编译时异常进行事务回滚

默认情况下只有抛出RuntimeException，Spring才会回滚事务  
通过设置@Transactional(rollbackFor = Exception.class)可以实现对指定编译时异常进行回滚事务  

## 事务回滚的范围

事务默认只回滚当前方法内的操作，但方法又会调用其他方法，其他方法可能也被事务管理，因此涉及到事务的传播行为

# Spring事务的7个传播行为

- PROPAGATION_REQUIRED
- PROPAGATION_SUPPORTS
- PROPAGATION_MANDATORY
- PROPAGATION_REQUIRES_NEW
- PROPAGATION_NOT_SUPPORTED
- PROPAGATION_NEVER
- PROPAGATION_NESTED

代码中分别提供了测试类通过A方法调用B方法来演示各种传播行为下向数据库account表插入数据的情况 

创建springboot-transaction-demo数据库，创建account表  
MySQL数据库初始化account表的DDL
```
CREATE TABLE `account` {
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
} ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

## PROPAGATION_REQUIRED

- 当前存在事务，则加入事务。A、B方法都受同一事务管理。
- 当前不存在事务，则创建新的事务。A方法不受事务管理，B方法受事务管理。

## PROPAGATION_SUPPORTS

- 当前存在事务，则加入事务。A、B方法都受同一事务管理。
- 当前不存在事务，则不使用事务。A、B方法都不受事务管理。

## PROPAGATION_MANDATORY

- 当前存在事务，则加入事务。A、B方法都受同一事务管理。
- 当前不存在事务，B方法直接抛出异常。

上述当B加入A的事务后，事务的会被标记未rollback-only，即B内抛出的异常不能在A中catch，否则抛出如下异常  
org.springframework.transaction.UnexpectedRollbackException: Transaction rolled back because it has been marked as rollback-only

## PROPAGATION_REQUIRES_NEW

- 当前存在事务，则先挂起事务，再创建新事务，A、B方法受不同事务管理。
- 当前不存在事务，则创建新的事务。A方法不受事务管理，B方法受事务管理。

## PROPAGATION_NOT_SUPPORTED

- 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。

## PROPAGATION_NEVER

- 以非事务方式执行，如果当前存在事务，则抛出异常。

## PROPAGATION_NESTED

- 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作。