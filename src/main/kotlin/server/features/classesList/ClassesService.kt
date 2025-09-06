package server.features.classesList

import server.database.DBResult
import server.database.dao.ClassesDAO
import shared.protocol.Response
import shared.protocol.Status

object ClassesService {

    fun getClassesList(): Response {
        println("getting classesList")
        return when (val query = ClassesDAO.getAllClasses()) {
            is DBResult.WithData -> {
                Response(Status.SUCCESS, null, query.data)
            }

            is DBResult.ResponseOnly -> {
                query.response
            }
        }
    }

}

