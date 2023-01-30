# yunjiproject
the first project
# 1.需求分析
## 1.1功能分析
![image](https://user-images.githubusercontent.com/101503750/203713692-3eb48cfa-4b7d-48de-a8f0-c0cc7a4da1af.png)
## 1.2 技术选型
- JSP
- Servlet
- Jquery
- log4j
- Junit
## 1.3技术细节
- 增删改查
- 免登录：Session和 Cookie
- 非法访问：过滤器Filter
- 文件上传
- 分页
- Ajax:Dom操作
- 富文本编辑器
## 1.4开发环境
IDEA+Maven+MySQL+Jdk1.8+Tomcat

# 2.数据库设计
根据系统架构设计，本系统采用简单灵巧的MySQL数据库，遵循关系型数据库标准，符合三范式设计，一切从简，相关表如下：
## 2.1E-R图表
![image](https://user-images.githubusercontent.com/101503750/203716389-30e49b97-84b6-4670-8b27-6d34c6528a77.png)
## 2.2表结构详情
### 2.2.1用户表
tb_user
|字段名称|字段类型|字段描述|
|----|----|----|----|
|userId|int|主键，自增|
|uname|varchar|用户名称|
|upwd|varchar|用户密码|
|nick|varchar|用户昵称|
|head|varchar|用户头像|
|mood|varchar|用户心情|
### 2.2.2类型表
tb_note_type
|字段名称|字段类型|字段描述|
|----|----|----|----|
|typeId|int|主键，自增|
|typeName|varchar|类型名称|
|userId|int|用户ID|
### 2.2.3云记表
tb_note
|字段名称|字段类型|字段描述|
|----|----|----|----|
|noteId|int|主键，自增|
|title|varchar|标题|
|content|text|内容|
|typeId|int|类型ID|
|pubTime|datetime|发布时间|
|lon|float|经度|
|lat|float|纬度|
## 3.环境搭建
### 3.1分层思想
![image](https://user-images.githubusercontent.com/101503750/203721195-64efcbff-a94f-468c-9c95-3248846e6352.png)
Java的几种对象解释
- PO
persistant object持久对象，可以看成是与数据库汇总的表相映射的Java对象。
最简单的PO就是对应数据库中某个表中的一条记录，多个记录可以用PO的集合
PO中应该不包含任何对数据库的操作
- VO
value object值对象，通常用于业务层之间的数据传递，和PO一样也是仅仅包含数据而已
但应该是抽象出的业务对象，可以和表对应，也可以不，这根据业务的需要
- DAO
data access object数据访问对象，此对象用于访问数据库
通常与PO结合使用，DAO中包含了各种数据库的操作方法
通过它的方法结合PO对数据库进行相关的操作
- BO
business object业务对象，封装业务逻辑的java对象
通过调用DAO方法结合PO,VO进行业务操作
- POJO
plain ordinary java object简单无规则Java对象


