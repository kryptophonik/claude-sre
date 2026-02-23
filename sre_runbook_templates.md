# Runbook Templates

```yaml
name: sre_runbook_templates
label: "Runbook Templates"
description: "Short runbook skeleton for all alerts."

runbook_template:
  quick_checks:
    - "Dashboards"
    - "Dependencies"
    - "Recent deploys"
  mitigation:
    - "Rollback"
    - "Failover"
    - "Scale"
    - "Restart pods"
  escalation:
    - "Primary on-call"
    - "Secondary"
    - "Service owner"
  verification:
    - "SLO burn stopped"
    - "Latency/5xx normal"
    - "Synthetic checks passing"
```
