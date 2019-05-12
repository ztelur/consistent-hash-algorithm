package cn.remcarpediem.consistenthash.benchmark;

import cn.remcarpediem.consistenthash.ConsistentHashNodeLocator;
import cn.remcarpediem.consistenthash.DefaultHashAlgorithm;
import cn.remcarpediem.consistenthash.MemcachedNode;
import cn.remcarpediem.consistenthash.NodeLocator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class JMHBenchmark {


    private NodeLocator nodeLocator;
    private List<String> keys;

    @Benchmark
    public void sleepAWhile() {
        for (String key : keys) {
            MemcachedNode node = nodeLocator.getPrimary(key);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void prepare() {
        List<MemcachedNode> servers = new ArrayList<>();
        for (String ip : ips) {
            servers.add(new MemcachedNode(new InetSocketAddress(ip, 8080)));
        }
        nodeLocator = new ConsistentHashNodeLocator(servers, DefaultHashAlgorithm.MURMUR_HASH);
        // 构造 10000 随机请求
        keys = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            keys.add(UUID.randomUUID().toString());
        }

    }

    @TearDown
    public void shutdown() {
    }

    static String[] ips = {
            "11.10.172.215",
            "11.10.176.96",
            "11.14.65.34",
            "11.14.64.205",
            "11.14.65.67",
            "11.134.247.206",
            "11.10.173.47",
            "11.14.65.117",
            "11.14.69.32",
            "11.10.173.46",
            "11.14.65.170",
            "11.14.65.159",
            "11.14.65.172",
            "11.14.65.171",
            "11.13.137.50",
            "11.13.129.20",
            "11.14.65.60",
            "11.13.167.124",
            "11.13.137.175",
            "11.14.65.17",
            "11.14.65.79",
            "11.14.65.179",
            "11.13.129.114",
            "11.13.129.123",
            "11.14.64.107",
            "11.14.65.177",
            "11.14.64.254",
            "11.14.65.63",
            "11.13.137.48",
            "11.14.64.235",
            "11.14.65.155",
            "11.13.129.121",
            "11.14.65.142",
            "11.14.69.45",
            "11.10.173.57",
            "11.10.173.54",
            "11.10.185.203",
            "11.10.176.102",
            "11.179.205.41",
            "11.179.206.58",
            "11.179.206.227",
            "11.179.205.71",
            "11.10.176.100",
            "11.179.206.42",
            "11.10.176.140",
            "11.10.173.115",
            "11.10.173.82",
            "11.10.185.105",
            "11.10.176.134",
            "11.179.206.27",
            "11.179.206.190",
            "11.15.246.86",
            "11.15.92.53",
            "11.15.214.36",
            "11.15.180.34",
            "11.14.67.4",
            "11.13.111.15",
            "11.8.239.196",
            "11.10.147.202",
            "11.10.174.220",
            "11.17.110.6",
            "11.14.68.78",
            "11.17.110.108",
            "11.17.110.107",
            "11.21.132.41",
            "11.17.98.170",
            "11.13.166.82",
            "11.17.97.234",
            "11.14.69.38",
            "11.27.62.112",
            "11.27.78.248",
            "11.27.146.130",
            "11.27.122.51",
            "11.27.134.108",
            "11.27.127.67",
            "11.27.134.107",
            "11.23.58.112",
            "11.23.90.169",
            "11.24.58.112",
            "11.24.50.24",
            "11.23.120.8",
            "11.26.228.195",
            "11.26.240.203",
            "11.27.19.252",
            "11.23.91.19",
            "11.17.110.52",
            "11.27.61.119",
            "11.27.85.228",
            "11.224.244.121",
            "11.226.220.49",
            "11.27.0.108",
            "11.8.17.104",
            "11.11.68.168",
            "11.14.65.133",
            "11.134.247.244",
            "11.10.192.114",
            "11.10.192.115",
            "11.10.192.116",
            "11.10.192.117",
            "11.10.192.118"
    };

}
