package com.fpt.cpdm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushNotification {
    private List<String> keys;
    private String title;
    private String detail;
    private String url;
}
