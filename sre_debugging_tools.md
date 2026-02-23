# SRE Debugging Tools

```yaml
name: sre_debugging_tools
label: "Debugging Tools"
description: >
  Structured debugging workflows, log analysis patterns, metric inspection
  commands, and trace queries for incident investigation.

quick_checks:
  dashboards:
    - "Check Grafana/cloud dashboards for error spikes"
    - "Look for latency increases (p95, p99)"
    - "Check request rate changes"
    - "Verify dependency health"

  recent_changes:
    - "Review recent deployments (last 1-2 hours)"
    - "Check config changes"
    - "Review infrastructure changes"
    - "Check DNS/routing changes"

log_analysis:
  patterns:
    - "Filter by error level: level=error OR level=fatal"
    - "Filter by service: service=checkout"
    - "Filter by time: @timestamp > now-1h"
    - "Filter by status: status>=500"

  tools:
    - "Loki: logcli query '{service=\"api\"} |= \"error\"'"
    - "Elasticsearch: Kibana Discover"
    - "Cloud: CloudWatch Logs Insights, Log Analytics"

metric_queries:
  prometheus:
    - "Error rate: rate(http_requests_total{status=~\"5..\"}[5m])"
    - "Latency: histogram_quantile(0.95, http_request_duration_seconds)"
    - "Saturation: node_memory_MemAvailable_bytes"

  datadog:
    - "Errors: sum:trace.errors{env:prod}"
    - "Latency: avg:trace.duration{env:prod}"
    - "Saturation: system.mem.available"

trace_analysis:
  queries:
    - "Find slow traces: duration > 1s"
    - "Find error traces: error = true"
    - "Trace by trace ID: trace_id=<id>"

  tools:
    - "Jaeger: UI search and trace inspection"
    - "Tempo: TraceQL queries"
    - "Datadog: Trace search"

correlation_checks:
  - "Cross-reference errors with deploys"
  - "Check if latency correlates with load"
  - "Verify if errors are isolated to one region/zone"
  - "Check if all instances or subset affected"

hypothesis_testing:
  - "A/B test hypotheses with canary deployments"
  - "Use feature flags to toggle suspected code"
  - "Reproduce in staging if possible"
  - "Compare healthy vs unhealthy instances"

quick_commands:
  kubernetes:
    - "kubectl logs -f deployment/app -n prod"
    - "kubectl describe pod <pod-name>"
    - "kubectl get events -n prod --sort-by='.lastTimestamp'"
    - "kubectl top pod -n prod"

  system:
    - "top/htop for CPU/memory"
    - "iotop for I/O"
    - "netstat/tcpdump for network"
    - "ss -tulpn for listening ports"
```
