package com.enhui.zookeeper.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.proto.WatcherEvent;
@Slf4j
public class NodeWatch implements Watcher{
    @Override
    public void process(WatchedEvent event) {
        log.info("node watch");
        Watcher.Event.EventType type = event.getType();
        String path = event.getPath();
        Watcher.Event.KeeperState state = event.getState();
        switch (type) {
            case None:
                log.info("node watch::路径：{}——Node", path);
                break;
            case NodeCreated:
                log.info("node watch::路径：{}——NodeCreated", path);
                break;
            case NodeDeleted:
                log.info("node watch::路径：{}——NodeDeleted", path);
                break;
            case NodeDataChanged:
                log.info("node watch::路径：{}——NodeDataChanged", path);
                break;
            case NodeChildrenChanged:
                log.info("node watch::路径：{}——NodeChildrenChanged", path);
                break;
            case DataWatchRemoved:
                log.info("node watch::路径：{}——DataWatchRemoved", path);
                break;
            case ChildWatchRemoved:
                log.info("node watch::路径：{}——ChildWatchRemoved", path);
                break;
            case PersistentWatchRemoved:
                log.info("node watch::路径：{}——PersistentWatchRemoved", path);
                break;
        }

        switch (state) {
            case Unknown:
                log.info("node watch::state::路径：{}——Unknown", path);
                break;
            case Disconnected:
                log.info("node watch::state::路径：{}——Disconnected", path);
                break;
            case NoSyncConnected:
                log.info("node watch::state::路径：{}——NoSyncConnected", path);
                break;
            case SyncConnected:
                log.info("node watch::state::路径：{}——SyncConnected", path);
                break;
            case AuthFailed:
                log.info("node watch::state::路径：{}——AuthFailed", path);
                break;
            case ConnectedReadOnly:
                log.info("node watch::state::路径：{}——ConnectedReadOnly", path);
                break;
            case SaslAuthenticated:
                log.info("node watch::state::路径：{}——SaslAuthenticated", path);
                break;
            case Expired:
                log.info("node watch::state::路径：{}——Expired", path);
                break;
            case Closed:
                log.info("node watch::state::路径：{}——Closed", path);
                break;
        }
    }
}
