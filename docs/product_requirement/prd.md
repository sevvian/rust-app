# Product Requirements Document (PRD)
## Appointment Booking Agent - Rust Android Application

**Version:** 3.5.2  
**Date:** April 6, 2026  
**Status:** Production Ready  
**Author:** Technical Architecture Team

---

## 1. Executive Summary

### 1.1 Product Overview
The Appointment Booking Agent is a high-performance native Android application built with Rust core and Kotlin UI. It automates the process of booking museum passes and appointments through web scraping and automated form submission with browser fingerprint emulation capabilities.

### 1.2 Key Value Propositions
- **High Performance**: Rust core provides memory-safe, zero-cost abstractions
- **Stealth Operation**: Browser impersonation prevents detection as automation
- **Precision Timing**: Microsecond-accurate execution for high-demand bookings
- **Cross-Platform Core**: Rust engine can be reused for iOS/Desktop variants
- **Modern UI**: Jetpack Compose with Material Design 3

### 1.3 Target Audience
- Museum patrons requiring timed entry passes
- Users booking high-demand limited-capacity events
- Power users needing automated booking assistance

---

## 2. Functional Requirements

### 2.1 Core Features

#### FR-001: Multi-Site Configuration
**Priority:** P0  
**Description:** Support multiple booking sites with unique configurations
- Each site has unique URL patterns, form fields, and authentication
- Site configuration via JSON with hot-reload capability
- Support for museum-specific sub-configurations

**Acceptance Criteria:**
- [ ] Can configure minimum 10 different sites simultaneously
- [ ] Site switching without app restart
- [ ] Encrypted credential storage per site

#### FR-002: Automated Authentication
**Priority:** P0  
**Description:** Automatic login form detection and submission
- Detect login forms via CSS selectors
- Support for session cookies and CSRF tokens
- Credential validation before booking attempt

**Acceptance Criteria:**
- [ ] Automatic login form detection accuracy > 95%
- [ ] Session persistence across booking attempts
- [ ] Graceful handling of authentication failures

#### FR-003: Availability Monitoring
**Priority:** P0  
**Description:** Real-time monitoring of booking slot availability
- AJAX-based availability checking
- Multi-month lookahead capability
- Regex fallback for HTML parsing resilience

**Acceptance Criteria:**
- [ ] Check availability across 6 months
- [ ] Response time < 2 seconds per check
- [ ] Handle HTML structure changes gracefully

#### FR-004: Precision Booking Execution
**Priority:** P0  
**Description:** Execute bookings at exact scheduled times
- Microsecond-precision timing using spin-loop for final milliseconds
- Pre-warm TLS sessions 30 seconds before drop time
- Jitter injection to avoid pattern detection

**Acceptance Criteria:**
- [ ] Timing accuracy within 5ms of target
- [ ] Success rate > 80% for high-demand drops
- [ ] Automatic retry with exponential backoff

#### FR-005: Browser Impersonation
**Priority:** P1  
**Description:** Evade bot detection through browser fingerprint emulation
- JA3 fingerprint emulation using BoringSSL
- Randomized User-Agent rotation
- Cookie store persistence
- Gzip/Brotli compression support

**Acceptance Criteria:**
- [ ] Pass Cloudflare and DataDome basic checks
- [ ] Rotate between 4+ browser profiles
- [ ] TLS fingerprint matches real browsers

#### FR-006: Real-time Status Monitoring
**Priority:** P1  
**Description:** Live feedback during booking operations
- Progress indicators for multi-step operations
- Log stream display in UI
- Cancellation capability mid-operation

**Acceptance Criteria:**
- [ ] Status updates every 100ms
- [ ] Cancel operation within 500ms
- [ ] Persistent log history

#### FR-007: Alert Mode vs Booking Mode
**Priority:** P2  
**Description:** Dual operation modes
- **Alert Mode**: Notify when slots become available without booking
- **Booking Mode**: Automatically attempt reservation

**Acceptance Criteria:**
- [ ] Mode switch without configuration change
- [ ] Alert notifications via NTFY
- [ ] Mode-specific success indicators

---

## 3. Non-Functional Requirements

### 3.1 Performance Requirements

| Metric | Requirement | Measurement |
|--------|-------------|-------------|
| Cold Start | < 3 seconds | App launch to interactive |
| Booking Request | < 500ms | Form submission round-trip |
| Availability Check | < 2 seconds | AJAX request completion |
| Memory Usage | < 150MB | Peak heap allocation |
| Battery Impact | Minimal | Background execution efficiency |

### 3.2 Security Requirements

#### SR-001: Credential Encryption
- AES-256 encryption for stored credentials
- Android Keystore System integration
- Biometric authentication for sensitive operations

#### SR-002: Network Security
- Certificate pinning for known endpoints
- BoringSSL with modern cipher suites
- No sensitive data in logs

#### SR-003: Code Security
- Rust memory safety guarantees
- ProGuard/R8 obfuscation for release builds
- OWASP Mobile Top 10 compliance

### 3.3 Reliability Requirements

- **Availability**: 99.9% uptime for background monitoring
- **Error Recovery**: Automatic retry with exponential backoff
- **Data Integrity**: Checksum validation for all configurations
- **Crash Rate**: < 0.1% crash-free sessions

### 3.4 Compatibility Requirements

- **Android Versions**: 8.0 (API 26) to 15 (API 35)
- **Architectures**: ARM64, ARMv7, x86_64, x86
- **Screen Sizes**: Phone (5") to Tablet (13")
- **Orientations**: Portrait and Landscape

---

## 4. User Stories

### US-001: First-Time Setup
> As a museum patron, I want to configure the app with my library credentials so that I can book passes automatically.

**Acceptance Criteria:**
- Guided setup wizard
- Credential validation
- Test booking confirmation

### US-002: High-Demand Drop
> As a user, I want the app to automatically book a pass the moment it becomes available at 9:00 AM.

**Acceptance Criteria:**
- Precision timing execution
- Visual countdown timer
- Immediate success/failure feedback

### US-003: Multi-Museum Management
> As a power user, I want to monitor availability across multiple museums simultaneously.

**Acceptance Criteria:**
- Tab-based museum switching
- Aggregated availability view
- Per-museum configuration

### US-004: Stealth Operation
> As a user, I want the app to avoid detection as a bot to prevent IP banning.

**Acceptance Criteria:**
- Randomized request patterns
- Human-like timing jitter
- Browser fingerprint rotation

---

## 5. Technical Constraints

### 5.1 Development Constraints
- **Language**: Rust 1.70+ (core), Kotlin 2.1+ (UI)
- **Build System**: Cargo + Gradle
- **UI Framework**: Jetpack Compose 2025.02.00
- **Minimum SDK**: Android API 26 (Android 8.0)

### 5.2 Runtime Constraints
- **NDK**: Version 25.2.9519653 or newer
- **Rust Targets**: 4 Android architectures
- **Memory**: Work within Android memory limits
- **Permissions**: Internet, Notifications

### 5.3 Legal Constraints
- Compliance with museum Terms of Service
- User consent for automation
- Rate limiting adherence

---

## 6. Success Metrics

### 6.1 Business Metrics
- **Booking Success Rate**: > 80% for high-demand slots
- **User Retention**: > 60% monthly active users
- **App Store Rating**: > 4.5 stars

### 6.2 Technical Metrics
- **Crash-Free Rate**: > 99.9%
- **ANR Rate**: < 0.1%
- **Cold Start Time**: < 3 seconds
- **APK Size**: < 50MB

### 6.3 User Experience Metrics
- **Setup Completion**: > 90% finish onboarding
- **Booking Success**: > 80% achieve desired slots
- **Support Tickets**: < 5% of active users

---

## 7. Roadmap

### Phase 1: MVP (Completed)
- [x] Basic booking automation
- [x] Single site support
- [x] Manual trigger only

### Phase 2: Enhanced (Current)
- [x] Multi-site configuration
- [x] Scheduled strikes
- [x] Browser impersonation
- [x] Alert notifications

### Phase 3: Advanced (Q3 2026)
- [ ] Machine learning for drop pattern prediction
- [ ] Social features (group booking)
- [ ] iOS port using shared Rust core

### Phase 4: Enterprise (Q4 2026)
- [ ] Admin dashboard
- [ ] Usage analytics
- [ ] Institutional licensing

---

## 8. Glossary

| Term | Definition |
|------|------------|
| **Strike** | A booking attempt executed at a specific time |
| **Drop Time** | The exact moment when new slots become available |
| **Pre-warm** | Establishing TLS connection before drop time |
| **JA3** | TLS fingerprinting technique |
| **Mimic Client** | HTTP client with browser impersonation |
| **UDL** | UniFFI Interface Definition Language |

---

## 9. Document History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 2026-04-06 | Architecture Team | Initial PRD |
| 3.5.2 | 2026-04-06 | Tech Lead | Updated for wreq migration |

---

**Next Document:** [Architecture Specification](./02-architecture/architecture-spec.md)
