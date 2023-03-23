package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * canvas drawing
 *
 *
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class CanvasDraw {
    @Id
    private String id;
    private String classRoomId;

    private List path;
    private String left;
    private String top;
    private String stroke;
    private String strokeWidth;
    private String fill;
    private String originX;
    private String originY;
    private String angle;
    private String scaleX;
    private String scaleY;
    private String strokeLineCap;
    private String strokeLineJoin;
    private String writerWidth;

    private String userSeq;
    private Integer tabIndex;
    private Integer pageIndex;
}
