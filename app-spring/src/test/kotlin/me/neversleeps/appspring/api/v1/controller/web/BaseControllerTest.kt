package me.neversleeps.appspring.api.v1.controller.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

abstract class BaseControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    fun <Req : Any, Res : Any> testStub(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        val request = mapper.writeValueAsString(requestObj)
        val response = mapper.writeValueAsString(responseObj)

        val requestBuilder = MockMvcRequestBuilders.post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(request)
        mvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(response))
    }
}
