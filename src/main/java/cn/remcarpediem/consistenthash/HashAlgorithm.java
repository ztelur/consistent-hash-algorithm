package cn.remcarpediem.consistenthash;

public interface HashAlgorithm {
    /**
     * Compute the hash for the given key.
     *
     * @return a positive integer hash
     */
    long hash(final String k);
}
