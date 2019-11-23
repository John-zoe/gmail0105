# gmail0105 本地修改版本
#gmall用户服务8080
gmall-user-service用户服务的service层8070
gmall-user-web用户服务的web层8080

gmall-manage-web  port:8081
gmall-manage-service  port:8071

gmall-item-web  port:8082
gmall-item-service  port:8072



service:
mapper  访问数据层接口
serviceimpl  业务层实现类

api:
bean  数据库对象
service  业务层接口

web:
controller  客户层类调用service层方法

