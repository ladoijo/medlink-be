package com.medlink.be.dto;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;

public record PaginatedDto<T>(
    List<T> items,
    PageMeta meta
) {

  public static <T, R> PaginatedDto<R> from(Page<T> page, Function<? super T, R> mapper) {
    var mapped = page.map(mapper);
    return new PaginatedDto<>(mapped.getContent(), PageMeta.from(page));
  }

  public record PageMeta(
      int page,
      int size,
      long totalElements,
      int totalPages,
      boolean first,
      boolean last,
      boolean hasNext,
      boolean hasPrevious
  ) {

    public static PageMeta from(Page<?> page) {
      return new PageMeta(
          Math.max(page.getNumber(), 1),
          page.getSize(),
          page.getTotalElements(),
          page.getTotalPages(),
          page.isFirst(),
          page.isLast(),
          page.hasNext(),
          page.hasPrevious()
      );
    }
  }
}
