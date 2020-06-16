# 装饰模式

> 装饰模式主要包含以下角色。    
1.抽象构件角色：定义一个抽象接口以规范准备接收附加责任的对象。     
2.具体构件角色：实现抽象构件，通过装饰角色为其添加一些职责。    
3.抽象装饰角色：继承抽象构件，并包含具体构件的实例，可以通过其子类扩展具体构件的功能。     
4.具体装饰角色：实现抽象装饰的相关方法，并给具体构件对象添加附加的责任。    

> 以Tank项目为例    
GameObject是一个抽象类     
Tank，Bullet等物体继承GameObject     
现希望给Tank加边框，给Tank加拖尾效果，给Bullet加边框等    
可以将拖尾，边框作为装饰器，分别与需要的物体进行组合     
使用的时候：    
new RectDecorator(new Tank(200, 400, Group.GOOD, Direction.UP))    
new TailDecorator(new Tank(200, 400, Group.GOOD, Direction.UP))    
上面两个还可以直接写成：new TailDecorator(new RectDecorator(new Tank(200, 400, Group.GOOD, Direction.UP)))         
new RectDecorator(new Bullet(200, 400, Group.GOOD, Direction.UP))      

![Alt](./img/Decorator.png) 
