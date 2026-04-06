fn main() {
    // Generates the Rust scaffolding (the glue between C and Rust)
    uniffi::generate_scaffolding("src/agent.udl")
        .expect("Failed to generate UniFFI scaffolding");
}
