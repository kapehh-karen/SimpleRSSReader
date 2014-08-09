package com.example.karen.simplerssreader.core.rss;

import java.util.List;

public interface FeedParser {
    List<Message> parse() throws Exception;
}