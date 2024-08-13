package com.example.SecureAndBox.dto;



import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Getter
public class VideoRequestDto {
    private String title;
    private String url;
    private String imageUrl;
    private String topic;
    private String description;
    private List<Tags> tags;

    @Getter
    @Builder
    public static class Tags {
        private String variant;
        private String value;
    }

    public List<Tags> getTags() { return tags != null ? new ArrayList<>(tags) : Collections.emptyList(); }
}
