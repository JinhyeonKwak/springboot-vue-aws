package com.hodolog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {

    private static final int MIN_PAGE = 1;
    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

    public long getOffset() {
        return (long) (Math.max(MIN_PAGE, page) - 1) * Math.min(MAX_SIZE, size);
    }
}
