package com.YouTubeTools.Service;

import com.YouTubeTools.Model.SearchVideo;
import com.YouTubeTools.Model.Video;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class YouTubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SearchVideo searchVideos(String videoTitle) throws Exception {

        // 1️⃣ SEARCH API (Get Video IDs)
        String searchUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q="
                + videoTitle + "&maxResults=5&key=" + apiKey;

        String searchResponse = restTemplate.getForObject(searchUrl, String.class);
        JsonNode searchRoot = objectMapper.readTree(searchResponse);
        JsonNode searchItems = searchRoot.get("items");

        List<String> videoIds = new ArrayList<>();

        for (JsonNode item : searchItems) {
            String videoId = item.get("id").get("videoId").asText();
            videoIds.add(videoId);
        }

        if (videoIds.isEmpty()) {
            throw new Exception("No videos found");
        }

        // 2️⃣ VIDEOS API (Get Full Details + Tags)
        String ids = String.join(",", videoIds);

        String detailsUrl = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id="
                + ids + "&key=" + apiKey;

        String detailsResponse = restTemplate.getForObject(detailsUrl, String.class);
        JsonNode detailsRoot = objectMapper.readTree(detailsResponse);
        JsonNode detailsItems = detailsRoot.get("items");

        List<Video> videos = new ArrayList<>();

        for (JsonNode item : detailsItems) {

            String videoId = item.get("id").asText();
            JsonNode snippet = item.get("snippet");

            List<String> tags = new ArrayList<>();

            if (snippet.has("tags")) {
                for (JsonNode tag : snippet.get("tags")) {
                    tags.add(tag.asText());
                }
            }

            Video video = Video.builder()
                    .id(videoId)
                    .title(snippet.get("title").asText())
                    .channelTitle(snippet.get("channelTitle").asText())
                    .description(snippet.get("description").asText())
                    .publishedAt(snippet.get("publishedAt").asText())
                    .thumbnailUrl(
                            snippet.get("thumbnails")
                                    .get("high")
                                    .get("url")
                                    .asText()
                    )
                    .tags(tags)
                    .build();

            videos.add(video);
        }

        return SearchVideo.builder()
                .primaryVideo(videos.get(0))
                .relatedVideos(videos.subList(1, videos.size()))
                .build();
    }
    public Video getVideoDetails(String videoId) throws Exception {

        String url = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id="
                + videoId + "&key=" + apiKey;

        String response = restTemplate.getForObject(url, String.class);

        JsonNode root = objectMapper.readTree(response);
        JsonNode items = root.get("items");

        if (items.isEmpty()) {
            throw new Exception("Video not found");
        }

        JsonNode snippet = items.get(0).get("snippet");

        List<String> tags = new ArrayList<>();

        if (snippet.has("tags")) {
            for (JsonNode tag : snippet.get("tags")) {
                tags.add(tag.asText());
            }
        }

        return Video.builder()
                .id(videoId)
                .title(snippet.get("title").asText())
                .channelTitle(snippet.get("channelTitle").asText())
                .description(snippet.get("description").asText())
                .publishedAt(snippet.get("publishedAt").asText())
                .thumbnailUrl(
                        snippet.get("thumbnails")
                                .get("high")
                                .get("url")
                                .asText()
                )
                .tags(tags)
                .build();
    }

    }
