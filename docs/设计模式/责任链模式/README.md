# 责任链模式

> 职责链模式主要包含以下角色:    
抽象处理者角色：定义一个处理请求的接口，包含抽象处理方法和一个后继连接。     
具体处理者角色：实现抽象处理者的处理方法，判断能否处理本次请求，如果可以处理请求则处理，否则将该请求转给它的后继者。     
客户类角色：创建处理链，并向链头的具体处理者对象提交请求，它不关心处理细节和请求的传递过程。      

> Tank项目中的例子：      
GameObject是一个抽象类        
Tank，Bullet等物体继承GameObject    
想要解决物体之间的碰撞（坦克和坦克，坦克和子弹等）      
 
![Alt](./img/ChainOfResponsibility.png)    
方法主要代码如下：      
```
// GameModel 的 paint方法
 public void paint(Graphics g) {
      // 画主坦克
     mainTank.paint(g);
     // 画其他所有物体
     for (int i = 0; i < gameObjects.size(); i++) {
         gameObjects.get(i).paint(g);
     }
      // 对其他所有物体进行碰撞检测
     for (int i = 0; i < gameObjects.size(); i++) {
         for (int j = i + 1; j < gameObjects.size(); j++) {
             GameObject o1 = gameObjects.get(i);
             GameObject o2 = gameObjects.get(j);
             colliderChain.collide(o1, o2);
         }
     }
 }
     
 // colliderChain 的 collide 方法
 public boolean collide(GameObject o1, GameObject o2) {
     for (int i = 0; i < colliders.size(); i++) {
         if (!colliders.get(i).collide(o1, o2)) {
             return false;
         }
     }
     return true;
 }
 
 // TankTankCollider 的 collide 方法
public boolean collide(GameObject o1, GameObject o2) {
     if (o1 instanceof Tank && o2 instanceof Tank) {
         Tank tank1 = (Tank) o1;
         Tank tank2 = (Tank) o2;
         // 如果坦克相撞，返回前一位置
         if (tank1.getRectangle().intersects(tank2.getRectangle())) {
             tank1.goBack();
             tank2.goBack();
         }
     }
     return true;
 }
 ```
