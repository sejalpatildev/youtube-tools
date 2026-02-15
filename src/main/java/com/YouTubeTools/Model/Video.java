package com.YouTubeTools.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    private String id;
    private String title;
    private String channelTitle;

    private String description;     // ✅ ADD THIS
    private String publishedAt;     // ✅ ADD THIS
    private String thumbnailUrl;    // ✅ MAKE SURE THIS EXISTS
    private List<String> tags;      // ✅ MAKE SURE THIS EXISTS
}
