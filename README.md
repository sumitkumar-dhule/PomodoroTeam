# Pomodoro Team

A cross-platform Pomodoro and availability app built as a **learning project for Kotlin Multiplatform (KMP), Kotlin Multiplatform Mobile (KMM), and Compose Multiplatform**.

The goal is not only to build a Pomodoro timer, but to use a real-world application to learn **cross-platform architecture, system design, networking, local device discovery, state synchronization, and collaborative software development**.

> 🚧 **Status: Learning / Experimental**
>
> This project is being built in public. Architecture, APIs, UI, and implementation details may change as we learn.

---

## 🎯 Project Goals

This project has two primary goals.

### 1. Learn Kotlin Multiplatform + Compose Multiplatform

We want to understand how much application logic can be shared across platforms while still providing a good native experience.

Areas we want to explore:

- Kotlin Multiplatform (KMP)
- Kotlin Multiplatform Mobile (KMM)
- Compose Multiplatform
- Shared business logic
- Shared UI
- Platform-specific implementations
- Dependency injection
- Networking
- Persistence
- Coroutines
- Flow / StateFlow
- Background processing
- Notifications
- Platform APIs

The goal is to understand **where code should be shared and where platform-specific code is the better choice**.

---

### 2. Learn System Design

The Pomodoro timer itself is intentionally simple.

The interesting engineering challenge is making it possible for multiple people to see each other's:

- Current mode
- Availability
- Remaining Pomodoro time
- Break status
- Presence on the local network

This gives us a small but realistic problem to explore:

```text
             ┌─────────────────┐
             │     User A      │
             │   Pomodoro App  │
             └────────┬────────┘
                      │
                      │ status
                      ▼
             ┌─────────────────┐
             │                 │
             │  Presence /     │
             │  Synchronization│
             │     Layer       │
             │                 │
             └────────┬────────┘
                      │
            ┌─────────┴─────────┐
            ▼                   ▼
      ┌───────────┐       ┌───────────┐
      │   User B  │       │   User C  │
      │    App    │       │    App    │
      └───────────┘       └───────────┘
```

We want to use this project to learn how such a system could work **with and without a central server**.

---

# 🍅 What Are We Building?

A Pomodoro timer designed for people working together.

The application has two related concepts:

### Personal Timer

A user can start a Pomodoro session and see:

- Current mode
- Remaining time
- Session progress
- Break status
- Availability

### Team Availability

Other team members can optionally see:

- Who is currently available
- Who is focusing
- Who is on a break
- How much time remains in their current session

The idea is that a colleague should be able to glance at the application and immediately understand:

> **Can I disturb this person right now?**

---

# 🟢🔴 Modes

## Casual

The user is available for normal interaction.

```text
🟢 CASUAL

AVAILABLE

Feel free to interrupt
```

The UI uses a green theme.

---

## 🔴 Focus

The user is concentrating on a task.

```text
🔴 FOCUS

18:42

Please don't disturb
```

The remaining time should be clearly visible.

---

## 🟡 Break

The user is taking a break.

```text
🟡 BREAK

04:32

Back soon
```

---

# 🎨 Design Principles

The application should prioritize **status visibility over decoration**.

A person should be able to understand the current state within a second or two.

### The primary information hierarchy is:

```text
MODE
  ↓
REMAINING TIME
  ↓
AVAILABILITY
```

For example:

```text
┌───────────────────────────────┐
│                               │
│          🔴 FOCUS             │
│                               │
│            18:42              │
│                               │
│      Please don't disturb     │
│                               │
│       ███████████░░░          │
│                               │
└───────────────────────────────┘
```

Color should reinforce the state, but **color must not be the only indication**.

The mode should always be represented using text as well.

---

# ✨ Features

## MVP

The first version should focus on the fundamentals.

### Timer

- Start Pomodoro
- Pause
- Resume
- Reset
- Countdown
- Configurable duration
- Break duration
- Automatic transition between focus and break

### Modes

- Casual
- Focus / DND
- Break

### Availability

The current state should be visually obvious.

Example:

```text
🟢 CASUAL
Available
```

```text
🔴 FOCUS
21:43 remaining
```

```text
🟡 BREAK
04:18 remaining
```

---

# 👥 Team Availability

A future feature is a separate **Team** screen.

Example:

```text
TEAM

🟢 Priya
   CASUAL
   Available

🔴 Rahul
   FOCUS
   18:42 remaining

🟡 Amit
   BREAK
   03:21 remaining

🔴 Neha
   FOCUS
   07:54 remaining
```

The objective is to make this useful on:

- A developer's desktop
- A laptop
- A shared office display
- A team dashboard

---

# 📡 Experimental Feature: Local Team Discovery

One of the more interesting parts of this project is exploring whether team members can discover each other **without requiring a traditional backend server**.

The initial idea is:

> If several people are working in the same office and connected to the same Wi-Fi network, can their Pomodoro applications discover each other and share their current availability?

For example:

```text
Same Wi-Fi Network

┌──────────┐
│ Laptop A │
│ 🔴 18:42 │
└────┬─────┘
     │
     │ Local Network
     │
─────┼────────────────────
     │
┌────┴─────┐       ┌──────────┐
│ Laptop B │       │ Laptop C │
│ 🟢 FREE  │       │ 🟡 03:21 │
└──────────┘       └──────────┘
```

This feature is intentionally exploratory.

We don't yet want to assume that Wi-Fi is the correct solution.

---

# 🔬 Possible Approaches

We want to investigate several approaches and understand their trade-offs.

## Option 1 — Central Server

All clients connect to a backend.

```text
       ┌──────────────┐
       │    Server    │
       └──────┬───────┘
          ┌───┼───┐
          ▼   ▼   ▼
         A    B   C
```

Possible technologies:

- REST API
- WebSocket
- Server-Sent Events
- Firebase / similar services
- Custom Kotlin backend

### Advantages

- Works across different networks
- Easier user identity
- Easier synchronization
- Easier persistence
- Can support remote teams

### Disadvantages

- Requires infrastructure
- Requires authentication
- Requires internet connectivity
- More operational complexity

---

# 📡 Option 2 — Local Wi-Fi Discovery

Devices on the same local network discover each other.

Potential mechanisms to investigate:

- UDP broadcast
- Multicast
- mDNS / Bonjour
- Local HTTP server
- WebSocket over LAN
- Service discovery protocols

Possible architecture:

```text
             Local Wi-Fi

       ┌─────────────────────┐
       │                     │
       │   Device Discovery  │
       │                     │
       └─────────────────────┘
          ▲       ▲       ▲
          │       │       │
        User A  User B  User C
```

### Advantages

- No cloud server required
- Works well for an office
- Interesting networking problem
- Good system-design learning opportunity

### Disadvantages

- Network restrictions
- Firewall issues
- Some networks isolate clients
- Discovery behavior differs across platforms
- Mobile platforms may restrict background networking

---

# 📱 Option 3 — Bluetooth / Nearby Devices

Another area worth investigating is device-to-device communication using Bluetooth or platform-specific nearby-device APIs.

The goal is **not** to reinvent a messaging application.

Instead, we want to investigate whether the same general concepts used by nearby chat applications can allow us to:

1. Discover nearby devices
2. Identify users
3. Exchange small status messages
4. Keep availability synchronized

Example:

```text
Phone A  ))))))  Phone B
   🔴                🟢
 18:42             FREE
```

Potential challenges include:

- Bluetooth permissions
- Platform differences
- Background execution
- Device discovery
- Connection lifecycle
- Battery usage
- Security
- iOS / Android API differences

This makes it a particularly useful **KMP learning exercise**, because platform-specific implementations may be necessary.

---

# 🧪 Option 4 — Hybrid Architecture

Eventually, we may support both local and remote team presence.

```text
                 ┌──────────────┐
                 │ Cloud Server │
                 └──────┬───────┘
                        │
             Internet / Remote
                        │
        ┌───────────────┴───────────────┐
        │                               │
     User A                          User D
        │
        │ Local Network
        │
   ┌────┴────┐
   │         │
 User B    User C
```

The application could prefer local communication when possible and fall back to a server when necessary.

This is **not an MVP requirement**.

It is an area we may explore as the project evolves.

---

# 🏗️ Architecture

The project should intentionally separate:

### Shared

```text
shared/
├── domain/
├── data/
├── networking/
├── timer/
├── presence/
└── synchronization/
```

### Platform-specific

```text
android/
ios/
desktop/
```

The exact structure will evolve as we learn.

A key goal is to avoid blindly sharing everything.

Instead, we want to learn:

> **What should be shared, and what should remain platform-specific?**

---

# 🧩 High-Level Architecture

The application will aim for a layered architecture.

```text
┌───────────────────────────────┐
│             UI                │
│       Compose Multiplatform   │
└───────────────┬───────────────┘
                │
┌───────────────▼───────────────┐
│          Presentation         │
│ ViewModels / State / Events   │
└───────────────┬───────────────┘
                │
┌───────────────▼───────────────┐
│            Domain             │
│ Timer / Presence / Availability│
└───────────────┬───────────────┘
                │
┌───────────────▼───────────────┐
│             Data              │
│ Repository / Local Storage    │
└───────────────┬───────────────┘
                │
       ┌────────┴────────┐
       ▼                 ▼
   Local Data       Network Layer
                         │
                 ┌───────┴───────┐
                 ▼               ▼
               Local           Cloud
             Discovery         Server
```

This architecture is intentionally more sophisticated than required for a simple timer because **learning system design is one of the project's goals**.

---

# ⏱️ Timer Design

The timer should not depend entirely on UI updates.

Instead, the application should model the session using timestamps/state.

Conceptually:

```text
Session

startedAt
duration
mode
status
```

Remaining time can then be calculated from the current time.

For example:

```text
remaining =
    endTime - currentTime
```

This is preferable to treating:

```text
remainingSeconds--
```

as the authoritative state.

This allows us to explore:

- App lifecycle
- Background execution
- Clock synchronization
- Reconnection
- State restoration
- Multiple clients observing the same session

---

# 🔄 Presence Model

A user's presence could eventually look something like:

```text
UserPresence

userId
displayName
mode
availability
sessionStartedAt
sessionEndsAt
lastSeen
deviceId
```

Example:

```text
{
    "userId": "user-123",
    "displayName": "Rahul",
    "mode": "FOCUS",
    "availability": "DO_NOT_DISTURB",
    "sessionEndsAt": "...",
    "lastSeen": "..."
}
```

The exact model will evolve.

---

# 🔐 Privacy & Security

Because this may eventually expose team availability, privacy is important.

The application should avoid sharing unnecessary information.

For example, other users generally need to know:

```text
Rahul
FOCUS
18:42 remaining
```

They don't necessarily need to know:

```text
Rahul
FOCUS
18:42 remaining
Working on confidential-project-x
At IP address 192.168.1.23
Device ID abc123
```

We should therefore follow the principle:

> **Share the minimum information necessary to provide availability.**

If local discovery is implemented, we also need to investigate:

- Device identity
- Authentication
- Unauthorized users on the same network
- Spoofed presence
- Encryption
- Trust between devices
- Network isolation

---

# 🛠️ Technology

The initial technology direction is:

| Area | Technology |
|---|---|
| Language | Kotlin |
| Multiplatform | Kotlin Multiplatform |
| UI | Compose Multiplatform |
| Async | Kotlin Coroutines |
| Reactive state | Flow / StateFlow |
| Architecture | Layered / Clean Architecture |
| Networking | To be evaluated |
| Persistence | To be evaluated |
| Local discovery | To be evaluated |
| Backend | To be evaluated |

Technology choices should be documented as we make them.

The objective is **learning and understanding trade-offs**, not collecting technologies.

---

# 📚 What We Want to Learn

This repository should be useful to someone reading the code.

Areas we want to document as we go:

### Kotlin

- Coroutines
- Flow
- StateFlow
- Sealed classes
- Generics
- Extension functions
- Structured concurrency

### Kotlin Multiplatform

- `commonMain`
- `androidMain`
- `iosMain`
- `expect` / `actual`
- Platform-specific APIs
- Dependency management

### Compose Multiplatform

- State management
- Navigation
- Reusable components
- UI architecture
- Platform-specific UI behavior

### System Design

- Client/server architecture
- Local-first architecture
- Presence systems
- Service discovery
- Synchronization
- Eventual consistency
- Conflict resolution
- Connection lifecycle
- Failure handling
- Scalability
- Security

---

# 🧠 Architecture Decisions

Important technical decisions should be documented.

Instead of only committing code, we want to record **why** we chose an approach.

For example:

```text
docs/
├── architecture/
│   ├── timer.md
│   ├── presence.md
│   ├── synchronization.md
│   └── networking.md
│
└── adr/
    ├── 001-compose-multiplatform.md
    ├── 002-presence-architecture.md
    └── 003-local-discovery.md
```

Architecture Decision Records (ADRs) should explain:

1. Context
2. Problem
3. Options considered
4. Decision
5. Trade-offs
6. Consequences

---

# 🤝 Ways of Working

This is a public learning repository, so collaboration is encouraged.

## Small Pull Requests

Prefer small PRs that answer one question.

Good:

```text
Add timer domain model
```

```text
Add Compose timer screen
```

```text
Experiment with mDNS discovery
```

Less desirable:

```text
Implement entire application
```

---

## Explain Learning Decisions

When introducing something unfamiliar, explain it.

For example:

```kotlin
// We use StateFlow here because the UI needs
// to observe the current timer state and receive
// updates whenever the session changes.
```

The goal isn't to comment every line.

The goal is to make **learning decisions discoverable**.

---

# 🌱 Contribution Philosophy

You do not need to be an expert in Kotlin Multiplatform to contribute.

Contributions that are especially welcome:

- Bug fixes
- UI improvements
- Architecture experiments
- Documentation
- Tests
- Platform-specific implementations
- Performance experiments
- Networking experiments
- System-design discussions
- Alternative approaches

A contribution that demonstrates:

> "I tried another approach and here are the trade-offs"

is particularly valuable.

---

# 🧪 Experiments

Some parts of the repository may intentionally contain experiments.

For example:

```text
experiments/
├── udp-discovery/
├── mdns-discovery/
├── bluetooth/
└── websocket/
```

An experiment does not necessarily need to become production code.

The purpose is to answer questions.

For example:

> Can Android and desktop clients discover each other using mDNS?

or:

> Can two devices exchange Pomodoro state over Bluetooth?

The result should ideally document:

```text
Question
   ↓
Hypothesis
   ↓
Experiment
   ↓
Result
   ↓
What we learned
```

---

# 🗺️ Roadmap

## Phase 1 — Timer

- [ ] Project setup
- [ ] Compose Multiplatform setup
- [ ] Timer domain model
- [ ] Timer state machine
- [ ] Countdown UI
- [ ] Start / pause / reset
- [ ] Focus mode
- [ ] Casual mode
- [ ] Break mode
- [ ] Persistent settings

## Phase 2 — Better UX

- [ ] Progress indicator
- [ ] Notifications
- [ ] Background timer behavior
- [ ] State restoration
- [ ] Accessibility
- [ ] Desktop experience

## Phase 3 — Team Presence

- [ ] User identity
- [ ] Team screen
- [ ] Presence model
- [ ] Availability state
- [ ] Last-seen information
- [ ] Real-time state updates

## Phase 4 — Local Discovery

Investigate:

- [ ] UDP discovery
- [ ] mDNS
- [ ] Local HTTP
- [ ] WebSocket over LAN
- [ ] Bluetooth / nearby-device APIs

The goal is to compare approaches rather than immediately pick one.

## Phase 5 — Distributed Presence

- [ ] Synchronization
- [ ] Reconnection
- [ ] Offline behavior
- [ ] Conflict handling
- [ ] Heartbeats
- [ ] Presence expiration
- [ ] Security

## Phase 6 — Optional Cloud Architecture

If useful:

- [ ] Backend
- [ ] Authentication
- [ ] Remote teams
- [ ] WebSocket synchronization
- [ ] Cloud persistence

---

# 💡 Questions We Want to Answer

This project is successful if, by the end, we can answer questions such as:

### KMP

- What code should be shared?
- What code should remain platform-specific?
- How do we structure platform abstractions?
- What are the limitations of Compose Multiplatform?

### Timer

- How should timer state be modeled?
- How should timers behave when the application is backgrounded?
- How do we restore timer state after an application restart?

### Networking

- How can devices discover each other?
- Can local Wi-Fi discovery work reliably?
- When should we use Bluetooth?
- When does a central server become necessary?

### Distributed Systems

- What happens when a device disconnects?
- How do we know whether someone is still online?
- What happens when two devices have different views of state?
- How should presence expire?
- How do we synchronize state after reconnecting?

### Security

- Can anyone on the same Wi-Fi network see team members?
- How do devices establish trust?
- What information should be shared?
- How can local communication be secured?

---

# 🚫 What This Project Is Not

This project is **not initially intended to be**:

- A commercial productivity platform
- A Slack replacement
- A full messaging application
- A complicated enterprise collaboration system

Messaging may be explored as a technical experiment, but the primary product concept remains:

> **A simple, visually obvious team Pomodoro and availability system.**

---

# 🌟 Long-Term Vision

The ideal experience is extremely simple.

You open the application.

Your colleagues can immediately see:

```text
┌────────────────────────────────────┐
│          TEAM AVAILABILITY         │
├────────────────────────────────────┤
│                                    │
│ 🟢 Priya       AVAILABLE           │
│                                    │
│ 🔴 Rahul       FOCUS     18:42     │
│                                    │
│ 🟡 Amit        BREAK      03:21    │
│                                    │
│ 🔴 Neha        FOCUS      07:54    │
│                                    │
└────────────────────────────────────┘
```

No one needs to ask:

> "Can I disturb Rahul?"

They can simply look.

And underneath that simple experience is a project that teaches us how to build:

- Multiplatform applications
- Reactive UIs
- Distributed presence
- Local networking
- Device discovery
- Synchronization
- Fault-tolerant systems
- Secure communication
- Collaborative software

---

# 📖 Learning in Public

This repository is intentionally public.

We want the codebase to show not only the final solution, but also the **engineering journey**.

If an approach fails, document it.

If an architecture changes, document why.

If a networking experiment doesn't work, document what we learned.

The repository should ultimately answer:

> **"How did we go from a simple Pomodoro timer to a cross-platform, distributed team availability system?"**

That's the real project.
