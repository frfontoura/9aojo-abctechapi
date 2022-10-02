package br.com.fiap.abctechapi.fixtures

import br.com.fiap.abctechapi.model.User
import java.util.UUID

object UserFixture {

    fun random(
        id: Long = 1L,
        username: String = "user1",
        password: String = UUID.randomUUID().toString(),
        email: String = "user1@mock.com",
        userCode: UUID = UUID.randomUUID()
    ) = User(
        id = id,
        username = username,
        password = password,
        email = email,
        userCode = userCode
    )

}