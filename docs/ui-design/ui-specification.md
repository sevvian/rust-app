# UI/UX Design Specification
## Appointment Booking Agent - User Interface

**Version:** 3.5.2  
**Date:** April 6, 2026  
**Design System:** Material Design 3  
**Framework:** Jetpack Compose 2025.02.00

---

## 1. Design Principles

### 1.1 Core Principles

| Principle | Description | Implementation |
|-----------|-------------|----------------|
| **Clarity** | Users should instantly understand app state | Real-time status indicators, clear typography |
| **Efficiency** | Minimize steps to complete tasks | Quick actions, saved configurations |
| **Trust** | Users must trust automation | Transparent logging, manual override options |
| **Feedback** | Immediate response to actions | Haptic feedback, visual animations |

### 1.2 Visual Language

**Color Palette:**
```kotlin
// Primary Colors
val Primary = Color(0xFF6750A4)        // Purple - Main brand
val OnPrimary = Color(0xFFFFFFFF)      // White text on primary
val PrimaryContainer = Color(0xFFEADDFF) // Light purple container

// Secondary Colors
val Secondary = Color(0xFF625B71)      // Muted purple
val SecondaryContainer = Color(0xFFE8DEF8)

// Status Colors
val Success = Color(0xFF4CAF50)        // Green - Success
val Warning = Color(0xFFFFA726)        // Orange - Warning
val Error = Color(0xFFE53935)          // Red - Error
val Info = Color(0xFF2196F3)           // Blue - Info

// Background
val Background = Color(0xFFFFFBFE)     // Off-white
val Surface = Color(0xFFFFFBFE)        // Cards/surfaces
```

**Typography:**
- **Display**: Large headers, bold weight
- **Headline**: Section titles, medium weight
- **Title**: Card titles, medium weight
- **Body**: Primary text, regular weight
- **Label**: Buttons, captions, medium weight

---

## 2. Screen Specifications

### 2.1 Main Dashboard (Home Screen)

**Purpose:** Central hub showing active operations and quick access

**Layout:**
```
┌─────────────────────────────────────┐
│  Status Bar (Time, Battery)         │
├─────────────────────────────────────┤
│  ┌─────────────────────────────┐   │
│  │  AGENT STATUS               │   │
│  │  ┌─────────────────────┐   │   │
│  │  │ ● Running           │   │   │
│  │  │ Strike: Museum Visit│   │   │
│  │  │ Time: 00:32 elapsed │   │   │
│  │  └─────────────────────┘   │   │
│  │  [Stop Button]              │   │
│  └─────────────────────────────┘   │
│                                     │
│  ┌─────────────────────────────┐   │
│  │  LOG CONSOLE                │   │
│  │  ┌─────────────────────┐   │   │
│  │  │ > Pre-warming TLS   │   │   │
│  │  │ > Identity: Chrome  │   │   │
│  │  │ > Checking...       │   │   │
│  └─────────────────────────────┘   │
│                                     │
│  ┌─────────────────────────────┐   │
│  │  UPCOMING STRIKES           │   │
│  │  ┌─────────────────────┐   │   │
│  │  │ 🕐 9:00 AM - MoMA   │   │   │
│  │  │ 🕐 10:00 AM - MET   │   │   │
│  └─────────────────────────────┘   │
│                                     │
│  [+ New Strike]  [View History]     │
└─────────────────────────────────────┘
```

**Components:**

1. **Status Card**
   - Visual indicator (pulsing dot when running)
   - Current operation name
   - Elapsed time counter
   - Cancel button (red, prominent)

2. **Log Console**
   - Scrollable text view
   - Auto-scroll to bottom
   - Timestamp prefix
   - Color-coded by severity

3. **Upcoming Strikes List**
   - Time and venue
   - Swipe to edit/delete
   - Tap to view details

**Animations:**
- Status dot: Pulsing animation (1.5s cycle)
- Log entries: Fade-in (200ms)
- Cards: Elevation change on scroll

---

### 2.2 Strike Configuration Screen

**Purpose:** Configure new booking attempts

**Layout:**
```
┌─────────────────────────────────────┐
│  ← Configure Strike                 │
├─────────────────────────────────────┤
│                                     │
│  SITE SELECTION                     │
│  ┌─────────────────────────────┐   │
│  │ ▼ Select Site               │   │
│  │   Brooklyn Public Library   │   │
│  └─────────────────────────────┘   │
│                                     │
│  MUSEUM                             │
│  ┌─────────────────────────────┐   │
│  │ ○ MoMA                      │   │
│  │ ○ Brooklyn Museum           │   │
│  │ ● MET                       │   │
│  └─────────────────────────────┘   │
│                                     │
│  DROP TIME                          │
│  ┌─────────────────────────────┐   │
│  │ 📅 Apr 15, 2026             │   │
│  │ 🕐 09:00:00 AM              │   │
│  └─────────────────────────────┘   │
│                                     │
│  MODE                               │
│  ┌─────────────┬─────────────┐     │
│  │   ALERT    │   BOOKING   │     │
│  │  (Notify)  │  (Auto-buy) │     │
│  └─────────────┴─────────────┘     │
│                                     │
│  CREDENTIALS                        │
│  ┌─────────────────────────────┐   │
│  │ ▼ Select Credential         │   │
│  │   Library Card #1234        │   │
│  └─────────────────────────────┘   │
│                                     │
│  ADVANCED OPTIONS                   │
│  ┌─────────────────────────────┐   │
│  │ Pre-warm: 30s before        │   │
│  │ Check window: 60s           │   │
│  │ Preferred days: Mon, Wed    │   │
│  └─────────────────────────────┘   │
│                                     │
│  [    SAVE STRIKE    ]              │
│                                     │
└─────────────────────────────────────┘
```

**Input Components:**

1. **Dropdown Selection**
   - Sites, museums, credentials
   - Searchable if > 5 items
   - Icons for visual identification

2. **Date/Time Picker**
   - Calendar for date
   - Time wheel for precision
   - Timezone display

3. **Segmented Control (Mode)**
   - Alert vs Booking
   - Visual distinction
   - Description text below

4. **Expandable Advanced Section**
   - Initially collapsed
   - Chevron indicator
   - Smooth expansion animation

**Validation:**
- Real-time validation feedback
- Disable save until valid
- Error tooltips

---

### 2.3 Site Management Screen

**Purpose:** Manage booking site configurations

**Layout:**
```
┌─────────────────────────────────────┐
│  ← Manage Sites           [+]       │
├─────────────────────────────────────┤
│                                     │
│  ┌─────────────────────────────┐   │
│  │ 🏛️ Brooklyn Public Library  │   │
│  │    3 museums configured     │   │
│  │    [Edit] [Test] [Delete]   │   │
│  └─────────────────────────────┘   │
│                                     │
│  ┌─────────────────────────────┐   │
│  │ 🏛️ New York Public Library  │   │
│  │    5 museums configured     │   │
│  │    [Edit] [Test] [Delete]   │   │
│  └─────────────────────────────┘   │
│                                     │
└─────────────────────────────────────┘
```

**Features:**
- Swipe actions (edit/delete)
- Drag to reorder
- Import/Export JSON
- Duplicate site config

---

### 2.4 Credential Management Screen

**Purpose:** Securely store login credentials

**Layout:**
```
┌─────────────────────────────────────┐
│  ← Credentials            [+]       │
├─────────────────────────────────────┤
│                                     │
│  🔐 Stored Credentials              │
│  Encrypted with device key          │
│                                     │
│  ┌─────────────────────────────┐   │
│  │ 👤 Library Card #1234       │   │
│  │    Brooklyn Public Library  │   │
│  │    ●●●●●●●● (hidden)        │   │
│  │    [Edit] [Delete]          │   │
│  └─────────────────────────────┘   │
│                                     │
│  ┌─────────────────────────────┐   │
│  │ 👤 john@email.com           │   │
│  │    NYPL                     │   │
│  │    ●●●●●●●● (hidden)        │   │
│  │    [Edit] [Delete]          │   │
│  └─────────────────────────────┘   │
│                                     │
│  Security: Hardware-backed ✓        │
└─────────────────────────────────────┘
```

**Security Indicators:**
- Lock icon
- Encryption status
- Biometric auth prompt

---

### 2.5 Live Strike Screen

**Purpose:** Full-screen monitoring during active booking

**Layout:**
```
┌─────────────────────────────────────┐
│  LIVE STRIKE          [X Cancel]    │
├─────────────────────────────────────┤
│                                     │
│           ┌─────────┐               │
│           │  00:32  │               │
│           │ elapsed │               │
│           └─────────┘               │
│                                     │
│  ┌─────────────────────────────┐   │
│  │  Target: MoMA               │   │
│  │  Time: 9:00 AM              │   │
│  │  Mode: Booking              │   │
│  │  Profile: Chrome 123        │   │
│  └─────────────────────────────┘   │
│                                     │
│  LIVE LOG                           │
│  ┌─────────────────────────────┐   │
│  │ ▶ [09:00:00] Strike start   │   │
│  │ ▶ [09:00:01] TLS warmed     │   │
│  │ ▶ [09:00:02] Checking...    │   │
│  │ ▶ [09:00:05] Match found!   │   │
│  │ ▶ [09:00:06] Booking...     │   │
│  │ ▶ [09:00:08] Success!       │   │
│  └─────────────────────────────┘   │
│                                     │
│  ┌─────────────────────────────┐   │
│  │  ✅ BOOKING CONFIRMED       │   │
│  │  Pass: Adult Admission      │   │
│  │  Date: Apr 15, 2026         │   │
│  │  [View Confirmation]        │   │
│  └─────────────────────────────┘   │
│                                     │
└─────────────────────────────────────┘
```

**States:**
1. **Countdown**: Before drop time
2. **Active**: During strike window
3. **Success**: Booking confirmed
4. **Failed**: Error or no availability

**Animations:**
- Countdown: Circular progress indicator
- Success: Confetti animation (optional)
- Log: Typewriter effect

---

## 3. Component Library

### 3.1 Custom Components

#### StatusIndicator
```kotlin
@Composable
fun StatusIndicator(
    isRunning: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(12.dp)
            .background(
                color = if (isRunning) Success else Error,
                shape = CircleShape
            )
    ) {
        if (isRunning) {
            // Pulsing animation
            val infiniteTransition = rememberInfiniteTransition()
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500),
                    repeatMode = RepeatMode.Reverse
                )
            )
            // Pulsing ring
        }
    }
}
```

#### LogConsole
```kotlin
@Composable
fun LogConsole(
    logs: List<LogEntry>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.Black.copy(alpha = 0.9f))
            .padding(8.dp)
    ) {
        items(logs) { log ->
            Text(
                text = "[${log.timestamp}] ${log.message}",
                color = when (log.level) {
                    LogLevel.INFO -> Color.White
                    LogLevel.WARN -> Warning
                    LogLevel.ERROR -> Error
                },
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
```

#### StrikeCard
```kotlin
@Composable
fun StrikeCard(
    strike: ScheduledStrike,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = strike.museumName,
                    style = MaterialTheme.typography.titleMedium
                )
                StatusBadge(status = strike.status)
            }
            // ... more content
        }
    }
}
```

---

## 4. Navigation Structure

```
MainActivity
├── NavHost (Bottom Navigation)
│   ├── Home (Dashboard)
│   ├── Strikes (List + Config)
│   ├── Sites (Management)
│   └── Settings (Credentials + App)
│
├── Dialogs
│   ├── StrikeConfigDialog
│   ├── SiteEditDialog
│   └── CredentialDialog
│
└── Full-Screen
    └── LiveStrikeActivity
```

**Navigation Transitions:**
- Default: Slide horizontally
- Dialogs: Fade + scale
- Full-screen: Shared element transition

---

## 5. Responsive Design

### 5.1 Breakpoints

| Device | Width | Layout Adjustments |
|--------|-------|-------------------|
| Phone (Portrait) | < 600dp | Single column, bottom nav |
| Phone (Landscape) | 600-840dp | Two columns where applicable |
| Tablet | > 840dp | Side navigation, master-detail |

### 5.2 Adaptive Layouts

**Phone:**
- Bottom navigation bar
- Full-screen dialogs
- Single-column lists

**Tablet:**
- Navigation rail
- Side-by-side configuration
- Two-pane layout for lists

---

## 6. Accessibility

### 6.1 Requirements

- **Screen Reader**: All elements labeled
- **Touch Targets**: Minimum 48x48dp
- **Color Contrast**: WCAG AA compliance (4.5:1)
- **Font Scaling**: Support 100-200% system font size

### 6.2 Implementation

```kotlin
// Content descriptions
Icon(
    imageVector = Icons.Default.Play,
    contentDescription = "Start strike operation"
)

// Semantic properties
Text(
    text = "Success",
    modifier = Modifier.semantics {
        stateDescription = "Booking completed successfully"
    }
)

// Focus management
LaunchedEffect(Unit) {
    focusRequester.requestFocus()
}
```

---

## 7. Micro-interactions

### 7.1 Feedback Patterns

| Action | Feedback |
|--------|----------|
| Button Tap | Ripple effect (300ms) |
| Save Success | Snackbar + haptic (light) |
| Error | Snackbar (long) + haptic (heavy) |
| Swipe Delete | Color change + icon reveal |
| Pull Refresh | Circular progress |

### 7.2 Animation Durations

| Animation | Duration | Easing |
|-----------|----------|--------|
| Button ripple | 300ms | Linear |
| Card elevation | 200ms | EaseOut |
| Screen transition | 300ms | EaseInOut |
| Dialog appearance | 250ms | Decelerate |
| List item add | 300ms | Spring |

---

## 8. Assets

### 8.1 Icons

| Icon | Usage | Source |
|------|-------|--------|
| Play | Start operation | Material Icons |
| Stop | Cancel operation | Material Icons |
| Schedule | Strike scheduling | Material Icons |
| Museum | Site identification | Custom SVG |
| Lock | Security indicator | Material Icons |
| Check | Success state | Material Icons |

### 8.2 Illustrations

- Empty state illustrations
- Onboarding graphics
- Error state images

---

## 9. Prototyping

### 9.1 Figma Structure

- **Components**: Reusable UI elements
- **Variants**: Different states (default, pressed, disabled)
- **Auto-layout**: Responsive containers
- **Prototypes**: Interactive flows

### 9.2 Key Screens to Prototype

1. Main dashboard (all states)
2. Strike configuration
3. Live strike (countdown, active, success, failure)
4. Site management
5. Credential input

---

**Next Document:** [API Reference](../04-api-reference/api-docs.md)
