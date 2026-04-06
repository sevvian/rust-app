# Testing Strategy
## Comprehensive Testing Plan

**Version:** 3.5.2  
**Date:** April 6, 2026  
**Coverage Target:** > 80%

---

## 1. Testing Pyramid

```
       ┌─────────┐
       │   E2E   │  ← UI Tests (10%)
       │  Tests  │
      ┌┴─────────┴┐
      │ Integration│  ← Component Tests (20%)
      │   Tests    │
     ┌┴────────────┴┐
     │    Unit       │  ← Unit Tests (70%)
     │    Tests      │
     └───────────────┘
```

---

## 2. Unit Testing

### 2.1 Rust Unit Tests

**Location:** `rust-engine/src/` (inline or `tests/` directory)

#### Test Structure

```rust
// In lib.rs or tests/test_booking.rs
#[cfg(test)]
mod tests {
    use super::*;
    use tokio::test;

    // Test fixtures
    fn create_test_config() -> AppConfig {
        AppConfig {
            sites: HashMap::new(),
            active_site: "test".to_string(),
            // ...
        }
    }

    #[test]
    fn test_config_parsing() {
        let json = r#"{"active_site": "test", "mode": "booking"}"#;
        let config: AppConfig = serde_json::from_str(json).unwrap();
        assert_eq!(config.active_site, "test");
    }

    #[tokio::test]
    async fn test_agent_lifecycle() {
        let agent = BookingAgent::new();

        // Initial state
        let status = agent.get_status().await;
        assert!(!status.is_running);
        assert!(status.current_run_id.is_empty());

        // Stop should be safe when not running
        agent.stop();

        let status = agent.get_status().await;
        assert!(!status.is_running);
    }
}
```

#### Running Tests

```bash
cd rust-engine

# Run all tests
cargo test

# Run with output
cargo test -- --nocapture

# Run specific test
cargo test test_agent_lifecycle

# Run with coverage
cargo tarpaulin --out Html

# Test all features
cargo test --all-features
```

### 2.2 HTTP Client Tests

```rust
#[cfg(test)]
mod http_tests {
    use super::*;

    #[test]
    fn test_mimic_client_creation() {
        let client = MimicClient::new(30000);
        assert!(client.is_ok());

        let client = client.unwrap();
        assert!(!client.profile_name.is_empty());
    }

    #[test]
    fn test_emulation_rotation() {
        // Test that different profiles can be created
        let profiles = vec![
            (Emulation::Chrome123, "Chrome"),
            (Emulation::Safari17_2_1, "Safari"),
            (Emulation::Firefox128, "Firefox"),
        ];

        for (emulation, name) in profiles {
            let client = MimicClient::with_profile(30000, emulation, name);
            assert!(client.is_ok());
        }
    }
}
```

### 2.3 Scraper Engine Tests

```rust
#[cfg(test)]
mod scraper_tests {
    use super::*;

    const TEST_HTML: &str = r#"
        <html>
            <body>
                <a class="s-lc-pass-availability s-lc-pass-digital s-lc-pass-available" 
                   href="/book/123">April 15</a>
                <a class="s-lc-pass-availability s-lc-pass-available" 
                   href="/book/124">April 16</a>
            </body>
        </html>
    "#;

    #[test]
    fn test_parse_html() {
        let document = Html::parse_document(TEST_HTML);
        let selector = Selector::parse("a.s-lc-pass-available").unwrap();

        let elements: Vec<_> = document.select(&selector).collect();
        assert_eq!(elements.len(), 2);
    }

    #[test]
    fn test_regex_fallback() {
        let html = r#"<a href="/book/123">15</a>"#;
        let re = Regex::new(r#"(?i)<a[^>]*href="([^"]*book[^"]*)"[^>]*>(\d+)"#).unwrap();

        let caps = re.captures(html).unwrap();
        assert_eq!(&caps[1], "/book/123");
        assert_eq!(&caps[2], "15");
    }
}
```

---

## 3. Integration Testing

### 3.1 Rust Integration Tests

**Location:** `rust-engine/tests/integration_tests.rs`

```rust
use rust_engine::*;
use tokio::test;

#[tokio::test]
async fn test_full_booking_flow() {
    // Setup test server (mock)
    let mut server = mockito::Server::new_async().await;

    // Mock endpoints
    let _m = server.mock("GET", "/availability")
        .with_status(200)
        .with_body(r#"<a class='s-lc-pass-available' href='/book/123'>April 15</a>"#)
        .create();

    // Create config pointing to mock server
    let mut config = create_test_config();
    config.sites.get_mut("test").unwrap().baseurl = server.url();

    // Execute
    let agent = BookingAgent::new();
    let result = agent.start_strike(
        serde_json::to_string(&config).unwrap(),
        "test-run".to_string()
    ).await;

    // Verify
    assert!(result.is_ok());
}
```

### 3.2 Android Integration Tests

**Location:** `android-app/app/src/androidTest/`

```kotlin
@RunWith(AndroidJUnit4::class)
class BookingAgentInstrumentedTest {

    @Test
    fun testAgentCreation() {
        val agent = BookingAgent()
        val status = agent.getStatus()

        assertNotNull(status)
        assertFalse(status.isRunning)
    }

    @Test
    fun testConfigSerialization() {
        val config = AppConfig(
            sites = mapOf("test" to createTestSite()),
            activeSite = "test",
            mode = "booking"
        )

        val json = Json.encodeToString(config)
        assertTrue(json.contains("test"))

        val deserialized = Json.decodeFromString<AppConfig>(json)
        assertEquals(config.activeSite, deserialized.activeSite)
    }
}
```

### 3.3 UI Tests (Espresso)

```kotlin
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomeScreenDisplaysCorrectly() {
        composeTestRule.setContent {
            HomeScreen()
        }

        // Verify elements exist
        composeTestRule.onNodeWithText("Agent Status").assertExists()
        composeTestRule.onNodeWithText("Start Strike").assertExists()
        composeTestRule.onNodeWithText("Stop").assertExists()
    }

    @Test
    fun testStartStrikeButton() {
        composeTestRule.setContent {
            HomeScreen()
        }

        // Click start
        composeTestRule.onNodeWithText("Start Strike").performClick()

        // Verify loading state
        composeTestRule.onNodeWithText("Running...").assertExists()
    }
}
```

---

## 4. End-to-End Testing

### 4.1 Manual Test Cases

#### Test Case 1: Successful Booking
**Preconditions:**
- Valid credentials configured
- Site accessible
- Drop time in future

**Steps:**
1. Configure strike for 2 minutes from now
2. Start strike
3. Wait for execution

**Expected:**
- Pre-warm phase executes
- Booking succeeds
- Confirmation displayed

#### Test Case 2: Cancellation
**Steps:**
1. Start long-running strike
2. Click cancel within 5 seconds

**Expected:**
- Operation stops immediately
- Status changes to "Cancelled"
- No side effects

#### Test Case 3: Network Failure
**Steps:**
1. Enable airplane mode
2. Start strike

**Expected:**
- Network error displayed
- Graceful degradation
- Retry option available

### 4.2 Automated E2E (Maestro)

**File:** `.maestro/flow.yml`

```yaml
appId: com.apptcheck.agent
---
- launchApp
- assertVisible: "Agent Status"
- tapOn: "Start Strike"
- assertVisible: "Running..."
- tapOn: "Stop"
- assertVisible: "Ready"
```

Run:
```bash
maestro test .maestro/flow.yml
```

---

## 5. Performance Testing

### 5.1 Rust Benchmarks

```rust
use criterion::{black_box, criterion_group, criterion_main, Criterion};

fn benchmark_scraper(c: &mut Criterion) {
    let html = include_str!("test_data/sample.html");

    c.bench_function("parse_html", |b| {
        b.iter(|| {
            let doc = Html::parse_document(black_box(html));
            black_box(doc)
        })
    });
}

criterion_group!(benches, benchmark_scraper);
criterion_main!(benches);
```

Run:
```bash
cargo bench
```

### 5.2 Android Performance

```kotlin
@RunWith(AndroidJUnit4::class)
class PerformanceTest {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.apptcheck.agent",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()
    }
}
```

---

## 6. Security Testing

### 6.1 Static Analysis

**Rust:**
```bash
# Clippy (linting)
cargo clippy --all-targets --all-features -- -D warnings

# Security audit
cargo audit

# Format check
cargo fmt -- --check
```

**Kotlin:**
```bash
# Detekt
./gradlew detekt

# Android Lint
./gradlew lint
```

### 6.2 Dependency Scanning

```bash
# Rust
cargo audit

# Android
./gradlew dependencyCheckAnalyze
```

### 6.3 Penetration Testing Checklist

- [ ] Credential storage encryption
- [ ] Network traffic interception (MITM)
- [ ] Root detection bypass
- [ ] Certificate pinning validation
- [ ] Input validation (injection attacks)
- [ ] Memory dump analysis

---

## 7. Continuous Integration

### 7.1 GitHub Actions Workflow

```yaml
name: Test Suite

on: [push, pull_request]

jobs:
  test-rust:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Setup Rust
        uses: dtolnay/rust-toolchain@stable

      - name: Cache dependencies
        uses: Swatinem/rust-cache@v2

      - name: Run tests
        run: cd rust-engine && cargo test --all-features

      - name: Check formatting
        run: cd rust-engine && cargo fmt -- --check

      - name: Run clippy
        run: cd rust-engine && cargo clippy -- -D warnings

      - name: Generate coverage
        run: |
          cargo install cargo-tarpaulin
          cargo tarpaulin --out Xml

      - name: Upload coverage
        uses: codecov/codecov-action@v3
        with:
          files: ./rust-engine/cobertura.xml

  test-android:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Setup JDK
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Run unit tests
        run: ./gradlew test

      - name: Run lint
        run: ./gradlew lint
```

---

## 8. Test Data Management

### 8.1 Mock Data

```rust
// test_helpers.rs
pub fn create_mock_site() -> Site {
    Site {
        name: "Test Museum".to_string(),
        baseurl: "https://test.example.com".to_string(),
        availabilityendpoint: "/api/availability".to_string(),
        // ...
    }
}

pub fn create_mock_html() -> String {
    r#"<html>...</html>"#.to_string()
}
```

### 8.2 Test Fixtures

**Location:** `rust-engine/tests/fixtures/`

- `sample_page.html` - Real HTML samples
- `config.json` - Test configurations
- `responses/` - Mock API responses

---

## 9. Coverage Requirements

| Component | Minimum Coverage |
|-----------|------------------|
| Rust Core | 80% |
| HTTP Client | 75% |
| Scraper | 85% |
| Booker | 70% |
| Kotlin ViewModel | 70% |
| UI Components | 60% |

---

## 10. Test Maintenance

### 10.1 Flaky Test Prevention

- Use deterministic data
- Mock time sources
- Avoid network dependencies
- Use test retries sparingly

### 10.2 Test Documentation

Each test should document:
- What it tests
- Preconditions
- Expected behavior
- Cleanup requirements

---

**Next Document:** [Deployment Guide](../07-deployment/deployment-guide.md)
