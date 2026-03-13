package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever

class GameResultsControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock()
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult_returnsCorrectResult() {
        val result = GameResult(1, "Jakob", 100, 12.0)

        whenever(mockedService.getGameResult(1)).thenReturn(result)

        val res = controller.getGameResult(1)

        verify(mockedService).getGameResult(1)
        assertEquals(result, res)
    }

    @Test
    fun test_getAllGameResults_returnsAllResults() {
        val first = GameResult(1, "Jakob", 100, 12.0)
        val second = GameResult(2, "Niko", 90, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(first, second))

        val res = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(listOf(first, second), res)
    }

    @Test
    fun test_addGameResult_callsService() {
        val result = GameResult(1, "Jakob", 100, 12.0)

        controller.addGameResult(result)

        verify(mockedService).addGameResult(result)
    }

    @Test
    fun test_deleteGameResult_callsService() {
        controller.deleteGameResult(1)

        verify(mockedService).deleteGameResult(1)
    }
}