package server.features.classesList

import server.features.authorization.AuthService
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.ClassesList

object ClassesApi {
    fun handleGetClasses(): Response {
        return try {
            println("classesApi")
            ClassesService.getClassesList()
        } catch (e: Exception) {
            Response(Status.ERROR, "In classesAPI: ${e.message}" ?: "Invalid request")
        }
    }
}