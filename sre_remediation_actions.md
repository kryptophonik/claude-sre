# SRE Remediation Actions

```yaml
name: sre_remediation_actions
label: "Remediation Actions"
description: >
  Recovery procedures, rollback commands, scaling actions, and common
  remediation patterns for incident response.

remediation_hierarchy:
  priority_order:
    1: "Quick wins (restarts, config tweaks)"
    2: "Rollback recent changes"
    3: "Scale resources"
    4: "Failover to backup"
    5: "Disable non-critical features"

common_actions:
  restart:
    kubernetes:
      - "kubectl rollout restart deployment/<name> -n <namespace>"
      - "kubectl delete pod <pod-name> -n <namespace>"
      - "kubectl rollout undo deployment/<name> -n <namespace>"
    systemd:
      - "systemctl restart <service>"
      - "systemctl reload <service> (for config reloads)"
    docker:
      - "docker restart <container-id>"
      - "docker-compose restart <service>"

  rollback:
    kubernetes:
      - "kubectl rollout undo deployment/<name>"
      - "kubectl rollout history deployment/<name>"
      - "kubectl rollout undo deployment/<name> --to-revision=<n>"
    cloud:
      - "Cloud: Select previous deployment version"
      - "Terraform: terraform rollback"
    database:
      - "Flashback to restore point"
      - "PITR (Point-in-Time Recovery)"

  scale:
    kubernetes:
      - "kubectl scale deployment/<name> --replicas=<n> -n <namespace>"
      - "kubectl autoscale deployment/<name> --min=<min> --max=<max> -n <namespace>"
    cloud:
      - "AWS: Modify auto-scaling group"
      - "GCP: Update instance group size"
      - "Azure: Scale VM scale set"

  failover:
    database:
      - "Promote read replica to primary"
      - "Update connection strings"
      - "DNS failover"
    load_balancer:
      - "Drain unhealthy backend"
      - "Enable backup region"
      - "Update health check thresholds"

incident_specific_actions:
  high_error_rate:
    - "Check recent deployments → Rollback if correlated"
    - "Verify dependencies (DB, cache, upstream services)"
    - "Check for config changes"
    - "Restart affected services"
    - "Scale up if underprovisioned"
    - "Enable degraded mode if needed"

  high_latency:
    - "Check for resource saturation (CPU, memory, I/O)"
    - "Review database queries (slow query log)"
    - "Check for cache misses"
    - "Verify network issues"
    - "Scale up or optimize bottlenecks"

  memory_exhaustion:
    - "Check for memory leaks (heap dumps)"
    - "Restart affected instances"
    - "Increase memory limits"
    - "Add more instances"
    - "Implement memory limits (cgroups, limits)"

  database_issues:
    - "Check connection pool exhaustion"
    - "Review slow queries (EXPLAIN, profiling)"
    - "Check for locks/blocking queries"
    - "Restart connection pooler"
    - "Failover to replica if needed"
    - "Scale database resources"

communication:
  during_incident:
    - "Update status page"
    - "Notify stakeholders via established channels"
    - "Document actions in incident log"
    - "Estimate resolution time if possible"

  post_incident:
    - "Declare incident resolved"
    - "Send postmortem notification"
    - "Schedule postmortem meeting"
    - "Create follow-up tickets"

verification:
  after_remediation:
    - "Confirm error rates returned to baseline"
    - "Verify latency within SLO"
    - "Check synthetic monitors passing"
    - "Confirm user-facing issues resolved"
    - "Monitor for stability (5-15 min)"

safety_checks:
  before_action:
    - "Confirm impact of action"
    - "Get approval if required by policy"
    - "Have rollback plan ready"
    - "Notify affected parties"

  during_action:
    - "Monitor system response"
    - "Stop if conditions worsen"
    - "Document what was done"

  after_action:
    - "Verify improvement"
    - "Monitor for side effects"
    - "Document for postmortem"
```
