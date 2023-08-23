package me.neversleeps.appspring

import com.ninjasquad.springmockk.MockkBean
import me.neversleeps.repository.postgresql.RepositoryProjectSQL
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTests {
    @MockkBean
    private lateinit var repository: RepositoryProjectSQL

    @Test
    fun contextLoads() {
    }
}
