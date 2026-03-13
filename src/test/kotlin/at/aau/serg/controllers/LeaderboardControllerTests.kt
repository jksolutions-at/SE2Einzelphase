package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(second, res[0])
        assertEquals(third, res[1])
        assertEquals(first, res[2])
    }

    @Test
    fun test_getLeaderboard_withRank() {
        val p1 = GameResult(1, "p1", 70, 10.0)
        val p2 = GameResult(2, "p2", 60, 10.0)
        val p3 = GameResult(3, "p3", 50, 10.0)
        val p4 = GameResult(4, "p4", 40, 10.0)
        val p5 = GameResult(5, "p5", 30, 10.0)
        val p6 = GameResult(6, "p6", 20, 10.0)
        val p7 = GameResult(7, "p7", 10, 10.0)
        val p8 = GameResult(8, "p8", 5, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(p5, p3, p8, p1, p7, p2, p4, p6))

        val res: List<GameResult> = controller.getLeaderboard(4)

        verify(mockedService).getGameResults()
        assertEquals(7, res.size)
        assertEquals(listOf(p1, p2, p3, p4, p5, p6, p7), res)
    }

    @Test
    fun test_getLeaderboard_invalidRank_throwsBadRequest() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(first, second))

        val exception = assertFailsWith<ResponseStatusException> {
            controller.getLeaderboard(0)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
    }

    @Test
    fun test_getLeaderboard_rankTooLarge_throwsBadRequest() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(first, second))

        val exception = assertFailsWith<ResponseStatusException> {
            controller.getLeaderboard(3)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
    }

}