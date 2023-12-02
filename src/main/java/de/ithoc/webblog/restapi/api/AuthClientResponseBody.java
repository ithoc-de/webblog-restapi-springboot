package de.ithoc.webblog.restapi.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthClientResponseBody {

    private boolean valid;
    private int code;
    private String message;

}
