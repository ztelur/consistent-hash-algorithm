package cn.remcarpediem.consistenthash;

import java.net.SocketAddress;

public class MemcachedNode {
    private final SocketAddress socketAddress;

    public MemcachedNode(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }



    /**
     * Get the SocketAddress of the server to which this node is connected.
     */
    public  SocketAddress getSocketAddress() {
        return socketAddress;
    }
}
