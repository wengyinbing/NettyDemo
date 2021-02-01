package code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wengyinbing
 * @data 2021/2/1 20:46
 **/
@AllArgsConstructor
@Getter
public enum PackageType {

    HttpResponse_pack(0),
    HttpRequest_pack(1);

    private final int code;
}