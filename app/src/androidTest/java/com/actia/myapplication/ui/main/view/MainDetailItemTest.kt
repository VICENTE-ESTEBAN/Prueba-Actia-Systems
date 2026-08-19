package com.actia.myapplication.ui.main.view


import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.actia.myapplication.R
import com.actia.myapplication.data.domain.model.DetailItem
import com.actia.myapplication.utils.TestBaseData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainDetailItemTest : TestBaseData() {

    private val RESPONSE_DATE = "Tue, 12 May 2020 10:14:36 GMT"

    @Before
    fun setup() {
        disableAnimations()
        koinTestSetUp()
    }

    @After
    fun autoClose() {
        enableAnimations()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun mainDetailItemTest()= runTest {
        runningMockWebServer { mockWebServer: MockWebServer ->

            val scenario = ActivityScenario.launch(MainActivity::class.java)

            val responseList =
                """{"Search":[{"Title":"Rambo","Year":"2008","imdbID":"tt0462499","Type":"movie","Poster":"https://m.media-amazon.com/images/M/MV5BMTI5Mjg1MzM4NF5BMl5BanBnXkFtZTcwNTAyNzUzMw@@._V1_SX300.jpg"},{"Title":"Rambo: First Blood Part II","Year":"1985","imdbID":"tt0089880","Type":"movie","Poster":"https://m.media-amazon.com/images/M/MV5BZWFkY2I1ZDAtNmZhNS00NjVlLWJiMGQtMGQ1ZmM0ZDA5ODg5XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg"},{"Title":"Rambo III","Year":"1988","imdbID":"tt0095956","Type":"movie","Poster":"https://m.media-amazon.com/images/M/MV5BZTQ4NjhkZWEtODY1MC00MzllLTkzNDYtZWVkMjNlNWRiOTdjXkEyXkFqcGdeQXVyMTAzMDM4MjM0._V1_SX300.jpg"},{"Title":"Rambo: Last Blood","Year":"2019","imdbID":"tt1206885","Type":"movie","Poster":"https://m.media-amazon.com/images/M/MV5BNTAxZWM2OTgtOTQzOC00ZTI5LTgyYjktZTRhYWM4YWQxNWI0XkEyXkFqcGdeQXVyMjMwNDgzNjc@._V1_SX300.jpg"},{"Title":"Rambo","Year":"1986","imdbID":"tt0222619","Type":"series","Poster":"https://m.media-amazon.com/images/M/MV5BZDQ0M2M2MjktMmViYy00MDM5LWE1NWEtZmRhNzZmMGM3MzkxXkEyXkFqcGdeQXVyMjY3MjUzNDk@._V1_SX300.jpg"},{"Title":"Arthur Rambo","Year":"2021","imdbID":"tt10951972","Type":"movie","Poster":"https://m.media-amazon.com/images/M/MV5BNzg5ZjJiNWEtNzhiZC00NzNhLThjZDgtZGU4OTMwN2I5YWY1XkEyXkFqcGdeQXVyNDgzNjg5Nw@@._V1_SX300.jpg"},{"Title":"Rambo III","Year":"1989","imdbID":"tt0301766","Type":"game","Poster":"https://m.media-amazon.com/images/M/MV5BNDE3Y2NkODgtMzhmNi00M2M3LTgxMTAtNjBhNTJiOTdmZDIzXkEyXkFqcGdeQXVyMjY3MjUzNDk@._V1_SX300.jpg"},{"Title":"Rambo: First Blood Part II","Year":"1986","imdbID":"tt0301768","Type":"game","Poster":"https://m.media-amazon.com/images/M/MV5BOWUzMDE1NTktMTU0OS00NTE3LWE2NzItMzA3MGM2NzdkYTJlXkEyXkFqcGdeQXVyMjY3MjUzNDk@._V1_SX300.jpg"},{"Title":"Rambo","Year":"2012","imdbID":"tt3107798","Type":"movie","Poster":"https://m.media-amazon.com/images/M/MV5BNDUwOGYwYWUtZjMzNi00MDIwLWE2NjgtYTdhODJmMTQwMGMyXkEyXkFqcGdeQXVyMzQzMDc2MDk@._V1_SX300.jpg"},{"Title":"Rambo","Year":"1987","imdbID":"tt0301765","Type":"game","Poster":"https://m.media-amazon.com/images/M/MV5BYmMxZGVjYzYtNWY4ZC00ZjAwLWFjNWQtOWFjNGEzNDQ2ZDM3XkEyXkFqcGdeQXVyMTgwOTE5NDk@._V1_SX300.jpg"}],"totalResults":"51","Response":"True"}"""

            val responseDetail =
                """{"Title":"Rambo: First Blood Part II","Year":"1985","Rated":"R","Released":"22 May 1985","Runtime":"96 min","Genre":"Action, Adventure, Thriller","Director":"George P. Cosmatos","Writer":"David Morrell, Kevin Jarre, Sylvester Stallone","Actors":"Sylvester Stallone, Richard Crenna, Charles Napier","Plot":"Rambo returns to the jungles of Vietnam on a mission to infiltrate an enemy base-camp and rescue the American POWs still held captive there.","Language":"English, Vietnamese, Russian","Country":"United States, Mexico","Awards":"Nominated for 1 Oscar. 5 wins & 5 nominations total","Poster":"https://m.media-amazon.com/images/M/MV5BZWFkY2I1ZDAtNmZhNS00NjVlLWJiMGQtMGQ1ZmM0ZDA5ODg5XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg","Ratings":[{"Source":"Internet Movie Database","Value":"6.5/10"},{"Source":"Rotten Tomatoes","Value":"33%"},{"Source":"Metacritic","Value":"47/100"}],"Metascore":"47","imdbRating":"6.5","imdbVotes":"170,205","imdbID":"tt0089880","Type":"movie","DVD":"23 Nov 2004","BoxOffice":"${'$'}150,415,432","Production":"N/A","Website":"N/A","Response":"True"}"""

            mockWebServer.enqueue(
                MockResponse.Builder()
                    .code(200)
                    .setHeader("Date", RESPONSE_DATE)
                    .body(responseList).build()
            )

            mockWebServer.enqueue(
                MockResponse.Builder()
                    .code(200)
                    .setHeader("Date", RESPONSE_DATE)
                    .body(responseDetail).build()
            )

            val appCompatEditText = onView(
                allOf(
                    withId(R.id.etTitle),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.nav_fragment),
                            0
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            appCompatEditText.perform(replaceText("rambo"), closeSoftKeyboard())

            val appCompatButton = onView(
                allOf(
                    withId(R.id.btnBuscar), withText("Buscar"),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.nav_fragment),
                            0
                        ),
                        1
                    ),
                    isDisplayed()
                )
            )

            appCompatButton.perform(click())

            advanceUntilIdle()


            val cardView = onView(
                allOf(
                    withId(R.id.card_item),
                    childAtPosition(
                        allOf(
                            withId(R.id.rvItems),
                            childAtPosition(
                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                4
                            )
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            cardView.perform(click())


            testScreenValues()

            scenario.close()
        }
    }


    private fun testScreenValues(){
        val expectedResult = DetailItem(
            title="Rambo: First Blood Part II",
            poster="https://m.media-amazon.com/images/M/MV5BZWFkY2I1ZDAtNmZhNS00NjVlLWJiMGQtMGQ1ZmM0ZDA5ODg5XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg",
            director="George P. Cosmatos",
            releaseYear="1985",
            duration="96 min",
            description="Rambo returns to the jungles of Vietnam on a mission to infiltrate an enemy base-camp and rescue the American POWs still held captive there.",
            score="6.5")


        onView(allOf(withId(R.id.txtTitle),
            isDisplayed())
        ).check(matches(withText(expectedResult.title)))


        onView(allOf(withId(R.id.txtDirector),
            isDisplayed())
        ).check(matches(withText(expectedResult.director)))


        onView(allOf(withId(R.id.txtReleaseYear),
            isDisplayed())
        ).check(matches(withText(expectedResult.releaseYear)))

        onView(allOf(withId(R.id.txtDuration),
            isDisplayed())
        ).check(matches(withText(expectedResult.duration)))

        onView(allOf(withId(R.id.txtDescription),
            isDisplayed())
        ).check(matches(withText(expectedResult.description)))

        onView(allOf(withId(R.id.txtScore),
            isDisplayed())
        ).check(matches(withText(expectedResult.score)))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
