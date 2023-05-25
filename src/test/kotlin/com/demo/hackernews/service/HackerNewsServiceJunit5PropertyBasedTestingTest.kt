package com.demo.hackernews.service

import com.demo.hackernews.model.Item
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.client.RestTemplate

@MockKExtension.ConfirmVerification
@MockKExtension.CheckUnnecessaryStub
@ExtendWith(MockKExtension::class)
@TestInstance(PER_CLASS)
class HackerNewsServiceJunit5PropertyBasedTestingTest {

    private val BASE_URL = "https://hacker-news.firebaseio.com/v0/"

    @InjectMockKs
    lateinit var hackerNewsService: HackerNewsService

    @MockK
    lateinit var restTestTemplate: RestTemplate

    @Test
    fun `top stories should return stories`() {
        // given
        val storyId = 1
        var topStoryItem = Item()
        topStoryItem.id = storyId
        topStoryItem.title = "Top story title"
        topStoryItem.score = 6
        topStoryItem.type = "story"
        topStoryItem.url = "http://top-story-title.fakeurl"

        every { restTestTemplate.getForObject("${BASE_URL}topstories.json", IntArray::class.java) } returns intArrayOf(storyId)
        every { restTestTemplate.getForObject("${BASE_URL}item/$storyId.json", Item::class.java) } returns topStoryItem
        // when
        val topStories = hackerNewsService.getTopStories(5)

        // then
        topStories shouldHaveSize 1
        topStories.first().title shouldBe "Top story title"
        topStories.first().url shouldBe "http://top-story-title.fakeurl"

        verify {
            restTestTemplate.getForObject("${BASE_URL}topstories.json", IntArray::class.java)
            restTestTemplate.getForObject("${BASE_URL}item/$storyId.json", Item::class.java)
        }
    }

}