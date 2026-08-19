package com.actia.myapplication

import com.actia.myapplication.data.domain.model.Item
import com.actia.myapplication.data.domain.model.Result
import com.actia.myapplication.data.domain.usecase.GetItemsUseCase
import com.actia.myapplication.util.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.component.inject


class TestOMDBListItems:TestBaseData() {

    companion object {
        private const val RESPONSE_DATE = "Tue, 12 May 2020 10:14:36 GMT"
    }

    private val useCase: GetItemsUseCase by inject()

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
            val response = """{"Search":[{"Title":"Rambo","Year":"2008","imdbID":"tt0462499","Type":"movie","Poster":"https://m.media-amazon.com/images/M/MV5BMTI5Mjg1MzM4NF5BMl5BanBnXkFtZTcwNTAyNzUzMw@@._V1_SX300.jpg"},{"Title":"Rambo: First Blood Part II","Year":"1985","imdbID":"tt0089880","Type":"movie","Poster":"https://m.media-amazon.com/images/M/MV5BZWFkY2I1ZDAtNmZhNS00NjVlLWJiMGQtMGQ1ZmM0ZDA5ODg5XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg"},{"Title":"Rambo III","Year":"1988","imdbID":"tt0095956","Type":"movie","Poster":"https://m.media-amazon.com/images/M/MV5BZTQ4NjhkZWEtODY1MC00MzllLTkzNDYtZWVkMjNlNWRiOTdjXkEyXkFqcGdeQXVyMTAzMDM4MjM0._V1_SX300.jpg"},{"Title":"Rambo: Last Blood","Year":"2019","imdbID":"tt1206885","Type":"movie","Poster":"https://m.media-amazon.com/images/M/MV5BNTAxZWM2OTgtOTQzOC00ZTI5LTgyYjktZTRhYWM4YWQxNWI0XkEyXkFqcGdeQXVyMjMwNDgzNjc@._V1_SX300.jpg"},{"Title":"Rambo","Year":"1986","imdbID":"tt0222619","Type":"series","Poster":"https://m.media-amazon.com/images/M/MV5BZDQ0M2M2MjktMmViYy00MDM5LWE1NWEtZmRhNzZmMGM3MzkxXkEyXkFqcGdeQXVyMjY3MjUzNDk@._V1_SX300.jpg"},{"Title":"Arthur Rambo","Year":"2021","imdbID":"tt10951972","Type":"movie","Poster":"https://m.media-amazon.com/images/M/MV5BNzg5ZjJiNWEtNzhiZC00NzNhLThjZDgtZGU4OTMwN2I5YWY1XkEyXkFqcGdeQXVyNDgzNjg5Nw@@._V1_SX300.jpg"},{"Title":"Rambo III","Year":"1989","imdbID":"tt0301766","Type":"game","Poster":"https://m.media-amazon.com/images/M/MV5BNDE3Y2NkODgtMzhmNi00M2M3LTgxMTAtNjBhNTJiOTdmZDIzXkEyXkFqcGdeQXVyMjY3MjUzNDk@._V1_SX300.jpg"},{"Title":"Rambo: First Blood Part II","Year":"1986","imdbID":"tt0301768","Type":"game","Poster":"https://m.media-amazon.com/images/M/MV5BOWUzMDE1NTktMTU0OS00NTE3LWE2NzItMzA3MGM2NzdkYTJlXkEyXkFqcGdeQXVyMjY3MjUzNDk@._V1_SX300.jpg"},{"Title":"Rambo","Year":"2012","imdbID":"tt3107798","Type":"movie","Poster":"https://m.media-amazon.com/images/M/MV5BNDUwOGYwYWUtZjMzNi00MDIwLWE2NjgtYTdhODJmMTQwMGMyXkEyXkFqcGdeQXVyMzQzMDc2MDk@._V1_SX300.jpg"},{"Title":"Rambo","Year":"1987","imdbID":"tt0301765","Type":"game","Poster":"https://m.media-amazon.com/images/M/MV5BYmMxZGVjYzYtNWY4ZC00ZjAwLWFjNWQtOWFjNGEzNDQ2ZDM3XkEyXkFqcGdeQXVyMTgwOTE5NDk@._V1_SX300.jpg"}],"totalResults":"51","Response":"True"}"""

            mockWebServer.enqueue(
                MockResponse.Builder().code(200).body(response).setHeader("Date", RESPONSE_DATE).build()
            )

            val listResult = useCase
                .execute(Constants.APIKEY, "Ejemplo Title")
                .first()

            Assert.assertTrue(listResult is Result.Success)

            val responseToTest = (listResult as Result.Success).value

            Assert.assertTrue(responseToTest is List<Item>)

            Assert.assertTrue(responseToTest.size == 10)

            val expectedResult = listOf<Item>(
                Item(
                    title = "Rambo: First Blood Part II",
                    releaseYear = "1985",
                    imdb = "tt0089880",
                    poster = """https://m.media-amazon.com/images/M/MV5BZWFkY2I1ZDAtNmZhNS00NjVlLWJiMGQtMGQ1ZmM0ZDA5ODg5XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg"""
                ),
                Item(
                    title = "Rambo",
                    releaseYear = "1986",
                    imdb = "tt0222619",
                    poster="https://m.media-amazon.com/images/M/MV5BZDQ0M2M2MjktMmViYy00MDM5LWE1NWEtZmRhNzZmMGM3MzkxXkEyXkFqcGdeQXVyMjY3MjUzNDk@._V1_SX300.jpg"),
                Item(
                    title= "Rambo: First Blood Part II",
                    releaseYear= "1986",
                    imdb= "tt0301768",
                    poster= "https://m.media-amazon.com/images/M/MV5BOWUzMDE1NTktMTU0OS00NTE3LWE2NzItMzA3MGM2NzdkYTJlXkEyXkFqcGdeQXVyMjY3MjUzNDk@._V1_SX300.jpg"),
                Item(
                    title="Rambo",
                    releaseYear = "1987",
                    imdb = "tt0301765",
                    poster = "https://m.media-amazon.com/images/M/MV5BYmMxZGVjYzYtNWY4ZC00ZjAwLWFjNWQtOWFjNGEzNDQ2ZDM3XkEyXkFqcGdeQXVyMTgwOTE5NDk@._V1_SX300.jpg"),
                Item(
                    title = "Rambo III",
                    releaseYear = "1988",
                    imdb = "tt0095956",
                    poster = "https://m.media-amazon.com/images/M/MV5BZTQ4NjhkZWEtODY1MC00MzllLTkzNDYtZWVkMjNlNWRiOTdjXkEyXkFqcGdeQXVyMTAzMDM4MjM0._V1_SX300.jpg"),
                Item(
                    title= "Rambo III",
                    releaseYear = "1989",
                    imdb = "tt0301766",
                    poster="https://m.media-amazon.com/images/M/MV5BNDE3Y2NkODgtMzhmNi00M2M3LTgxMTAtNjBhNTJiOTdmZDIzXkEyXkFqcGdeQXVyMjY3MjUzNDk@._V1_SX300.jpg"),

                Item(
                    title = "Rambo",
                    releaseYear = "2008",
                    imdb = "tt0462499",
                    poster = "https://m.media-amazon.com/images/M/MV5BMTI5Mjg1MzM4NF5BMl5BanBnXkFtZTcwNTAyNzUzMw@@._V1_SX300.jpg"),
                Item(
                    title="Rambo",
                    releaseYear="2012",
                    imdb     ="tt3107798",
                    poster     = "https://m.media-amazon.com/images/M/MV5BNDUwOGYwYWUtZjMzNi00MDIwLWE2NjgtYTdhODJmMTQwMGMyXkEyXkFqcGdeQXVyMzQzMDc2MDk@._V1_SX300.jpg"),

                Item(
                    title = "Rambo: Last Blood",
                    releaseYear = "2019",
                    imdb = "tt1206885",
                    poster = "https://m.media-amazon.com/images/M/MV5BNTAxZWM2OTgtOTQzOC00ZTI5LTgyYjktZTRhYWM4YWQxNWI0XkEyXkFqcGdeQXVyMjMwNDgzNjc@._V1_SX300.jpg"),
                Item(
                    title = "Arthur Rambo",
                    releaseYear = "2021",
                    imdb = "tt10951972",
                    poster="https://m.media-amazon.com/images/M/MV5BNzg5ZjJiNWEtNzhiZC00NzNhLThjZDgtZGU4OTMwN2I5YWY1XkEyXkFqcGdeQXVyNDgzNjg5Nw@@._V1_SX300.jpg")
            )

            assertThat(responseToTest, `is`(expectedResult))
        }
    }
}