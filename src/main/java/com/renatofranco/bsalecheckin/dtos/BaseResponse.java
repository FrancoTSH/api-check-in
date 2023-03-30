package com.renatofranco.bsalecheckin.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(hidden = true)
public class BaseResponse<T> {

  @Builder.Default
  private Integer code = 200;
  private T data;
}
