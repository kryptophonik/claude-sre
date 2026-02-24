# SRE Architect Skill Pack for Claude Code

**Principal-level Site Reliability Engineering capabilities at your fingertips.**

A unified SRE skill pack that equips Claude with observability design, SLO creation, alerting strategy, incident response, operational excellence guidance, and hands-on tactical execution.

---

## Overview

The SRE Architect Skill Pack provides a complete, modular SRE capability set for Claude Code. It includes two primary personas—**SRE Architect** (strategic) and **SRE Engineer** (tactical)—and specialized sub-skills for tools, SLOs, alerting, runbooks, debugging, performance, and remediation.

Whether you're designing observability for a new service, defining SLOs, fixing alert fatigue, debugging incidents, tuning performance, or creating runbooks, this skill pack delivers comprehensive SRE guidance tailored to your needs.

---

## Features

- **Dual-persona model** - Strategic Architect for design/reviews, tactical Engineer for debugging/execution
- **Principal-level SRE reasoning** - Get guidance grounded in real-world SRE practices
- **Modular sub-skills** - Specialized capabilities for tools, SLOs, alerting, runbooks, debugging, performance, remediation, **chaos engineering**, and **postmortems**
- **SLO-driven approach** - All recommendations tie back to service level objectives
- **On-call friendly** - Designs for humans under stress, minimizing alert fatigue
- **Intelligent routing** - Automatically directs requests to the appropriate skill
- **Validated guidance** - Recommendations include testing strategies (synthetic, chaos, load)
- **Hands-on execution** - Concrete commands and remediation actions

---

## Architecture

```
┌───────────────────────────────────────────────────────────────────┐
│                    SRE Architect Skill Pack                       │
│                       (sre_architect_pack)                        │
├───────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌─────────────┐  ┌─────────────────────────────────────────┐     │
│  │  router.md  │→ │   Intent-based routing to appropriate   │     │
│  └─────────────┘  │   primary skill (Architect or Engineer) │     │
│                   └─────────────────────────────────────────┘     │
│                                                                   │
│  ┌──────────────┐ ┌─────────────────────────────────────────┐     │
│  │ manifest.md  │→ │  Skill declarations & dependencies     │     │
│  └──────────────┘ └─────────────────────────────────────────┘     │
│                           │                                       │
│        ┌──────────────────┴─────────────────┐                     │
│        │                                    │                     │
│  ┌─────▼──────────────────────────────┐  ┌──▼─────────────────┐   │
│  │     sre_architect.md (Strategic)   │  │ sre_engineer.md    │   │
│  │  • Observability design            │  │   (Tactical)       │   │
│  │  • SLO/SLI definition              │  │  • Debugging       │   │
│  │  • Alerting strategy               │  │  • Performance     │   │
│  │  • Reliability reviews             │  │  • Remediation     │   │
│  │  • Postmortem facilitation         │  │  • Runbook exec    │   │
│  │  • Chaos strategy                  │  │                    │   │
│  └─────┬──────────────────────────────┘  └──┬─────────────────┘   │
│        │                                    │                     │
│        └──────────────────┬─────────────────┘                     │
│                           │                                       │
│  ┌───────────┬─────────── ┴──────────┬───────────┬────────────┐   │
│  │           │            │          │           │            │   │
│┌─▼───┐ ┌─────▼────┐ ┌─────▼────┐ ┌───▼────┐ ┌────▼────┐ ┌─────▼─┐ │
││Tools│ │   SLOs   │ │ Alerting │ │Runbooks│ │Postmort │ │ Chaos │ │
││Cat  │ │Templates │ │Templates │ │Template│ │Templates│ │ Eng   │ │
│└─────┘ └──────────┘ └──────────┘ └────────┘ └─────────┘ └───────┘ │
│       ┌───────────┐                       ┌─────────────┐         │
│       │   PRR     │                       │  Incident   │         │
│       │ Checklist │                       │  Comms      │         │
│       └───────────┘                       └─────────────┘         │
│                                                   ┌───────────┐   │
│                                                   │Debugging  │   │
│                                                   │   Tools   │   │
│                                                   └───────────┘   │
│                                                   ┌───────────┐   │
│                                                   │Performance│   │
│                                                   │   Tools   │   │
│                                                   └───────────┘   │
│                                                   ┌───────────┐   │
│                                                   │Remediation│   │
│                                                   │  Actions  │   │
│                                                   └───────────┘   │
└───────────────────────────────────────────────────────────────────┘
```

---

## Components

| File | Purpose |
|------|---------|
| **manifest.md** | Declares all SRE skills and their dependencies |
| **router.md** | Determines which skill handles which user intent |
| **sre_architect.md** | Strategic SRE architect persona and workflows |
| **sre_engineer.md** | Tactical SRE engineer persona and workflows |
| **sre_tools_catalog.md** | Tooling recommendations across all SRE domains |
| **sre_slo_templates.md** | SLO templates and error budget models |
| **sre_alerting_templates.md** | Alerting templates and taxonomy |
| **sre_runbook_templates.md** | Runbook skeletons for incidents |
| **sre_postmortem_templates.md** | Blameless postmortem templates and facilitation |
| **sre_chaos_engineering.md** | Chaos engineering workflows and experiments |
| **sre_debugging_tools.md** | Debugging workflows, log analysis, trace queries |
| **sre_performance_tools.md** | Profiling tools and tuning guidance |
| **sre_remediation_actions.md** | Recovery procedures and remediation patterns |
| **sre_production_readiness.md** | PRR checklists and templates |
| **sre_incident_communication.md** | Stakeholder comms templates |

---

## Installation

### Global Installation (recommended)

Clone this repository into your Claude Code skills directory:

```bash
# Create the skills directory if it doesn't exist
mkdir -p ~/.claude/skills

# Clone this repository
git clone https://github.com/yourusername/Claude-SRE-Skills.git ~/.claude/skills/sre_architect_pack
```

### Project-Local Installation

For project-specific usage:

```bash
# In your project directory
mkdir -p .claude/skills

# Copy or symlink the skill pack
ln -s ~/.claude/skills/sre_architect_pack .claude/skills/
```

### Verify Installation

Start Claude Code and invoke the skill:

```
> /skill sre_architect_pack
```

---

## Quick Start

The SRE Architect Pack activates automatically when you use SRE-related keywords. Try these prompts:

### Strategic Tasks (Architect)
### Production Readiness
```
Run a production readiness review for our new payment gateway.
```

### Observability Design
```
Design observability for our API gateway service.
```

### SLO Creation
```
Help me define SLOs for our checkout service.
```

### Alerting Strategy
```
We're experiencing alert fatigue. Can you help redesign our alerts?
```

### Reliability Review
```
Review the reliability of our microservices architecture.
```

### Tactical Tasks (Engineer)
### Incident Debugging & Comms
```
Help debug this incident. We're seeing high 5xx rates. Draft an initial announcement for Slack.
```

### Performance Issues
```
Our API is slow. Help me tune it.
```

### Remediation
```
How do I roll back the last deployment?
```

### Runbook Execution
```
Walk me through the runbook for database connection issues.
```

### Tooling
```
What tools should I use for metrics, logs, and traces in Kubernetes?
```


---

## Trigger Keywords

The skill pack activates on these keywords:

**Strategic (Architect):**
- `sre`, `reliability`, `observability`, `incident`, `oncall`
- `alerting`, `slo`, `runbook`, `error budget`

**Tactical (Engineer):**
- `debug`, `troubleshoot`, `diagnose`, `investigate`
- `slow`, `latency`, `performance`, `bottleneck`
- `fix`, `remediate`, `recover`, `rollback`, `restart`, `scale`

---

## Core Behaviors

All responses follow these principles:

1. **User-impact first** - Focus on protecting user experience
2. **SLO-driven** - Tie recommendations to SLIs/SLOs
3. **Concrete steps** - Provide actionable guidance, not theory
4. **On-call friendly** - Design for humans under stress
5. **Validate everything** - Include testing strategies
6. **State assumptions** - Call out missing context

---

## License

MIT License - see LICENSE file for details.

---

## Contributing

Contributions welcome! Please open an issue or submit a pull request.

---

## Version

1.2.0
