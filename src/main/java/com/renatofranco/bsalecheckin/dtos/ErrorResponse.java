package com.renatofranco.bsalecheckin.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(hidden = true)
public class ErrorResponse {
  private Integer code;
  private String errors;
}
