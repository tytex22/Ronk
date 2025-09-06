package server.features.classesList

import shared.protocol.Response
import shared.protocol.Status

object ClassesApi {
    fun handleGetClasses(): Response {
        return try {
            println("classesApi")
            ClassesService.getClassesList()
        } catch (e: Exception) {
            Response(Status.ERROR, e.message ?: "Invalid request")
        }
    }
}