package com.YouTubeTools.Model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchVideo {

    private Video primaryVideo;
    private List<Video> relatedVideos;

}
