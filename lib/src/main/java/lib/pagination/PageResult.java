package lib.pagination;

import java.util.List;
import lombok.Getter;

@Getter
public class PageResult<T> {

    private final List<T> itemList;     // 현재 페이지 데이터
    private final int page;             // 현재 페이지 번호
    private final int size;             // 페이지 크기
    private final long totalCount;      // 전체 데이터 개수 (Slice면 -1 허용)
    private final boolean hasNext;      // 다음 페이지 존재 여부

    private PageResult(List<T> itemList, int page, int size, long totalCount, boolean hasNext) {

        this.itemList = itemList;
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
        this.hasNext = hasNext;
    }

    // totalCount를 아는 일반적인 페이징용
    public static <T> PageResult<T> of(List<T> itemList, int page, int size, long totalCount) {

        boolean hasNext = ((long) page + 1) * size < totalCount;

        return new PageResult<>(itemList, page, size, totalCount, hasNext);
    }

    // totalCount를 모르는 Slice 기반 페이징용
    public static <T> PageResult<T> of(List<T> itemList, int page, int size, boolean hasNext) {
        return new PageResult<>(itemList, page, size, -1, hasNext);
    }
}
