use serde::{Deserialize, Serialize};
use std::collections::HashMap;
use chrono::{DateTime, Utc};

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct Museum {
    pub name: String,
    pub slug: String,
    pub museumid: String,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct FormFieldConfig {
    pub name: String,
    #[serde(rename = "type")]
    pub field_type: String,
    pub value: String,
    pub selector: String,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct LoginFormConfig {
    pub usernamefield: String,
    pub passwordfield: String,
    pub submitbutton: String,
    pub csrfselector: String,
    pub authidselector: String,
    pub loginurlselector: String,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct BookingFormConfig {
    pub actionurl: String,
    pub fields: Vec<FormFieldConfig>,
    pub emailfield: String,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct Site {
    pub name: String,
    pub baseurl: String,
    pub availabilityendpoint: String,
    pub digital: bool,
    pub physical: bool,
    pub location: String,
    pub bookinglinkselector: String,
    pub loginform: LoginFormConfig,
    pub bookingform: BookingFormConfig,
    pub successindicator: String,
    pub museums: HashMap<String, Museum>,
    pub preferredslug: String,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct Credential {
    pub id: String,
    pub name: String,
    pub username: String,
    pub password: String,
    pub email: String,
    pub site: String,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct ScheduledRun {
    pub id: String,
    pub sitekey: String,
    pub museumslug: String,
    pub droptime: DateTime<Utc>,
    pub mode: String,
    pub credentialid: String,
    #[serde(default)]
    pub credential_error_notified: bool,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct AppConfig {
    pub sites: HashMap<String, Site>,
    pub active_site: String,
    pub mode: String,
    pub preferred_days: Vec<String>,
    pub strike_time: String,

    // Using milliseconds for easier JNI/JSON interop for durations
    pub check_window_ms: u64,
    pub check_interval_ms: u64,
    pub pre_warm_offset_ms: u64,

    pub ntfy_topic: String,
    pub max_workers: usize,
    pub request_jitter_ms: u64,
    pub months_to_check: u32,
    pub scheduled_runs: Vec<ScheduledRun>,
    pub rest_cycle_checks: u32,
    pub rest_cycle_duration_ms: u64,
    pub credentials: HashMap<String, Credential>,
    pub selected_credential: String,
}
