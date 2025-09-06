package shared.protocol.data

import kotlinx.serialization.Serializable

@Serializable
sealed interface Data

@Serializable
sealed interface ClientData : Data

@Serializable
sealed interface ServerData : Data

@Serializable
data class AuthData(val userName: String, val password: String) : ClientData

@Serializable
data class UserData(val storedUserName: String, val storedPassword: String, val salt: String) : ServerData

@Serializable
data class ClassesList(val classesList: List<String>) : ServerData
