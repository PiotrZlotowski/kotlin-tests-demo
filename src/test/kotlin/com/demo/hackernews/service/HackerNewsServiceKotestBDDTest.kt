package com.demo.hackernews.service

import com.demo.hackernews.model.Item
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.web.client.RestTemplate

const val BASE_URL = "https://hacker-news.firebaseio.com/v0/"

// https://kotest.io/docs/framework/testing-styles.html

class HackerNewsServiceKotestBDDTest: BehaviorSpec({

    val restTestTemplate = mockk<RestTemplate>()
    val hackerNewsService = HackerNewsService(restTestTemplate)

    given("hackernews top stories") {
        val storyId = 1
        var topStoryItem = Item()
        topStoryItem.id = storyId
        topStoryItem.title = "Top story title"
        topStoryItem.score = 6
        topStoryItem.type = "story"
        topStoryItem.url = "http://top-story.fakeurl"
        and("mocks are ready to use") {
            every { restTestTemplate.getForObject("${BASE_URL_2}topstories.json", IntArray::class.java) } returns intArrayOf(storyId)
            every { restTestTemplate.getForObject("${BASE_URL_2}item/$storyId.json", Item::class.java) } returns topStoryItem
            `when`("I want to retrieve hackernews top news") {
                val topStories = hackerNewsService.getTopStories(5)
                then("one top news should be retrieved") {
                    topStories shouldHaveSize 1
                    topStories.first().title shouldBe "Top story title"
                    topStories.first().url shouldBe "http://top-story.fakeurl"
                    verify {
                        restTestTemplate.getForObject("${BASE_URL_2}topstories.json", IntArray::class.java)
                        restTestTemplate.getForObject("${BASE_URL_2}item/$storyId.json", Item::class.java)
                    }
                }
            }
            `when`("I request stories with min score 10") {
                val topStories = hackerNewsService.getTopStories(10)
                then("the response should be empty") {
                    topStories shouldHaveSize 0
                }
            }
        }
    }


})