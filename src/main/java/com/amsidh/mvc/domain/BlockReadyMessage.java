package com.amsidh.mvc.domain;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class BlockReadyMessage implements Serializable {
    private UUID uuid;
    private String bucketName;
    private String fileName;
    private String contentType;
}
