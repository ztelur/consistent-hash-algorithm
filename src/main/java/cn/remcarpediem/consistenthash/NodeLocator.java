package cn.remcarpediem.consistenthash;

public interface NodeLocator {
    /**
     * Get the primary location for the given key.
     *
     * @param k the object key
     * @return the QueueAttachment containing the primary storage for a key
     */
    MemcachedNode getPrimary(String k);
}
