package fun.enhui.thread.juc.apitest;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 分阶段的栅栏
 * Phaser类
 *
 * @author 胡恩会
 * @date 2020-8-12
 */
public class T04_PhaserDemo {
    static Random random = new Random();

    // 自定义继承了 Phaser
    static MarriagePhaser phaser = new MarriagePhaser();

    /**
     * 随机休眠
     *
     * @param milli
     */
    static void milliSleep(int milli) {
        try {
            TimeUnit.MILLISECONDS.sleep(milli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 注册人数 7
        phaser.bulkRegister(7);
        // 五个参加婚礼的人
        for (int i = 0; i < 5; i++) {
            new Thread(new Person("p" + i)).start();
        }
        // 新郎和新娘两个人
        new Thread(new Person("新郎")).start();
        new Thread(new Person("新娘")).start();
    }

    /**
     * Person 实体类
     */
    static class Person implements Runnable {
        String name;

        public Person(String name) {
            this.name = name;
        }

        public void arrive() {
            milliSleep(random.nextInt(1000));
            System.out.printf("%s 到达现场 \n", name);
            phaser.arriveAndAwaitAdvance();
        }

        public void eat() {
            milliSleep(random.nextInt(1000));
            System.out.printf("%s 吃完 \n", name);
            phaser.arriveAndAwaitAdvance();
        }

        public void leave() {
            milliSleep(random.nextInt(1000));
            System.out.printf("%s 离开 \n", name);
            phaser.arriveAndAwaitAdvance();
        }

        private void hug() {
            if ("新郎".equals(name) || "新娘".equals(name)) {
                milliSleep(random.nextInt(1000));
                System.out.printf("%s 洞房 \n", name);
                // 进入下一阶段
                phaser.arriveAndAwaitAdvance();
            } else {
                // 不进入下一阶段
                phaser.arriveAndDeregister();
            }
        }

        @Override
        public void run() {
            arrive();

            eat();

            leave();

            hug();
        }
    }

    /**
     * 结婚栅栏
     */
    static class MarriagePhaser extends Phaser {
        /**
         * 到达各个阶段时 自动执行
         *
         * @param phase             当前阶段-整数
         * @param registeredParties 目前参与人数
         * @return
         */
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase) {
                case 0:
                    System.out.println("所有人到齐了！" + registeredParties);
                    System.out.println();
                    return false;
                case 1:
                    System.out.println("所有人吃完了！" + registeredParties);
                    System.out.println();
                    return false;
                case 2:
                    System.out.println("所有人离开了！" + registeredParties);
                    System.out.println();
                    return false;
                case 3:
                    System.out.println("婚礼结束，新郎新娘抱抱！" + registeredParties);
                    return true;
                default:
                    return true;
            }
        }
    }
}


