package top.gmfcj.component;

import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class MyDefaultHandshakeHandler extends DefaultHandshakeHandler {

    public MyDefaultHandshakeHandler(RequestUpgradeStrategy requestUpgradeStrategy) {
        // 自定义requestUpgradeStrategy
        super(requestUpgradeStrategy);
    }
}
