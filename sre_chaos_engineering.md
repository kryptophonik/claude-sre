# Chaos Engineering Skill

```yaml
name: sre_chaos_engineering
label: "Chaos Engineering"
description: >
  Workflows and experiments for proactively testing system resilience.

chaos_principles:
  - "Build a hypothesis around steady-state behavior."
  - "Vary real-world events (server crashes, network latency)."
  - "Run experiments in production (carefully!) or production-like environments."
  - "Automate experiments to run continuously."
  - "Minimize blast radius."

experiment_workflow:
  steps:
    - "Define 'Steady State': Metrics that indicate the system is healthy."
    - "Formulate Hypothesis: 'If we do X, the system will still be Y'."
    - "Design Experiment: Choose tool and parameters (use: sre_tools_catalog -> chaos)."
    - "Define Abort Conditions: When to stop the experiment automatically."
    - "Run Experiment & Analyze: Compare results to hypothesis."
    - "Improve Resilience: Fix any failures found."

common_experiments:
  network:
    - Latency: "Inject 200ms delay into service-to-service calls."
    - Packet Loss: "Drop 5% of incoming packets."
    - Partition: "Block traffic between two availability zones."
  infrastructure:
    - Pod Kill: "Randomly terminate pods in a deployment."
    - Node Drain: "Simulate losing an entire cluster node."
    - Resource Stress: "Force 90% CPU or Memory usage on a pod."
  application:
    - API Errors: "Return 500s for a percentage of requests."
    - Dependency Failure: "Simulate database or cache being unavailable."

safety_checks:
  - "Always have a 'Stop' button (Abort condition)."
  - "Start with a small blast radius (1% of traffic or 1 instance)."
  - "Ensure observability is working *before* starting the experiment."
  - "Notify stakeholders and on-call engineers."
```
