# SRE Skill Manifest

```yaml
skill_manifest:
  version: "1.2.0"
  namespace: "sre"

  skills:
    - name: "sre_architect"
      type: "primary"
      description: "Core SRE architect persona and workflows."
      provides:
        - "observability_design"
        - "alerting_design"
        - "reliability_review"
        - "incident_response"
        - "postmortem_facilitation"
        - "chaos_engineering_strategy"
      depends_on:
        - "sre_tools_catalog"
        - "sre_slo_templates"
        - "sre_alerting_templates"
        - "sre_runbook_templates"
        - "sre_postmortem_templates"
        - "sre_chaos_engineering"
        - "sre_production_readiness"

    - name: "sre_production_readiness"
      type: "module"
      description: "Checklist and template for ensuring services are ready for production."
      provides:
        - "prr_checklist"
        - "prr_document_template"

    - name: "sre_tools_catalog"
      type: "module"
      description: "Catalog of SRE tools across metrics, logs, traces, alerting, chaos, etc."
      provides:
        - "tools"

    - name: "sre_slo_templates"
      type: "module"
      description: "SLO document template + error budget model."
      provides:
        - "sli_selection_guide"
        - "slo_markdown_template"
        - "error_budget_best_practices"

    - name: "sre_alerting_templates"
      type: "module"
      description: "Alert policy + taxonomy + required alerts."
      provides:
        - "alerting_principles"
        - "burn_rate_strategies"
        - "alert_definition_template"
        - "alert_reduction_techniques"

    - name: "sre_runbook_templates"
      type: "module"
      description: "Runbook skeleton for alerts and incidents."
      provides:
        - "runbook_template"

    - name: "sre_postmortem_templates"
      type: "module"
      description: "Blameless postmortem templates and facilitation guidance."
      provides:
        - "postmortem_principles"
        - "postmortem_markdown_template"
        - "facilitation_tips"

    - name: "sre_chaos_engineering"
      type: "module"
      description: "Workflows and experiments for proactively testing system resilience."
      provides:
        - "chaos_principles"
        - "experiment_workflow"
        - "common_experiments"
        - "safety_checks"

    - name: "sre_engineer"
      type: "primary"
      description: "Hands-on SRE engineer for tactical operations and execution."
      provides:
        - "incident_debugging"
        - "performance_tuning"
        - "remediation_actions"
        - "runbook_execution"
        - "chaos_experiment_execution"
      depends_on:
        - "sre_debugging_tools"
        - "sre_performance_tools"
        - "sre_remediation_actions"
        - "sre_runbook_templates"
        - "sre_tools_catalog"
        - "sre_postmortem_templates"
        - "sre_chaos_engineering"
        - "sre_incident_communication"

    - name: "sre_incident_communication"
      type: "module"
      description: "Standardized templates for incident updates and stakeholder communication."
      provides:
        - "communication_channels"
        - "templates"
        - "best_practices"

    - name: "sre_debugging_tools"
      type: "module"
      description: "Structured debugging workflows, log analysis, and trace queries."
      provides:
        - "quick_checks"
        - "log_analysis"
        - "metric_queries"
        - "trace_analysis"
        - "correlation_checks"
        - "hypothesis_testing"
        - "quick_commands"

    - name: "sre_performance_tools"
      type: "module"
      description: "Profiling tools and bottleneck identification techniques."
      provides:
        - "profiling_tools"
        - "bottleneck_identification"
        - "tuning_guidance"
        - "profiling_workflow"
        - "quick_commands"

    - name: "sre_remediation_actions"
      type: "module"
      description: "Recovery procedures and common remediation patterns."
      provides:
        - "remediation_hierarchy"
        - "common_actions"
        - "incident_specific_actions"
        - "communication"
        - "verification"
        - "safety_checks"
