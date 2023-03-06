package com.learnershi.rclasssocket.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Example {
    private String id;

    private String name;
}
