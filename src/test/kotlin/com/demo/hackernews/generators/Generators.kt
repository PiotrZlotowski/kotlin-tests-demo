package com.demo.hackernews.generators

import com.demo.hackernews.model.Item
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*

fun Arb.Companion.HackerNewsTopItem(): Arb<Item> =
    arbitrary {
        var topStoryItem = Item()
        topStoryItem.id = Arb.int().bind()
        topStoryItem.title = Arb.string(minSize = 0, maxSize = 100).bind()
        topStoryItem.score = Arb.int(min=5, max = 99).bind()
        topStoryItem.type = Arb.choose(2 to "story", 1 to Arb.string(maxSize = 10).bind()).bind()
        topStoryItem.url = Arb.element(listOf("http://top-story.fakeurl", Arb.string(maxSize = 10).bind())).bind()
        topStoryItem
    }

fun Arb.Companion.HackerNewsItem(): Arb<Item> =
    arbitrary {
        var item = Item()
        item.id = Arb.int().bind()
        item.title = Arb.string(minSize = 0, maxSize = 100).bind()
        item.score = Arb.int(min=0, max = 4).bind()
        item.type = Arb.choose(2 to "story", 1 to Arb.string(maxSize = 10).bind()).bind()
        item.url = Arb.element(listOf("http://top-story.fakeurl", Arb.string(maxSize = 10).bind())).bind()
        item
    }