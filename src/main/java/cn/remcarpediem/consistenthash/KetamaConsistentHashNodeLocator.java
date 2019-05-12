package cn.remcarpediem.consistenthash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class KetamaConsistentHashNodeLocator implements NodeLocator {
    private final static int VIRTUAL_NODE_SIZE = 12;
    private final static String VIRTUAL_NODE_SUFFIX = "-";

    private static MessageDigest md5Digest;

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }


    private volatile TreeMap<Long, MemcachedNode> hashRing;
    private final HashAlgorithm hashAlg;
    private final int virtualNodeSize = VIRTUAL_NODE_SIZE;

    protected KetamaConsistentHashNodeLocator(List<MemcachedNode> ketamaNodes, HashAlgorithm hashAlg) {
        this.hashRing = buildConsistentHashRing(ketamaNodes);
        this.hashAlg = hashAlg;
    }

    @Override
    public MemcachedNode getPrimary(String k) {
        long hash = hashAlg.hash(k);
        return getNodeForKey(hashRing, hash);
    }

    private MemcachedNode getNodeForKey(TreeMap<Long, MemcachedNode> hashRing, long hash) {
        /* 向右找到第一个key */
        Map.Entry<Long, MemcachedNode> locatedNode = hashRing.ceilingEntry(hash);
        /* 想象成为一个环，超出尾部取出第一个 */
        if (locatedNode == null) {
            locatedNode = hashRing.firstEntry();
        }
        return locatedNode.getValue();
    }


    private TreeMap<Long, MemcachedNode> buildConsistentHashRing(List<MemcachedNode> servers) {
        TreeMap<Long, MemcachedNode> virtualNodeRing = new TreeMap<>();
        for (MemcachedNode server : servers) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE / 4; i++) {
                byte[] digest = computeMd5(server.getSocketAddress().toString() + VIRTUAL_NODE_SUFFIX + i);
                for (int h = 0; h < 4; h++) {
                    Long k = ((long) (digest[3 + h * 4] & 0xFF) << 24)
                            | ((long) (digest[2 + h * 4] & 0xFF) << 16)
                            | ((long) (digest[1 + h * 4] & 0xFF) << 8)
                            | (digest[h * 4] & 0xFF);
                    virtualNodeRing.put(k, server);

                }
            }
        }
        return virtualNodeRing;
    }

    private static byte[] computeMd5(String k) {
        MessageDigest md5;
        try {
            md5 = (MessageDigest) md5Digest.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("clone of MD5 not supported", e);
        }
        md5.update(k.getBytes());
        return md5.digest();
    }

}
