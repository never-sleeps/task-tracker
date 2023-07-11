package me.neversleeps.acceptance.blackbox.fixture.client

/**
 * Клиент к нашему приложению в docker-compose, который умеет отправлять запрос и получать ответ.
 * Способ отправки/получения зависит от приложения - rabbit, http, ws, ...
 */
interface Client {
    /**
     * @param path путь к ресурсу, имя топика и т.п. (ad/create)
     * @param request тело сообщения в виде строки
     * @return тело ответа
     */
    suspend fun sendAndReceive(version: String, path: String, request: String): String
}
