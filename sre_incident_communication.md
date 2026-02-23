# SRE Incident Communication Templates

```yaml
name: sre_incident_communication
label: "Incident Communication"
description: >
  Standardized templates for internal and external communication during
  and after an incident.

communication_channels:
  slack_internal: "Primary coordination and high-frequency updates."
  slack_external: "Customer-facing or broad internal announcements."
  status_page: "Formal external status."
  email: "Executive summaries and post-incident follow-ups."

templates:
  initial_announcement: |
    🚨 **NEW INCIDENT DECLARED** 🚨
    **Service:** [Service Name]
    **Severity:** [SEV-1/SEV-2]
    **Symptom:** [e.g., High 5xx rates on Checkout]
    **Incident Lead:** [@User]
    **Bridge/Channel:** [#inc-checkout-2026-02-23]
    **Status:** Investigating.

  status_update: |
    🔄 **INCIDENT UPDATE** ([Time] UTC)
    **Current Status:** [Investigating / Identified / Mitigating]
    **Actions Taken:** [e.g., Rolled back v1.2.3, scaling up K8s cluster]
    **Next Update:** [Estimated time or "When more info is available"]

  resolution_announcement: |
    ✅ **INCIDENT RESOLVED**
    **Service:** [Service Name]
    **Resolution Time:** [Time] UTC
    **Summary:** [Short description of fix]
    **Follow-up:** A blameless postmortem will be scheduled within 48 hours.

  executive_summary: |
    ## Executive Summary: Incident [ID]
    **Incident:** [Brief Title]
    **Business Impact:** [e.g., Estimated $X revenue lost, Y users affected]
    **Root Cause:** [High-level technical cause]
    **Short-term Fix:** [What we did to stop the bleeding]
    **Long-term Prevention:** [What we are doing to ensure it never happens again]

best_practices:
  - "Be concise and factual."
  - "Avoid technical jargon in executive summaries."
  - "Stick to a regular heartbeat (e.g., update every 15-30 mins for SEV-1)."
  - "Under-promise and over-deliver on resolution times."
```
