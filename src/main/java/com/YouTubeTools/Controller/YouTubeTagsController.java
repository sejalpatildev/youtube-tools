package com.YouTubeTools.Controller;


import com.YouTubeTools.Model.SearchVideo;
import com.YouTubeTools.Service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/youtube")
public class YouTubeTagsController {

    @Autowired
    private YouTubeService youTubeService;

    @Value("${youtube.api.key}")
    private String apiKey;

    private boolean isApiKeyConfiguried(){
        return apiKey!=null && !apiKey.isEmpty();
    }

    @PostMapping("/serach")
    public String videoTags(@RequestParam ("videoTitle") String videoTitle, Model model){

        if(!isApiKeyConfiguried()){
            model.addAttribute("error","Api key is not configuried");
            return "home";
        }

        if(videoTitle==null || videoTitle.isEmpty()){
            model.addAttribute("error","Video Title is Required");
            return "home";
        }

        try {
            SearchVideo result=youTubeService.searchVideos(videoTitle);
            model.addAttribute("primaryVideo",result.getPrimaryVideo());
            model.addAttribute("relatedVideos",result.getRelatedVideos());
            return "home";
        }catch (Exception e){
            model.addAttribute("error",e.getMessage());
            return "home";
        }
        return null;
    }

}
