package com.amsidh.mvc.domain;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class BlockReadyMessage implements Serializable {
    private String fileMinioPath;
}
