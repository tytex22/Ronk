package server.features.authorization

import server.database.dao.UserDAO
import server.database.entities.UserDTO
import server.util.PasswordHasher
import server.util.Validation
import shared.protocol.Response
import shared.protocol.Status

object AuthService {
//    init {
//        DatabaseManager.createTable()
//    }

    fun signIn(userName: String, password: String): Response {

        if (!UserDAO.checkIfUserExistByName(userName)) {
            return Response(Status.FAIL, "Wrong username or password")
        }

        val user: UserDTO = UserDAO.getByUserName(userName)

        return if (PasswordHasher.verifyPassword(password, user.salt, user.password)) {
            Response(Status.SUCCESS)
        } else {
            Response(Status.FAIL, "Wrong username or password")
        }
    }

    fun signUp(userName: String, password: String): Response {

        val (isStrong, notStrongReason) = Validation.isStrongEnough(password)

        if (!isStrong) return Response(Status.FAIL, notStrongReason)

        if (UserDAO.checkIfUserExistByName(userName)) {
            return Response(Status.FAIL, "username already taken")
        }

        return try {
            val salt = PasswordHasher.generateSalt()
            val hashedPassword = PasswordHasher.hashPassword(password, salt)
            UserDAO.insertUser(userName, hashedPassword, salt)
            Response(Status.SUCCESS)
        } catch (e: RuntimeException) {
            Response(Status.ERROR, "Failed to create user: ${e.message}")
        }

    }

}