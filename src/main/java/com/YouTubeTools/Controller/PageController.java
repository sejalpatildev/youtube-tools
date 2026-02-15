package com.YouTubeTools.Controller;


import org.springframework.ui.Model;
import com.YouTubeTools.Model.Video;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.YouTubeTools.Service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class PageController {
    @Autowired
    private YouTubeService youTubeService;

    @GetMapping({"/","home"})
    public String home(){
        return "home";
    }




    @GetMapping("/video-details")
    public String videoDetails(
            @RequestParam(required = false) String videoUrlOrId,
            Model model) {

        if (videoUrlOrId != null && !videoUrlOrId.isEmpty()) {
            try {
                // extract video ID if full URL
                String videoId = videoUrlOrId;

                if (videoUrlOrId.contains("v=")) {
                    videoId = videoUrlOrId.split("v=")[1];
                }

                Video video = youTubeService.getVideoDetails(videoId);

                model.addAttribute("videoDetails", video);
                model.addAttribute("videoUrlOrId", videoUrlOrId);

            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        return "video-details";
    }

}
