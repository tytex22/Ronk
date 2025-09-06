package server.features.classesList

import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.ClassesList

object ClassesApi {
    fun handleGetClasses(): Response {

        return Response(Status.SUCCESS, null, ClassesList(listOf("A", "B", "C")))
    }
}