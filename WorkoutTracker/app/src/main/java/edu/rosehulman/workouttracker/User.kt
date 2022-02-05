package edu.rosehulman.workouttracker

data class User(
    var name: String = "",
    var age: Int = -1,
    var major: String = "unknown",
    var storageUriString: String = "",
    var hasCompletedSetup: Boolean = false,
) {
    companion object {
        const val COLLECTION_PATH = "users"
    }
}
