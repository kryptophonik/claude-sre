# SRE Skill Router

```yaml
skill_router:
  namespace: "sre"

  routing_rules:

    - match:
        any:
          - "sre"
          - "reliability"
          - "observability"
          - "incident"
          - "oncall"
          - "alerting"
          - "slo"
          - "runbook"
      route_to: "sre_architect"

    - match:
        any:
          - "tools"
          - "stack"
          - "instrumentation"
          - "metrics"
          - "logs"
          - "traces"
      route_to: "sre_tools_catalog"

    - match:
        any:
          - "slo"
          - "error budget"
          - "availability target"
          - "latency target"
      route_to: "sre_slo_templates"

    - match:
        any:
          - "alert"
          - "alerting"
          - "pager"
          - "oncall policy"
      route_to: "sre_alerting_templates"

    - match:
        any:
          - "runbook"
          - "mitigation"
          - "triage"
      route_to: "sre_runbook_templates"

    - match:
        any:
          - "postmortem"
          - "incident review"
          - "retrospective"
          - "rca"
      route_to: "sre_postmortem_templates"

    - match:
        any:
          - "production readiness"
          - "prr"
          - "launch checklist"
          - "go live"
      route_to: "sre_production_readiness"

    - match:
        any:
          - "status update"
          - "stakeholder comms"
          - "incident announcement"
          - "executive summary"
      route_to: "sre_incident_communication"

    - match:
        any:
          - "chaos"
          - "resilience"
          - "fault injection"
          - "experiment"
      route_to: "sre_chaos_engineering"

    - match:
        any:
          - "debug"
          - "troubleshoot"
          - "diagnose"
          - "investigate"
          - "slow"
          - "latency"
          - "performance"
          - "bottleneck"
          - "fix"
          - "remediate"
          - "recover"
          - "rollback"
          - "restart"
          - "scale"
      route_to: "sre_engineer"

  fallback:
    route_to: "sre_architect"