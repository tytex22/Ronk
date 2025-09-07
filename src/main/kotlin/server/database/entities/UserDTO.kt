package server.database.entities

data class UserDTO(
    val userName: String,
    val password: String,
    val salt: String
)