package com.actia.myapplication

import com.actia.myapplication.data.domain.model.DetailItem
import com.actia.myapplication.data.domain.model.Result
import com.actia.myapplication.data.domain.usecase.GetDetailItemByImdbUseCase
import com.actia.myapplication.util.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.component.inject


class TestOMDBDetailItem:TestBaseData() {

    private val RESPONSE_DATE = "Tue, 12 May 2020 10:14:36 GMT"

    private val useCase: GetDetailItemByImdbUseCase by inject()

    @Before
    fun setup() {
        koinTestSetUp()
    }

    @After
    fun autoClose() {
        koinTestClose()
    }

    @Test
    fun testListOfItemsOMDB() = runningMockWebServer { mockWebServer ->
        runTest {
            val response = """{"Title":"Rambo: First Blood Part II","Year":"1985","Rated":"R","Released":"22 May 1985","Runtime":"96 min","Genre":"Action, Adventure, Thriller","Director":"George P. Cosmatos","Writer":"David Morrell, Kevin Jarre, Sylvester Stallone","Actors":"Sylvester Stallone, Richard Crenna, Charles Napier","Plot":"Rambo returns to the jungles of Vietnam on a mission to infiltrate an enemy base-camp and rescue the American POWs still held captive there.","Language":"English, Vietnamese, Russian","Country":"United States, Mexico","Awards":"Nominated for 1 Oscar. 5 wins & 5 nominations total","Poster":"https://m.media-amazon.com/images/M/MV5BZWFkY2I1ZDAtNmZhNS00NjVlLWJiMGQtMGQ1ZmM0ZDA5ODg5XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg","Ratings":[{"Source":"Internet Movie Database","Value":"6.5/10"},{"Source":"Rotten Tomatoes","Value":"33%"},{"Source":"Metacritic","Value":"47/100"}],"Metascore":"47","imdbRating":"6.5","imdbVotes":"170,205","imdbID":"tt0089880","Type":"movie","DVD":"23 Nov 2004","BoxOffice":"${'$'}150,415,432","Production":"N/A","Website":"N/A","Response":"True"}"""

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setHeader("Date", RESPONSE_DATE)
                    .setBody(response)
            )

            val listResult = useCase
                .execute(Constants.APIKEY, "Ejemplo Title")
                .first()

            Assert.assertTrue(listResult is Result.Success)

            val responseToTest = (listResult as Result.Success).value

            Assert.assertTrue(responseToTest is DetailItem)

            val responseDetail = (responseToTest as DetailItem)

            val expectedResult = DetailItem(
                            title="Rambo: First Blood Part II",
                            poster="https://m.media-amazon.com/images/M/MV5BZWFkY2I1ZDAtNmZhNS00NjVlLWJiMGQtMGQ1ZmM0ZDA5ODg5XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg",
                            director="George P. Cosmatos",
                            releaseYear="1985",
                            duration="96 min",
                description="Rambo returns to the jungles of Vietnam on a mission to infiltrate an enemy base-camp and rescue the American POWs still held captive there.",
                score="6.5")

            assertThat(responseDetail, `is`(expectedResult))
        }
    }
}