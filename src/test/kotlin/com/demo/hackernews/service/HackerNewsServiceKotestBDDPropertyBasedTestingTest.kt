package com.demo.hackernews.service

import com.demo.hackernews.generators.HackerNewsItem
import com.demo.hackernews.model.Item
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.web.client.RestTemplate

const val BASE_URL_2 = "https://hacker-news.firebaseio.com/v0/"

// https://kotest.io/docs/proptest/property-test-generators-list.html

class HackerNewsServiceKotestBDDPropertyBasedTestingTest: BehaviorSpec({

    val restTestTemplate = mockk<RestTemplate>()
    val hackerNewsService = HackerNewsService(restTestTemplate)

    given("hackernews top stories") {
        checkAll(
            Arb.HackerNewsItem()
        ) { hackerNewsItem ->
            and("mocks are ready to use") {
                every {
                    restTestTemplate.getForObject(
                        "${BASE_URL_2}topstories.json",
                        IntArray::class.java
                    )
                } returns intArrayOf(hackerNewsItem.id)
                every {
                    restTestTemplate.getForObject(
                        "${BASE_URL_2}item/${hackerNewsItem.id}.json",
                        Item::class.java
                    )
                } returns hackerNewsItem
                `when`("I want to retrieve hackernews top news") {
                    val topStories = hackerNewsService.getTopStories(5)
                    then("one top news should be retrieved") {
                        topStories shouldHaveSize 1
                        topStories.first().title shouldBe hackerNewsItem.title
                        topStories.first().url shouldBe hackerNewsItem.url
                        verify {
                            restTestTemplate.getForObject("${BASE_URL_2}topstories.json", IntArray::class.java)
                            restTestTemplate.getForObject("${BASE_URL_2}item/${hackerNewsItem.id}.json", Item::class.java)
                        }
                    }
                }
                `when`("I request stories with min score 1000") {
                    val topStories = hackerNewsService.getTopStories(1000)
                    then("the response should be empty") {
                        topStories shouldHaveSize 0
                    }
                }
            }
        }
    }


})