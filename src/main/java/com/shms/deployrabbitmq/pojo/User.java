package com.shms.deployrabbitmq.pojo;


import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 7186944970241886064L;
    private String username;
    private String password;
}
