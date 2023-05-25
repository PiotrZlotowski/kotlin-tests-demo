package com.demo.hackernews.service

import com.demo.hackernews.model.Item
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import net.jqwik.api.Arbitraries
import net.jqwik.api.Arbitrary
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.api.Provide
import net.jqwik.api.lifecycle.BeforeProperty
import net.jqwik.kotlin.api.any
import net.jqwik.kotlin.api.combine
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.client.RestTemplate

/**
 * Support for spring
 * https://github.com/jqwik-team/jqwik-spring
 */
@MockKExtension.ConfirmVerification
@MockKExtension.CheckUnnecessaryStub
@TestInstance(PER_CLASS)
class HackerNewsServiceJunit5Test {

    private val BASE_URL = "https://hacker-news.firebaseio.com/v0/"

    val restTestTemplate = mockk<RestTemplate>()
    val hackerNewsService = HackerNewsService(restTestTemplate)

    @Property
    fun `top stories should return stories`(@ForAll("hackerNewsItem") topStoryItem: Item) {
        // given
        every { restTestTemplate.getForObject("${BASE_URL}topstories.json", IntArray::class.java) } returns intArrayOf(topStoryItem.id)
        every { restTestTemplate.getForObject("${BASE_URL}item/${topStoryItem.id}.json", Item::class.java) } returns topStoryItem
        // when
        val topStories = hackerNewsService.getTopStories(5)

        // then
        topStories shouldHaveSize 1
        topStories.first().title shouldBe topStoryItem.title
        topStories.first().url shouldBe topStoryItem.url

        verify {
            restTestTemplate.getForObject("${BASE_URL}topstories.json", IntArray::class.java)
            restTestTemplate.getForObject("${BASE_URL}item/${topStoryItem.id}.json", Item::class.java)
        }
    }

    fun title() : Arbitrary<String> = String.any().alpha().ofMinLength(1).ofMaxLength(300)
    fun id() : Arbitrary<Int> = Int.any().between(0, 100)
    fun type() : Arbitrary<String> = Arbitraries.of("story")
    fun url() : Arbitrary<String> = Arbitraries.of("http://top-story-title.fakeurl")
    fun score() : Arbitrary<Int> = Int.any().greaterOrEqual(5)

    @Provide
    fun hackerNewsItem(): Arbitrary<Item> = combine(id(), title(), type(), url(), score()) { id, title, type, url, score ->
       val item = Item()
        item.id = id
        item.title = title
        item.type = type
        item.url = url
        item.score = score
        item
    }


}