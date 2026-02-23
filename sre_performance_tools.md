# SRE Performance Tools

```yaml
name: sre_performance_tools
label: "Performance Tools"
description: >
  Profiling tools, bottleneck identification techniques, and tuning guidance
  for optimizing system performance.

profiling_tools:
  cpu:
    - "pprof: Go CPU profiling"
    - "perf: Linux kernel profiling"
    - "Flame graphs: Brendan Gregg's flamegraph.pl"
    - "py-spy: Python profiling"
    - "Java: Java Mission Control, JProfiler"

  memory:
    - "pprof: Go memory profiling"
    - "heap dumps: jmap, gcore"
    - "valgrind: Memory leak detection"
    - "jemalloc: Memory allocator profiling"

  i_o:
    - "iotop: I/O monitoring"
    - "iostat -x: Disk statistics"
    - "strace: System call tracing"
    - "bcc/eBPF: Advanced I/O profiling"

bottleneck_identification:
  cpu_bottleneck:
    symptoms:
      - "High CPU usage (>80%)"
      - "High load average"
      - "Process queue buildup"
    investigation:
      - "top/htop to identify process"
      - "perf top for hot functions"
      - "flame graph for call stack analysis"
    remedies:
      - "Horizontal scaling (add instances)"
      - "Optimize hot code paths"
      - "Caching"
      - "Load shedding"

  memory_bottleneck:
    symptoms:
      - "High memory usage"
      - "OOM kills"
      - "Swap usage"
    investigation:
      - "free -h for memory overview"
      - "ps aux --sort=-%mem for top consumers"
      - "heap dump for leak analysis"
    remedies:
      - "Vertical scaling (more RAM)"
      - "Fix memory leaks"
      - "Optimize data structures"
      - "Connection pooling"

  i_o_bottleneck:
    symptoms:
      - "High iowait"
      - "Slow disk operations"
      - "Database connection exhaustion"
    investigation:
      - "iostat -x for disk stats"
      - "iotop for process I/O"
      - "Database query analysis (EXPLAIN, slow query log)"
    remedies:
      - "Faster storage (SSD, NVMe)"
      - "Query optimization"
      - "Indexing"
      - "Caching layer"

  network_bottleneck:
    symptoms:
      - "High network latency"
      - "Packet loss"
      - "Connection timeouts"
    investigation:
      - "ping/traceroute for path analysis"
      - "netstat/ss for connection stats"
      - "tcpdump for packet capture"
    remedies:
      - "Connection pooling"
      - "CDN for static content"
      - "Compression"
      - "Regional deployment"

tuning_guidance:
  application:
    - "Increase worker threads/processes"
    - "Optimize database queries"
    - "Implement caching (Redis, Memcached)"
    - "Use connection pools"
    - "Enable compression"

  database:
    - "Add appropriate indexes"
    - "Partition large tables"
    - "Tune connection pool size"
    - "Enable query cache"
    - "Read replicas for scale"

  infrastructure:
    - "Increase instance size"
    - "Add more instances (horizontal scale)"
    - "Use auto-scaling policies"
    - "Optimize load balancer configuration"

profiling_workflow:
  steps:
    - "Establish baseline (benchmark)"
    - "Profile under load"
    - "Identify hot code paths"
    - "Optimize top bottlenecks"
    - "Validate improvement"
    - "Repeat"

quick_commands:
  profiling:
    - "pprof: go tool pprof http://localhost:6060/debug/pprof/profile"
    - "perf: perf record -F 99 -p <pid> && perf report"
    - "py-spy: py-spy top --pid <pid>"
    - "Java: jcmd <pid> GC.heap_info"

  monitoring:
    - "Prometheus: query rate metrics"
    - "Statsd: emit timing metrics"
    - "OpenTelemetry: distributed tracing"
```
